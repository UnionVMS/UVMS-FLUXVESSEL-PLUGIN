package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.mapper;

import eu.europa.ec.fisheries.schema.vessel.*;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.StartupBean;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.constants.Settings;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListQueryUpdatesOverFLUX;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.UUID;

@Stateless
public class FLUXVesselQueryMessageMapper {

    @EJB
    private StartupBean startupBean;

    public FLUXVesselQueryMessage fromAssetListQueryUpdatesOverFLUX(AssetListQueryUpdatesOverFLUX query) {
        String typeCode = query.isOnlyUpdates() ? "Q-NEWS" : "Q-NR";
        String submitterFLUXParty = startupBean.getSetting(Settings.FLUX_LOCAL_NATION_CODE);
        DateTime startDate = new DateTime(query.getStartDate()).withZone(DateTimeZone.UTC);
        DateTime endDate = new DateTime(query.getEndDate()).withZone(DateTimeZone.UTC);
        String countryToLookFor = query.getCountry();

        VesselQueryType vesselQuery = new VesselQueryType();
        vesselQuery.setID(new IDType()
                .withSchemeID("UUID")
                .withValue(UUID.randomUUID().toString()));
        vesselQuery.setSubmittedDateTime(new DateTimeType()
                .withDateTime(new DateTime().withZone(DateTimeZone.UTC)));
        vesselQuery.setTypeCode(new CodeType()
                .withListID("FLUX_VESSEL_QUERY_TYPE")
                .withValue(typeCode));
        vesselQuery.setSubmitterFLUXParty(new FLUXPartyType().withIDS(new IDType()
                .withSchemeID("FLUX_GP_PARTY")
                .withValue(submitterFLUXParty)));
        vesselQuery.setSpecifiedDelimitedPeriod(new DelimitedPeriodType()
                .withStartDateTime(new DateTimeType().withDateTime(startDate))
                .withEndDateTime(new DateTimeType().withDateTime(endDate)));
        vesselQuery.setSubjectVesselIdentity(new VesselIdentityType().withVesselRegistrationCountryID(new IDType()
                .withSchemeID("TERRITORY")
                .withValue(countryToLookFor)));

        FLUXVesselQueryMessage fluxVesselQueryMessage = new FLUXVesselQueryMessage();
        fluxVesselQueryMessage.setVesselQuery(vesselQuery);
        return fluxVesselQueryMessage;
    }
}
