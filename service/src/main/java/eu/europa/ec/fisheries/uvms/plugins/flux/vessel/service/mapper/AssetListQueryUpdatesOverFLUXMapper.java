package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.mapper;

import eu.europa.ec.fisheries.schema.exchange.plugin.v1.SendQueryAssetInformationRequest;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.exception.PluginException;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListQueryUpdatesOverFLUX;

import javax.ejb.Stateless;
import javax.xml.bind.JAXBException;

@Stateless
public class AssetListQueryUpdatesOverFLUXMapper {

    public AssetListQueryUpdatesOverFLUX fromSendQueryAssetInformationRequest(SendQueryAssetInformationRequest request) {
        try {
            return JAXBUtils.unMarshallMessage(request.getQuery(), AssetListQueryUpdatesOverFLUX.class);
        } catch (JAXBException e) {
            throw new PluginException("Could not extra query from SendQueryAssetInformationRequest", e);
        }
    }

}
