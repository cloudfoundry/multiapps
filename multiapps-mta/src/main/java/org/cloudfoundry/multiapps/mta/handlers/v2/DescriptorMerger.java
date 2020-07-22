package org.cloudfoundry.multiapps.mta.handlers.v2;

import java.util.List;

import org.cloudfoundry.multiapps.mta.mergers.v2.ExtensionDescriptorMerger;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.ExtensionDescriptor;

public class DescriptorMerger {

    protected final DescriptorHandler handler;

    public DescriptorMerger() {
        this(new DescriptorHandler());
    }

    public DescriptorMerger(DescriptorHandler handler) {
        this.handler = handler;
    }

    public ExtensionDescriptorMerger getExtensionDescriptorMerger(ExtensionDescriptor extension) {
        return new ExtensionDescriptorMerger(extension, handler);
    }

    public DeploymentDescriptor merge(DeploymentDescriptor deploymentDescriptor, List<ExtensionDescriptor> extensionDescriptorsChain) {
        for (ExtensionDescriptor extension : extensionDescriptorsChain) {
            deploymentDescriptor = getExtensionDescriptorMerger(extension).merge(deploymentDescriptor);
        }
        return deploymentDescriptor;
    }
}
