package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.mapper;

import eu.europa.ec.fisheries.schema.vessel.*;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.StartupBean;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetContact;
import eu.europa.ec.fisheries.wsdl.asset.types.HullMaterial;
import eu.europa.ec.fisheries.wsdl.asset.types.VesselEventType_0020;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class FLUXReportVesselInformationMapperTest {

    @InjectMocks
    private FLUXReportVesselInformationMapper mapper;

    @Mock
    private StartupBean startupBean;

    @Mock
    private ContactPartyTypeMapper contactPartyTypeMapper;

    @Test
    public void fromAsset() {
        //data set
        String countryOfRegistration = "BEL";
        String cfr = "CFR";
        String uvi = "UVI";
        VesselEventType_0020 vesselEventType = VesselEventType_0020.CST;
        Date eventOccurrence = new DateTime(2012, 12 ,12, 12, 12, 0, 0, DateTimeZone.UTC).toDate();
        String registrationNumber = "54654654";
        String externalMarking = "MY_SHIP_B*TCHES";
        String nameOfVessel = "AwesomeShip";
        String registrationLocation = "Haaltert";
        String ircs = "ircs";
        String hasIRCS = "Y";
        String hasLicense = "Y";
        String vmsIndicator = "Y";
        String ersIndicator = "Y";
        String aisIndicator = "Y";
        String mmsi = "123456789";
        String vesselType = "boat";
        String mainFishingGear = "main";
        String subsidiaryFishingGear = "subsidiary";
        BigDecimal lengthOverAll = BigDecimal.valueOf(12.3);
        BigDecimal lengthBetweenPerpendiculars = BigDecimal.valueOf(3.45);
        BigDecimal grossTonnage = BigDecimal.valueOf(56.7);
        BigDecimal otherGrossTonnage = BigDecimal.valueOf(8.92);
        BigDecimal safetyGrossTonnage = BigDecimal.valueOf(8.32);
        BigDecimal powerMain = BigDecimal.valueOf(373.2);
        BigDecimal powerAux = BigDecimal.valueOf(291.23);
        HullMaterial hullMaterial = HullMaterial.GLAS_PLASTIC_FIBER;
        Date entryIntoService = new DateTime(2012, 12, 12, 12, 12, 12, 0, DateTimeZone.UTC).toDate();
        String segment = "seg";
        String countryOfImportOrExport = "BEL";
        String typeOfExport = "test";
        String publicAid = "none";
        String yearOfConstruction = "2014";
        DateTime dateTimeOfConstruction = new DateTime(yearOfConstruction + "-01-01T00:00:00.00000Z").withZone(DateTimeZone.UTC);
        ContactPartyType contactPartyType = new ContactPartyType();
        AssetContact assetContact = new AssetContact();
        List<ContactPartyType> contactPartyTypes = Arrays.asList(contactPartyType);
        List<AssetContact> assetContacts = Arrays.asList(assetContact);


        Asset asset = new Asset();
        asset.setCountryCode(countryOfRegistration);
        asset.setCfr(cfr);
        asset.setUvi(uvi);
        asset.setVesselEventType(vesselEventType);
        asset.setDateOfEvent(eventOccurrence);
        asset.setRegistrationNumber(registrationNumber);
        asset.setExternalMarking(externalMarking);
        asset.setName(nameOfVessel);
        asset.setHomePort(registrationLocation);
        asset.setIrcs(ircs);
        asset.setHasIrcs(hasIRCS);
        asset.setHasLicense(true);
        asset.setVmsIndicator(true);
        asset.setErsIndicator(true);
        asset.setAisIndicator(true);
        asset.setMmsiNo(mmsi);
        asset.setVesselType(vesselType);
        asset.setMainFishingGear(mainFishingGear);
        asset.setSubsidiaryFishingGear(subsidiaryFishingGear);
        asset.setLengthOverAll(lengthOverAll);
        asset.setLengthBetweenPerpendiculars(lengthBetweenPerpendiculars);
        asset.setGrossTonnage(grossTonnage);
        asset.setOtherGrossTonnage(otherGrossTonnage);
        asset.setSafetyGrossTonnage(safetyGrossTonnage);
        asset.setPowerMain(powerMain);
        asset.setPowerAux(powerAux);
        asset.setHullMaterial(hullMaterial);
        asset.setVesselDateOfEntry(entryIntoService);
        asset.setSegment(segment);
        asset.setCountryOfImportOrExport(countryOfImportOrExport);
        asset.setTypeOfExport(typeOfExport);
        asset.setPublicAid(publicAid);
        asset.getContact().addAll(assetContacts);
        asset.setYearOfConstruction(yearOfConstruction);


        //mock
        doReturn("BEL").when(startupBean).getSetting("flux_local_nation_code");
        doReturn(contactPartyTypes).when(contactPartyTypeMapper).fromAssetContact(assetContacts);

        //execute
        FLUXReportVesselInformation vesselInformation = mapper.fromAsset(asset);
        VesselEventType vesselEventResult = vesselInformation.getVesselEvents().get(0);
        FLUXReportDocumentType fluxReportDocumentResult = vesselInformation.getFLUXReportDocument();
        VesselTransportMeansType vesselTransportMeansResult = vesselEventResult.getRelatedVesselTransportMeans();

        //assert
        assertEquals(1, fluxReportDocumentResult.getIDS().size());
        assertEquals("UUID", fluxReportDocumentResult.getIDS().get(0).getSchemeID());
        assertNotNull(fluxReportDocumentResult.getIDS().get(0).getValue());
        assertNotNull(fluxReportDocumentResult.getCreationDateTime().getDateTime());
        assertEquals(1, fluxReportDocumentResult.getOwnerFLUXParty().getIDS().size());
        assertEquals("FLUX_GP_PARTY", fluxReportDocumentResult.getOwnerFLUXParty().getIDS().get(0).getSchemeID());
        assertEquals("BEL", fluxReportDocumentResult.getOwnerFLUXParty().getIDS().get(0).getValue());
        assertEquals("FLUX_GP_PURPOSE", fluxReportDocumentResult.getPurposeCode().getListID());
        assertEquals("9", fluxReportDocumentResult.getPurposeCode().getValue());
        assertEquals("FLUX_VESSEL_REPORT_TYPE", fluxReportDocumentResult.getTypeCode().getListID());
        assertEquals("SUB-VCD", fluxReportDocumentResult.getTypeCode().getValue());
        assertEquals(1, vesselInformation.getVesselEvents().size());
        assertEquals("TERRITORY", vesselTransportMeansResult.getRegistrationVesselCountry().getID().getSchemeID());
        assertEquals(countryOfRegistration, vesselTransportMeansResult.getRegistrationVesselCountry().getID().getValue());
        assertEquals(6, vesselTransportMeansResult.getIDS().size());
        assertEquals("CFR", vesselTransportMeansResult.getIDS().get(0).getSchemeID());
        assertEquals(cfr, vesselTransportMeansResult.getIDS().get(0).getValue());
        assertEquals("UVI", vesselTransportMeansResult.getIDS().get(1).getSchemeID());
        assertEquals(uvi, vesselTransportMeansResult.getIDS().get(1).getValue());
        assertEquals("REG_NBR", vesselTransportMeansResult.getIDS().get(2).getSchemeID());
        assertEquals(registrationNumber, vesselTransportMeansResult.getIDS().get(2).getValue());
        assertEquals("EXT_MARK", vesselTransportMeansResult.getIDS().get(3).getSchemeID());
        assertEquals(externalMarking, vesselTransportMeansResult.getIDS().get(3).getValue());
        assertEquals("IRCS", vesselTransportMeansResult.getIDS().get(4).getSchemeID());
        assertEquals(ircs, vesselTransportMeansResult.getIDS().get(4).getValue());
        assertEquals("MMSI", vesselTransportMeansResult.getIDS().get(5).getSchemeID());
        assertEquals(mmsi, vesselTransportMeansResult.getIDS().get(5).getValue());
        assertEquals("VESSEL_EVENT", vesselEventResult.getTypeCode().getListID());
        assertEquals(vesselEventType.value(), vesselEventResult.getTypeCode().getValue());
        assertEquals("2012-12-12T12:12:00.000Z", vesselEventResult.getOccurrenceDateTime().getDateTime().toString());
        assertEquals(1, vesselTransportMeansResult.getNames().size());
        assertEquals(nameOfVessel, vesselTransportMeansResult.getNames().get(0).getValue());
        assertEquals(1, vesselTransportMeansResult.getSpecifiedRegistrationEvents().size());
        assertEquals(1, vesselTransportMeansResult.getSpecifiedRegistrationEvents().get(0).getRelatedRegistrationLocation().getIDS().size());
        assertEquals("VESSEL_PORT", vesselTransportMeansResult.getSpecifiedRegistrationEvents().get(0).getRelatedRegistrationLocation().getIDS().get(0).getSchemeID());
        assertEquals(registrationLocation, vesselTransportMeansResult.getSpecifiedRegistrationEvents().get(0).getRelatedRegistrationLocation().getIDS().get(0).getValue());
        assertEquals("TERRITORY", vesselTransportMeansResult.getSpecifiedRegistrationEvents().get(0).getRelatedRegistrationLocation().getCountryID().getSchemeID());
        assertEquals(countryOfImportOrExport, vesselTransportMeansResult.getSpecifiedRegistrationEvents().get(0).getRelatedRegistrationLocation().getCountryID().getValue());
        assertEquals(4, vesselTransportMeansResult.getApplicableVesselEquipmentCharacteristics().size());
        assertEquals("FLUX_VESSEL_EQUIP_TYPE", vesselTransportMeansResult.getApplicableVesselEquipmentCharacteristics().get(0).getTypeCode().getListID());
        assertEquals("IRCS_IND", vesselTransportMeansResult.getApplicableVesselEquipmentCharacteristics().get(0).getTypeCode().getValue());
        assertEquals(1, vesselTransportMeansResult.getApplicableVesselEquipmentCharacteristics().get(0).getValueCodes().size());
        assertEquals("BOOLEAN_TYPE", vesselTransportMeansResult.getApplicableVesselEquipmentCharacteristics().get(0).getValueCodes().get(0).getListID());
        assertEquals("Y", vesselTransportMeansResult.getApplicableVesselEquipmentCharacteristics().get(0).getValueCodes().get(0).getValue());
        assertEquals("FLUX_VESSEL_EQUIP_TYPE", vesselTransportMeansResult.getApplicableVesselEquipmentCharacteristics().get(1).getTypeCode().getListID());
        assertEquals("VMS_IND", vesselTransportMeansResult.getApplicableVesselEquipmentCharacteristics().get(1).getTypeCode().getValue());
        assertEquals(1, vesselTransportMeansResult.getApplicableVesselEquipmentCharacteristics().get(1).getValueCodes().size());
        assertEquals("BOOLEAN_TYPE", vesselTransportMeansResult.getApplicableVesselEquipmentCharacteristics().get(1).getValueCodes().get(0).getListID());
        assertEquals(vmsIndicator, vesselTransportMeansResult.getApplicableVesselEquipmentCharacteristics().get(1).getValueCodes().get(0).getValue());
        assertEquals("FLUX_VESSEL_EQUIP_TYPE", vesselTransportMeansResult.getApplicableVesselEquipmentCharacteristics().get(2).getTypeCode().getListID());
        assertEquals("ERS_IND", vesselTransportMeansResult.getApplicableVesselEquipmentCharacteristics().get(2).getTypeCode().getValue());
        assertEquals(1, vesselTransportMeansResult.getApplicableVesselEquipmentCharacteristics().get(2).getValueCodes().size());
        assertEquals("BOOLEAN_TYPE", vesselTransportMeansResult.getApplicableVesselEquipmentCharacteristics().get(2).getValueCodes().get(0).getListID());
        assertEquals(ersIndicator, vesselTransportMeansResult.getApplicableVesselEquipmentCharacteristics().get(2).getValueCodes().get(0).getValue());
        assertEquals("FLUX_VESSEL_EQUIP_TYPE", vesselTransportMeansResult.getApplicableVesselEquipmentCharacteristics().get(3).getTypeCode().getListID());
        assertEquals("AIS_IND", vesselTransportMeansResult.getApplicableVesselEquipmentCharacteristics().get(3).getTypeCode().getValue());
        assertEquals(1, vesselTransportMeansResult.getApplicableVesselEquipmentCharacteristics().get(3).getValueCodes().size());
        assertEquals("BOOLEAN_TYPE", vesselTransportMeansResult.getApplicableVesselEquipmentCharacteristics().get(3).getValueCodes().get(0).getListID());
        assertEquals(aisIndicator, vesselTransportMeansResult.getApplicableVesselEquipmentCharacteristics().get(3).getValueCodes().get(0).getValue());
        assertEquals(5, vesselTransportMeansResult.getApplicableVesselAdministrativeCharacteristics().size());
        assertEquals("FLUX_VESSEL_ADMIN_TYPE", vesselTransportMeansResult.getApplicableVesselAdministrativeCharacteristics().get(0).getTypeCode().getListID());
        assertEquals("LICENSE", vesselTransportMeansResult.getApplicableVesselAdministrativeCharacteristics().get(0).getTypeCode().getValue());
        assertEquals("BOOLEAN_TYPE", vesselTransportMeansResult.getApplicableVesselAdministrativeCharacteristics().get(0).getValueCode().getListID());
        assertEquals(hasLicense, vesselTransportMeansResult.getApplicableVesselAdministrativeCharacteristics().get(0).getValueCode().getValue());
        assertEquals("FLUX_VESSEL_ADMIN_TYPE", vesselTransportMeansResult.getApplicableVesselAdministrativeCharacteristics().get(1).getTypeCode().getListID());
        assertEquals("EIS", vesselTransportMeansResult.getApplicableVesselAdministrativeCharacteristics().get(1).getTypeCode().getValue());
        assertEquals("2012-12-12T12:12:12.000Z", vesselTransportMeansResult.getApplicableVesselAdministrativeCharacteristics().get(1).getValueDateTime().getDateTime().toString());
        assertEquals("FLUX_VESSEL_ADMIN_TYPE", vesselTransportMeansResult.getApplicableVesselAdministrativeCharacteristics().get(2).getTypeCode().getListID());
        assertEquals("SEG", vesselTransportMeansResult.getApplicableVesselAdministrativeCharacteristics().get(2).getTypeCode().getValue());
        assertEquals("VESSEL_SEGMENT", vesselTransportMeansResult.getApplicableVesselAdministrativeCharacteristics().get(2).getValueCode().getListID());
        assertEquals(segment, vesselTransportMeansResult.getApplicableVesselAdministrativeCharacteristics().get(2).getValueCode().getValue());
        assertEquals("FLUX_VESSEL_ADMIN_TYPE", vesselTransportMeansResult.getApplicableVesselAdministrativeCharacteristics().get(3).getTypeCode().getListID());
        assertEquals("EXPORT", vesselTransportMeansResult.getApplicableVesselAdministrativeCharacteristics().get(3).getTypeCode().getValue());
        assertEquals("VESSEL_EXPORT_TYPE", vesselTransportMeansResult.getApplicableVesselAdministrativeCharacteristics().get(3).getValueCode().getListID());
        assertEquals(typeOfExport, vesselTransportMeansResult.getApplicableVesselAdministrativeCharacteristics().get(3).getValueCode().getValue());
        assertEquals("FLUX_VESSEL_ADMIN_TYPE", vesselTransportMeansResult.getApplicableVesselAdministrativeCharacteristics().get(4).getTypeCode().getListID());
        assertEquals("AID", vesselTransportMeansResult.getApplicableVesselAdministrativeCharacteristics().get(4).getTypeCode().getValue());
        assertEquals("VESSEL_PUBLIC_AID_TYPE", vesselTransportMeansResult.getApplicableVesselAdministrativeCharacteristics().get(4).getValueCode().getListID());
        assertEquals(publicAid, vesselTransportMeansResult.getApplicableVesselAdministrativeCharacteristics().get(4).getValueCode().getValue());
        assertEquals(1, vesselTransportMeansResult.getTypeCodes().size());
        assertEquals("VESSEL_TYPE", vesselTransportMeansResult.getTypeCodes().get(0).getListID());
        assertEquals(vesselType, vesselTransportMeansResult.getTypeCodes().get(0).getValue());
        assertEquals(2, vesselTransportMeansResult.getOnBoardFishingGears().size());
        assertEquals(1, vesselTransportMeansResult.getOnBoardFishingGears().get(0).getRoleCodes().size());
        assertEquals("FLUX_VESSEL_GEAR_ROLE", vesselTransportMeansResult.getOnBoardFishingGears().get(0).getRoleCodes().get(0).getListID());
        assertEquals("MAIN", vesselTransportMeansResult.getOnBoardFishingGears().get(0).getRoleCodes().get(0).getValue());
        assertEquals("GEAR_TYPE", vesselTransportMeansResult.getOnBoardFishingGears().get(0).getTypeCode().getListID());
        assertEquals(mainFishingGear, vesselTransportMeansResult.getOnBoardFishingGears().get(0).getTypeCode().getValue());
        assertEquals(1, vesselTransportMeansResult.getOnBoardFishingGears().get(1).getRoleCodes().size());
        assertEquals("FLUX_VESSEL_GEAR_ROLE", vesselTransportMeansResult.getOnBoardFishingGears().get(1).getRoleCodes().get(0).getListID());
        assertEquals("AUX", vesselTransportMeansResult.getOnBoardFishingGears().get(1).getRoleCodes().get(0).getValue());
        assertEquals("GEAR_TYPE", vesselTransportMeansResult.getOnBoardFishingGears().get(1).getTypeCode().getListID());
        assertEquals(subsidiaryFishingGear, vesselTransportMeansResult.getOnBoardFishingGears().get(1).getTypeCode().getValue());
        assertEquals(5, vesselTransportMeansResult.getSpecifiedVesselDimensions().size());
        assertEquals("FLUX_VESSEL_DIM_TYPE", vesselTransportMeansResult.getSpecifiedVesselDimensions().get(0).getTypeCode().getListID());
        assertEquals("LOA", vesselTransportMeansResult.getSpecifiedVesselDimensions().get(0).getTypeCode().getValue());
        assertEquals("MTR", vesselTransportMeansResult.getSpecifiedVesselDimensions().get(0).getValueMeasure().getUnitCode());
        assertEquals(lengthOverAll, vesselTransportMeansResult.getSpecifiedVesselDimensions().get(0).getValueMeasure().getValue());
        assertEquals("FLUX_VESSEL_DIM_TYPE", vesselTransportMeansResult.getSpecifiedVesselDimensions().get(1).getTypeCode().getListID());
        assertEquals("LBP", vesselTransportMeansResult.getSpecifiedVesselDimensions().get(1).getTypeCode().getValue());
        assertEquals("MTR", vesselTransportMeansResult.getSpecifiedVesselDimensions().get(1).getValueMeasure().getUnitCode());
        assertEquals(lengthBetweenPerpendiculars, vesselTransportMeansResult.getSpecifiedVesselDimensions().get(1).getValueMeasure().getValue());
        assertEquals("FLUX_VESSEL_DIM_TYPE", vesselTransportMeansResult.getSpecifiedVesselDimensions().get(2).getTypeCode().getListID());
        assertEquals("GT", vesselTransportMeansResult.getSpecifiedVesselDimensions().get(2).getTypeCode().getValue());
        assertEquals("TNE", vesselTransportMeansResult.getSpecifiedVesselDimensions().get(2).getValueMeasure().getUnitCode());
        assertEquals(grossTonnage, vesselTransportMeansResult.getSpecifiedVesselDimensions().get(2).getValueMeasure().getValue());
        assertEquals("FLUX_VESSEL_DIM_TYPE", vesselTransportMeansResult.getSpecifiedVesselDimensions().get(3).getTypeCode().getListID());
        assertEquals("TOTH", vesselTransportMeansResult.getSpecifiedVesselDimensions().get(3).getTypeCode().getValue());
        assertEquals("TNE", vesselTransportMeansResult.getSpecifiedVesselDimensions().get(3).getValueMeasure().getUnitCode());
        assertEquals(otherGrossTonnage, vesselTransportMeansResult.getSpecifiedVesselDimensions().get(3).getValueMeasure().getValue());
        assertEquals("FLUX_VESSEL_DIM_TYPE", vesselTransportMeansResult.getSpecifiedVesselDimensions().get(4).getTypeCode().getListID());
        assertEquals("GTS", vesselTransportMeansResult.getSpecifiedVesselDimensions().get(4).getTypeCode().getValue());
        assertEquals("TNE", vesselTransportMeansResult.getSpecifiedVesselDimensions().get(4).getValueMeasure().getUnitCode());
        assertEquals(safetyGrossTonnage, vesselTransportMeansResult.getSpecifiedVesselDimensions().get(4).getValueMeasure().getValue());
        assertEquals(2, vesselTransportMeansResult.getAttachedVesselEngines().size());
        assertEquals("FLUX_VESSEL_ENGINE_ROLE", vesselTransportMeansResult.getAttachedVesselEngines().get(0).getRoleCode().getListID());
        assertEquals("MAIN", vesselTransportMeansResult.getAttachedVesselEngines().get(0).getRoleCode().getValue());
        assertEquals(1, vesselTransportMeansResult.getAttachedVesselEngines().get(0).getPowerMeasures().size());
        assertEquals("KWT", vesselTransportMeansResult.getAttachedVesselEngines().get(0).getPowerMeasures().get(0).getUnitCode());
        assertEquals(powerMain, vesselTransportMeansResult.getAttachedVesselEngines().get(0).getPowerMeasures().get(0).getValue());
        assertEquals("FLUX_VESSEL_ENGINE_ROLE", vesselTransportMeansResult.getAttachedVesselEngines().get(1).getRoleCode().getListID());
        assertEquals("AUX", vesselTransportMeansResult.getAttachedVesselEngines().get(1).getRoleCode().getValue());
        assertEquals(1, vesselTransportMeansResult.getAttachedVesselEngines().get(1).getPowerMeasures().size());
        assertEquals("KWT", vesselTransportMeansResult.getAttachedVesselEngines().get(1).getPowerMeasures().get(0).getUnitCode());
        assertEquals(powerAux, vesselTransportMeansResult.getAttachedVesselEngines().get(1).getPowerMeasures().get(0).getValue());
        assertEquals(1, vesselTransportMeansResult.getApplicableVesselTechnicalCharacteristics().size());
        assertEquals("FLUX_VESSEL_TECH_TYPE", vesselTransportMeansResult.getApplicableVesselTechnicalCharacteristics().get(0).getTypeCode().getListID());
        assertEquals("HULL", vesselTransportMeansResult.getApplicableVesselTechnicalCharacteristics().get(0).getTypeCode().getValue());
        assertEquals("VESSEL_HULL_TYPE", vesselTransportMeansResult.getApplicableVesselTechnicalCharacteristics().get(0).getValueCode().getListID());
        assertEquals(asset.getHullMaterial().value(), vesselTransportMeansResult.getApplicableVesselTechnicalCharacteristics().get(0).getValueCode().getValue());
        assertEquals(contactPartyTypes, vesselTransportMeansResult.getSpecifiedContactParties());
        assertEquals(dateTimeOfConstruction, vesselTransportMeansResult.getSpecifiedConstructionEvent().getOccurrenceDateTime().getDateTime());
    }
}