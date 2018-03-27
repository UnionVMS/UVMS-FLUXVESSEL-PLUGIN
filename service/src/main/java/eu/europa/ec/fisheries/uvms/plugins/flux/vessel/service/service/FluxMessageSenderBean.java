package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.service;

import eu.europa.ec.fisheries.schema.vessel.FLUXReportVesselInformation;
import eu.europa.ec.fisheries.schema.vessel.POSTMSG;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.PortInitiator;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.StartupBean;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.exception.PluginException;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.mapper.PostMsgTypeMapper;
import xeu.connector_bridge.wsdl.v1.BridgeConnectorPortType;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.Map;

@LocalBean
@Stateless
public class FluxMessageSenderBean {

    @EJB
    private PortInitiator port;

    @EJB
    private StartupBean startupBean;

    @EJB
    private PostMsgTypeMapper postMsgTypeMapper;


    public void sendVesselInformationToFlux(FLUXReportVesselInformation vesselInformation) {
        POSTMSG request = postMsgTypeMapper.wrapInPostMsgType(vesselInformation, startupBean.getSetting("DF"), "XEU");
        sendPostMsgType(request);
    }

    public void sendPostMsgType(POSTMSG request) {
        try {
            Map<String, String> headerValues = new HashMap<>();
            headerValues.put("connectorID", startupBean.getSetting("CLIENT_ID"));

            BridgeConnectorPortType portType = port.getPort();
            postMsgTypeMapper.addHeaderValueToRequest(portType, headerValues);

            portType.post(request);
        } catch (Exception e) {
            throw new PluginException("Error when sending a message to FLUX", e);
        }
    }

}
