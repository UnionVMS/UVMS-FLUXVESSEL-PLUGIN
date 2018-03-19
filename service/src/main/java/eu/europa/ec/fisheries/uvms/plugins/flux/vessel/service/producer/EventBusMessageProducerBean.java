package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.producer;

import eu.europa.ec.fisheries.uvms.commons.message.impl.AbstractTopicProducer;
import eu.europa.ec.fisheries.uvms.exchange.model.constant.ExchangeModelConstants;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class EventBusMessageProducerBean extends AbstractTopicProducer {

    @Override
    public String getDestinationName() { return ExchangeModelConstants.PLUGIN_EVENTBUS; }

}
