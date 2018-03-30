package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.integrationtest.deployment;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TestOnGoodWorkingPlugin {

    static final Logger LOG = LoggerFactory.getLogger(TestOnGoodWorkingPlugin.class);

    @Deployment(name = "good-working-plugin", order = 1)
    public static Archive<?> createFluxActivityWSPluginDeployment() {
        WebArchive archive = DeploymentFactory.createStandardDeployment();
        archive.addClass("eu.europa.ec.fisheries.uvms.plugins.flux.vessel.integrationtest.mock.EventBusConsumerMock");
        return archive;
    }

}
