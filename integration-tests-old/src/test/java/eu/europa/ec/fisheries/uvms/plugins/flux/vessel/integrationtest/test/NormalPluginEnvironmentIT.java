package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.integrationtest.test;

import eu.europa.ec.fisheries.schema.vessel.Connector2BridgeRequest;
import eu.europa.ec.fisheries.schema.vessel.Connector2BridgeResponse;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.integrationtest.deployment.TestOnGoodWorkingPlugin;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.integrationtest.test.state.ReceivedMessagesInExchange;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.StartupBean;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.webservice.FluxMessageReceiverBean;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.ejb.EJB;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class NormalPluginEnvironmentIT extends TestOnGoodWorkingPlugin {

	@EJB
	private StartupBean startupBean;

    @EJB
    private FluxMessageReceiverBean fluxMessageReceiverBean;

    @EJB
    private ReceivedMessagesInExchange receivedMessagesInExchange;

    @Before
    public void before() {
        receivedMessagesInExchange.clear();
    }

    
    @Test @Ignore
	@OperateOnDeployment("good-working-plugin")
	public void pluginRegistersAndStartsUpSuccessfully() {
        await().atMost(30, SECONDS)
                .until(new Callable<Boolean>() {
                    public Boolean call() throws Exception {
                        return startupBean.isRegistered();
                    }
                });

        await().atMost(30, SECONDS)
                .until(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                return startupBean.isEnabled();
            }
        });
	}


    @Test
    @OperateOnDeployment("good-working-plugin")
    public void receiveFLUXReportVesselInformation() {
        await().atMost(30, SECONDS)
                .until(new Callable<Boolean>() {
                    public Boolean call() {
                        return startupBean.isRegistered();
                    }
                });

        await().atMost(30, SECONDS)
                .until(new Callable<Boolean>() {
                    public Boolean call() throws Exception {
                        return startupBean.isEnabled();
                    }
                });

        Connector2BridgeRequest request = new Connector2BridgeRequest();
        request.setAny(marshalToDOM(VESSEL_INFORMATION));

        Connector2BridgeResponse response = fluxMessageReceiverBean.post(request);

        //check that the response is OK
        assertEquals("OK", response.getStatus());

        //check that the message has been sent to exchange
        /*await().atMost(30, SECONDS)
                .until(new Callable<Boolean>() {
                    public Boolean call() throws Exception {
                        return !receivedMessagesInExchange.getAll().isEmpty();
                    }
                });
        assertTrue(receivedMessagesInExchange.getAll().get(0).startsWith(BEGIN_OF_MESSAGE_SENT_TO_EXCHANGE));
        assertTrue(receivedMessagesInExchange.getAll().get(0).endsWith(END_OF_MESSAGE_SENT_TO_EXCHANGE));*/
    }

    @Test @Ignore
    @OperateOnDeployment("good-working-plugin")
    public void receiveInvalidMessage() {
        await().atMost(30, SECONDS)
                .until(new Callable<Boolean>() {
                    public Boolean call() {
                        return startupBean.isRegistered();
                    }
                });

        await().atMost(30, SECONDS)
                .until(new Callable<Boolean>() {
                    public Boolean call() throws Exception {
                        return startupBean.isEnabled();
                    }
                });

        Connector2BridgeRequest request = new Connector2BridgeRequest();

        Connector2BridgeResponse response = fluxMessageReceiverBean.post(request);

        //check that the response is OK
        assertEquals("NOK", response.getStatus());
    }

    private Element marshalToDOM(String message) {
        try {
            InputStream xmlAsInputStream = new ByteArrayInputStream(message.getBytes("UTF-8"));

            javax.xml.parsers.DocumentBuilderFactory b = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            b.setNamespaceAware(false);
            DocumentBuilder db = b.newDocumentBuilder();

            Document document = db.parse(xmlAsInputStream);
            return document.getDocumentElement();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException("Could not marshall message into an Element", e);
        }
    }

    private static final String VESSEL_INFORMATION = "<rsm:FLUXReportVesselInformation xmlns:clm63155CommunicationChannelCode=\"urn:un:unece:uncefact:codelist:standard:UNECE:CommunicationMeansTypeCode:D16A\" xmlns:qdt=\"urn:un:unece:uncefact:data:Standard:QualifiedDataType:20\" xmlns:ram=\"urn:un:unece:uncefact:data:standard:ReusableAggregateBusinessInformationEntity:20\" xmlns:udt=\"urn:un:unece:uncefact:data:standard:UnqualifiedDataType:20\" xsi:schemaLocation=\"urn:un:unece:uncefact:data:standard:FLUXReportVesselInformation:5 FLUXReportVesselInformation_5p1.xsd\" xmlns:rsm=\"urn:un:unece:uncefact:data:standard:FLUXReportVesselInformation:5\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
            "    <rsm:FLUXReportDocument>\n" +
            "        <ram:ID schemeID=\"UUID\">e1cfd745-4ff9-45ab-b94a-5cd19f094c3e</ram:ID>\n" +
            "        <ram:ReferencedID schemeID=\"UUID\">45a00671-5569-41dc-bc60-781ba43eb217</ram:ReferencedID>\n" +
            "        <ram:CreationDateTime>\n" +
            "            <udt:DateTime>2017-06-01T12:10:38Z</udt:DateTime>\n" +
            "        </ram:CreationDateTime>\n" +
            "        <ram:PurposeCode listID=\"FLUX_GP_PURPOSE\">9</ram:PurposeCode>\n" +
            "        <ram:TypeCode listID=\"FLUX_VESSEL_REPORT_TYPE\">SNAP-L</ram:TypeCode>\n" +
            "        <ram:OwnerFLUXParty>\n" +
            "            <ram:ID schemeID=\"FLUX_GP_PARTY\">BEL</ram:ID>\n" +
            "        </ram:OwnerFLUXParty>\n" +
            "    </rsm:FLUXReportDocument>\n" +
            "    <rsm:VesselEvent>\n" +
            "        <ram:TypeCode listID=\"VESSEL_EVENT\">MOD</ram:TypeCode>\n" +
            "        <ram:OccurrenceDateTime>\n" +
            "            <udt:DateTime>2015-03-27T00:00:00Z</udt:DateTime>\n" +
            "        </ram:OccurrenceDateTime>\n" +
            "        <ram:RelatedVesselTransportMeans>\n" +
            "            <ram:ID schemeID=\"CFR\">${#TestCase#cfr}</ram:ID>\n" +
            "            <ram:ID schemeID=\"EXT_MARK\">O.2</ram:ID>\n" +
            "            <ram:ID schemeID=\"IRCS\">OPAB</ram:ID>\n" +
            "            <ram:ID schemeID=\"REG_NBR\">007</ram:ID>\n" +
            "            <ram:ID schemeID=\"MMSI\">123456789</ram:ID>\n" +
            "            <ram:ID schemeID=\"UVI\">1234567</ram:ID>\n" +
            "            <ram:Name>TIS DE MICHEL</ram:Name>\n" +
            "            <ram:TypeCode listID=\"VESSEL_TYPE\">FX</ram:TypeCode>\n" +
            "            <ram:RegistrationVesselCountry>\n" +
            "                <ram:ID schemeID=\"TERRITORY\">BEL</ram:ID>\n" +
            "            </ram:RegistrationVesselCountry>\n" +
            "            <ram:SpecifiedRegistrationEvent>\n" +
            "                <ram:RelatedRegistrationLocation>\n" +
            "                    <ram:ID schemeID=\"VESSEL_PORT\">BEOOST</ram:ID>\n" +
            "                    <ram:TypeCode listID=\"FLUX_VESSEL_REGSTR_TYPE\">PORT</ram:TypeCode>\n" +
            "                </ram:RelatedRegistrationLocation>\n" +
            "            </ram:SpecifiedRegistrationEvent>\n" +
            "            <ram:SpecifiedConstructionEvent>\n" +
            "                <ram:OccurrenceDateTime>\n" +
            "                    <udt:DateTime>1964-01-01T00:00:00Z</udt:DateTime>\n" +
            "                </ram:OccurrenceDateTime>\n" +
            "            </ram:SpecifiedConstructionEvent>\n" +
            "            <ram:AttachedVesselEngine>\n" +
            "                <ram:RoleCode listID=\"FLUX_VESSEL_ENGINE_ROLE\">MAIN</ram:RoleCode>\n" +
            "                <ram:PowerMeasure unitCode=\"KWT\">213</ram:PowerMeasure>\n" +
            "            </ram:AttachedVesselEngine>\n" +
            "            <ram:AttachedVesselEngine>\n" +
            "                <ram:RoleCode listID=\"FLUX_VESSEL_ENGINE_ROLE\">AUX</ram:RoleCode>\n" +
            "                <ram:PowerMeasure unitCode=\"KWT\">0</ram:PowerMeasure>\n" +
            "            </ram:AttachedVesselEngine>\n" +
            "            <ram:SpecifiedVesselDimension>\n" +
            "                <ram:TypeCode listID=\"FLUX_VESSEL_DIM_TYPE\">LOA</ram:TypeCode>\n" +
            "                <ram:ValueMeasure unitCode=\"MTR\">21.39</ram:ValueMeasure>\n" +
            "            </ram:SpecifiedVesselDimension>\n" +
            "            <ram:SpecifiedVesselDimension>\n" +
            "                <ram:TypeCode listID=\"FLUX_VESSEL_DIM_TYPE\">LBP</ram:TypeCode>\n" +
            "                <ram:ValueMeasure unitCode=\"MTR\">18.66</ram:ValueMeasure>\n" +
            "            </ram:SpecifiedVesselDimension>\n" +
            "            <ram:SpecifiedVesselDimension>\n" +
            "                <ram:TypeCode listID=\"FLUX_VESSEL_DIM_TYPE\">GT</ram:TypeCode>\n" +
            "                <ram:ValueMeasure unitCode=\"TNE\">53</ram:ValueMeasure>\n" +
            "            </ram:SpecifiedVesselDimension>\n" +
            "            <ram:SpecifiedVesselDimension>\n" +
            "                <ram:TypeCode listID=\"FLUX_VESSEL_DIM_TYPE\">TOTH</ram:TypeCode>\n" +
            "                <ram:ValueMeasure unitCode=\"TNE\">53</ram:ValueMeasure>\n" +
            "            </ram:SpecifiedVesselDimension>\n" +
            "            <ram:SpecifiedVesselDimension>\n" +
            "                <ram:TypeCode listID=\"FLUX_VESSEL_DIM_TYPE\">GTS</ram:TypeCode>\n" +
            "                <ram:ValueMeasure unitCode=\"TNE\">53</ram:ValueMeasure>\n" +
            "            </ram:SpecifiedVesselDimension>\n" +
            "            <ram:OnBoardFishingGear>\n" +
            "                <ram:TypeCode listID=\"GEAR_TYPE\">TBB</ram:TypeCode>\n" +
            "                <ram:RoleCode listID=\"FLUX_VESSEL_GEAR_ROLE\">MAIN</ram:RoleCode>\n" +
            "            </ram:OnBoardFishingGear>\n" +
            "            <ram:OnBoardFishingGear>\n" +
            "                <ram:TypeCode listID=\"GEAR_TYPE\">OTB</ram:TypeCode>\n" +
            "                <ram:RoleCode listID=\"FLUX_VESSEL_GEAR_ROLE\">AUX</ram:RoleCode>\n" +
            "            </ram:OnBoardFishingGear>\n" +
            "            <ram:ApplicableVesselEquipmentCharacteristic>\n" +
            "                <ram:TypeCode listID=\"FLUX_VESSEL_EQUIP_TYPE\">IRCS_IND</ram:TypeCode>\n" +
            "                <ram:ValueCode listID=\"BOOLEAN_TYPE\">Y</ram:ValueCode>\n" +
            "            </ram:ApplicableVesselEquipmentCharacteristic>\n" +
            "            <ram:ApplicableVesselEquipmentCharacteristic>\n" +
            "                <ram:TypeCode listID=\"FLUX_VESSEL_EQUIP_TYPE\">VMS_IND</ram:TypeCode>\n" +
            "                <ram:ValueCode listID=\"BOOLEAN_TYPE\">Y</ram:ValueCode>\n" +
            "            </ram:ApplicableVesselEquipmentCharacteristic>\n" +
            "            <ram:ApplicableVesselEquipmentCharacteristic>\n" +
            "                <ram:TypeCode listID=\"FLUX_VESSEL_EQUIP_TYPE\">AIS_IND</ram:TypeCode>\n" +
            "                <ram:ValueCode listID=\"BOOLEAN_TYPE\">N</ram:ValueCode>\n" +
            "            </ram:ApplicableVesselEquipmentCharacteristic>\n" +
            "            <ram:ApplicableVesselAdministrativeCharacteristic>\n" +
            "                <ram:TypeCode listID=\"FLUX_VESSEL_ADMIN_TYPE\">LICENCE</ram:TypeCode>\n" +
            "                <ram:ValueCode listID=\"BOOLEAN_TYPE\">Y</ram:ValueCode>\n" +
            "            </ram:ApplicableVesselAdministrativeCharacteristic>\n" +
            "            <ram:ApplicableVesselAdministrativeCharacteristic>\n" +
            "                <ram:TypeCode listID=\"FLUX_VESSEL_ADMIN_TYPE\">SEG</ram:TypeCode>\n" +
            "                <ram:ValueCode listID=\"VESSEL_SEGMENT\">MFL</ram:ValueCode>\n" +
            "            </ram:ApplicableVesselAdministrativeCharacteristic>\n" +
            "            <ram:ApplicableVesselAdministrativeCharacteristic>\n" +
            "                <ram:TypeCode listID=\"FLUX_VESSEL_ADMIN_TYPE\">EIS</ram:TypeCode>\n" +
            "                <ram:ValueDateTime>\n" +
            "                    <udt:DateTime>1975-03-26T00:00:00Z</udt:DateTime>\n" +
            "                </ram:ValueDateTime>\n" +
            "            </ram:ApplicableVesselAdministrativeCharacteristic>\n" +
            "            <ram:SpecifiedContactParty>\n" +
            "                <ram:ID schemeID=\"IMO\">1234567</ram:ID>\n" +
            "                <ram:Name>Fake Owner Name (Company)</ram:Name>\n" +
            "                <ram:NationalityCountryID schemeID=\"TERRITORY\">BEL</ram:NationalityCountryID>\n" +
            "                <ram:RoleCode listID=\"FLUX_CONTACT_ROLE\">OWNER</ram:RoleCode>\n" +
            "                <ram:SpecifiedStructuredAddress>\n" +
            "                    <ram:StreetName>Joseph Street</ram:StreetName>\n" +
            "                    <ram:CityName>Brussels</ram:CityName>\n" +
            "                    <ram:CountryID schemeID=\"TERRITORY\">BEL</ram:CountryID>\n" +
            "                    <ram:PostOfficeBox>79</ram:PostOfficeBox>\n" +
            "                    <ram:PostalArea>1000</ram:PostalArea>\n" +
            "                </ram:SpecifiedStructuredAddress>\n" +
            "                <ram:URIEmailCommunication>\n" +
            "                    <ram:URIID schemeID=\"URI\">company@ec.com</ram:URIID>\n" +
            "                </ram:URIEmailCommunication>\n" +
            "                <ram:SpecifiedUniversalCommunication>\n" +
            "                    <ram:ChannelCode>TE</ram:ChannelCode>\n" +
            "                    <ram:CompleteNumber>+3222966741</ram:CompleteNumber>\n" +
            "                </ram:SpecifiedUniversalCommunication>\n" +
            "                <ram:SpecifiedUniversalCommunication>\n" +
            "                    <ram:ChannelCode>FX</ram:ChannelCode>\n" +
            "                    <ram:CompleteNumber>+3222966789</ram:CompleteNumber>\n" +
            "                </ram:SpecifiedUniversalCommunication>\n" +
            "            </ram:SpecifiedContactParty>\n" +
            "            <ram:SpecifiedContactParty>\n" +
            "                <ram:NationalityCountryID schemeID=\"TERRITORY\">BEL</ram:NationalityCountryID>\n" +
            "                <ram:RoleCode listID=\"FLUX_CONTACT_ROLE\">OPERATOR</ram:RoleCode>\n" +
            "                <ram:SpecifiedStructuredAddress>\n" +
            "                    <ram:StreetName>Joseph Street</ram:StreetName>\n" +
            "                    <ram:CityName>Brussels</ram:CityName>\n" +
            "                    <ram:CountryID schemeID=\"TERRITORY\">BEL</ram:CountryID>\n" +
            "                    <ram:PostOfficeBox>79</ram:PostOfficeBox>\n" +
            "                    <ram:PostalArea>1000</ram:PostalArea>\n" +
            "                </ram:SpecifiedStructuredAddress>\n" +
            "                <ram:SpecifiedContactPerson>\n" +
            "                    <ram:FamilyName>Fake Operator Name (Person)</ram:FamilyName>\n" +
            "                </ram:SpecifiedContactPerson>\n" +
            "                <ram:URIEmailCommunication>\n" +
            "                    <ram:URIID schemeID=\"URI\">company@ec.com</ram:URIID>\n" +
            "                </ram:URIEmailCommunication>\n" +
            "                <ram:SpecifiedUniversalCommunication>\n" +
            "                    <ram:ChannelCode>TE</ram:ChannelCode>\n" +
            "                    <ram:CompleteNumber>+3222966741</ram:CompleteNumber>\n" +
            "                </ram:SpecifiedUniversalCommunication>\n" +
            "                <ram:SpecifiedUniversalCommunication>\n" +
            "                    <ram:ChannelCode>FX</ram:ChannelCode>\n" +
            "                    <ram:CompleteNumber>+3222966789</ram:CompleteNumber>\n" +
            "                </ram:SpecifiedUniversalCommunication>\n" +
            "            </ram:SpecifiedContactParty>\n" +
            "            <ram:ApplicableVesselTechnicalCharacteristic>\n" +
            "                <ram:TypeCode listID=\"FLUX_VESSEL_TECH_TYPE\">HULL</ram:TypeCode>\n" +
            "                <ram:ValueCode listID=\"VESSEL_HULL_TYPE\">1</ram:ValueCode>\n" +
            "            </ram:ApplicableVesselTechnicalCharacteristic>\n" +
            "        </ram:RelatedVesselTransportMeans>\n" +
            "    </rsm:VesselEvent>\n" +
            "</rsm:FLUXReportVesselInformation>\n";

    private static final String BEGIN_OF_MESSAGE_SENT_TO_EXCHANGE = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"+
            "<ns2:SetFLUXFAReportMessageRequest xmlns:ns2=\"urn:module.exchange.schema.fisheries.ec.europa.eu:v1\">\n"+
            "    <method>SET_FLUX_FA_REPORT_MESSAGE</method>\n"+
            "    <username>M-CATCH</username>\n"+
            "    <pluginType>BELGIAN_ACTIVITY</pluginType>\n"+
            "    <senderOrReceiver>BEL</senderOrReceiver>\n"+
            "    <messageGuid></messageGuid>\n"+
            "    <date>";

    private static final String END_OF_MESSAGE_SENT_TO_EXCHANGE = "</date>\n"+
            "    <fluxDataFlow>urn:un:unece:uncefact:fisheries:FLUX:FA:EU:2</fluxDataFlow>\n"+
            "    <request>&lt;?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?&gt;\n"+
            "&lt;ns3:FLUXFAReportMessage xmlns=\"urn:un:unece:uncefact:data:standard:ReusableAggregateBusinessInformationEntity:20\" xmlns:ns2=\"urn:un:unece:uncefact:data:standard:UnqualifiedDataType:20\" xmlns:ns3=\"urn:un:unece:uncefact:data:standard:FLUXFAReportMessage:3\"&gt;\n"+
            "    &lt;ns3:FLUXReportDocument&gt;\n"+
            "        &lt;ID schemeID=\"UUID\"&gt;1000&lt;/ID&gt;\n"+
            "        &lt;CreationDateTime&gt;\n"+
            "            &lt;ns2:DateTime&gt;2018-01-27T13:58:00Z&lt;/ns2:DateTime&gt;\n"+
            "        &lt;/CreationDateTime&gt;\n"+
            "        &lt;Purpose&gt;9&lt;/Purpose&gt;\n"+
            "        &lt;TypeCode&gt;DECLARATION&lt;/TypeCode&gt;\n"+
            "        &lt;OwnerFLUXParty&gt;\n"+
            "            &lt;ID&gt;BEL&lt;/ID&gt;\n"+
            "        &lt;/OwnerFLUXParty&gt;\n"+
            "    &lt;/ns3:FLUXReportDocument&gt;\n"+
            "    &lt;ns3:FAReportDocument&gt;\n"+
            "        &lt;FMCMarkerCode&gt;BLA&lt;/FMCMarkerCode&gt;\n"+
            "        &lt;AcceptanceDateTime&gt;\n"+
            "            &lt;ns2:DateTime&gt;2018-01-27T13:58:00Z&lt;/ns2:DateTime&gt;\n"+
            "        &lt;/AcceptanceDateTime&gt;\n"+
            "        &lt;SpecifiedFishingActivity&gt;\n"+
            "            &lt;ID schemeID=\"UUID\"&gt;99&lt;/ID&gt;\n"+
            "        &lt;/SpecifiedFishingActivity&gt;\n"+
            "    &lt;/ns3:FAReportDocument&gt;\n"+
            "&lt;/ns3:FLUXFAReportMessage&gt;\n"+
            "</request>\n"+
            "</ns2:SetFLUXFAReportMessageRequest>\n";

    private String createFluxResponseMessage(String guid) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<ns3:FLUXResponseMessage xmlns=\"urn:un:unece:uncefact:data:standard:ReusableAggregateBusinessInformationEntity:20\" \n" +
                "xmlns:ns2=\"urn:un:unece:uncefact:data:standard:UnqualifiedDataType:20\" \n" +
                "xmlns:ns3=\"urn:un:unece:uncefact:data:standard:FLUXResponseMessage:6\">\n" +
                "    <ns3:FLUXResponseDocument>\n" +
                "        <ID schemeID=\"UUID\">933emff1-a4b8-4777-89l7-03ae4292d5d14</ID>\n" +
                "        <ReferencedID schemeID=\"UUID\">" + guid + "</ReferencedID>\n" +
                "        <CreationDateTime>\n" +
                "            <ns2:DateTime>2017-09-22T05:53:37.633Z</ns2:DateTime>\n" +
                "        </CreationDateTime>\n" +
                "        <ResponseCode listID=\"FLUX_GP_RESPONSE\">NOK</ResponseCode>\n" +
                "        <RejectionReason>VALIDATION</RejectionReason>\n" +
                "        <RelatedValidationResultDocument>\n" +
                "           <ValidatorID schemeID=\"FLUX_GP_PARTY\">XEU</ValidatorID>\n" +
                "           <CreationDateTime>\n" +
                "              <ns2:DateTime>2017-09-22T05:53:37.652Z</ns2:DateTime>\n" +
                "           </CreationDateTime>\n" +
                "        </RelatedValidationResultDocument>\n" +
                "        <RespondentFLUXParty>\n" +
                "            <ID schemeID=\"FLUX_GP_PARTY\">FLUX_LOCAL_NATION_CODE</ID>\n" +
                "        </RespondentFLUXParty>\n" +
                "    </ns3:FLUXResponseDocument>\n" +
                "</ns3:FLUXResponseMessage>\n";
    }
}
