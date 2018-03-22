package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.mapper;

import eu.europa.ec.fisheries.schema.vessel.FLUXReportVesselInformation;
import eu.europa.ec.fisheries.schema.vessel.IDType;
import eu.europa.ec.fisheries.schema.vessel.VesselEventType;
import eu.europa.ec.fisheries.schema.vessel.VesselTransportMeansType;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetContact;
import eu.europa.ec.fisheries.wsdl.asset.types.HullMaterial;
import eu.europa.ec.fisheries.wsdl.asset.types.VesselEventType_0020;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.math.BigDecimal;
import java.util.*;

@Stateless
@Slf4j
public class AssetMapper {

    @EJB
    private AssetContactMapper assetContactMapper;

    public List<Asset> fromFLUXReportVesselInformation(FLUXReportVesselInformation vesselInformation) {
        List<Asset> assets = new ArrayList<>();
        for (VesselEventType vesselEvent : vesselInformation.getVesselEvents()) {
            VesselTransportMeansType vesselTransportMeans = vesselEvent.getRelatedVesselTransportMeans();

            Asset asset = new Asset();
            asset.setCountryCode(getCountryOfRegistration(vesselTransportMeans));
            asset.setCfr(getCFR(vesselTransportMeans));
            asset.setUvi(getUVI(vesselTransportMeans));
            asset.setVesselEventType(getEventType(vesselEvent));
            asset.setDateOfEvent(getDateOfEvent(vesselEvent));
            asset.setRegistrationNumber(getRegistrationNumber(vesselTransportMeans));
            asset.setExternalMarking(getExternalMarking(vesselTransportMeans));
            asset.setName(getName(vesselTransportMeans));
            asset.setHomePort(getRegistrationLocation(vesselTransportMeans));
            asset.setIrcs(getIRCS(vesselTransportMeans));
            asset.setHasIrcs(getIRCSIndicator(vesselTransportMeans));
            asset.setHasLicense(getLicenseIndicator(vesselTransportMeans));
            asset.setVmsIndicator(getVMSIndicator(vesselTransportMeans));
            asset.setErsIndicator(getERSIndicator(vesselTransportMeans));
            asset.setAisIndicator(getAISIndicator(vesselTransportMeans));
            asset.setMmsiNo(getMMSI(vesselTransportMeans));
            asset.setVesselType(getVesselType(vesselTransportMeans));
            asset.setMainFishingGear(getMainFishingGearType(vesselTransportMeans));
            asset.setSubsidiaryFishingGear(getSubsidiaryFishingGearType(vesselTransportMeans));
            asset.setLengthOverAll(getLengthOverAll(vesselTransportMeans));
            asset.setLengthBetweenPerpendiculars(getLengthBetweenPerpendiculars(vesselTransportMeans));
            asset.setGrossTonnage(getGrossTonnage(vesselTransportMeans));
            asset.setOtherGrossTonnage(getOtherGrossTonnage(vesselTransportMeans));
            asset.setSafetyGrossTonnage(getSafetyGrossTonnage(vesselTransportMeans));
            asset.setPowerMain(getPowerMain(vesselTransportMeans));
            asset.setPowerAux(getPowerAux(vesselTransportMeans));
            asset.setHullMaterial(getHullMaterial(vesselTransportMeans));
            asset.setVesselDateOfEntry(getEntryIntoService(vesselTransportMeans));
            asset.setSegment(getSegment(vesselTransportMeans));
            asset.setCountryOfImportOrExport(getCountryOfImportOrExport(vesselTransportMeans));
            asset.setTypeOfExport(getTypeOfExport(vesselTransportMeans));
            asset.setPublicAid(getPublicAid(vesselTransportMeans));
            asset.setYearOfConstruction(getYearOfConstruction(vesselTransportMeans));
            asset.getContact().addAll(getContacts(vesselTransportMeans));

            assets.add(asset);
        }
        return assets;
    }

    private Collection<? extends AssetContact> getContacts(VesselTransportMeansType vesselTransportMeans) {
        try {
            return assetContactMapper.fromContactPartyType(vesselTransportMeans.getSpecifiedContactParties());
        } catch (RuntimeException ex) {
            log.info("Vessel contains no contacts", ex);
            return new ArrayList<>();
        }
    }

