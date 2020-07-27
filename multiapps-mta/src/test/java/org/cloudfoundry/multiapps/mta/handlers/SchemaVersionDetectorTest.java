package org.cloudfoundry.multiapps.mta.handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.Messages;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.ExtensionDescriptor;
import org.cloudfoundry.multiapps.mta.model.Version;
import org.junit.jupiter.api.Test;

class SchemaVersionDetectorTest {

    private static final String FOO_ID = "foo";
    private static final String BAR_ID = "bar";
    private static final String BAZ_ID = "baz";
    private static final String SCHEMA_VERSION_1_0 = "1.0.0";
    private static final String SCHEMA_VERSION_2_0 = "2.0.0";
    private static final String SCHEMA_VERSION_2_1 = "2.1.0";

    private final SchemaVersionDetector schemaVersionDetector = new SchemaVersionDetector();

    @Test
    void testDetectWithCompatibleSchemaVersions() {
        DeploymentDescriptor deploymentDescriptor = buildDeploymentDescriptor(SCHEMA_VERSION_2_0, FOO_ID);
        ExtensionDescriptor extensionDescriptor1 = buildExtensionDescriptor(SCHEMA_VERSION_2_1, BAR_ID);
        ExtensionDescriptor extensionDescriptor2 = buildExtensionDescriptor(SCHEMA_VERSION_2_0, BAZ_ID);
        List<ExtensionDescriptor> extensionDescriptorChain = Arrays.asList(extensionDescriptor1, extensionDescriptor2);

        Version schemaVersion = schemaVersionDetector.detect(deploymentDescriptor, extensionDescriptorChain);

        assertEquals(SCHEMA_VERSION_2_1, schemaVersion.toString());
    }

    @Test
    void testDetectWithSameSchemaVersions() {
        DeploymentDescriptor deploymentDescriptor = buildDeploymentDescriptor(SCHEMA_VERSION_2_0, FOO_ID);
        ExtensionDescriptor extensionDescriptor1 = buildExtensionDescriptor(SCHEMA_VERSION_2_0, BAR_ID);
        ExtensionDescriptor extensionDescriptor2 = buildExtensionDescriptor(SCHEMA_VERSION_2_0, BAZ_ID);
        List<ExtensionDescriptor> extensionDescriptorChain = Arrays.asList(extensionDescriptor1, extensionDescriptor2);

        Version schemaVersion = schemaVersionDetector.detect(deploymentDescriptor, extensionDescriptorChain);

        assertEquals(SCHEMA_VERSION_2_0, schemaVersion.toString());
    }

    @Test
    void testDetectWithIncompatibleSchemaVersions() {
        DeploymentDescriptor deploymentDescriptor = buildDeploymentDescriptor(SCHEMA_VERSION_2_0, FOO_ID);
        ExtensionDescriptor extensionDescriptor1 = buildExtensionDescriptor(SCHEMA_VERSION_1_0, BAR_ID);
        ExtensionDescriptor extensionDescriptor2 = buildExtensionDescriptor(SCHEMA_VERSION_2_0, BAZ_ID);
        List<ExtensionDescriptor> extensionDescriptorChain = Arrays.asList(extensionDescriptor1, extensionDescriptor2);

        ContentException contentException = assertThrows(ContentException.class,
                                                         () -> schemaVersionDetector.detect(deploymentDescriptor,
                                                                                            extensionDescriptorChain));
        String expectedMessage = MessageFormat.format(Messages.EXTENSION_DESCRIPTORS_MUST_HAVE_THE_SAME_MAJOR_SCHEMA_VERSION_AS_THE_DEPLOYMENT_DESCRIPTOR_BUT_0_DO_NOT,
                                                      BAR_ID);

        assertEquals(expectedMessage, contentException.getMessage());
    }

    private DeploymentDescriptor buildDeploymentDescriptor(String schemaVersion, String id) {
        return DeploymentDescriptor.createV2()
                                   .setSchemaVersion(schemaVersion)
                                   .setId(id);
    }

    private ExtensionDescriptor buildExtensionDescriptor(String schemaVersion, String id) {
        return ExtensionDescriptor.createV2()
                                  .setSchemaVersion(schemaVersion)
                                  .setId(id);
    }

}
