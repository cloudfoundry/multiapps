package com.sap.cloud.lm.sl.mta.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.builders.ExtensionChainBuilder;
import com.sap.cloud.lm.sl.mta.message.Messages;
import com.sap.cloud.lm.sl.mta.model.ExtensionElement;
import com.sap.cloud.lm.sl.mta.model.IdentifiableElement;
import com.sap.cloud.lm.sl.mta.model.Version;
import com.sap.cloud.lm.sl.mta.parsers.v1_0.DeploymentDescriptorParser;
import com.sap.cloud.lm.sl.mta.parsers.v3_0.ExtensionDescriptorParser;
import com.sap.cloud.lm.sl.mta.util.YamlUtil;

public class MtaSchemaVersionDetector {

    private static final String DEFAULT_SCHEMA_VERSION = "1.0.0";
    private static final String SCHEMA_VERSION_KEY = "_schema-version";

    public Version detect(String deploymentDescriptorYaml, List<String> extensionDescriptorYamls) throws ContentException {
        List<RawExtensionDescriptor> rawExtensionDescriptors = getRawExtensionDescriptors(extensionDescriptorYamls);
        RawDeploymentDescriptor rawDeploymentDescriptor = getRawDeploymentDescriptor(deploymentDescriptorYaml);
        List<RawExtensionDescriptor> relevantRawExtensionDescriptors = getRelevantExtensionDescriptors(rawDeploymentDescriptor,
            rawExtensionDescriptors);

        Version deploymentDescriptorVersion = getSchemaVersion(rawDeploymentDescriptor);
        List<Version> extensionDescriptorVersions = new ArrayList<>();
        for (RawExtensionDescriptor descriptor : relevantRawExtensionDescriptors) {
            extensionDescriptorVersions.add(getSchemaVersion(descriptor));
        }
        validateCompatibility(deploymentDescriptorVersion, extensionDescriptorVersions);

        return Collections.max(getAllVersions(extensionDescriptorVersions, deploymentDescriptorVersion));
    }

    private List<Version> getAllVersions(List<Version> extensionDescriptorVersions, Version deploymentDescriptorVersion) {
        List<Version> result = new ArrayList<>(extensionDescriptorVersions);
        result.add(deploymentDescriptorVersion);
        return result;
    }

    private List<RawExtensionDescriptor> getRawExtensionDescriptors(List<String> extensionDescriptorYamls) {
        List<RawExtensionDescriptor> result = new ArrayList<>();
        for (String extensionDescriptorString : extensionDescriptorYamls) {
            RawExtensionDescriptor rawDescriptor = new RawExtensionDescriptor(YamlUtil.convertYamlToMap(extensionDescriptorString));
            result.add(rawDescriptor);
        }
        return result;
    }

    private RawDeploymentDescriptor getRawDeploymentDescriptor(String deploymentDescriptorYaml) {
        return new RawDeploymentDescriptor((YamlUtil.convertYamlToMap(deploymentDescriptorYaml)));
    }

    private List<RawExtensionDescriptor> getRelevantExtensionDescriptors(RawDeploymentDescriptor rawDeploymentDescriptor,
        List<RawExtensionDescriptor> rawExtensionDescriptors) {
        return new ExtensionChainBuilder<>(rawDeploymentDescriptor, rawExtensionDescriptors, false).build();
    }

    private Version getSchemaVersion(RawDescriptor rawDescriptor) {
        Object schemaVersionElement = rawDescriptor.getAsMap()
            .get(SCHEMA_VERSION_KEY);
        String schemaVersion = (schemaVersionElement != null) ? schemaVersionElement.toString() : DEFAULT_SCHEMA_VERSION;
        return Version.parseVersion(schemaVersion);
    }

    private void validateCompatibility(Version deploymentDescriptorVersion, List<Version> extensionDescriptorVersions) {
        for (Version extensionDescriptorVersion : extensionDescriptorVersions) {
            if (!areCompatible(deploymentDescriptorVersion, extensionDescriptorVersion)) {
                throw new ContentException(Messages.INCOMPATIBLE_VERSIONS, deploymentDescriptorVersion, extensionDescriptorVersion);
            }
        }
    }

    private boolean areCompatible(Version version1, Version version2) {
        return version1.getMajor() == version2.getMajor();
    }

    private static class RawDeploymentDescriptor extends RawDescriptor implements IdentifiableElement {

        public RawDeploymentDescriptor(Map<String, Object> descriptorAsMap) {
            super(descriptorAsMap);
        }

        @Override
        public String getId() {
            return (String) getAsMap().get(DeploymentDescriptorParser.ID);
        }

    }

    private static class RawExtensionDescriptor extends RawDescriptor implements ExtensionElement {

        public RawExtensionDescriptor(Map<String, Object> descriptorAsMap) {
            super(descriptorAsMap);
        }

        @Override
        public String getParentId() {
            return (String) getAsMap().get(ExtensionDescriptorParser.EXTENDS);
        }

        @Override
        public String getId() {
            return (String) getAsMap().get(ExtensionDescriptorParser.ID);
        }

    }

    private static class RawDescriptor {

        private final Map<String, Object> descriptorAsMap;

        public RawDescriptor(Map<String, Object> descriptorAsMap) {
            this.descriptorAsMap = descriptorAsMap;
        }

        public Map<String, Object> getAsMap() {
            return descriptorAsMap;
        }

    }

}