    private String getYearOfConstruction(VesselTransportMeansType vesselTransportMeans) {
        try {
            return vesselTransportMeans.getSpecifiedConstructionEvent()
                    .getOccurrenceDateTime()
                    .getDateTime()
                    .getYear() + "";
        } catch (RuntimeException ex) {
            log.info("Vessel contains no construction date", ex);
            return null;
        }
    }

    private String getCountryOfImportOrExport(VesselTransportMeansType vesselTransportMeans) {
        try {
            return vesselTransportMeans.getSpecifiedRegistrationEvents().get(0)
                    .getRelatedRegistrationLocation().getCountryID().getValue();
        } catch (RuntimeException ex) {
            log.info("Vessel contains no country of import or export", ex);
            return null;
        }
    }

    private HullMaterial getHullMaterial(VesselTransportMeansType vesselTransportMeans) {
        try {
            return vesselTransportMeans.getApplicableVesselTechnicalCharacteristics().stream()
                    .filter(characteristic -> characteristic.getTypeCode().getValue().equals("HULL"))
                    .map(characteristic -> characteristic.getValueCode().getValue())
                    .map(HullMaterial::fromValue)
                    .findFirst().orElse(null);
        } catch (RuntimeException ex) {
            log.info("Vessel contains no hull material", ex);
            return null;
        }
    }

    private BigDecimal getPowerAux(VesselTransportMeansType vesselTransportMeans) {
        return getPower(vesselTransportMeans, "AUX");
    }

    private BigDecimal getPowerMain(VesselTransportMeansType vesselTransportMeans) {
        return getPower(vesselTransportMeans, "MAIN");
    }

    private BigDecimal getPower(VesselTransportMeansType vesselTransportMeans, String role) {
        try {
            return vesselTransportMeans.getAttachedVesselEngines().stream()
                    .filter(engine -> engine.getRoleCode().getValue().equals(role))
                    .map(engine -> engine.getPowerMeasures().get(0).getValue())
                    .findFirst().orElse(null);
        } catch (RuntimeException ex) {
            log.info("Vessel contains no vessel engine for " + role + " power.", ex);
            return null;
        }
    }

    private BigDecimal getLengthOverAll(VesselTransportMeansType vesselTransportMeans) {
        return getDimension(vesselTransportMeans, "LOA");
    }

    private BigDecimal getLengthBetweenPerpendiculars(VesselTransportMeansType vesselTransportMeans) {
        return getDimension(vesselTransportMeans, "LBP");
    }

    private BigDecimal getGrossTonnage(VesselTransportMeansType vesselTransportMeans) {
        return getDimension(vesselTransportMeans, "GT");
    }

    private BigDecimal getOtherGrossTonnage(VesselTransportMeansType vesselTransportMeans) {
        return getDimension(vesselTransportMeans, "TOTH");
    }

    private BigDecimal getSafetyGrossTonnage(VesselTransportMeansType vesselTransportMeans) {
        return getDimension(vesselTransportMeans, "GTS");
    }

    private BigDecimal getDimension(VesselTransportMeansType vesselTransportMeans, String type) {
        try {
            return vesselTransportMeans.getSpecifiedVesselDimensions().stream()
                    .filter(dimension -> dimension.getTypeCode().getValue().equals(type))
                    .map(dimension -> dimension.getValueMeasure().getValue())
                    .findFirst().orElse(null);
        } catch (RuntimeException ex) {
            log.info("Vessel contains no length over all", ex);
            return null;
        }
    }

    private String getCountryOfRegistration(VesselTransportMeansType vesselTransportMeans) {
        try {
            return vesselTransportMeans.getRegistrationVesselCountry().getID().getValue();
        } catch (RuntimeException ex) {
            log.info("Vessel contains no country of registration", ex);
            return null;
        }
    }

    private String getCFR(VesselTransportMeansType vesselTransportMeans) {
        return getIDValueOfVesselTransportMeansForIDType(vesselTransportMeans, "CFR");
    }

