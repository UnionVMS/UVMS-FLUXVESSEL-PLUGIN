package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.mapper;

import eu.europa.ec.fisheries.schema.vessel.FLUXVesselQueryMessage;
import eu.europa.ec.fisheries.schema.vessel.VesselQueryType;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.StartupBean;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.constants.Settings;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListQueryUpdatesOverFLUX;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.joda.time.DateTimeZone.UTC;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FLUXVesselQueryMessageMapperTest {

    @InjectMocks
    private FLUXVesselQueryMessageMapper fluxVesselQueryMessageMapper;

    @Mock
    private StartupBean startupBean;

    @Test
    public void fromAssetListQueryUpdatesOverFLUX() {
        //data set
        String typeCode = "Q-NEWS";
        String submitterFLUXParty = "BEL";
        Date startDate = new DateTime(2018, 1, 1, 1, 0, 0, 0, UTC).toDate();
        DateTime startDateTime = new DateTime(startDate).withZone(UTC);
        Date endDate = new DateTime(2018, 1, 3, 3, 0, 0, 0, UTC).toDate();
        DateTime endDateTime = new DateTime(endDate).withZone(UTC);
        String countryToLookFor = "NLD";

        AssetListQueryUpdatesOverFLUX assetListQueryUpdatesOverFLUX = new AssetListQueryUpdatesOverFLUX();
        assetListQueryUpdatesOverFLUX.setOnlyUpdates(true);
        assetListQueryUpdatesOverFLUX.setStartDate(startDate);
        assetListQueryUpdatesOverFLUX.setEndDate(endDate);
        assetListQueryUpdatesOverFLUX.setCountry(countryToLookFor);

        //mock
        doReturn(submitterFLUXParty).when(startupBean).getSetting(Settings.FLUX_LOCAL_NATION_CODE);

        //execute
        FLUXVesselQueryMessage vesselQueryMessage = fluxVesselQueryMessageMapper.fromAssetListQueryUpdatesOverFLUX(assetListQueryUpdatesOverFLUX);

        //verify and asset
        verify(startupBean).getSetting(Settings.FLUX_LOCAL_NATION_CODE);

        VesselQueryType vesselQuery = vesselQueryMessage.getVesselQuery();
        assertEquals("UUID", vesselQuery.getID().getSchemeID());
        assertNotNull(vesselQuery.getID().getValue());
        assertNotNull(vesselQuery.getSubmittedDateTime().getDateTime());
        assertEquals(UTC, vesselQuery.getSubmittedDateTime().getDateTime().getZone());
        assertEquals("FLUX_VESSEL_QUERY_TYPE", vesselQuery.getTypeCode().getListID());
        assertEquals(typeCode, vesselQuery.getTypeCode().getValue());
        assertEquals(1, vesselQuery.getSubmitterFLUXParty().getIDS().size());
        assertEquals("FLUX_GP_PARTY", vesselQuery.getSubmitterFLUXParty().getIDS().get(0).getSchemeID());
        assertEquals(submitterFLUXParty, vesselQuery.getSubmitterFLUXParty().getIDS().get(0).getValue());
        assertEquals(startDateTime, vesselQuery.getSpecifiedDelimitedPeriod().getStartDateTime().getDateTime());
        assertEquals(endDateTime, vesselQuery.getSpecifiedDelimitedPeriod().getEndDateTime().getDateTime());
        assertEquals("TERRITORY", vesselQuery.getSubjectVesselIdentity().getVesselRegistrationCountryID().getSchemeID());
        assertEquals(countryToLookFor, vesselQuery.getSubjectVesselIdentity().getVesselRegistrationCountryID().getValue());
    }
}