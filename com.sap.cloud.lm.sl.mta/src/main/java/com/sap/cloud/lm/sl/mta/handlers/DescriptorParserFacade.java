package com.sap.cloud.lm.sl.mta.handlers;

import java.io.InputStream;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.common.util.YamlUtil;
import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorParser;
import com.sap.cloud.lm.sl.mta.message.Messages;
import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.Version;

public class DescriptorParserFacade {

    private static final String SCHEMA_VERSION_KEY = "_schema-version";

    public DeploymentDescriptor parseDeploymentDescriptor(InputStream yaml) {
        Map<String, Object> descriptor = YamlUtil.convertYamlToMap(yaml);
        return parseDeploymentDescriptor(descriptor);
    }

    public DeploymentDescriptor parseDeploymentDescriptor(String yaml) {
        Map<String, Object> descriptor = YamlUtil.convertYamlToMap(yaml);
        return parseDeploymentDescriptor(descriptor);
    }

    private DeploymentDescriptor parseDeploymentDescriptor(Map<String, Object> descriptor) {
        if (descriptor == null) {
            throw new ContentException(Messages.ERROR_EMPTY_DEPLOYMENT_DESCRIPTOR);
        }
        DescriptorParser parser = getDescriptorParser(descriptor);
        return parser.parseDeploymentDescriptor(descriptor);
    }

    public ExtensionDescriptor parseExtensionDescriptor(InputStream yaml) {
        Map<String, Object> descriptor = YamlUtil.convertYamlToMap(yaml);
        return parseExtensionDescriptor(descriptor);
    }

    public ExtensionDescriptor parseExtensionDescriptor(String yaml) {
        Map<String, Object> descriptor = YamlUtil.convertYamlToMap(yaml);
        return parseExtensionDescriptor(descriptor);
    }

    private ExtensionDescriptor parseExtensionDescriptor(Map<String, Object> descriptor) {
        if (descriptor == null) {
            throw new ContentException(Messages.ERROR_EMPTY_EXTENSION_DESCRIPTOR);
        }
        DescriptorParser parser = getDescriptorParser(descriptor);
        return parser.parseExtensionDescriptor(descriptor);
    }

    private DescriptorParser getDescriptorParser(Map<String, Object> descriptor) {
        Version schemaVersion = extractSchemaVersion(descriptor);
        return getDescriptorParser(schemaVersion);
    }

    private Version extractSchemaVersion(Map<String, Object> descriptor) {
        Object schemaVersion = descriptor.get(SCHEMA_VERSION_KEY);
        if (schemaVersion == null) {
            throw new ContentException(Messages.MISSING_REQUIRED_KEY, SCHEMA_VERSION_KEY);
        }
        return Version.parseVersion(schemaVersion.toString());
    }

    private DescriptorParser getDescriptorParser(Version schemaVersion) {
        HandlerFactory handlerFactory = new HandlerFactory(schemaVersion.getMajor());
        return handlerFactory.getDescriptorParser();
    }

}
