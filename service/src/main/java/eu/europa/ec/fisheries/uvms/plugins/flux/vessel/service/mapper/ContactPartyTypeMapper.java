package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.mapper;

import eu.europa.ec.fisheries.schema.vessel.*;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetContact;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.Stateless;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
@Slf4j
public class ContactPartyTypeMapper {

    public List<ContactPartyType> fromAssetContact(List<AssetContact> contactPartyTypes) {
        return contactPartyTypes.stream()
                .map(this::fromAssetContact)
                .collect(Collectors.toList());
    }

    public ContactPartyType fromAssetContact(AssetContact assetContact) {
        ContactPartyType contactPartyType = new ContactPartyType();
        contactPartyType.setName(new TextType()
                .withValue(assetContact.getName()));
        contactPartyType.getRoleCodes().add(new CodeType()
                .withListID("FLUX_CONTACT_ROLE")
                .withValue(assetContact.getType().value()));
        contactPartyType.getSpecifiedStructuredAddresses().add(new StructuredAddressType()
                .withStreetName(new TextType()
                        .withValue(assetContact.getStreetName()))
                .withCityName(new TextType()
                        .withValue(assetContact.getCityName()))
                .withCountryID(new IDType()
                    .withSchemeID("TERRITORY")
                    .withValue(assetContact.getCountryCode()))
                .withPostalArea(new TextType()
                    .withValue(assetContact.getPostalCode()))
                .withPostOfficeBox(new TextType()
                    .withValue(assetContact.getPostOfficeBox())));
        contactPartyType.getNationalityCountryIDs().add(new IDType()
                .withSchemeID("TERRITORY")
                .withValue(assetContact.getNationality()));
        contactPartyType.getURIEmailCommunications().add(new EmailCommunicationType()
                .withURIID(new IDType()
                    .withSchemeID("URI")
                    .withValue(assetContact.getEmail())));
        contactPartyType.getSpecifiedUniversalCommunications().add(new UniversalCommunicationType()
                    .withChannelCode(new CommunicationChannelCodeType()
                            .withValue(CommunicationMeansTypeCodeContentType.TE))
                    .withCompleteNumber(new TextType()
                            .withValue(assetContact.getNumber())));
        return contactPartyType;
    }

}
