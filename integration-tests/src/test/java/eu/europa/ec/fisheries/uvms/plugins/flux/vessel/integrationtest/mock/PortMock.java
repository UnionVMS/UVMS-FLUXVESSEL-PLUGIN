package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.integrationtest.mock;

import eu.europa.ec.fisheries.schema.vessel.POSTMSG;
import eu.europa.ec.fisheries.schema.vessel.POSTMSGOUT;
import xeu.connector_bridge.wsdl.v1.BridgeConnectorPortType;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.EndpointReference;
import java.util.*;

@Singleton
@LocalBean
public class PortMock implements BridgeConnectorPortType, BindingProvider {

    private List<POSTMSG> sentMessages;
    private Map<String, Object> requestContext;

    @PostConstruct
    public void init() {
        sentMessages = new ArrayList<>();
        requestContext = new HashMap<>();
    }

    public void clear() {
        this.sentMessages.clear();
    }

    public List<POSTMSG> getAllSentMessages() {
        return Collections.unmodifiableList(this.sentMessages);
    }

    @Override
    public POSTMSGOUT post(POSTMSG postMsg) {
        this.sentMessages.add(postMsg);
        return new POSTMSGOUT();
    }

    @Override
    public Map<String, Object> getRequestContext() {
        return requestContext;
    }

    @Override
    public Map<String, Object> getResponseContext() {
        throw new UnsupportedOperationException("Not supported in PortMock");
    }

    @Override
    public Binding getBinding() {
        throw new UnsupportedOperationException("Not supported in PortMock");
    }

    @Override
    public EndpointReference getEndpointReference() {
        throw new UnsupportedOperationException("Not supported in PortMock");
    }

    @Override
    public <T extends EndpointReference> T getEndpointReference(Class<T> aClass) {
        throw new UnsupportedOperationException("Not supported in PortMock");
    }
}
