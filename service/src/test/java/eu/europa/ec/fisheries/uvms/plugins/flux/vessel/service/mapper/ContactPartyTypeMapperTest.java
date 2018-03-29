package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.mapper;

import eu.europa.ec.fisheries.schema.vessel.CommunicationMeansTypeCodeContentType;
import eu.europa.ec.fisheries.schema.vessel.ContactPartyType;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetContact;
import eu.europa.ec.fisheries.wsdl.asset.types.ContactType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ContactPartyTypeMapperTest {

    @InjectMocks
    private ContactPartyTypeMapper contactPartyTypeMapper;

    @Test
    public void fromAssetContact() {
        String phone = "45621354";
        String email = "tellMeWhy@IDontLikeMondays.com";
        String nationality = "BEL";
        String postOfficeBox = "B";
        String postalCode = "9865";
        String countryCode = "FRA";
        String cityName = "Paris";
        String streetName = "Mondieu";
        ContactType contactType = ContactType.OPERATOR;
        String name = "Wannes";

        AssetContact assetContact = new AssetContact();
        assetContact.setNumber(phone);
        assetContact.setEmail(email);
        assetContact.setNationality(nationality);
        assetContact.setPostOfficeBox(postOfficeBox);
        assetContact.setPostalCode(postalCode);
        assetContact.setCountryCode(countryCode);
        assetContact.setCityName(cityName);
        assetContact.setStreetName(streetName);
        assetContact.setType(contactType);
        assetContact.setName(name);

        ContactPartyType contactPartyType = contactPartyTypeMapper.fromAssetContact(assetContact);

        assertEquals(name, contactPartyType.getName().getValue());
        assertEquals(1, contactPartyType.getRoleCodes().size());
        assertEquals("FLUX_CONTACT_ROLE", contactPartyType.getRoleCodes().get(0).getListID());
        assertEquals(contactType.value(), contactPartyType.getRoleCodes().get(0).getValue());
        assertEquals(1, contactPartyType.getSpecifiedStructuredAddresses().size());
        assertEquals(streetName, contactPartyType.getSpecifiedStructuredAddresses().get(0).getStreetName().getValue());
        assertEquals(cityName, contactPartyType.getSpecifiedStructuredAddresses().get(0).getCityName().getValue());
        assertEquals("TERRITORY", contactPartyType.getSpecifiedStructuredAddresses().get(0).getCountryID().getSchemeID());
        assertEquals(countryCode, contactPartyType.getSpecifiedStructuredAddresses().get(0).getCountryID().getValue());
        assertEquals(postalCode, contactPartyType.getSpecifiedStructuredAddresses().get(0).getPostalArea().getValue());
        assertEquals(postOfficeBox, contactPartyType.getSpecifiedStructuredAddresses().get(0).getPostOfficeBox().getValue());
        assertEquals(1, contactPartyType.getNationalityCountryIDs().size());
        assertEquals("TERRITORY", contactPartyType.getNationalityCountryIDs().get(0).getSchemeID());
        assertEquals(nationality, contactPartyType.getNationalityCountryIDs().get(0).getValue());
        assertEquals(1, contactPartyType.getURIEmailCommunications().size());
        assertEquals("URI", contactPartyType.getURIEmailCommunications().get(0).getURIID().getSchemeID());
        assertEquals(email, contactPartyType.getURIEmailCommunications().get(0).getURIID().getValue());
        assertEquals(1, contactPartyType.getSpecifiedUniversalCommunications().size());
        assertEquals(CommunicationMeansTypeCodeContentType.TE, contactPartyType.getSpecifiedUniversalCommunications().get(0).getChannelCode().getValue());
        assertEquals(phone, contactPartyType.getSpecifiedUniversalCommunications().get(0).getCompleteNumber().getValue());
    }
}