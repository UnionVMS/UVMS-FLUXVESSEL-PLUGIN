package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.integrationtest.test;

import eu.europa.ec.fisheries.schema.vessel.Connector2BridgeRequest;
import eu.europa.ec.fisheries.schema.vessel.Connector2BridgeResponse;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.integrationtest.deployment.TestOnPluginWithDefectEventBus;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.StartupBean;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.webservice.FluxMessageReceiverBean;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.util.concurrent.Callable;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class PluginWillNotRegisterAndStartUpIfExchangeIsDownIT extends TestOnPluginWithDefectEventBus {

	@EJB
	private StartupBean startupBean;

	@EJB
	private FluxMessageReceiverBean fluxMessageReceiverBean;

	@Test
	@OperateOnDeployment("plugin-with-defect-event-bus")
	public void pluginWillNotRegisterAndStartUpIfExchangeIsDown() throws Exception {
        await().pollDelay(30, SECONDS)
				.timeout(40, SECONDS)
                .until(new Callable<Boolean>() {
                    public Boolean call() throws Exception {
                        return !startupBean.isRegistered() && !startupBean.isEnabled() ;
                    }
                });

        //check if posting something returns NOK
        Connector2BridgeRequest connector2BridgeRequest = new Connector2BridgeRequest();
        Connector2BridgeResponse response = fluxMessageReceiverBean.post(connector2BridgeRequest);
        assertEquals("NOK", response.getStatus());
    }
}
