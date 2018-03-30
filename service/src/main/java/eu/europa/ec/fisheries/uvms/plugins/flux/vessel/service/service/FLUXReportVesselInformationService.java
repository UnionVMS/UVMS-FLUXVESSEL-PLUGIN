package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.service;

import eu.europa.ec.fisheries.schema.vessel.FLUXReportVesselInformation;
import eu.europa.ec.fisheries.uvms.asset.model.exception.AssetModelMapperException;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.exception.PluginException;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.mapper.AssetMapper;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.mapper.MapperHelper;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class FLUXReportVesselInformationService {

    @EJB
    private AssetMapper assetMapper;

    @EJB
    private MapperHelper mapperHelper;

    @EJB
    private ExchangeService exchange;

    public void receiveVesselInformation(FLUXReportVesselInformation vesselInformation) {
        try {
            List<Asset> assets = assetMapper.fromFLUXReportVesselInformation(vesselInformation);
            String upsertAssetListRequest = mapperHelper.mapUpsertAssetList(assets, "FLUX");
            exchange.sendVesselInformation(upsertAssetListRequest);
        } catch (AssetModelMapperException e) {
            throw new PluginException("Something went wrong processing incoming vessel information", e);
        }
    }
}
