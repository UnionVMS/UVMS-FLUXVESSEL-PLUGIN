package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.webservice;

import eu.europa.ec.fisheries.schema.vessel.Connector2BridgeRequest;
import eu.europa.ec.fisheries.schema.vessel.Connector2BridgeResponse;
import eu.europa.ec.fisheries.schema.vessel.FLUXReportVesselInformation;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.StartupBean;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.helper.Connector2BridgeRequestHelper;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.mapper.FLUXReportVesselInformationMapper;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.service.ExchangeService;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.service.FLUXReportVesselInformationService;
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

    @Mock
    private FLUXReportVesselInformationMapper fluxReportVesselInformationMapper;

    @Mock
    private FLUXReportVesselInformationService fluxReportVesselInformationService;


    @Test
    public void testPostActivityReportWhenSuccessfullyReceivingVesselInformation() throws Exception {
        //data set
        Connector2BridgeRequest request = new Connector2BridgeRequest();
        FLUXReportVesselInformation vesselInformation = new FLUXReportVesselInformation();

        //mock
        doReturn(true).when(startupBean).isEnabled();
        doReturn(FLUX_REPORT_VESSEL_INFORMATION).when(requestHelper).determineMessageType(request);
        doReturn(vesselInformation).when(fluxReportVesselInformationMapper).fromConnector2BridgeRequest(request);

        //execute
        Connector2BridgeResponse response = fluxMessageReceiverBean.post(request);

        //verify and assert
        verify(startupBean).isEnabled();
        verify(requestHelper).determineMessageType(request);
        verify(fluxReportVesselInformationMapper).fromConnector2BridgeRequest(request);
        verify(fluxReportVesselInformationService).receiveVesselInformation(vesselInformation);
        verifyNoMoreInteractions(startupBean, exchange, requestHelper, fluxReportVesselInformationMapper, fluxReportVesselInformationService);

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
        verifyNoMoreInteractions(startupBean, exchange, requestHelper, fluxReportVesselInformationMapper, fluxReportVesselInformationService);

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
        verifyNoMoreInteractions(startupBean, exchange, requestHelper, fluxReportVesselInformationMapper, fluxReportVesselInformationService);

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
        verifyNoMoreInteractions(startupBean, exchange, requestHelper, fluxReportVesselInformationMapper, fluxReportVesselInformationService);

        assertNotNull(response);
        assertEquals("NOK", response.getStatus());
    }
}
