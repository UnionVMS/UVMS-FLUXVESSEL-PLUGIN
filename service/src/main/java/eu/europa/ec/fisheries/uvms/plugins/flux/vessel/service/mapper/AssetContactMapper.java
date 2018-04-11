package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.mapper;

import eu.europa.ec.fisheries.schema.vessel.CommunicationMeansTypeCodeContentType;
import eu.europa.ec.fisheries.schema.vessel.ContactPartyType;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetContact;
import eu.europa.ec.fisheries.wsdl.asset.types.ContactType;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.Stateless;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
@Slf4j
public class AssetContactMapper {

    public List<AssetContact> fromContactPartyType(List<ContactPartyType> contactPartyTypes) {
        return contactPartyTypes.stream()
                .map(this::fromContactPartyType)
                .collect(Collectors.toList());
    }

    public AssetContact fromContactPartyType(ContactPartyType contactPartyType) {
        AssetContact assetContact = new AssetContact();
        assetContact.setName(getName(contactPartyType));
        assetContact.setType(getType(contactPartyType));
        assetContact.setStreetName(getStreetName(contactPartyType));
        assetContact.setCityName(getCityName(contactPartyType));
        assetContact.setCountryCode(getCountryCode(contactPartyType));
        assetContact.setPostalCode(getPostalCode(contactPartyType));
        assetContact.setPostOfficeBox(getPostOfficeBox(contactPartyType));
        assetContact.setNationality(getNationality(contactPartyType));
        assetContact.setEmail(getEmail(contactPartyType));
        assetContact.setNumber(getPhone(contactPartyType));
        assetContact.setFaxNumber(getFax(contactPartyType));
        return assetContact;
    }

    private String getPhone(ContactPartyType contactPartyType) {
        try {
            return contactPartyType.getSpecifiedUniversalCommunications().stream()
                    .filter(communication -> communication.getChannelCode().getValue().equals(CommunicationMeansTypeCodeContentType.TE))
                    .map(communication -> communication.getCompleteNumber().getValue())
                    .findFirst().orElse(null);
        } catch (RuntimeException ex) {
            log.info("Contact has no phone. " + ex.getMessage());
            return null;
        }
    }

    private String getFax(ContactPartyType contactPartyType) {
        try {
            return contactPartyType.getSpecifiedUniversalCommunications().stream()
                    .filter(communication -> communication.getChannelCode().getValue().equals(CommunicationMeansTypeCodeContentType.FX))
                    .map(communication -> communication.getCompleteNumber().getValue())
                    .findFirst().orElse(null);
        } catch (RuntimeException ex) {
            log.info("Contact has no phone. " + ex.getMessage());
            return null;
        }
    }


    private String getEmail(ContactPartyType contactPartyType) {
        try {
            return contactPartyType.getURIEmailCommunications().get(0).getURIID().getValue();
        } catch (RuntimeException ex) {
            log.info("Contact has no email. " + ex.getMessage());
            return null;
        }
    }

    private String getNationality(ContactPartyType contactPartyType) {
        try {
            return contactPartyType.getNationalityCountryIDs().get(0).getValue();
        }  catch (RuntimeException ex) {
            log.info("Contact has no nationality. " + ex.getMessage());
            return null;
        }
    }

    private String getStreetName(ContactPartyType contactPartyType) {
        try {
            return contactPartyType.getSpecifiedStructuredAddresses().get(0).getStreetName().getValue();
        }  catch (RuntimeException ex) {
            log.info("Contact has no street name. " + ex.getMessage());
            return null;
        }
    }

    private String getCityName(ContactPartyType contactPartyType) {
        try {
            return contactPartyType.getSpecifiedStructuredAddresses().get(0).getCityName().getValue();
        }  catch (RuntimeException ex) {
            log.info("Contact has no city name. " + ex.getMessage());
            return null;
        }
    }

    private String getCountryCode(ContactPartyType contactPartyType) {
        try {
            return contactPartyType.getSpecifiedStructuredAddresses().get(0).getCountryID().getValue();
        }  catch (RuntimeException ex) {
            log.info("Contact has no country id. " + ex.getMessage());
            return null;
        }
    }

    private String getPostalCode(ContactPartyType contactPartyType) {
        try {
            return contactPartyType.getSpecifiedStructuredAddresses().get(0).getPostalArea().getValue();
        }  catch (RuntimeException ex) {
            log.info("Contact has no postal area. " + ex.getMessage());
            return null;
        }
    }

    private String getPostOfficeBox(ContactPartyType contactPartyType) {
        try {
            return contactPartyType.getSpecifiedStructuredAddresses().get(0).getPostOfficeBox().getValue();
        }  catch (RuntimeException ex) {
            log.info("Contact has no post office box. " + ex.getMessage());
            return null;
        }
    }

    private String getName(ContactPartyType contactPartyType) {
        try {
            return contactPartyType.getName().getValue();
        }  catch (RuntimeException ex) {
            log.info("Contact has no name. " + ex.getMessage());
            return null;
        }
    }

    private ContactType getType(ContactPartyType contactPartyType) {
        try {
            return ContactType.valueOf(contactPartyType.getRoleCodes().get(0).getValue());
        }  catch (RuntimeException ex) {
            log.info("Contact has no type. " + ex.getMessage());
            return null;
        }
    }
}
