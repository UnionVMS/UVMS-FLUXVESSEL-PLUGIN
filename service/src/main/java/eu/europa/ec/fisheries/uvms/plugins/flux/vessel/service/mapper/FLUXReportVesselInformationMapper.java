package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.mapper;

import eu.europa.ec.fisheries.schema.vessel.*;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.StartupBean;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.constants.Settings;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Stateless
public class FLUXReportVesselInformationMapper {

    public static final String FLUX_VESSEL_ADMIN_TYPE = "FLUX_VESSEL_ADMIN_TYPE";
    public static final String FLUX_VESSEL_DIM_TYPE = "FLUX_VESSEL_DIM_TYPE";
    public static final String FLUX_VESSEL_EQUIP_TYPE = "FLUX_VESSEL_EQUIP_TYPE";
    public static final String BOOLEAN_TYPE = "BOOLEAN_TYPE";
    @EJB
    private StartupBean startupBean;

    @EJB
    private ContactPartyTypeMapper contactPartyTypeMapper;

    public FLUXReportVesselInformation fromConnector2BridgeRequest(Connector2BridgeRequest request) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(FLUXReportVesselInformation.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        return (FLUXReportVesselInformation) unmarshaller.unmarshal(request.getAny());
    }

    public List<FLUXReportVesselInformation> fromAssets(Collection<Asset> assets) {
        return assets.stream()
                .map(this::fromAsset)
                .collect(Collectors.toList());
    }

