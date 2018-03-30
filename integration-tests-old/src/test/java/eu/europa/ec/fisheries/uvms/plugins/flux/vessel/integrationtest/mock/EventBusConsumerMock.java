package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.integrationtest.mock;

import eu.europa.ec.fisheries.uvms.exchange.model.constant.ExchangeModelConstants;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.integrationtest.test.producer.EventBusMessageProducer;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;

@MessageDriven(mappedName = ExchangeModelConstants.PLUGIN_EVENTBUS, activationConfig = {
        @ActivationConfigProperty(propertyName = "messageSelector", propertyValue = ExchangeModelConstants.SERVICE_NAME + " = '" + ExchangeModelConstants.EXCHANGE_REGISTER_SERVICE + "'"),
        @ActivationConfigProperty(propertyName = "messagingType", propertyValue = ExchangeModelConstants.CONNECTION_TYPE),
        @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = ExchangeModelConstants.DESTINATION_TYPE_TOPIC),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = ExchangeModelConstants.EVENTBUS_NAME),
        @ActivationConfigProperty(propertyName = "destinationJndiName", propertyValue = ExchangeModelConstants.PLUGIN_EVENTBUS),
        @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = ExchangeModelConstants.EXCHANGE_REGISTER_SERVICE),
        @ActivationConfigProperty(propertyName = "clientId", propertyValue = ExchangeModelConstants.EXCHANGE_REGISTER_SERVICE),
        @ActivationConfigProperty(propertyName = "connectionFactoryJndiName", propertyValue = ExchangeModelConstants.CONNECTION_FACTORY)
})
public class EventBusConsumerMock implements MessageListener {

    @EJB
    private EventBusMessageProducer eventBusMessageProducer;

    @Override
    public void onMessage(Message message) {
        sendRegisterSuccessful();
        sendStart();
    }

    private void sendStart() {
        eventBusMessageProducer.sendMessageToEventBus(START, "eu.europa.ec.fisheries.uvms.plugins.flux.vessel");
    }

    private void sendRegisterSuccessful() {
        eventBusMessageProducer.sendMessageToEventBus(REGISTER_SUCCESSFUL, "eu.europa.ec.fisheries.uvms.plugins.flux.vessel.PLUGIN_RESPONSE");
    }

    private static final String REGISTER_SUCCESSFUL = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<ns2:RegisterServiceResponse xmlns:ns2=\"urn:registry.exchange.schema.fisheries.ec.europa.eu:v1\">\n" +
            "    <method>REGISTER_SERVICE</method>\n" +
            "    <ack>\n" +
            "        <messageId>ID:ALV-VPC-1048-20882-1519729180350-13:1:24:1:1</messageId>\n" +
            "        <type>OK</type>\n" +
            "    </ack>\n" +
            "    <service>\n" +
            "        <serviceResponseMessageName>eu.europa.ec.fisheries.uvms.plugins.belgian.activity.PLUGIN_RESPONSE</serviceResponseMessageName>\n" +
            "        <serviceClassName>eu.europa.ec.fisheries.uvms.plugins.belgian.activity</serviceClassName>\n" +
            "        <name>belgian-activity</name>\n" +
            "        <description>Plugin for sending and receiving data to and from Belgian activity for activity messages</description>\n" +
            "        <pluginType>BELGIAN_ACTIVITY</pluginType>\n" +
            "        <settingList>\n" +
            "            <setting>\n" +
            "                <key>eu.europa.ec.fisheries.uvms.plugins.belgian.activity.FLUX_FA_REPORT_MESSAGE_USER_NAME</key>\n" +
            "                <value>M-CATCH</value>\n" +
            "            </setting>\n" +
            "            <setting>\n" +
            "                <key>eu.europa.ec.fisheries.uvms.plugins.belgian.activity.FLUX_FA_REPORT_MESSAGE_SENDER_RECEIVER</key>\n" +
            "                <value>BEL</value>\n" +
            "            </setting>\n" +
            "            <setting>\n" +
            "                <key>eu.europa.ec.fisheries.uvms.plugins.belgian.activity.FLUX_FA_REPORT_MESSAGE_DF_VALUE</key>\n" +
            "                <value>urn:un:unece:uncefact:fisheries:FLUX:FA:EU:2</value>\n" +
            "            </setting>\n" +
            "        </settingList>\n" +
            "        <capabilityList>\n" +
            "            <capability>\n" +
            "                <type>SAMPLING</type>\n" +
            "                <value>TRUE</value>\n" +
            "            </capability>\n" +
            "            <capability>\n" +
            "                <type>MULTIPLE_OCEAN</type>\n" +
            "                <value>TRUE</value>\n" +
            "            </capability>\n" +
            "            <capability>\n" +
            "                <type>POLLABLE</type>\n" +
            "                <value>TRUE</value>\n" +
            "            </capability>\n" +
            "            <capability>\n" +
            "                <type>ONLY_SINGLE_OCEAN</type>\n" +
            "                <value>TRUE</value>\n" +
            "            </capability>\n" +
            "            <capability>\n" +
            "                <type>CONFIGURABLE</type>\n" +
            "                <value>TRUE</value>\n" +
            "            </capability>\n" +
            "        </capabilityList>\n" +
            "        <status>STARTED</status>\n" +
            "        <active>true</active>\n" +
            "    </service>\n" +
            "</ns2:RegisterServiceResponse>\n";

    private static final String START = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<ns2:StartRequest xmlns:ns2=\"urn:plugin.exchange.schema.fisheries.ec.europa.eu:v1\">\n" +
            "    <method>START</method>\n" +
            "</ns2:StartRequest>";
}
