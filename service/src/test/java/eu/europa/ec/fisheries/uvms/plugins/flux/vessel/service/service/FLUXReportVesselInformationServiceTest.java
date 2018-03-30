package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.service;

import eu.europa.ec.fisheries.schema.vessel.FLUXReportVesselInformation;
import eu.europa.ec.fisheries.uvms.asset.model.exception.AssetModelMapperException;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.exception.PluginException;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.mapper.AssetMapper;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.mapper.MapperHelper;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FLUXReportVesselInformationServiceTest {

    @InjectMocks
    private FLUXReportVesselInformationService service;

    @Mock
    private AssetMapper assetMapper;

    @Mock
    private MapperHelper mapperHelper;

    @Mock
    private ExchangeService exchange;

    @Test
    public void receiveVesselInformationWhenSuccess() throws AssetModelMapperException {
        FLUXReportVesselInformation vesselInformation = new FLUXReportVesselInformation();
        Asset asset = new Asset();
        List<Asset> assets = Arrays.asList(asset);
        String upsertAssetListRequest = "</>";

        doReturn(assets).when(assetMapper).fromFLUXReportVesselInformation(vesselInformation);
        doReturn(upsertAssetListRequest).when(mapperHelper).mapUpsertAssetList(assets, "FLUX");

        service.receiveVesselInformation(vesselInformation);

        verify(assetMapper).fromFLUXReportVesselInformation(vesselInformation);
        verify(mapperHelper).mapUpsertAssetList(assets, "FLUX");
        verify(exchange).sendVesselInformation(upsertAssetListRequest);
    }

    @Test(expected = PluginException.class)
    public void receiveVesselInformationWhenException() throws AssetModelMapperException {
        FLUXReportVesselInformation vesselInformation = new FLUXReportVesselInformation();
        Asset asset = new Asset();
        List<Asset> assets = Arrays.asList(asset);

        doReturn(assets).when(assetMapper).fromFLUXReportVesselInformation(vesselInformation);
        doThrow(new AssetModelMapperException("test")).when(mapperHelper).mapUpsertAssetList(assets, "FLUX");

        service.receiveVesselInformation(vesselInformation);
    }
}