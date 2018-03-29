package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.mapper;

import eu.europa.ec.fisheries.schema.vessel.*;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetContact;
import eu.europa.ec.fisheries.wsdl.asset.types.ContactType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class AssetContactMapperTest {

    @InjectMocks
    private AssetContactMapper assetContactMapper;


    @Test
    public void fromContactPartyTypeWhenSuccess() {
        //data set
        String name = "Mickey Mouse";
        String type = "OWNER";
        String streetName = "Bruulstraat";
        String cityName = "Haaltert";
        String countryCode = "BEL";
        String postalCode = "9450";
        String postOfficeBox = "40 bus 5";
        String nationality = "BEL";
        String email = "lv@vlaanderen.be";
        String tel = "0478/4364654";

        StructuredAddressType address = new StructuredAddressType()
                .withStreetName(new TextType().withValue(streetName))
                .withCityName(new TextType().withValue(cityName))
                .withCountryID(new IDType().withValue(countryCode))
                .withPostalArea(new TextType().withValue(postalCode))
                .withPostOfficeBox(new TextType().withValue(postOfficeBox));
        UniversalCommunicationType telephoneType = new UniversalCommunicationType()
                .withChannelCode(new CommunicationChannelCodeType().withValue(CommunicationMeansTypeCodeContentType.TE))
                .withCompleteNumber(new TextType().withValue(tel));

        ContactPartyType contactPartyType = new ContactPartyType();
        contactPartyType.setName(new TextType().withValue(name));
        contactPartyType.getRoleCodes().add(new CodeType().withValue(type));
        contactPartyType.getSpecifiedStructuredAddresses().add(address);
        contactPartyType.getNationalityCountryIDs().add(new IDType().withValue(nationality));
        contactPartyType.getURIEmailCommunications().add(new EmailCommunicationType().withURIID(new IDType().withValue(email)));
        contactPartyType.getSpecifiedUniversalCommunications().add(telephoneType);

        //execute
        AssetContact result = assetContactMapper.fromContactPartyType(contactPartyType);

        //assert
        assertEquals(name, result.getName());
        assertEquals(ContactType.OWNER, result.getType());
        assertEquals(streetName, result.getStreetName());
        assertEquals(cityName, result.getCityName());
        assertEquals(postalCode, result.getPostalCode());
        assertEquals(postOfficeBox, result.getPostOfficeBox());
        assertEquals(nationality, result.getNationality());
        assertEquals(email, result.getEmail());
        assertEquals(tel, result.getNumber());
    }

    @Test
    public void fromContactPartyTypeWhenNoDataIsFilledIn() {
        //data set
        ContactPartyType contactPartyType = new ContactPartyType();

        //execute
        AssetContact result = assetContactMapper.fromContactPartyType(contactPartyType);

        //assert
        assertNull(result.getName());
        assertNull(result.getType());
        assertNull(result.getStreetName());
        assertNull(result.getCityName());
        assertNull(result.getPostalCode());
        assertNull(result.getPostOfficeBox());
        assertNull(result.getNationality());
        assertNull(result.getEmail());
        assertNull(result.getNumber());
    }
}