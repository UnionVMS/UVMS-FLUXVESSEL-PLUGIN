package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service;

import xeu.connector_bridge.wsdl.v1.BridgeConnectorPortType;

public interface PortInitiator {
    BridgeConnectorPortType getPort();
    void updatePort();
}
