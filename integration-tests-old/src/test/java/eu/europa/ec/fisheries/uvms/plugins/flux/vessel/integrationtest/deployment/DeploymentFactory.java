package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.integrationtest.deployment;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class DeploymentFactory {

    private DeploymentFactory() {

    }

    static final Logger LOG = LoggerFactory.getLogger(DeploymentFactory.class);

    public static WebArchive createStandardDeployment() {
        WebArchive archive = createArchiveWithTestFiles("test");
        archive.addPackages(true, "eu.europa.ec.fisheries.uvms.commons.message");
        archive.addPackages(true, "eu.europa.ec.fisheries.uvms.plugins.flux.vessel.model");
        archive.addPackages(true, "xeu.bridge_connector.wsdl.v1");
        archive.addPackages(true, "xeu.connector_bridge.wsdl.v1");
        archive.addPackages(true, "eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service");
        archive.addPackages(true, "eu.europa.ec.fisheries.uvms.plugins.flux.vessel.webservice");
        archive.addPackages(true, "eu.europa.ec.fisheries.wsdl.asset");
        archive.addPackages(true, "eu.europa.ec.fisheries.schema");

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
        testWar.addAsResource("logback-test.xml", "logback.xml");
        testWar.addAsManifestResource("jboss-deployment-structure.xml","jboss-deployment-structure.xml");

        testWar.addAsLibraries(files);

        testWar.addPackages(true, "eu.europa.ec.fisheries.uvms.plugins.flux.vessel.integrationtest.test");
        testWar.addPackages(true, "eu.europa.ec.fisheries.uvms.plugins.flux.vessel.integrationtest.deployment");
        testWar.addPackages(true, "org.awaitility");

        return testWar;
    }

}
