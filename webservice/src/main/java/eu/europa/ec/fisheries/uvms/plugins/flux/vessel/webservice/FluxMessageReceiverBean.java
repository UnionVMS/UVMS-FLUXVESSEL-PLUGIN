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
package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.webservice;

import eu.europa.ec.fisheries.schema.vessel.Connector2BridgeRequest;
import eu.europa.ec.fisheries.schema.vessel.Connector2BridgeResponse;
import eu.europa.ec.fisheries.schema.vessel.FLUXReportVesselInformation;
import eu.europa.ec.fisheries.uvms.asset.model.exception.AssetModelMapperException;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.StartupBean;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.constants.MessageType;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.exception.PluginException;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.helper.Connector2BridgeRequestHelper;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.mapper.FLUXReportVesselInformationMapper;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.service.FLUXReportVesselInformationService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import xeu.bridge_connector.wsdl.v1.BridgeConnectorPortType;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.bind.JAXBException;
import java.util.UUID;

/**
 * This is the entry point for incoming FLUX messages.
 */
@Stateless
@Slf4j
@WebService(serviceName = "VesselService", targetNamespace = "urn:xeu:bridge-connector:wsdl:v1", portName = "BridgeConnectorPortType", endpointInterface = "xeu.bridge_connector.wsdl.v1.BridgeConnectorPortType")
public class FluxMessageReceiverBean implements BridgeConnectorPortType {

    @EJB
    private StartupBean startupBean;

    @EJB
    private Connector2BridgeRequestHelper requestHelper;

    @EJB
    private FLUXReportVesselInformationService fluxReportVesselInformationService;

    @EJB
    private FLUXReportVesselInformationMapper fluxReportVesselInformationMapper;

    @Override
    public Connector2BridgeResponse post(Connector2BridgeRequest request) {
        MDC.put("requestId", UUID.randomUUID().toString());

        Connector2BridgeResponse response = new Connector2BridgeResponse();
        if (!startupBean.isEnabled()) {
            response.setStatus("NOK");
            return response;
        }

        try {
            String messageType = requestHelper.determineMessageType(request);
            switch (messageType) {
                case MessageType.FLUX_REPORT_VESSEL_INFORMATION:
                    receiveFLUXReportVesselInformation(request);
                    break;
                case MessageType.FLUX_VESSEL_QUERY_MESSAGE:
                    receiveFLUXVesselQueryMessage(request);
                    break;
                case MessageType.FLUX_VESSEL_RESPONSE_MESSAGE:
                    receiveFLUXVesselResponseMessage(request);
                    break;
                default: throw new PluginException("In the Vessel FLUX plugin, no action is defined for a FLUX request with message type " + messageType);
            }

            response.setStatus("OK");
            return response;
        } catch (Exception e) {
            log.error("[ Error when receiving data from FLUX. ]", e);
            response.setStatus("NOK");
            return response;
        }
    }

    private void receiveFLUXReportVesselInformation(Connector2BridgeRequest request) throws JAXBException, AssetModelMapperException {
        log.debug("Got FLUXReportVesselInformation from FLUX in Vessel FLUX plugin");
        FLUXReportVesselInformation vesselInformation = fluxReportVesselInformationMapper.fromConnector2BridgeRequest(request);
        fluxReportVesselInformationService.receiveVesselInformation(vesselInformation);
    }

    private void receiveFLUXVesselQueryMessage(Connector2BridgeRequest request) {
        log.error("Received a FLUXVesselQueryMessage, but this is not supported by Union! For the contents of the message, check the FLUX tables for operation number " + request.getON());
    }

    private void receiveFLUXVesselResponseMessage(Connector2BridgeRequest request) {
        log.info("Received a FLUXVesselResponseMessage, but this is not supported by Union! For the contents of the message, check the FLUX tables for operation number " + request.getON());
    }

}