    private String getUVI(VesselTransportMeansType vesselTransportMeans) {
        return getIDValueOfVesselTransportMeansForIDType(vesselTransportMeans, "UVI");
    }

    private VesselEventType_0020 getEventType(VesselEventType vesselEvent) {
        try {
            return VesselEventType_0020.valueOf(vesselEvent.getTypeCode().getValue());
        } catch (RuntimeException ex) {
            log.info("Vessel has no event type", ex);
            return null;
        }
    }

    private Date getDateOfEvent(VesselEventType vesselEvent) {
        try {
            return vesselEvent.getOccurrenceDateTime().getDateTime().toDate();
        } catch (RuntimeException ex) {
            log.info("Vessel has no occurrence date", ex);
            return null;
        }

    }

    private String getRegistrationNumber(VesselTransportMeansType vesselTransportMeans) {
        return getIDValueOfVesselTransportMeansForIDType(vesselTransportMeans, "REG_NBR");
    }

    private String getExternalMarking(VesselTransportMeansType vesselTransportMeans) {
        return getIDValueOfVesselTransportMeansForIDType(vesselTransportMeans, "EXT_MARK");
    }

    private String getIRCS(VesselTransportMeansType vesselTransportMeans) {
        return getIDValueOfVesselTransportMeansForIDType(vesselTransportMeans, "IRCS");
    }

    private String getName(VesselTransportMeansType vesselTransportMeans) {
        try {
            return vesselTransportMeans.getNames().get(0).getValue();
        } catch (RuntimeException ex) {
            log.info("Vessel has no name", ex);
            return null;
        }
    }

    private String getRegistrationLocation(VesselTransportMeansType vesselTransportMeans) {
        try {
            return vesselTransportMeans
                    .getSpecifiedRegistrationEvents().get(0)
                    .getRelatedRegistrationLocation()
                    .getIDS().get(0).getValue();
        } catch (RuntimeException ex) {
            log.info("Vessel has no name", ex);
            return null;
        }
    }

    private String getIRCSIndicator(VesselTransportMeansType vesselTransportMeans) {
        Boolean ircsInd = getVesselEquipmentCharacteristic(vesselTransportMeans, "IRCS_IND");
        if (ircsInd == null) {
            return null;
        } else if (ircsInd){
            return "Y";
        } else {
            return "N";
        }
    }

    private Boolean getVMSIndicator(VesselTransportMeansType vesselTransportMeans) {
        return getVesselEquipmentCharacteristic(vesselTransportMeans, "VMS_IND");
    }

    private Boolean getERSIndicator(VesselTransportMeansType vesselTransportMeans) {
        return getVesselEquipmentCharacteristic(vesselTransportMeans, "ERS_IND");
    }

    private Boolean getAISIndicator(VesselTransportMeansType vesselTransportMeans) {
        return getVesselEquipmentCharacteristic(vesselTransportMeans, "AIS_IND");
    }

    private boolean getLicenseIndicator(VesselTransportMeansType vesselTransportMeans) {
        try {
            return vesselTransportMeans.getApplicableVesselAdministrativeCharacteristics().stream()
                    .filter(characteristic -> characteristic.getTypeCode().getValue().equals("LICENCE"))
                    .map(characteristic -> characteristic.getValueCode().getValue().equals("Y"))
                    .findFirst().orElse(false);

        } catch (RuntimeException ex) {
            log.info("Vessel contains no characteristic indicator for license", ex);
            return false;
        }
    }


    private Date getEntryIntoService(VesselTransportMeansType vesselTransportMeans) {
        try {
            return vesselTransportMeans.getApplicableVesselAdministrativeCharacteristics().stream()
                    .filter(characteristic -> characteristic.getTypeCode().getValue().equals("EIS"))
                    .map(characteristic -> characteristic.getValueDateTime().getDateTime().toDate())
                    .findFirst().orElse(null);

        } catch (RuntimeException ex) {
            log.info("Vessel contains no characteristic indicator for entry into service", ex);
            return null;
        }
    }