    public FLUXReportVesselInformation fromAsset(Asset asset) {

        VesselCountryType countryOfRegistration = new VesselCountryType();
        countryOfRegistration.setID(new IDType()
                .withSchemeID("TERRITORY")
                .withValue(asset.getCountryCode()));

        RegistrationLocationType registrationLocation = new RegistrationLocationType();
        registrationLocation.getIDS().add(new IDType()
                .withSchemeID("VESSEL_PORT")
                .withValue(asset.getHomePort()));
        registrationLocation.setCountryID(new IDType()
                .withSchemeID("TERRITORY")
                .withValue(asset.getCountryOfImportOrExport()));
        RegistrationEventType registrationEvent = new RegistrationEventType();
        registrationEvent.setRelatedRegistrationLocation(registrationLocation);

        VesselEquipmentCharacteristicType ircsCharacteristic = new VesselEquipmentCharacteristicType();
        ircsCharacteristic.setTypeCode(new CodeType()
                .withListID(FLUX_VESSEL_EQUIP_TYPE)
                .withValue("IRCS_IND"));
        ircsCharacteristic.getValueCodes().add(new CodeType()
                .withListID(BOOLEAN_TYPE)
                .withValue(asset.getHasIrcs()));

        VesselEquipmentCharacteristicType vmsCharacteristic = new VesselEquipmentCharacteristicType();
        vmsCharacteristic.setTypeCode(new CodeType()
                .withListID(FLUX_VESSEL_EQUIP_TYPE)
                .withValue("VMS_IND"));
        vmsCharacteristic.getValueCodes().add(new CodeType()
                .withListID(BOOLEAN_TYPE)
                .withValue(asset.isVmsIndicator() == null ? null : asset.isVmsIndicator() ? "Y" : "N"));

        VesselEquipmentCharacteristicType ersCharacteristic = new VesselEquipmentCharacteristicType();
        ersCharacteristic.setTypeCode(new CodeType()
                .withListID(FLUX_VESSEL_EQUIP_TYPE)
                .withValue("ERS_IND"));
        ersCharacteristic.getValueCodes().add(new CodeType()
                .withListID(BOOLEAN_TYPE)
                .withValue(asset.isErsIndicator() == null ? null : asset.isErsIndicator() ? "Y" : "N"));

        VesselEquipmentCharacteristicType aisCharacteristic = new VesselEquipmentCharacteristicType();
        aisCharacteristic.setTypeCode(new CodeType()
                .withListID(FLUX_VESSEL_EQUIP_TYPE)
                .withValue("AIS_IND"));
        aisCharacteristic.getValueCodes().add(new CodeType()
                .withListID(BOOLEAN_TYPE)
                .withValue(asset.isAisIndicator() == null ? null : asset.isAisIndicator() ? "Y" : "N"));

        VesselAdministrativeCharacteristicType licenseCharacteristic = new VesselAdministrativeCharacteristicType();
        licenseCharacteristic.setTypeCode(new CodeType()
                .withListID(FLUX_VESSEL_ADMIN_TYPE)
                .withValue("LICENSE"));
        licenseCharacteristic.setValueCode(new CodeType()
                .withListID(BOOLEAN_TYPE)
                .withValue(asset.isHasLicense() ? "Y" : "N"));

        VesselAdministrativeCharacteristicType entryIntoService = new VesselAdministrativeCharacteristicType();
        entryIntoService.setTypeCode(new CodeType()
                .withListID(FLUX_VESSEL_ADMIN_TYPE)
                .withValue("EIS"));
        entryIntoService.setValueDateTime(new DateTimeType()
                .withDateTime(new DateTime(asset.getVesselDateOfEntry()).withZone(DateTimeZone.UTC)));

        VesselAdministrativeCharacteristicType segment = new VesselAdministrativeCharacteristicType();
        segment.setTypeCode(new CodeType()
                .withListID(FLUX_VESSEL_ADMIN_TYPE)
                .withValue("SEG"));
        segment.setValueCode(new CodeType()
                .withListID("VESSEL_SEGMENT")
                .withValue(asset.getSegment()));

        VesselAdministrativeCharacteristicType typeOfExport = new VesselAdministrativeCharacteristicType();
        typeOfExport.setTypeCode(new CodeType()
                .withListID(FLUX_VESSEL_ADMIN_TYPE)
                .withValue("EXPORT"));
        typeOfExport.setValueCode(new CodeType()
                .withListID("VESSEL_EXPORT_TYPE")
                .withValue(asset.getTypeOfExport()));

        VesselAdministrativeCharacteristicType publicAid = new VesselAdministrativeCharacteristicType();
        publicAid.setTypeCode(new CodeType()
                .withListID(FLUX_VESSEL_ADMIN_TYPE)
                .withValue("AID"));
        publicAid.setValueCode(new CodeType()
                .withListID("VESSEL_PUBLIC_AID_TYPE")
                .withValue(asset.getPublicAid()));

        FishingGearType mainGear = new FishingGearType();
        mainGear.getRoleCodes().add(new CodeType()
                .withListID("FLUX_VESSEL_GEAR_ROLE")
                .withValue("MAIN"));
        mainGear.setTypeCode(new CodeType()
                .withListID("GEAR_TYPE")
                .withValue(asset.getMainFishingGear()));

        FishingGearType subsidiaryGear = new FishingGearType();
        subsidiaryGear.getRoleCodes().add(new CodeType()
                .withListID("FLUX_VESSEL_GEAR_ROLE")
                .withValue("AUX"));
        subsidiaryGear.setTypeCode(new CodeType()
                .withListID("GEAR_TYPE")
                .withValue(asset.getSubsidiaryFishingGear()));

        VesselDimensionType lengthOverAll = new VesselDimensionType();
        lengthOverAll.setTypeCode(new CodeType()
                .withListID(FLUX_VESSEL_DIM_TYPE)
                .withValue("LOA"));
        lengthOverAll.setValueMeasure(new MeasureType()
                .withUnitCode("MTR")
                .withValue(asset.getLengthOverAll()));

        VesselDimensionType lengthBetweenPerpendiculars = new VesselDimensionType();
        lengthBetweenPerpendiculars.setTypeCode(new CodeType()
                .withListID(FLUX_VESSEL_DIM_TYPE)
                .withValue("LBP"));
        lengthBetweenPerpendiculars.setValueMeasure(new MeasureType()
                .withUnitCode("MTR")
                .withValue(asset.getLengthBetweenPerpendiculars()));

        VesselDimensionType grossTonnage = new VesselDimensionType();
        grossTonnage.setTypeCode(new CodeType()
                .withListID(FLUX_VESSEL_DIM_TYPE)
                .withValue("GT"));
        grossTonnage.setValueMeasure(new MeasureType()
                .withUnitCode("TNE")
                .withValue(asset.getGrossTonnage()));

        VesselDimensionType otherGrossTonnage = new VesselDimensionType();
        otherGrossTonnage.setTypeCode(new CodeType()
                .withListID(FLUX_VESSEL_DIM_TYPE)
                .withValue("TOTH"));
        otherGrossTonnage.setValueMeasure(new MeasureType()
                .withUnitCode("TNE")
                .withValue(asset.getOtherGrossTonnage()));

        VesselDimensionType safetyGrossTonnage = new VesselDimensionType();
        safetyGrossTonnage.setTypeCode(new CodeType()
                .withListID(FLUX_VESSEL_DIM_TYPE)
                .withValue("GTS"));
        safetyGrossTonnage.setValueMeasure(new MeasureType()
                .withUnitCode("TNE")
                .withValue(asset.getSafetyGrossTonnage()));

        VesselEngineType powerMainType = new VesselEngineType();
        powerMainType.setRoleCode(new CodeType()
                .withListID("FLUX_VESSEL_ENGINE_ROLE")
                .withValue("MAIN"));
        powerMainType.getPowerMeasures().add(new MeasureType()
                .withUnitCode("KWT")
                .withValue(asset.getPowerMain()));

        VesselEngineType powerAuxType = new VesselEngineType();
        powerAuxType.setRoleCode(new CodeType()
                .withListID("FLUX_VESSEL_ENGINE_ROLE")
                .withValue("AUX"));
        powerAuxType.getPowerMeasures().add(new MeasureType()
                .withUnitCode("KWT")
                .withValue(asset.getPowerAux()));

        VesselTechnicalCharacteristicType hullMaterialType = new VesselTechnicalCharacteristicType();
        hullMaterialType.setTypeCode(new CodeType()
                .withListID("FLUX_VESSEL_TECH_TYPE")
                .withValue("HULL"));
        hullMaterialType.setValueCode(new CodeType()
                .withListID("VESSEL_HULL_TYPE")
                .withValue(asset.getHullMaterial()));

        int yearOfConstruction = Integer.parseInt(asset.getYearOfConstruction());
        ConstructionEventType constructionEvent = new ConstructionEventType()
                .withOccurrenceDateTime(new DateTimeType()
                .withDateTime(new DateTime(yearOfConstruction, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC)));

        VesselTransportMeansType vesselTransportMeans = new VesselTransportMeansType();
        vesselTransportMeans.setRegistrationVesselCountry(countryOfRegistration);
        vesselTransportMeans.getIDS().add(new IDType()
                .withSchemeID("CFR")
                .withValue(asset.getCfr()));
        vesselTransportMeans.getIDS().add(new IDType()
                .withSchemeID("UVI")
                .withValue(asset.getUvi()));
        vesselTransportMeans.getIDS().add(new IDType()
                .withSchemeID("REG_NBR")
                .withValue(asset.getRegistrationNumber()));
        vesselTransportMeans.getIDS().add(new IDType()
                .withSchemeID("EXT_MARK")
                .withValue(asset.getExternalMarking()));
        vesselTransportMeans.getIDS().add(new IDType()
                .withSchemeID("IRCS")
                .withValue(asset.getIrcs()));
        vesselTransportMeans.getIDS().add(new IDType()
                .withSchemeID("MMSI")
                .withValue(asset.getMmsiNo()));
        vesselTransportMeans.getNames().add(new TextType()
                .withValue(asset.getName()));
        vesselTransportMeans.getSpecifiedRegistrationEvents().add(registrationEvent);
        vesselTransportMeans.getApplicableVesselEquipmentCharacteristics().add(ircsCharacteristic);
        vesselTransportMeans.getApplicableVesselEquipmentCharacteristics().add(vmsCharacteristic);
        vesselTransportMeans.getApplicableVesselEquipmentCharacteristics().add(ersCharacteristic);
        vesselTransportMeans.getApplicableVesselEquipmentCharacteristics().add(aisCharacteristic);
        vesselTransportMeans.getApplicableVesselAdministrativeCharacteristics().add(licenseCharacteristic);
        vesselTransportMeans.getApplicableVesselAdministrativeCharacteristics().add(entryIntoService);
        vesselTransportMeans.getApplicableVesselAdministrativeCharacteristics().add(segment);
        vesselTransportMeans.getApplicableVesselAdministrativeCharacteristics().add(typeOfExport);
        vesselTransportMeans.getApplicableVesselAdministrativeCharacteristics().add(publicAid);
        vesselTransportMeans.getTypeCodes().add(new CodeType()
                .withListID("VESSEL_TYPE")
                .withValue(asset.getVesselType()));
        vesselTransportMeans.getOnBoardFishingGears().add(mainGear);
        vesselTransportMeans.getOnBoardFishingGears().add(subsidiaryGear);
        vesselTransportMeans.getSpecifiedVesselDimensions().add(lengthOverAll);
        vesselTransportMeans.getSpecifiedVesselDimensions().add(lengthBetweenPerpendiculars);
        vesselTransportMeans.getSpecifiedVesselDimensions().add(grossTonnage);
        vesselTransportMeans.getSpecifiedVesselDimensions().add(otherGrossTonnage);
        vesselTransportMeans.getSpecifiedVesselDimensions().add(safetyGrossTonnage);
        vesselTransportMeans.getAttachedVesselEngines().add(powerMainType);
        vesselTransportMeans.getAttachedVesselEngines().add(powerAuxType);
        vesselTransportMeans.getApplicableVesselTechnicalCharacteristics().add(hullMaterialType);
        vesselTransportMeans.getSpecifiedContactParties().addAll(contactPartyTypeMapper.fromAssetContact(asset.getContact()));
        vesselTransportMeans.setSpecifiedConstructionEvent(constructionEvent);

        VesselEventType vesselEvent = new VesselEventType();
        vesselEvent.setRelatedVesselTransportMeans(vesselTransportMeans);
        vesselEvent.setTypeCode(new CodeType()
                .withListID("VESSEL_EVENT")
                .withValue(asset.getVesselEventType().value()));
        vesselEvent.setOccurrenceDateTime(new DateTimeType()
                .withDateTime(new DateTime(asset.getDateOfEvent()).withZone(DateTimeZone.UTC)));

        FLUXReportVesselInformation vesselInformation = new FLUXReportVesselInformation();
        vesselInformation.setFLUXReportDocument(createNewFLUXReportDocumentType());
        vesselInformation.getVesselEvents().add(vesselEvent);
        return vesselInformation;
    }

    private FLUXReportDocumentType createNewFLUXReportDocumentType() {
        String countryOfHost = startupBean.getSetting(Settings.FLUX_LOCAL_NATION_CODE);

        FLUXReportDocumentType fluxReportDocument = new FLUXReportDocumentType();

        fluxReportDocument.getIDS().add(new IDType()
                .withSchemeID("UUID")
                .withValue(UUID.randomUUID().toString()));
        fluxReportDocument.setCreationDateTime(new DateTimeType()
                .withDateTime(new DateTime().withZone(DateTimeZone.UTC)));
        fluxReportDocument.setOwnerFLUXParty(new FLUXPartyType().withIDS(new IDType()
                .withSchemeID("FLUX_GP_PARTY")
                .withValue(countryOfHost)));
        fluxReportDocument.setPurposeCode(new CodeType()
                .withListID("FLUX_GP_PURPOSE")
                .withValue("9"));
        fluxReportDocument.setTypeCode(new CodeType()
                .withListID("FLUX_VESSEL_REPORT_TYPE")
                .withValue("SUB-VCD"));
        return fluxReportDocument;
    }

}
