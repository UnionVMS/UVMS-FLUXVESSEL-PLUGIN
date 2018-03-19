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
package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service;

import eu.europa.ec.fisheries.schema.exchange.plugin.types.v1.PluginType;
import eu.europa.ec.fisheries.schema.exchange.service.v1.CapabilityListType;
import eu.europa.ec.fisheries.schema.exchange.service.v1.ServiceType;
import eu.europa.ec.fisheries.schema.exchange.service.v1.SettingListType;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.exchange.model.constant.ExchangeModelConstants;
import eu.europa.ec.fisheries.uvms.exchange.model.exception.ExchangeModelMarshallException;
import eu.europa.ec.fisheries.uvms.exchange.model.mapper.ExchangeModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.mapper.ServiceMapper;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.producer.EventBusMessageProducerBean;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.service.FileHandlerBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.*;
import javax.jms.DeliveryMode;
import java.util.Map;

@Singleton
@Startup
@DependsOn({"FileHandlerBean", "EventBusMessageProducerBean", "PluginRegistrationListener", "FLUXVesselPluginListener"})
public class StartupBean extends PluginDataHolder {

    static final Logger LOG = LoggerFactory.getLogger(StartupBean.class);

    private static final long TIME_TO_LIVE = 1800000L;
    private static final int MAX_NUMBER_OF_TRIES = 10;
    private boolean isRegistered = false;
    private boolean isEnabled = false;
    private boolean waitingForResponse = false;
    private int numberOfTriesExecuted = 0;
    private String registerClassName = "";

    @EJB
    EventBusMessageProducerBean eventBusMessageProducerBean;

    @EJB
    private FileHandlerBean fileHandler;

    private CapabilityListType capabilities;
    private SettingListType settingList;
    private ServiceType serviceType;

    @PostConstruct
    public void startup() {
        //This must be loaded first!!! Not doing that will end in dire problems later on!
        super.setPluginApplicationProperties(fileHandler.getPropertiesFromFile(PLUGIN_PROPERTIES));
        registerClassName = getPluginApplicationProperty("application.groupid");

        //These can be loaded in any order
        super.setPluginProperties(fileHandler.getPropertiesFromFile(PROPERTIES));
        super.setPluginCapabilities(fileHandler.getPropertiesFromFile(CAPABILITIES));

        ServiceMapper.mapToMapFromProperties(super.getSettings(), super.getPluginProperties(), getRegisterClassName());
        ServiceMapper.mapToMapFromProperties(super.getCapabilities(), super.getPluginCapabilities(), null);

        capabilities = ServiceMapper.getCapabilitiesListTypeFromMap(super.getCapabilities());
        settingList = ServiceMapper.getSettingsListTypeFromMap(super.getSettings());

        serviceType = ServiceMapper.getServiceType(
                getRegisterClassName(),
                getApplicationName(),
                "Plugin for sending and receiving data to and from FLUX for Vessel messages",
                PluginType.FLUX,
                getPluginResponseSubscriptionName());

        register();

        LOG.debug("Settings updated in plugin {}", registerClassName);
        for (Map.Entry<String, String> entry : super.getSettings().entrySet()) {
            LOG.debug("Setting: KEY: {} , VALUE: {}", entry.getKey(), entry.getValue());
        }

        LOG.info("PLUGIN STARTED");
    }

    @PreDestroy
    public void shutdown() {
        unregister();
    }

    @Schedule(second = "*/30", minute = "*", hour = "*", persistent = false)
    public void timeout(Timer timer) {
        if (!waitingForResponse && !isRegistered && numberOfTriesExecuted < MAX_NUMBER_OF_TRIES) {
            LOG.info(getRegisterClassName() + " is not registered, trying to register");
            register();
            numberOfTriesExecuted++;
        }
        if (isRegistered) {
            LOG.info(getRegisterClassName() + " is registered. Cancelling timer.");
            timer.cancel();
        } else if(numberOfTriesExecuted >= MAX_NUMBER_OF_TRIES) {
            LOG.info(getRegisterClassName() + " failed to register, maximum number of retries reached.");
        }
    }

    private void register() {
        LOG.info("Registering to Exchange Module");
        setWaitingForResponse(true);
        try {
            String registerServiceRequest = ExchangeModuleRequestMapper.createRegisterServiceRequest(serviceType, capabilities, settingList);
            eventBusMessageProducerBean.sendEventBusMessage(registerServiceRequest, ExchangeModelConstants.EXCHANGE_REGISTER_SERVICE, DeliveryMode.NON_PERSISTENT, TIME_TO_LIVE);
        } catch (MessageException | ExchangeModelMarshallException e) {
            LOG.error("Failed to send registration message to {}", ExchangeModelConstants.EXCHANGE_REGISTER_SERVICE);
            setWaitingForResponse(false);
        }

    }

    private void unregister() {
        LOG.info("Unregistering from Exchange Module");
        try {
            String unregisterServiceRequest = ExchangeModuleRequestMapper.createUnregisterServiceRequest(serviceType);
            eventBusMessageProducerBean.sendEventBusMessage(unregisterServiceRequest, ExchangeModelConstants.EXCHANGE_REGISTER_SERVICE, DeliveryMode.NON_PERSISTENT, TIME_TO_LIVE);
        } catch (MessageException | ExchangeModelMarshallException e) {
            LOG.error("Failed to send unregistration message to {}", ExchangeModelConstants.EXCHANGE_REGISTER_SERVICE);
        }
    }

    public String getPluginApplicationProperty(String key) {
        try {
            return (String) super.getPluginApplicationProperties().get(key);
        } catch (Exception e) {
            LOG.error("Failed to getSetting for key: " + key, getRegisterClassName());
            return null;
        }
    }

    public String getPluginResponseSubscriptionName() {
        return getRegisterClassName() + "." + getPluginApplicationProperty("application.responseTopicName");
    }

    public String getResponseTopicMessageName() {
        return getSetting("application.groupid");
    }

    public String getRegisterClassName() {
        return registerClassName;
    }

    public String getApplicationName() {
        return getPluginApplicationProperty("application.name");
    }

    public String getSetting(String key) {
        try {
            LOG.debug("Trying to get setting {} ", registerClassName + "." + key);
            return super.getSettings().get(registerClassName + "." + key);
        } catch (Exception e) {
            LOG.error("Failed to getSetting for key: " + key, registerClassName);
            return null;
        }
    }

    public boolean isWaitingForResponse() {
        return waitingForResponse;
    }

    public void setWaitingForResponse(boolean waitingForResponse) {
        this.waitingForResponse = waitingForResponse;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean isRegistered) {
        this.isRegistered = isRegistered;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

}
