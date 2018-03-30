package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.integrationtest.deployment;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;

public abstract class TestOnPluginWithDefectEventBus {

    @Deployment(name = "plugin-with-defect-event-bus", order = 1)
    public static Archive<?> createDeploymentWithDefectEventBus() {
        return DeploymentFactory.createStandardDeployment();
    }

}
