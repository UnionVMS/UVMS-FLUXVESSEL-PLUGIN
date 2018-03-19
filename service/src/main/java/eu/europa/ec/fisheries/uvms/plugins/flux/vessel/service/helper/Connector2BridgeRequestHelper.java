package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.helper;

import eu.europa.ec.fisheries.schema.vessel.Connector2BridgeRequest;

import javax.ejb.Stateless;

@Stateless
public class Connector2BridgeRequestHelper {

    public String determineMessageType(Connector2BridgeRequest request) {
        return request.getAny().getLocalName();
    }

    public String getFRPropertyOrNull(Connector2BridgeRequest request) {
        return "FRA";
        //return request.getOtherAttributes().get(QName.valueOf("FR"));
    }

    public String getONPropertyOrNull(Connector2BridgeRequest request) {
        return request.getON();
    }
}
