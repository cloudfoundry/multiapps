package com.sap.cloud.lm.sl.mta.handlers.v2_0;

import com.sap.cloud.lm.sl.mta.mergers.v2_0.ExtensionDescriptorMerger;
import com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionDescriptor;

public class DescriptorMerger extends com.sap.cloud.lm.sl.mta.handlers.v1_0.DescriptorMerger {

    public DescriptorMerger() {
        super(new DescriptorHandler());
    }

    public DescriptorMerger(DescriptorHandler handler) {
        super(handler);
    }

    @Override
    public ExtensionDescriptorMerger getExtensionDescriptorMerger(com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionDescriptor extension) {
        return new ExtensionDescriptorMerger((ExtensionDescriptor) extension, (DescriptorHandler) handler);
    }

}