    private String getSegment(VesselTransportMeansType vesselTransportMeans) {
        try {
            return vesselTransportMeans.getApplicableVesselAdministrativeCharacteristics().stream()
                    .filter(characteristic -> characteristic.getTypeCode().getValue().equals("SEG"))
                    .map(characteristic -> characteristic.getValueCode().getValue())
                    .findFirst().orElse(null);

        } catch (RuntimeException ex) {
            log.info("Vessel contains no characteristic indicator for segment", ex);
            return null;
        }
    }

    private String getTypeOfExport(VesselTransportMeansType vesselTransportMeans) {
        try {
            return vesselTransportMeans.getApplicableVesselAdministrativeCharacteristics().stream()
                    .filter(characteristic -> characteristic.getTypeCode().getValue().equals("EXPORT"))
                    .map(characteristic -> characteristic.getValueCode().getValue())
                    .findFirst().orElse(null);

        } catch (RuntimeException ex) {
            log.info("Vessel contains no characteristic indicator for type of export", ex);
            return null;
        }
    }

    private String getPublicAid(VesselTransportMeansType vesselTransportMeans) {
        try {
            return vesselTransportMeans.getApplicableVesselAdministrativeCharacteristics().stream()
                    .filter(characteristic -> characteristic.getTypeCode().getValue().equals("AID"))
                    .map(characteristic -> characteristic.getValueCode().getValue())
                    .findFirst().orElse(null);

        } catch (RuntimeException ex) {
            log.info("Vessel contains no characteristic indicator for public aid", ex);
            return null;
        }
    }


    private String getMMSI(VesselTransportMeansType vesselTransportMeans) {
        return getIDValueOfVesselTransportMeansForIDType(vesselTransportMeans, "MMSI");
    }

    private String getVesselType(VesselTransportMeansType vesselTransportMeans) {
        try {
            return vesselTransportMeans.getTypeCodes().get(0).getValue();
        } catch (RuntimeException ex) {
            log.info("Vessel contains no vessel type", ex);
            return null;
        }
    }

    private String getMainFishingGearType(VesselTransportMeansType vesselTransportMeans) {
        return getFishingGearType(vesselTransportMeans, "MAIN");
    }

    private String getSubsidiaryFishingGearType(VesselTransportMeansType vesselTransportMeans) {
        return getFishingGearType(vesselTransportMeans, "AUX");
    }

    private String getFishingGearType(VesselTransportMeansType vesselTransportMeans, String role) {
        try {
            return vesselTransportMeans.getOnBoardFishingGears().stream()
                    .filter(gear -> gear.getRoleCodes().stream()
                                        .anyMatch(roleCode -> roleCode.getValue().equals(role)))
                    .map(gear -> gear.getTypeCode().getValue())
                    .findFirst().orElse(null);
        } catch (RuntimeException ex) {
            log.info("Vessel contains no fishing gear of role " + role);
            return null;
        }

    }

    private String getIDValueOfVesselTransportMeansForIDType(VesselTransportMeansType vesselTransportMeans, String idType) {
        try {
            return vesselTransportMeans.getIDS().stream()
                    .filter(id -> id.getSchemeID().equals(idType))
                    .map(IDType::getValue)
                    .findFirst().orElse(null);
        } catch (RuntimeException ex) {
            log.info("Vessel contains no ids", ex);
            return null;
        }
    }

    private Boolean getVesselEquipmentCharacteristic(VesselTransportMeansType vesselTransportMeans, String characteristicType) {
        try {
            Optional<String> foundCharacteristic = vesselTransportMeans.getApplicableVesselEquipmentCharacteristics().stream()
                .filter(characteristic -> characteristic.getTypeCode().getValue().equals(characteristicType))
                .map(characteristic -> characteristic.getValueCodes().get(0).getValue())
                .findFirst();

            if (foundCharacteristic.isPresent()) {
                return foundCharacteristic.get().equals("Y");
            } else {
                return null;
            }
        } catch (RuntimeException ex) {
            log.info("Vessel contains no characteristic indicator for " + characteristicType, ex);
            return null;
        }
    }
}
