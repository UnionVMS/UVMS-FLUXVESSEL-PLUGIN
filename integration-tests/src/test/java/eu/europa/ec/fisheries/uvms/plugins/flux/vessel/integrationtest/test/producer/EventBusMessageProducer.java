package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.integrationtest.test.producer;

import eu.europa.ec.fisheries.uvms.commons.message.impl.JMSUtils;
import eu.europa.ec.fisheries.uvms.exchange.model.constant.ExchangeModelConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.jms.*;

@Stateless
public class EventBusMessageProducer {

    private static final Logger LOG = LoggerFactory.getLogger(EventBusMessageProducer.class);

    private ConnectionFactory connectionFactory;
    private Topic eventBus;

    @PostConstruct
    public void initialize() {
        connectionFactory = JMSUtils.lookupConnectionFactory();
        eventBus = JMSUtils.lookupTopic(ExchangeModelConstants.PLUGIN_EVENTBUS);
    }

    public void sendMessageToEventBus(String message, String serviceName) {
        Connection connection = null;

        try {
            connection = connectionFactory.createConnection();
            final Session session = JMSUtils.connectToQueue(connection);

            TextMessage textMessage = session.createTextMessage();
            textMessage.setText(message);
            textMessage.setStringProperty(ExchangeModelConstants.SERVICE_NAME, serviceName);

            getProducer(session, eventBus).send(textMessage);
        } catch (Exception e) {
            LOG.error("[ Error when sending message. ] ", e);
            throw new RuntimeException(e);
        } finally {
            JMSUtils.disconnectQueue(connection);
        }
    }

    private MessageProducer getProducer(Session session, Destination destination) throws JMSException {
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        producer.setTimeToLive(30000L);
        return producer;
    }
}
