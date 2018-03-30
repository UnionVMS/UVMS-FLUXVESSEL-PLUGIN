package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.integrationtest.mother;

import eu.europa.ec.fisheries.schema.vessel.*;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetContact;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class FLUXReportVesselInformationMother {

    public static FLUXReportVesselInformation create() {
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
        DateTime eventDateTime = new DateTime(2018, 3, 30, 2, 31, 0, 0, DateTimeZone.UTC);
        String eventType = "IMP";
        BigDecimal loa = BigDecimal.valueOf(21.3);
        BigDecimal lbp = BigDecimal.valueOf(29.4);
        BigDecimal grossTonnage = BigDecimal.valueOf(829.4);
        BigDecimal otherGrossTonnage = BigDecimal.valueOf(826.8);
        BigDecimal gts = BigDecimal.valueOf(926.8);
        BigDecimal powerMain = BigDecimal.valueOf(123);
        BigDecimal powerAux = BigDecimal.valueOf(124);
        String hullMaterial = "2";
        DateTime entryIntoService = new DateTime(2018, 2, 2, 2, 31, 0, 0, DateTimeZone.UTC);
        String segment = "MFL";
        String countryOfImportOrExport = "BEL";
        String typeOfExport = "typeOfExport";
        String publicAid = "public aid";
        String yearOfConstruction = "1994";
        DateTime dateTimeOfConstruction = new DateTime(Integer.parseInt(yearOfConstruction), 1, 1, 0, 0, 0, 0, DateTimeZone.UTC);

        AssetContact assetContact1 = new AssetContact();
        assetContact1.setName("Stijn");
        AssetContact assetContact2 = new AssetContact();
        assetContact2.setName("Hooft");

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
        entryIntoServiceCharacteristic.setValueDateTime(new DateTimeType().withDateTime(entryIntoService));

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
        vesselEvent.setOccurrenceDateTime(new DateTimeType().withDateTime(eventDateTime));
        vesselEvent.setTypeCode(new CodeType().withValue(eventType));

        FLUXReportVesselInformation vesselInformation = new FLUXReportVesselInformation();
        vesselInformation.getVesselEvents().add(vesselEvent);

        return vesselInformation;
    }
}
