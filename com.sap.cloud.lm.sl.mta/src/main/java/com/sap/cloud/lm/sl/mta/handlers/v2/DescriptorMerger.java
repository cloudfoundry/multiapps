package com.sap.cloud.lm.sl.mta.handlers.v2;

import com.sap.cloud.lm.sl.mta.mergers.v2.ExtensionDescriptorMerger;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionDescriptor;

public class DescriptorMerger {

    protected final DescriptorHandler handler;

    public DescriptorMerger() {
        this(new DescriptorHandler());
    }

    public DescriptorMerger(DescriptorHandler handler) {
        this.handler = handler;
    }

    public ExtensionDescriptorMerger getExtensionDescriptorMerger(ExtensionDescriptor extension) {
        return new ExtensionDescriptorMerger((ExtensionDescriptor) extension, (DescriptorHandler) handler);
    }

}
