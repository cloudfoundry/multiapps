package com.sap.cloud.lm.sl.mta.builders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.message.Messages;
import com.sap.cloud.lm.sl.mta.model.Descriptor;
import com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionDescriptor;

public class ExtensionDescriptorChainBuilder {

    private final boolean isStrict;

    public ExtensionDescriptorChainBuilder() {
        this(true);
    }

    public ExtensionDescriptorChainBuilder(boolean isStrict) {
        this.isStrict = isStrict;
    }

    public List<ExtensionDescriptor> build(DeploymentDescriptor deploymentDescriptor, List<ExtensionDescriptor> extensionDescriptors)
        throws ContentException {
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
        if (!extensionDescriptorsPerParent.isEmpty() && isStrict) {
            throw new ContentException(Messages.CANNOT_BUILD_EXTENSION_DESCRIPTOR_CHAIN_BECAUSE_DESCRIPTORS_0_HAVE_AN_UNKNOWN_PARENT,
                String.join(",", Descriptor.getIds(extensionDescriptorsPerParent.values())));
        }
        return chain;
    }

    private Map<String, ExtensionDescriptor> getExtensionDescriptorsPerParent(List<ExtensionDescriptor> extensionDescriptors) {
        Map<String, List<ExtensionDescriptor>> extensionDescriptorsPerParent = extensionDescriptors.stream()
            .collect(Collectors.groupingBy(ExtensionDescriptor::getParentId));
        return prune(extensionDescriptorsPerParent);
    }

    private Map<String, ExtensionDescriptor> prune(Map<String, List<ExtensionDescriptor>> extensionDescriptorsPerParent) {
        validateSingleExtensionDescriptorPerParent(extensionDescriptorsPerParent);
        return extensionDescriptorsPerParent.entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue()
                .get(0)));
    }

    private void validateSingleExtensionDescriptorPerParent(Map<String, List<ExtensionDescriptor>> extensionDescriptorsPerParent) {
        for (Map.Entry<String, List<ExtensionDescriptor>> extensionDescriptorsForParent : extensionDescriptorsPerParent.entrySet()) {
            String parent = extensionDescriptorsForParent.getKey();
            List<ExtensionDescriptor> extensionDescriptors = extensionDescriptorsForParent.getValue();
            if (extensionDescriptors.size() > 1 && isStrict) {
                throw new ContentException(Messages.MULTIPLE_EXTENSION_DESCRIPTORS_EXTEND_THE_PARENT_0, parent);
            }
        }
    }

}
