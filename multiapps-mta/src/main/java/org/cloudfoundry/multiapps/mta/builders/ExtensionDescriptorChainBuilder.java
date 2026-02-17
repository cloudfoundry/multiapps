package org.cloudfoundry.multiapps.mta.builders;

import org.apache.commons.collections4.CollectionUtils;
import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.Constants;
import org.cloudfoundry.multiapps.mta.Messages;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.Descriptor;
import org.cloudfoundry.multiapps.mta.model.ExtensionDescriptor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExtensionDescriptorChainBuilder {

    private final boolean isStrict;

    private ExtensionDescriptor secureExtensionDescriptor;

    public ExtensionDescriptorChainBuilder() {
        this(true);
    }

    public ExtensionDescriptorChainBuilder(boolean isStrict) {
        this.isStrict = isStrict;
    }

    private boolean isSecureDescriptor(ExtensionDescriptor extensionDescriptor) {
        return extensionDescriptor.getId()
                                  .equals(Constants.SECURE_EXTENSION_DESCRIPTOR_ID);
    }

    public List<ExtensionDescriptor> build(DeploymentDescriptor deploymentDescriptor, List<ExtensionDescriptor> extensionDescriptors)
        throws ContentException {
        saveSecureExtensionDescriptor(extensionDescriptors);
        Map<String, ExtensionDescriptor> extensionDescriptorsPerParent = getExtensionDescriptorsPerParent(extensionDescriptors);
        return build(deploymentDescriptor, extensionDescriptorsPerParent);
    }

    private List<ExtensionDescriptor> build(DeploymentDescriptor deploymentDescriptor,
                                            Map<String, ExtensionDescriptor> extensionDescriptorsPerParent) {
        List<ExtensionDescriptor> chain = new ArrayList<>();
        Descriptor currentDescriptor = deploymentDescriptor;

        while (currentDescriptor != null) {
            ExtensionDescriptor nextDescriptor = extensionDescriptorsPerParent.remove(currentDescriptor.getId());
            CollectionUtils.addIgnoreNull(chain, nextDescriptor);
            currentDescriptor = nextDescriptor;
        }

        CollectionUtils.addIgnoreNull(chain, this.secureExtensionDescriptor);

        if (!extensionDescriptorsPerParent.isEmpty() && isStrict) {
            throw new ContentException(Messages.CANNOT_BUILD_EXTENSION_DESCRIPTOR_CHAIN_BECAUSE_DESCRIPTORS_0_HAVE_AN_UNKNOWN_PARENT,
                                       String.join(",", Descriptor.getIds(extensionDescriptorsPerParent.values())));
        }

        return chain;
    }

    private Map<String, ExtensionDescriptor> getExtensionDescriptorsPerParent(List<ExtensionDescriptor> extensionDescriptors) {
        Map<String, List<ExtensionDescriptor>> extensionDescriptorsPerParent = extensionDescriptors.stream()
                                                                                                   .collect(Collectors.groupingBy(
                                                                                                       ExtensionDescriptor::getParentId));
        validateSingleExtensionDescriptorPerParent(extensionDescriptorsPerParent);
        return prune(extensionDescriptorsPerParent);
    }

    private Map<String, ExtensionDescriptor> prune(Map<String, List<ExtensionDescriptor>> extensionDescriptorsPerParent) {
        Map<String, ExtensionDescriptor> resultMap = new LinkedHashMap<>();

        for (Map.Entry<String, List<ExtensionDescriptor>> entry : extensionDescriptorsPerParent.entrySet()) {
            ExtensionDescriptor firstNonSecureExtensionDescriptor = null;

            for (ExtensionDescriptor currentExtensionDescriptorForParent : entry.getValue()) {
                if (!isSecureDescriptor(currentExtensionDescriptorForParent)) {
                    firstNonSecureExtensionDescriptor = currentExtensionDescriptorForParent;
                    break;
                }
            }

            if (firstNonSecureExtensionDescriptor != null) {
                resultMap.put(entry.getKey(), firstNonSecureExtensionDescriptor);
            }
        }
        return resultMap;
    }

    private void validateSingleExtensionDescriptorPerParent(Map<String, List<ExtensionDescriptor>> extensionDescriptorsPerParent) {
        for (Map.Entry<String, List<ExtensionDescriptor>> extensionDescriptorsForParent : extensionDescriptorsPerParent.entrySet()) {
            String parent = extensionDescriptorsForParent.getKey();
            List<ExtensionDescriptor> extensionDescriptors = extensionDescriptorsForParent.getValue();
            long nonSecureCountOfExtensionDescriptors = extensionDescriptors.stream()
                                                                            .filter(descriptor -> !descriptor.getId()
                                                                                                             .equals(
                                                                                                                 Constants.SECURE_EXTENSION_DESCRIPTOR_ID))
                                                                            .count();
            if (nonSecureCountOfExtensionDescriptors > 1 && isStrict) {
                throw new ContentException(Messages.MULTIPLE_EXTENSION_DESCRIPTORS_EXTEND_THE_PARENT_0, parent);
            }
        }
    }

    private void saveSecureExtensionDescriptor(List<ExtensionDescriptor> extensionDescriptors) {
        ExtensionDescriptor lastSecureExtensionDescriptor = null;

        for (ExtensionDescriptor extensionDescriptor : extensionDescriptors) {
            if (isSecureDescriptor(extensionDescriptor)) {
                lastSecureExtensionDescriptor = extensionDescriptor;
            }
        }

        this.secureExtensionDescriptor = lastSecureExtensionDescriptor;
    }

}