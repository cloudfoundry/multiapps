package com.sap.cloud.lm.sl.mta.handlers.v1;

import java.util.ArrayList;
import java.util.List;

import com.sap.cloud.lm.sl.common.util.Pair;
import com.sap.cloud.lm.sl.mta.mergers.v1.ExtensionDescriptorMerger;
import com.sap.cloud.lm.sl.mta.model.v1.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1.ExtensionDescriptor;

public class DescriptorMerger {

    protected final DescriptorHandler handler;

    public DescriptorMerger() {
        this(new DescriptorHandler());
    }

    public DescriptorMerger(DescriptorHandler handler) {
        this.handler = handler;
    }

    public Pair<DeploymentDescriptor, List<String>> merge(DeploymentDescriptor deploymentDescriptor,
        List<ExtensionDescriptor> extensionDescriptorsChain) {
        Pair<DeploymentDescriptor, List<String>> result = new Pair<>(deploymentDescriptor, new ArrayList<>());
        for (ExtensionDescriptor extension : extensionDescriptorsChain) {
            result = getExtensionDescriptorMerger(extension).merge(result);
        }
        return result;
    }

    public ExtensionDescriptorMerger getExtensionDescriptorMerger(ExtensionDescriptor extension) {
        return new ExtensionDescriptorMerger(extension, handler);
    }

}
