package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.mapper;

import eu.europa.ec.fisheries.uvms.asset.model.exception.AssetModelMapperException;
import eu.europa.ec.fisheries.uvms.asset.model.mapper.AssetDataSourceRequestMapper;
import eu.europa.ec.fisheries.uvms.asset.model.mapper.AssetModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.exchange.model.exception.ExchangeModelMarshallException;
import eu.europa.ec.fisheries.uvms.exchange.model.mapper.ExchangeModuleRequestMapper;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import java.util.Collection;

@Stateless
public class MapperHelper {

    public String mapUpsertAssetList(Collection<Asset> assets, String username) throws AssetModelMapperException {
        return AssetModuleRequestMapper.createUpsertAssetListModuleRequest(assets, username);
    }

    public String mapReceiveAssetInformation(String request, String username) throws ExchangeModelMarshallException {
        return ExchangeModuleRequestMapper.createReceiveAssetInformation(request, username);
    }
}
