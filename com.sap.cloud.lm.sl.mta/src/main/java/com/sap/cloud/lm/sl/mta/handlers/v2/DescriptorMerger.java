package com.sap.cloud.lm.sl.mta.handlers.v2;

import com.sap.cloud.lm.sl.mta.mergers.v2.ExtensionDescriptorMerger;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionDescriptor;

public class DescriptorMerger extends com.sap.cloud.lm.sl.mta.handlers.v1.DescriptorMerger {

    public DescriptorMerger() {
        super(new DescriptorHandler());
    }

    public DescriptorMerger(DescriptorHandler handler) {
        super(handler);
    }

    @Override
    public ExtensionDescriptorMerger getExtensionDescriptorMerger(com.sap.cloud.lm.sl.mta.model.v1.ExtensionDescriptor extension) {
        return new ExtensionDescriptorMerger((ExtensionDescriptor) extension, (DescriptorHandler) handler);
    }

}
