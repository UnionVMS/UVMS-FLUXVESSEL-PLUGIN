package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.mapper;

import eu.europa.ec.fisheries.schema.vessel.*;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetContact;
import eu.europa.ec.fisheries.wsdl.asset.types.HullMaterial;
import eu.europa.ec.fisheries.wsdl.asset.types.VesselEventType_0020;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class AssetMapperTest {

    @InjectMocks
    private AssetMapper assetMapper;

    @Mock
    private AssetContactMapper assetContactMapper;

    @Test
    public void fromFLUXReportVesselInformationWhenSuccess() {
        //data set
        String countryOfRegistration = "NLD";
        String cfr = "CFR";
        String uvi = "UVI";
        String registrationNumber = "registrationNumber";
        String externalMarking = "externalMarking";
        String name = "name";
        String registrationLocation = "registrationLocation";
        String ircs = "IRCS";
        String ircsIndicator = "Y";
        String licenseIndicator = "Y";
        String vmsIndicator = "Y";
        String ersIndicator = "Y";
        String aisIndicator = "Y";
        String mmsi = "MMSI";
        String vesselType = "FX";
        String mainFishingGear = "MAIN";
        String subsidiaryFishingGear = "SUB";
        Date eventDate = new Date();
        String eventType = "IMP";
        BigDecimal loa = BigDecimal.valueOf(21.3);
        BigDecimal lbp = BigDecimal.valueOf(29.4);
        BigDecimal grossTonnage = BigDecimal.valueOf(829.4);
        BigDecimal otherGrossTonnage = BigDecimal.valueOf(826.8);
        BigDecimal gts = BigDecimal.valueOf(926.8);
        BigDecimal powerMain = BigDecimal.valueOf(123);
        BigDecimal powerAux = BigDecimal.valueOf(124);
        String hullMaterial = "METAL";
        Date entryIntoService = new Date();
        String segment = "MFL";
        String countryOfImportOrExport = "BEL";
        String typeOfExport = "typeOfExport";
        String publicAid = "public aid";
        String yearOfConstruction = "1994";
        DateTime dateTimeOfConstruction = new DateTime().withYear(Integer.parseInt(yearOfConstruction));

        AssetContact assetContact1 = new AssetContact();
        assetContact1.setName("Stijn");
        AssetContact assetContact2 = new AssetContact();
        assetContact2.setName("Hooft");
        List<AssetContact> assetContacts = Arrays.asList(assetContact1, assetContact2);

        ContactPartyType contactPartyType1 = new ContactPartyType().withName(new TextType().withValue("Stijn"));
        ContactPartyType contactPartyType2 = new ContactPartyType().withName(new TextType().withValue("Hooft"));
        List<ContactPartyType> contactPartyTypes = Arrays.asList(contactPartyType1, contactPartyType2);

        VesselCountryType vesselCountry = new VesselCountryType();
        vesselCountry.setID(new IDType().withValue(countryOfRegistration).withSchemeID("TERRITORY"));

        RegistrationLocationType registrationLocationType = new RegistrationLocationType();
        registrationLocationType.getIDS().add(new IDType().withValue(registrationLocation));
        registrationLocationType.setCountryID(new IDType().withValue(countryOfImportOrExport));

        RegistrationEventType registrationEvent = new RegistrationEventType();
        registrationEvent.setRelatedRegistrationLocation(registrationLocationType);

        VesselEquipmentCharacteristicType ircsCharacteristic = new VesselEquipmentCharacteristicType();
        ircsCharacteristic.setTypeCode(new CodeType().withValue("IRCS_IND"));
        ircsCharacteristic.getValueCodes().add(new CodeType().withValue(ircsIndicator));

        VesselAdministrativeCharacteristicType licenseCharacteristic = new VesselAdministrativeCharacteristicType();
        licenseCharacteristic.setTypeCode(new CodeType().withListID("FLUX_VESSEL_ADMIN_TYPE").withValue("LICENCE"));
        licenseCharacteristic.setValueCode(new CodeType().withValue(licenseIndicator));

        VesselAdministrativeCharacteristicType entryIntoServiceCharacteristic = new VesselAdministrativeCharacteristicType();
        entryIntoServiceCharacteristic.setTypeCode(new CodeType().withListID("FLUX_VESSEL_ADMIN_TYPE").withValue("EIS"));
        entryIntoServiceCharacteristic.setValueDateTime(new DateTimeType().withDateTime(new DateTime(entryIntoService)));

        VesselAdministrativeCharacteristicType segmentCharacteristic = new VesselAdministrativeCharacteristicType();
        segmentCharacteristic.setTypeCode(new CodeType().withListID("FLUX_VESSEL_ADMIN_TYPE").withValue("SEG"));
        segmentCharacteristic.setValueCode(new CodeType().withValue(segment));

        VesselAdministrativeCharacteristicType typeOfExportCharacteristic = new VesselAdministrativeCharacteristicType();
        typeOfExportCharacteristic.setTypeCode(new CodeType().withListID("FLUX_VESSEL_ADMIN_TYPE").withValue("EXPORT"));
        typeOfExportCharacteristic.setValueCode(new CodeType().withValue(typeOfExport));

        VesselAdministrativeCharacteristicType publicAidCharacteristic = new VesselAdministrativeCharacteristicType();
        publicAidCharacteristic.setTypeCode(new CodeType().withListID("FLUX_VESSEL_ADMIN_TYPE").withValue("AID"));
        publicAidCharacteristic.setValueCode(new CodeType().withValue(publicAid));

        VesselEquipmentCharacteristicType vmsCharacteristic = new VesselEquipmentCharacteristicType();
        vmsCharacteristic.setTypeCode(new CodeType().withValue("VMS_IND"));
        vmsCharacteristic.getValueCodes().add(new CodeType().withValue(vmsIndicator));

        VesselEquipmentCharacteristicType ersCharacteristic = new VesselEquipmentCharacteristicType();
        ersCharacteristic.setTypeCode(new CodeType().withValue("ERS_IND"));
        ersCharacteristic.getValueCodes().add(new CodeType().withValue(ersIndicator));

        VesselEquipmentCharacteristicType aisCharacteristic = new VesselEquipmentCharacteristicType();
        aisCharacteristic.setTypeCode(new CodeType().withValue("AIS_IND"));
        aisCharacteristic.getValueCodes().add(new CodeType().withValue(aisIndicator));

        FishingGearType mainFishingGearType = new FishingGearType();
        mainFishingGearType.getRoleCodes().add(new CodeType().withListID("FLUX_VESSEL_GEAR_ROLE").withValue("MAIN"));
        mainFishingGearType.setTypeCode(new CodeType().withListID("GEAR_TYPE").withValue(mainFishingGear));

        FishingGearType subsidiaryFishingGearType = new FishingGearType();
        subsidiaryFishingGearType.getRoleCodes().add(new CodeType().withListID("FLUX_VESSEL_GEAR_ROLE").withValue("AUX"));
        subsidiaryFishingGearType.setTypeCode(new CodeType().withListID("GEAR_TYPE").withValue(subsidiaryFishingGear));

        VesselDimensionType loaType = new VesselDimensionType();
        loaType.setTypeCode(new CodeType().withListID("FLUX_VESSEL_DIM_TYPE").withValue("LOA"));
        loaType.setValueMeasure(new MeasureType().withUnitCode("MTR").withValue(loa));

        VesselDimensionType lbpType = new VesselDimensionType();
        lbpType.setTypeCode(new CodeType().withListID("FLUX_VESSEL_DIM_TYPE").withValue("LBP"));
        lbpType.setValueMeasure(new MeasureType().withUnitCode("MTR").withValue(lbp));

        VesselDimensionType grossTonnageType = new VesselDimensionType();
        grossTonnageType.setTypeCode(new CodeType().withListID("FLUX_VESSEL_DIM_TYPE").withValue("GT"));
        grossTonnageType.setValueMeasure(new MeasureType().withUnitCode("TNE").withValue(grossTonnage));

        VesselDimensionType otherGrossTonnageType = new VesselDimensionType();
        otherGrossTonnageType.setTypeCode(new CodeType().withListID("FLUX_VESSEL_DIM_TYPE").withValue("TOTH"));
        otherGrossTonnageType.setValueMeasure(new MeasureType().withUnitCode("TNE").withValue(otherGrossTonnage));

        VesselDimensionType gtsType = new VesselDimensionType();
        gtsType.setTypeCode(new CodeType().withListID("FLUX_VESSEL_DIM_TYPE").withValue("GTS"));
        gtsType.setValueMeasure(new MeasureType().withUnitCode("TNE").withValue(gts));

        VesselEngineType powerMainType = new VesselEngineType();
        powerMainType.setRoleCode(new CodeType().withListID("FLUX_VESSEL_ENGINE_ROLE").withValue("MAIN"));
        powerMainType.getPowerMeasures().add(new MeasureType().withUnitCode("KWT").withValue(powerMain));

        VesselEngineType powerAuxType = new VesselEngineType();
        powerAuxType.setRoleCode(new CodeType().withListID("FLUX_VESSEL_ENGINE_ROLE").withValue("AUX"));
        powerAuxType.getPowerMeasures().add(new MeasureType().withUnitCode("KWT").withValue(powerAux));

        VesselTechnicalCharacteristicType hullMaterialType = new VesselTechnicalCharacteristicType();
        hullMaterialType.setTypeCode(new CodeType().withListID("FLUX_VESSEL_TECH_TYPE").withValue("HULL"));
        hullMaterialType.setValueCode(new CodeType().withListID("VESSEL_HULL_TYPE").withValue(hullMaterial));

        ConstructionEventType constructionEvent = new ConstructionEventType();
        constructionEvent.setOccurrenceDateTime(new DateTimeType().withDateTime(dateTimeOfConstruction));

        VesselTransportMeansType vesselTransportMeans = new VesselTransportMeansType();
        vesselTransportMeans.getNames().add(new TextType().withValue(name));
        vesselTransportMeans.setRegistrationVesselCountry(vesselCountry);
        vesselTransportMeans.getIDS().add(new IDType().withSchemeID("CFR").withValue(cfr));
        vesselTransportMeans.getIDS().add(new IDType().withSchemeID("UVI").withValue(uvi));
        vesselTransportMeans.getIDS().add(new IDType().withSchemeID("REG_NBR").withValue(registrationNumber));
        vesselTransportMeans.getIDS().add(new IDType().withSchemeID("EXT_MARK").withValue(externalMarking));
        vesselTransportMeans.getIDS().add(new IDType().withSchemeID("IRCS").withValue(ircs));
        vesselTransportMeans.getIDS().add(new IDType().withSchemeID("MMSI").withValue(mmsi));
        vesselTransportMeans.getSpecifiedRegistrationEvents().add(registrationEvent);
        vesselTransportMeans.getApplicableVesselEquipmentCharacteristics().add(ircsCharacteristic);
        vesselTransportMeans.getApplicableVesselAdministrativeCharacteristics().add(licenseCharacteristic);
        vesselTransportMeans.getApplicableVesselAdministrativeCharacteristics().add(entryIntoServiceCharacteristic);
        vesselTransportMeans.getApplicableVesselAdministrativeCharacteristics().add(segmentCharacteristic);
        vesselTransportMeans.getApplicableVesselAdministrativeCharacteristics().add(typeOfExportCharacteristic);
        vesselTransportMeans.getApplicableVesselAdministrativeCharacteristics().add(publicAidCharacteristic);
        vesselTransportMeans.getApplicableVesselEquipmentCharacteristics().add(vmsCharacteristic);
        vesselTransportMeans.getApplicableVesselEquipmentCharacteristics().add(ersCharacteristic);
        vesselTransportMeans.getApplicableVesselEquipmentCharacteristics().add(aisCharacteristic);
        vesselTransportMeans.getTypeCodes().add(new CodeType().withListID("VESSEL_TYPE").withValue(vesselType));
        vesselTransportMeans.getOnBoardFishingGears().add(mainFishingGearType);
        vesselTransportMeans.getOnBoardFishingGears().add(subsidiaryFishingGearType);
        vesselTransportMeans.getSpecifiedVesselDimensions().add(loaType);
        vesselTransportMeans.getSpecifiedVesselDimensions().add(lbpType);
        vesselTransportMeans.getSpecifiedVesselDimensions().add(grossTonnageType);
        vesselTransportMeans.getSpecifiedVesselDimensions().add(otherGrossTonnageType);
        vesselTransportMeans.getSpecifiedVesselDimensions().add(gtsType);
        vesselTransportMeans.getAttachedVesselEngines().add(powerMainType);
        vesselTransportMeans.getAttachedVesselEngines().add(powerAuxType);
        vesselTransportMeans.getApplicableVesselTechnicalCharacteristics().add(hullMaterialType);
        vesselTransportMeans.setSpecifiedConstructionEvent(constructionEvent);
        vesselTransportMeans.getSpecifiedContactParties().addAll(contactPartyTypes);

        VesselEventType vesselEvent = new VesselEventType();
        vesselEvent.setRelatedVesselTransportMeans(vesselTransportMeans);
        vesselEvent.setOccurrenceDateTime(new DateTimeType().withDateTime(new DateTime(eventDate)));
        vesselEvent.setTypeCode(new CodeType().withValue(eventType));

        FLUXReportVesselInformation vesselInformation = new FLUXReportVesselInformation();
        vesselInformation.getVesselEvents().add(vesselEvent);

        //mock
        doReturn(assetContacts).when(assetContactMapper).fromContactPartyType(contactPartyTypes);

        //execute
        List<Asset> result = assetMapper.fromFLUXReportVesselInformation(vesselInformation);

        //assert
        assertEquals(1, result.size());
        Asset asset = result.get(0);
        assertEquals(countryOfRegistration, asset.getCountryCode());
        assertEquals(cfr, asset.getCfr());
        assertEquals(uvi, asset.getUvi());
        assertEquals(registrationNumber, asset.getRegistrationNumber());
        assertEquals(externalMarking, asset.getExternalMarking());
        assertEquals(name, asset.getName());
        assertEquals(registrationLocation, asset.getHomePort());
        assertEquals(ircs, asset.getIrcs());
        assertEquals(ircsIndicator, asset.getHasIrcs());
        assertTrue(asset.isHasLicense());
        assertTrue(asset.isVmsIndicator());
        assertTrue(asset.isErsIndicator());
        assertTrue(asset.isAisIndicator());
        assertEquals(mmsi, asset.getMmsiNo());
        assertEquals(vesselType, asset.getVesselType());
        assertEquals(mainFishingGear, asset.getMainFishingGear());
        assertEquals(subsidiaryFishingGear, asset.getSubsidiaryFishingGear());
        assertEquals(eventDate, asset.getDateOfEvent());
        assertEquals(VesselEventType_0020.IMP, asset.getVesselEventType());
        assertEquals(loa, asset.getLengthOverAll());
        assertEquals(lbp, asset.getLengthBetweenPerpendiculars());
        assertEquals(grossTonnage, asset.getGrossTonnage());
        assertEquals(otherGrossTonnage, asset.getOtherGrossTonnage());
        assertEquals(gts, asset.getSafetyGrossTonnage());
        assertEquals(powerMain, asset.getPowerMain());
        assertEquals(powerAux, asset.getPowerAux());
        assertEquals(HullMaterial.METAL, asset.getHullMaterial());
        assertEquals(entryIntoService, asset.getVesselDateOfEntry());
        assertEquals(segment, asset.getSegment());
        assertEquals(countryOfImportOrExport, asset.getCountryOfImportOrExport());
        assertEquals(typeOfExport, asset.getTypeOfExport());
        assertEquals(publicAid, asset.getPublicAid());
        assertEquals(yearOfConstruction, asset.getYearOfConstruction());
        assertEquals(assetContacts, asset.getContact());
    }

    @Test
    public void fromFLUXReportVesselInformationWhenNoDataIsFilledIn() {
        //data set
        VesselEventType vesselEvent = new VesselEventType();

        FLUXReportVesselInformation vesselInformation = new FLUXReportVesselInformation();
        vesselInformation.getVesselEvents().add(vesselEvent);

        //execute
        List<Asset> result = assetMapper.fromFLUXReportVesselInformation(vesselInformation);

        //assert
        assertEquals(1, result.size());
        Asset asset = result.get(0);
        assertNull(asset.getCountryCode());
        assertNull(asset.getCfr());
        assertNull(asset.getUvi());
        assertNull(asset.getRegistrationNumber());
        assertNull(asset.getExternalMarking());
        assertNull(asset.getName());
        assertNull(asset.getHomePort());
        assertNull(asset.getIrcs());
        assertNull(asset.getHasIrcs());
        assertFalse(asset.isHasLicense());
        assertNull(asset.isVmsIndicator());
        assertNull(asset.isErsIndicator());
        assertNull(asset.isAisIndicator());
        assertNull(asset.getMmsiNo());
        assertNull(asset.getVesselType());
        assertNull(asset.getMainFishingGear());
        assertNull(asset.getSubsidiaryFishingGear());
        assertNull(asset.getDateOfEvent());
        assertNull(asset.getVesselEventType());
        assertNull(asset.getLengthOverAll());
        assertNull(asset.getLengthBetweenPerpendiculars());
        assertNull(asset.getGrossTonnage());
        assertNull(asset.getOtherGrossTonnage());
        assertNull(asset.getSafetyGrossTonnage());
        assertNull(asset.getPowerMain());
        assertNull(asset.getPowerAux());
        assertNull(asset.getHullMaterial());
        assertNull(asset.getVesselDateOfEntry());
        assertNull(asset.getSegment());
        assertNull(asset.getCountryOfImportOrExport());
        assertNull(asset.getTypeOfExport());
        assertNull(asset.getPublicAid());
        assertNull(asset.getYearOfConstruction());
        assertTrue(asset.getContact().isEmpty());
        assertNull(asset.getImo());
    }

}