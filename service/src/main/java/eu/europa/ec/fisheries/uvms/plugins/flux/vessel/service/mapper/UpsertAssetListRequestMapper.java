package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.mapper;

import eu.europa.ec.fisheries.uvms.asset.model.exception.AssetModelMapperException;
import eu.europa.ec.fisheries.uvms.asset.model.mapper.AssetDataSourceRequestMapper;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;

import javax.ejb.EJB;
import java.util.Collection;

@EJB
public class UpsertAssetListRequestMapper {

    public String mapUpsertAssetList(Collection<Asset> assets, String username) throws AssetModelMapperException {
        return AssetDataSourceRequestMapper.mapUpsertAssetList(assets, username);
    }
}
