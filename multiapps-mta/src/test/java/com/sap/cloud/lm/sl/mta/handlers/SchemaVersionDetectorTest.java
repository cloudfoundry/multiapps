package com.sap.cloud.lm.sl.mta.handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.message.Messages;
import com.sap.cloud.lm.sl.mta.model.Version;
import com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionDescriptor;

public class SchemaVersionDetectorTest {

    private static final String FOO_ID = "foo";
    private static final String BAR_ID = "bar";
    private static final String BAZ_ID = "baz";
    private static final String SCHEMA_VERSION_1_0 = "1.0.0";
    private static final String SCHEMA_VERSION_2_0 = "2.0.0";
    private static final String SCHEMA_VERSION_2_1 = "2.1.0";

    private final SchemaVersionDetector schemaVersionDetector = new SchemaVersionDetector();

    @Test
    public void testDetectWithCompatibleSchemaVersions() {
        DeploymentDescriptor deploymentDescriptor = buildDeploymentDescriptor(SCHEMA_VERSION_2_0, FOO_ID);
        ExtensionDescriptor extensionDescriptor1 = buildExtensionDescriptor(SCHEMA_VERSION_2_1, BAR_ID);
        ExtensionDescriptor extensionDescriptor2 = buildExtensionDescriptor(SCHEMA_VERSION_2_0, BAZ_ID);
        List<ExtensionDescriptor> extensionDescriptorChain = Arrays.asList(extensionDescriptor1, extensionDescriptor2);

        Version schemaVersion = schemaVersionDetector.detect(deploymentDescriptor, extensionDescriptorChain);

        assertEquals(SCHEMA_VERSION_2_1, schemaVersion.toString());
    }

    @Test
    public void testDetectWithSameSchemaVersions() {
        DeploymentDescriptor deploymentDescriptor = buildDeploymentDescriptor(SCHEMA_VERSION_2_0, FOO_ID);
        ExtensionDescriptor extensionDescriptor1 = buildExtensionDescriptor(SCHEMA_VERSION_2_0, BAR_ID);
        ExtensionDescriptor extensionDescriptor2 = buildExtensionDescriptor(SCHEMA_VERSION_2_0, BAZ_ID);
        List<ExtensionDescriptor> extensionDescriptorChain = Arrays.asList(extensionDescriptor1, extensionDescriptor2);

        Version schemaVersion = schemaVersionDetector.detect(deploymentDescriptor, extensionDescriptorChain);

        assertEquals(SCHEMA_VERSION_2_0, schemaVersion.toString());
    }

    @Test
    public void testDetectWithIncompatibleSchemaVersions() {
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
        DeploymentDescriptor.Builder builder = new DeploymentDescriptor.Builder();
        builder.setSchemaVersion(schemaVersion);
        builder.setId(id);
        return builder.build();
    }

    private ExtensionDescriptor buildExtensionDescriptor(String schemaVersion, String id) {
        ExtensionDescriptor.Builder builder = new ExtensionDescriptor.Builder();
        builder.setSchemaVersion(schemaVersion);
        builder.setId(id);
        return builder.build();
    }

}
