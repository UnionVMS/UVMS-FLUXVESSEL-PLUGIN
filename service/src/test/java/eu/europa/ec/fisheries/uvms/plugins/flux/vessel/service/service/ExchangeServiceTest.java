package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.service;

import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.mapper.MapperHelper;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.producer.ExchangeEventMessageProducerBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class ExchangeServiceTest {

    @InjectMocks
    private ExchangeService exchangeService;

    @Mock
    private ExchangeEventMessageProducerBean producer;

    @Mock
    private MapperHelper mapperHelper;

    @Test
    public void sendVesselInformation() throws Exception {
        //mock
        String request = "<upsert />";
        String receiveAssetInformation = "<ReceiveAssetInformation />";
        doReturn(receiveAssetInformation).when(mapperHelper).mapReceiveAssetInformation(request, "FLUX");

        //execute
        exchangeService.sendVesselInformation(request);

        //verify and assert
        verify(producer).sendModuleMessage(receiveAssetInformation, null);
        verifyNoMoreInteractions(producer);
    }

}
