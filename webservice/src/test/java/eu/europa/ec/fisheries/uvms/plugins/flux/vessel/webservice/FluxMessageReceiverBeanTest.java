package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.webservice;

import eu.europa.ec.fisheries.schema.vessel.Connector2BridgeRequest;
import eu.europa.ec.fisheries.schema.vessel.Connector2BridgeResponse;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.StartupBean;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.helper.Connector2BridgeRequestHelper;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.service.ExchangeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.constants.MessageType.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FluxMessageReceiverBeanTest {

    @InjectMocks
    private FluxMessageReceiverBean fluxMessageReceiverBean;

    @Mock
    private StartupBean startupBean;

    @Mock
    private ExchangeService exchange;

    @Mock
    private Connector2BridgeRequestHelper requestHelper;


    @Test
    public void testPostActivityReportWhenSuccessfullyReceivingVesselInformation() throws Exception {
        //data set
        Connector2BridgeRequest request = new Connector2BridgeRequest();

        //mock
        doReturn(true).when(startupBean).isEnabled();
        doReturn(FLUX_REPORT_VESSEL_INFORMATION).when(requestHelper).determineMessageType(request);

        //execute
        Connector2BridgeResponse response = fluxMessageReceiverBean.post(request);

        //verify and assert
        verify(startupBean).isEnabled();
        verify(requestHelper).determineMessageType(request);
        verify(exchange).sendVesselInformation();
        verifyNoMoreInteractions(startupBean, exchange, requestHelper);

        assertNotNull(response);
        assertEquals("OK", response.getStatus());
    }

    @Test
    public void testPostActivityReportWhenSuccessfullyReceivingAQuery() throws Exception {
        //data set
        Connector2BridgeRequest request = new Connector2BridgeRequest();

        //mock
        doReturn(true).when(startupBean).isEnabled();
        doReturn(FLUX_VESSEL_QUERY_MESSAGE).when(requestHelper).determineMessageType(request);

        //execute
        Connector2BridgeResponse response = fluxMessageReceiverBean.post(request);

        //verify and assert
        verify(startupBean).isEnabled();
        verify(requestHelper).determineMessageType(request);
        verifyNoMoreInteractions(startupBean, exchange, requestHelper);

        assertNotNull(response);
        assertEquals("OK", response.getStatus());
    }

    @Test
    public void testPostActivityReportWhenSuccessfullyReceivingAResponse() throws Exception {
        //data set
        Connector2BridgeRequest request = new Connector2BridgeRequest();

        //mock
        doReturn(true).when(startupBean).isEnabled();
        doReturn(FLUX_VESSEL_RESPONSE_MESSAGE).when(requestHelper).determineMessageType(request);

        //execute
        Connector2BridgeResponse response = fluxMessageReceiverBean.post(request);

        //verify and assert
        verify(startupBean).isEnabled();
        verify(requestHelper).determineMessageType(request);
        verifyNoMoreInteractions(startupBean, exchange, requestHelper);

        assertNotNull(response);
        assertEquals("OK", response.getStatus());
    }

    @Test
    public void testPostActivityReportWhenPluginDidNotStartUp() throws Exception {
        //data set
        Connector2BridgeRequest request = new Connector2BridgeRequest();

        //mock
        doReturn(false).when(startupBean).isEnabled();

        //execute
        Connector2BridgeResponse response = fluxMessageReceiverBean.post(request);

        //verify
        verify(startupBean).isEnabled();
        verifyNoMoreInteractions(startupBean, exchange, requestHelper);

        assertNotNull(response);
        assertEquals("NOK", response.getStatus());
    }
}
