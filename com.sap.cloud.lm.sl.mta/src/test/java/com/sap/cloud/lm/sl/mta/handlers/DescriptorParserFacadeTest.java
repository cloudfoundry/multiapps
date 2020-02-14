package com.sap.cloud.lm.sl.mta.handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.InputStream;
import java.text.MessageFormat;

import org.junit.Test;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.common.util.TestUtil;
import com.sap.cloud.lm.sl.mta.Messages;
import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.ExtensionDescriptor;

public class DescriptorParserFacadeTest {

    private static final String MTAD_VALID = "mtad-valid.yaml";
    private static final String DESCRIPTOR_EMPTY = "empty.yaml";
    private static final String MTAEXT_VALID = "config-valid.mtaext";
    private static final String DESCRIPTOR_MISSING_SCHEMA = "mtad-missing-schema.yaml";

    private static final String SCHEMA_VERSION_KEY = "_schema-version";

    DescriptorParserFacade parser = new DescriptorParserFacade();

    @Test
    public void testValidParseDeploymentDescriptorWithStream() {
        InputStream descriptorStream = TestUtil.getResourceAsInputStream(MTAD_VALID, getClass());
        DeploymentDescriptor descriptor = parser.parseDeploymentDescriptor(descriptorStream);
        assertNotNull(descriptor);
    }

    @Test
    public void testValidParseDeploymentDescriptorWithString() {
        String descriptorString = TestUtil.getResourceAsString(MTAD_VALID, getClass());
        DeploymentDescriptor descriptor = parser.parseDeploymentDescriptor(descriptorString);
        assertNotNull(descriptor);
    }

    @Test
    public void testEmptyParseDeploymentDescriptorWithStream() {
        InputStream descriptorStream = TestUtil.getResourceAsInputStream(DESCRIPTOR_EMPTY, getClass());
        try {
            parser.parseDeploymentDescriptor(descriptorStream);
            fail("Expected exception");
        } catch (ContentException e) {
            assertEquals(Messages.ERROR_EMPTY_DEPLOYMENT_DESCRIPTOR, e.getMessage());
        }
    }

    @Test
    public void testMissingSchemaParseDeploymentDescriptorWithStream() {
        InputStream descriptorStream = TestUtil.getResourceAsInputStream(DESCRIPTOR_MISSING_SCHEMA, getClass());
        try {
            parser.parseDeploymentDescriptor(descriptorStream);
            fail("Expected exception");
        } catch (ContentException e) {
            assertEquals(MessageFormat.format(Messages.MISSING_REQUIRED_KEY, SCHEMA_VERSION_KEY), e.getMessage());
        }
    }

    @Test
    public void testValidParseExtensionDescriptorWithStream() {
        InputStream descriptorStream = TestUtil.getResourceAsInputStream(MTAEXT_VALID, getClass());
        ExtensionDescriptor descriptor = parser.parseExtensionDescriptor(descriptorStream);
        assertNotNull(descriptor);
    }

    @Test
    public void testValidParseExtensionDescriptorWithString() {
        String descriptorString = TestUtil.getResourceAsString(MTAEXT_VALID, getClass());
        ExtensionDescriptor descriptor = parser.parseExtensionDescriptor(descriptorString);
        assertNotNull(descriptor);
    }

    @Test
    public void testEmptyParseExtensionDescriptorWithString() {
        InputStream descriptorStream = TestUtil.getResourceAsInputStream(DESCRIPTOR_EMPTY, getClass());
        try {
            parser.parseExtensionDescriptor(descriptorStream);
            fail("Expected exception");
        } catch (ContentException e) {
            assertEquals(Messages.ERROR_EMPTY_EXTENSION_DESCRIPTOR, e.getMessage());
        }
    }

}
