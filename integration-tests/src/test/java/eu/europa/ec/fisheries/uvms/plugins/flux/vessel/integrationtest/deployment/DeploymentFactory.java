package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.integrationtest.deployment;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class DeploymentFactory {

    final static Logger LOG = LoggerFactory.getLogger(TestOnPluginWithDefectEventBus.class);

    public static WebArchive createStandardDeployment() {
        WebArchive archive = createArchiveWithTestFiles("test");
        archive.addPackages(true, "eu.europa.ec.fisheries.uvms.commons.message");
        archive.addPackages(true, "eu.europa.ec.fisheries.uvms.plugins.flux.vessel.model");
        archive.addPackages(true, "eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service");
        archive.addPackages(true, "eu.europa.ec.fisheries.uvms.plugins.flux.vessel.webservice");
        archive.addPackages(true, "eu.europa.ec.fisheries.schema");
        archive.addPackages(true, "xeu.bridge_connector");
        archive.addPackages(true, "xeu.connector_bridge");

        archive.addAsResource("capabilities.properties", "capabilities.properties");
        archive.addAsResource("plugin.properties", "plugin.properties");
        archive.addAsResource("settings.properties", "settings.properties");

        return archive;
    }


    private static WebArchive createArchiveWithTestFiles(final String name) {
        File[] files = Maven.resolver().loadPomFromFile("pom.xml")
                .importRuntimeDependencies().resolve().withTransitivity().asFile();

        // Embedding war package which contains the test class is needed
        // So that Arquillian can invoke test class through its servlet test runner
        WebArchive testWar = ShrinkWrap.create(WebArchive.class, name + ".war");

        testWar.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        testWar.addAsWebInfResource("ejb-jar-test.xml", "ejb-jar.xml");
        testWar.addAsResource("logback-test.xml", "logback.xml");
        testWar.addAsManifestResource("jboss-deployment-structure.xml","jboss-deployment-structure.xml");

        testWar.addAsLibraries(files);

        testWar.addPackages(true, "eu.europa.ec.fisheries.uvms.plugins.flux.vessel.integrationtest.test");
        testWar.addPackages(true, "eu.europa.ec.fisheries.uvms.plugins.flux.vessel.integrationtest.deployment");
        testWar.addPackages(true, "eu.europa.ec.fisheries.uvms.plugins.flux.vessel.integrationtest.mother");
        testWar.addPackages(true, "org.awaitility");
        testWar.addClass("eu.europa.ec.fisheries.uvms.plugins.flux.vessel.integrationtest.mock.PortInitiatorMock");
        testWar.addClass("eu.europa.ec.fisheries.uvms.plugins.flux.vessel.integrationtest.mock.PortMock");

        return testWar;
    }

}
