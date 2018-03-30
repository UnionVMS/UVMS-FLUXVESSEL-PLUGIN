package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.integrationtest.mock;

import eu.europa.ec.fisheries.uvms.exchange.model.constant.ExchangeModelConstants;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.integrationtest.test.state.ReceivedMessagesInExchange;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(mappedName = ExchangeModelConstants.EXCHANGE_MESSAGE_IN_QUEUE, activationConfig = {
        @ActivationConfigProperty(propertyName = "messagingType", propertyValue = ExchangeModelConstants.CONNECTION_TYPE),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = ExchangeModelConstants.DESTINATION_TYPE_QUEUE),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = ExchangeModelConstants.EXCHANGE_MESSAGE_IN_QUEUE_NAME),
        @ActivationConfigProperty(propertyName = "destinationJndiName", propertyValue = ExchangeModelConstants.EXCHANGE_MESSAGE_IN_QUEUE),
        @ActivationConfigProperty(propertyName = "connectionFactoryJndiName", propertyValue = ExchangeModelConstants.CONNECTION_FACTORY)
})
public class ExchangeConsumerMock implements MessageListener {

    @EJB
    private ReceivedMessagesInExchange receivedMessagesInExchange;

    @Override
    public void onMessage(Message message) {
        try {
            receivedMessagesInExchange.add(((TextMessage)message).getText());
        } catch (JMSException e) {
            throw new RuntimeException("Could not receive Exchange message", e);
        }
    }
}
