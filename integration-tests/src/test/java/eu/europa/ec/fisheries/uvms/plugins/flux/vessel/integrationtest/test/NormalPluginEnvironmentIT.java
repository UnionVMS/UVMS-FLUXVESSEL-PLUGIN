package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.integrationtest.test;

import eu.europa.ec.fisheries.schema.vessel.Connector2BridgeRequest;
import eu.europa.ec.fisheries.schema.vessel.Connector2BridgeResponse;
import eu.europa.ec.fisheries.schema.vessel.FLUXReportVesselInformation;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.integrationtest.deployment.TestOnGoodWorkingPlugin;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.integrationtest.mother.FLUXReportVesselInformationMother;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.integrationtest.test.state.ReceivedMessagesInExchange;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.StartupBean;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.service.FLUXReportVesselInformationService;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.webservice.FluxMessageReceiverBean;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.util.concurrent.Callable;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class NormalPluginEnvironmentIT extends TestOnGoodWorkingPlugin {

	@EJB
	private StartupBean startupBean;

	@EJB
    private FluxMessageReceiverBean fluxMessageReceiverBean;

    @EJB
    private FLUXReportVesselInformationService fluxReportVesselInformationService;

    @EJB
    private ReceivedMessagesInExchange receivedMessagesInExchange;

    @Before
    public void before() {
        receivedMessagesInExchange.clear();
    }

    
    @Test
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
        FLUXReportVesselInformation vesselInformation = FLUXReportVesselInformationMother.create();

        // not invoking the webservice, because XML marshalling seems to behave differently in Arquillian, in comparison to real life :(
        fluxReportVesselInformationService.receiveVesselInformation(vesselInformation);

        //check that the message has been sent to exchange
        await().atMost(30, SECONDS)
                .until(new Callable<Boolean>() {
                    public Boolean call() {
                        return !receivedMessagesInExchange.getAll().isEmpty();
                    }
                });
        assertTrue(receivedMessagesInExchange.getAll().get(0).startsWith(BEGIN_OF_MESSAGE_SENT_TO_EXCHANGE));
        assertTrue(receivedMessagesInExchange.getAll().get(0).endsWith(END_OF_MESSAGE_SENT_TO_EXCHANGE));
    }

    @Test
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

    private static final String BEGIN_OF_MESSAGE_SENT_TO_EXCHANGE = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<ns2:ReceiveAssetInformationRequest xmlns:ns2=\"urn:module.exchange.schema.fisheries.ec.europa.eu:v1\">\n" +
            "    <method>RECEIVE_ASSET_INFORMATION</method>\n" +
            "    <username>FLUX</username>\n" +
            "    <senderOrReceiver>flux-vessel-plugin</senderOrReceiver>\n" +
            "    <date>";

    private static final String END_OF_MESSAGE_SENT_TO_EXCHANGE = "</date>\n" +
            "    <assets>&lt;?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?&gt;\n" +
            "&lt;ns2:UpsertAssetListModuleRequest xmlns:ns2=\"module.asset.wsdl.fisheries.ec.europa.eu\"&gt;\n" +
            "    &lt;method&gt;UPSERT_ASSET_LIST&lt;/method&gt;\n" +
            "    &lt;userName&gt;FLUX&lt;/userName&gt;\n" +
            "    &lt;asset&gt;\n" +
            "        &lt;active&gt;false&lt;/active&gt;\n" +
            "        &lt;name&gt;name&lt;/name&gt;\n" +
            "        &lt;countryCode&gt;NLD&lt;/countryCode&gt;\n" +
            "        &lt;hasIrcs&gt;Y&lt;/hasIrcs&gt;\n" +
            "        &lt;ircs&gt;IRCS&lt;/ircs&gt;\n" +
            "        &lt;externalMarking&gt;externalMarking&lt;/externalMarking&gt;\n" +
            "        &lt;cfr&gt;CFR&lt;/cfr&gt;\n" +
            "        &lt;mmsiNo&gt;MMSI&lt;/mmsiNo&gt;\n" +
            "        &lt;hasLicense&gt;true&lt;/hasLicense&gt;\n" +
            "        &lt;homePort&gt;registrationLocation&lt;/homePort&gt;\n" +
            "        &lt;lengthOverAll&gt;21.3&lt;/lengthOverAll&gt;\n" +
            "        &lt;lengthBetweenPerpendiculars&gt;29.4&lt;/lengthBetweenPerpendiculars&gt;\n" +
            "        &lt;grossTonnage&gt;829.4&lt;/grossTonnage&gt;\n" +
            "        &lt;otherGrossTonnage&gt;826.8&lt;/otherGrossTonnage&gt;\n" +
            "        &lt;safetyGrossTonnage&gt;926.8&lt;/safetyGrossTonnage&gt;\n" +
            "        &lt;powerMain&gt;123&lt;/powerMain&gt;\n" +
            "        &lt;powerAux&gt;124&lt;/powerAux&gt;\n" +
            "        &lt;contact&gt;\n" +
            "            &lt;name&gt;Stijn&lt;/name&gt;\n" +
            "            &lt;owner&gt;false&lt;/owner&gt;\n" +
            "        &lt;/contact&gt;\n" +
            "        &lt;contact&gt;\n" +
            "            &lt;name&gt;Hooft&lt;/name&gt;\n" +
            "            &lt;owner&gt;false&lt;/owner&gt;\n" +
            "        &lt;/contact&gt;\n" +
            "        &lt;uvi&gt;UVI&lt;/uvi&gt;\n" +
            "        &lt;ersIndicator&gt;true&lt;/ersIndicator&gt;\n" +
            "        &lt;aisIndicator&gt;true&lt;/aisIndicator&gt;\n" +
            "        &lt;vesselType&gt;FX&lt;/vesselType&gt;\n" +
            "        &lt;vesselDateOfEntry&gt;2018-02-02T03:31:00+01:00&lt;/vesselDateOfEntry&gt;\n" +
            "        &lt;hullMaterial&gt;METAL&lt;/hullMaterial&gt;\n" +
            "        &lt;yearOfConstruction&gt;1994&lt;/yearOfConstruction&gt;\n" +
            "        &lt;registrationNumber&gt;registrationNumber&lt;/registrationNumber&gt;\n" +
            "        &lt;vmsIndicator&gt;true&lt;/vmsIndicator&gt;\n" +
            "        &lt;vesselEventType&gt;IMP&lt;/vesselEventType&gt;\n" +
            "        &lt;dateOfEvent&gt;2018-03-30T04:31:00+02:00&lt;/dateOfEvent&gt;\n" +
            "        &lt;mainFishingGear&gt;MAIN&lt;/mainFishingGear&gt;\n" +
            "        &lt;subsidiaryFishingGear&gt;SUB&lt;/subsidiaryFishingGear&gt;\n" +
            "        &lt;segment&gt;MFL&lt;/segment&gt;\n" +
            "        &lt;countryOfImportOrExport&gt;BEL&lt;/countryOfImportOrExport&gt;\n" +
            "        &lt;typeOfExport&gt;typeOfExport&lt;/typeOfExport&gt;\n" +
            "        &lt;publicAid&gt;public aid&lt;/publicAid&gt;\n" +
            "    &lt;/asset&gt;\n" +
            "&lt;/ns2:UpsertAssetListModuleRequest&gt;\n" +
            "</assets>\n" +
            "</ns2:ReceiveAssetInformationRequest>\n";
}
