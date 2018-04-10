/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.consumer;

import eu.europa.ec.fisheries.schema.exchange.common.v1.AcknowledgeType;
import eu.europa.ec.fisheries.schema.exchange.common.v1.AcknowledgeTypeType;
import eu.europa.ec.fisheries.schema.exchange.plugin.v1.PluginBaseRequest;
import eu.europa.ec.fisheries.schema.exchange.plugin.v1.SendAssetInformationRequest;
import eu.europa.ec.fisheries.schema.exchange.plugin.v1.SendQueryAssetInformationRequest;
import eu.europa.ec.fisheries.schema.exchange.plugin.v1.SetConfigRequest;
import eu.europa.ec.fisheries.schema.vessel.FLUXReportVesselInformation;
import eu.europa.ec.fisheries.schema.vessel.FLUXVesselQueryMessage;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.message.context.MappedDiagnosticContext;
import eu.europa.ec.fisheries.uvms.exchange.model.constant.ExchangeModelConstants;
import eu.europa.ec.fisheries.uvms.exchange.model.exception.ExchangeModelMarshallException;
import eu.europa.ec.fisheries.uvms.exchange.model.mapper.ExchangePluginResponseMapper;
import eu.europa.ec.fisheries.uvms.exchange.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.StartupBean;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.mapper.AssetListQueryUpdatesOverFLUXMapper;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.mapper.AssetMapper;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.mapper.FLUXReportVesselInformationMapper;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.mapper.FLUXVesselQueryMessageMapper;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.producer.ExchangeEventMessageProducerBean;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.service.FluxMessageSenderBean;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.service.PluginService;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListQueryUpdatesOverFLUX;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.*;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;

@MessageDriven(mappedName = ExchangeModelConstants.PLUGIN_EVENTBUS, activationConfig = {
        @ActivationConfigProperty(propertyName = "messagingType", propertyValue = ExchangeModelConstants.CONNECTION_TYPE),
        @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = ExchangeModelConstants.DESTINATION_TYPE_TOPIC),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = ExchangeModelConstants.EVENTBUS_NAME),
        @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "eu.europa.ec.fisheries.uvms.plugins.flux.vessel"),
        @ActivationConfigProperty(propertyName = "clientId", propertyValue = "eu.europa.ec.fisheries.uvms.plugins.flux.vessel"),
        @ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "ServiceName='eu.europa.ec.fisheries.uvms.plugins.flux.vessel'")
})
@Slf4j
public class FLUXVesselPluginListener implements MessageListener {

    @EJB
    private PluginService service;

    @EJB
    private ExchangeEventMessageProducerBean messageProducer;

    @EJB
    private StartupBean startup;

    @EJB
    private FLUXReportVesselInformationMapper vesselInformationMapper;

    @EJB
    private AssetMapper assetMapper;

    @EJB
    private FluxMessageSenderBean fluxMessageSenderBean;

    @EJB
    private AssetListQueryUpdatesOverFLUXMapper assetListQueryUpdatesOverFLUXMapper;

    @EJB
    private FLUXVesselQueryMessageMapper fluxVesselQueryMessageMapper;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void onMessage(Message inMessage) {

        log.info("FLUXVesselPluginListener (MessageConstants.PLUGIN_SERVICE_CLASS_NAME): {}", startup.getRegisterClassName());

        TextMessage textMessage = (TextMessage) inMessage;
        MappedDiagnosticContext.addMessagePropertiesToThreadMappedDiagnosticContext(textMessage);

        try {
            PluginBaseRequest request = JAXBMarshaller.unmarshallTextMessage(textMessage, PluginBaseRequest.class);

            String responseMessage = null;

            switch (request.getMethod()) {
                case SET_CONFIG:
                    SetConfigRequest setConfigRequest = JAXBMarshaller.unmarshallTextMessage(textMessage, SetConfigRequest.class);
                    AcknowledgeTypeType setConfig = service.setConfig(setConfigRequest.getConfigurations());
                    AcknowledgeType setConfigAck = ExchangePluginResponseMapper.mapToAcknowledgeType(textMessage.getJMSMessageID(), setConfig);
                    responseMessage = ExchangePluginResponseMapper.mapToSetConfigResponse(startup.getRegisterClassName(), setConfigAck);
                    break;
                case START:
                    AcknowledgeTypeType start = service.start();
                    AcknowledgeType startAck = ExchangePluginResponseMapper.mapToAcknowledgeType(textMessage.getJMSMessageID(), start);
                    responseMessage = ExchangePluginResponseMapper.mapToStartResponse(startup.getRegisterClassName(), startAck);
                    break;
                case STOP:
                    AcknowledgeTypeType stop = service.stop();
                    AcknowledgeType stopAck = ExchangePluginResponseMapper.mapToAcknowledgeType(textMessage.getJMSMessageID(), stop);
                    responseMessage = ExchangePluginResponseMapper.mapToStopResponse(startup.getRegisterClassName(), stopAck);
                    break;
                case PING:
                    responseMessage = ExchangePluginResponseMapper.mapToPingResponse(startup.isEnabled(), startup.isEnabled());
                    break;
                case SEND_VESSEL_INFORMATION:
                    sendInformation(textMessage);
                    return;
                case SEND_VESSEL_QUERY:
                    sendQuery(textMessage);
                    return;
                default:
                    break;
            }

            if (responseMessage != null) {
                messageProducer.sendResponseMessageToSender(textMessage, responseMessage);
            }

        } catch (ExchangeModelMarshallException | NullPointerException e) {
            log.error("[ Error when receiving message in Belgian activity " + startup.getRegisterClassName() + " ]", e);
        } catch (JMSException | MessageException | JAXBException ex) {
            log.error("[ Error when handling JMS message in Belgian activity " + startup.getRegisterClassName() + " ]", ex);
        }
    }

    private void sendInformation(TextMessage textMessage) throws ExchangeModelMarshallException, JAXBException {
        SendAssetInformationRequest request = JAXBMarshaller.unmarshallTextMessage(textMessage, SendAssetInformationRequest.class);
        Asset asset = assetMapper.fromSendAssetInformationRequest(request);
        FLUXReportVesselInformation vesselInformation = vesselInformationMapper.fromAsset(asset);
        fluxMessageSenderBean.sendVesselInformationToFlux(vesselInformation);
    }

    private void sendQuery(TextMessage textMessage) throws ExchangeModelMarshallException {
        SendQueryAssetInformationRequest request = JAXBMarshaller.unmarshallTextMessage(textMessage, SendQueryAssetInformationRequest.class);
        AssetListQueryUpdatesOverFLUX query = assetListQueryUpdatesOverFLUXMapper.fromSendQueryAssetInformationRequest(request);
        FLUXVesselQueryMessage vesselQueryMessage = fluxVesselQueryMessageMapper.fromAssetListQueryUpdatesOverFLUX(query);
        fluxMessageSenderBean.sendQueryToFlux(vesselQueryMessage);
    }

}
