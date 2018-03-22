package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.service;

import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.producer.ExchangeEventMessageProducerBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class ExchangeServiceTest {

    @InjectMocks
    private ExchangeService exchangeService;

    @Mock
    private ExchangeEventMessageProducerBean producer;

    @Test
    public void sendVesselInformation() throws Exception {
        //execute
        exchangeService.sendVesselInformation("<upsert />");

        //verify and assert
        verify(producer).sendModuleMessage("TODO", null);
        verifyNoMoreInteractions(producer);
    }

}
