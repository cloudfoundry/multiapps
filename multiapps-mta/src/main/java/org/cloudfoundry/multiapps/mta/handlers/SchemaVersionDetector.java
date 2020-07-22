package org.cloudfoundry.multiapps.mta.handlers;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.collections4.ListUtils;
import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.Messages;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.Descriptor;
import org.cloudfoundry.multiapps.mta.model.ExtensionDescriptor;
import org.cloudfoundry.multiapps.mta.model.Version;

public class SchemaVersionDetector {

    public Version detect(DeploymentDescriptor deploymentDescriptor, List<ExtensionDescriptor> extensionDescriptorChain)
        throws ContentException {
        validateSchemaVersionCompatibility(deploymentDescriptor, extensionDescriptorChain);
        return getMax(deploymentDescriptor, extensionDescriptorChain);
    }

    private void validateSchemaVersionCompatibility(DeploymentDescriptor deploymentDescriptor,
                                                    List<ExtensionDescriptor> extensionDescriptorChain) {
        List<ExtensionDescriptor> incompatibleExtensionDescriptors = getIncompatibleExtensionDescriptors(deploymentDescriptor,
                                                                                                         extensionDescriptorChain);
        if (!incompatibleExtensionDescriptors.isEmpty()) {
            throw new ContentException(Messages.EXTENSION_DESCRIPTORS_MUST_HAVE_THE_SAME_MAJOR_SCHEMA_VERSION_AS_THE_DEPLOYMENT_DESCRIPTOR_BUT_0_DO_NOT,
                                       String.join(",", Descriptor.getIds(incompatibleExtensionDescriptors)));
        }
    }

    private List<ExtensionDescriptor> getIncompatibleExtensionDescriptors(DeploymentDescriptor deploymentDescriptor,
                                                                          List<ExtensionDescriptor> extensionDescriptors) {
        return ListUtils.select(extensionDescriptors, extensionDescriptor -> !areCompatible(deploymentDescriptor, extensionDescriptor));
    }

    private boolean areCompatible(DeploymentDescriptor deploymentDescriptor, ExtensionDescriptor extensionDescriptor) {
        return areCompatible(deploymentDescriptor.getSchemaVersion(), extensionDescriptor.getSchemaVersion());
    }

    private boolean areCompatible(String version1, String version2) {
        Version parsedVersion1 = Version.parseVersion(version1);
        Version parsedVersion2 = Version.parseVersion(version2);
        return parsedVersion1.getMajor() == parsedVersion2.getMajor();
    }

    private Version getMax(DeploymentDescriptor deploymentDescriptor, List<ExtensionDescriptor> extensionDescriptors) {
        SortedSet<Version> allVersions = getVersions(deploymentDescriptor, extensionDescriptors);
        return allVersions.last();
    }

    private SortedSet<Version> getVersions(DeploymentDescriptor deploymentDescriptor, List<ExtensionDescriptor> extensionDescriptors) {
        SortedSet<Version> version = new TreeSet<>();
        version.add(Version.parseVersion(deploymentDescriptor.getSchemaVersion()));
        for (ExtensionDescriptor extensionDescriptor : extensionDescriptors) {
            version.add(Version.parseVersion(extensionDescriptor.getSchemaVersion()));
        }
        return version;
    }

}
