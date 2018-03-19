package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.producer;

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;
import eu.europa.ec.fisheries.uvms.commons.message.impl.AbstractProducer;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class ExchangeEventMessageProducerBean extends AbstractProducer {

    public String getDestinationName() {
        return MessageConstants.QUEUE_EXCHANGE_EVENT;
    }

}
