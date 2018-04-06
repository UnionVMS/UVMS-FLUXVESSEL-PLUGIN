package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.helper;

import eu.europa.ec.fisheries.schema.vessel.Connector2BridgeRequest;

import javax.ejb.Stateless;
import javax.xml.namespace.QName;

@Stateless
public class Connector2BridgeRequestHelper {

    public String determineMessageType(Connector2BridgeRequest request) {
        return request.getAny().getLocalName();
    }

    public String getFRPropertyOrNull(Connector2BridgeRequest request) {
        String fr = request.getOtherAttributes().get(QName.valueOf("FR"));
        if (fr != null && fr.contains(":")){
            int indexOfColon = fr.indexOf(":");
            return fr.substring(0, indexOfColon);
        } else {
            return fr;
        }
    }

    public String getONPropertyOrNull(Connector2BridgeRequest request) {
        return request.getON();
    }
}
