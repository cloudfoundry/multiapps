package com.sap.cloud.lm.sl.mta.builders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.message.Messages;
import com.sap.cloud.lm.sl.mta.model.v1_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionDescriptor;

public class ExtensionDescriptorChainBuilderTest {

    private static final String FOO_ID = "foo";
    private static final String BAR_ID = "bar";
    private static final String BAZ_ID = "baz";
    private static final String QUX_ID = "qux";

    @Test
    public void testBuild() {
        DeploymentDescriptor deploymentDescriptor = buildDeploymentDescriptor(FOO_ID);
        ExtensionDescriptor extensionDescriptor1 = buildExtensionDescriptor(BAR_ID, FOO_ID);
        ExtensionDescriptor extensionDescriptor2 = buildExtensionDescriptor(BAZ_ID, BAR_ID);
        ExtensionDescriptor extensionDescriptor3 = buildExtensionDescriptor(QUX_ID, BAZ_ID);
        // Shuffle them around:
        List<ExtensionDescriptor> extensionDescriptors = Arrays.asList(extensionDescriptor2, extensionDescriptor3, extensionDescriptor1);
        List<ExtensionDescriptor> expectedExtensionDescriptorChain = Arrays.asList(extensionDescriptor1, extensionDescriptor2,
            extensionDescriptor3);

        ExtensionDescriptorChainBuilder extensionDescriptorChainBuilder = new ExtensionDescriptorChainBuilder();
        List<ExtensionDescriptor> extensionDescriptorChain = extensionDescriptorChainBuilder.build(deploymentDescriptor,
            extensionDescriptors);

        assertEquals(expectedExtensionDescriptorChain, extensionDescriptorChain);
    }

    @Test
    public void testBuildWithDescriptorWithUnknownParent() {
        DeploymentDescriptor deploymentDescriptor = buildDeploymentDescriptor(FOO_ID);
        ExtensionDescriptor extensionDescriptor1 = buildExtensionDescriptor(BAR_ID, FOO_ID);
        ExtensionDescriptor extensionDescriptor2 = buildExtensionDescriptor(QUX_ID, BAZ_ID);
        List<ExtensionDescriptor> extensionDescriptors = Arrays.asList(extensionDescriptor1, extensionDescriptor2);

        ExtensionDescriptorChainBuilder extensionDescriptorChainBuilder = new ExtensionDescriptorChainBuilder();
        ContentException contentException = assertThrows(ContentException.class,
            () -> extensionDescriptorChainBuilder.build(deploymentDescriptor, extensionDescriptors));
        String expectedMessage = MessageFormat.format(
            Messages.CANNOT_BUILD_EXTENSION_DESCRIPTOR_CHAIN_BECAUSE_DESCRIPTORS_0_HAVE_AN_UNKNOWN_PARENT, extensionDescriptor2.getId());

        assertEquals(expectedMessage, contentException.getMessage());
    }

    @Test
    public void testBuildWithMultipleDescriptorsExtendingTheSameParent() {
        DeploymentDescriptor deploymentDescriptor = buildDeploymentDescriptor(FOO_ID);
        ExtensionDescriptor extensionDescriptor1 = buildExtensionDescriptor(BAR_ID, FOO_ID);
        ExtensionDescriptor extensionDescriptor2 = buildExtensionDescriptor(QUX_ID, FOO_ID);
        List<ExtensionDescriptor> extensionDescriptors = Arrays.asList(extensionDescriptor1, extensionDescriptor2);

        ExtensionDescriptorChainBuilder extensionDescriptorChainBuilder = new ExtensionDescriptorChainBuilder();
        ContentException contentException = assertThrows(ContentException.class,
            () -> extensionDescriptorChainBuilder.build(deploymentDescriptor, extensionDescriptors));
        String expectedMessage = MessageFormat.format(Messages.MULTIPLE_EXTENSION_DESCRIPTORS_EXTEND_THE_PARENT_0,
            deploymentDescriptor.getId());

        assertEquals(expectedMessage, contentException.getMessage());
    }

    @Test
    public void testLaxBuildWithDescriptorsWithUnknownParents() {
        DeploymentDescriptor deploymentDescriptor = buildDeploymentDescriptor(FOO_ID);
        ExtensionDescriptor extensionDescriptor1 = buildExtensionDescriptor(BAR_ID, FOO_ID);
        ExtensionDescriptor extensionDescriptor2 = buildExtensionDescriptor(QUX_ID, BAZ_ID);
        List<ExtensionDescriptor> extensionDescriptors = Arrays.asList(extensionDescriptor2, extensionDescriptor1);
        List<ExtensionDescriptor> expectedExtensionDescriptorChain = Arrays.asList(extensionDescriptor1);

        ExtensionDescriptorChainBuilder extensionDescriptorChainBuilder = new ExtensionDescriptorChainBuilder(false);
        List<ExtensionDescriptor> extensionDescriptorChain = extensionDescriptorChainBuilder.build(deploymentDescriptor,
            extensionDescriptors);

        assertEquals(expectedExtensionDescriptorChain, extensionDescriptorChain);
    }

    @Test
    public void testLaxBuildWithMultipleDescriptorsExtendingTheSameParent() {
        DeploymentDescriptor deploymentDescriptor = buildDeploymentDescriptor(FOO_ID);
        ExtensionDescriptor extensionDescriptor1 = buildExtensionDescriptor(BAR_ID, BAZ_ID);
        ExtensionDescriptor extensionDescriptor2 = buildExtensionDescriptor(QUX_ID, BAZ_ID);
        List<ExtensionDescriptor> extensionDescriptors = Arrays.asList(extensionDescriptor2, extensionDescriptor1);

        ExtensionDescriptorChainBuilder extensionDescriptorChainBuilder = new ExtensionDescriptorChainBuilder(false);
        List<ExtensionDescriptor> extensionDescriptorChain = extensionDescriptorChainBuilder.build(deploymentDescriptor,
            extensionDescriptors);

        assertEquals(Collections.emptyList(), extensionDescriptorChain);
    }

    private DeploymentDescriptor buildDeploymentDescriptor(String id) {
        DeploymentDescriptor.Builder builder = new DeploymentDescriptor.Builder();
        builder.setId(id);
        return builder.build();
    }

    private ExtensionDescriptor buildExtensionDescriptor(String id, String parentId) {
        ExtensionDescriptor.Builder builder = new ExtensionDescriptor.Builder();
        builder.setParentId(parentId);
        builder.setId(id);
        return builder.build();
    }

}
