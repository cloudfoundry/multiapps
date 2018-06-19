package com.sap.cloud.lm.sl.mta.handlers.v3_1;

import com.sap.cloud.lm.sl.mta.handlers.v3_1.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.mergers.v3_1.ExtensionDescriptorMerger;
import com.sap.cloud.lm.sl.mta.model.v3_1.ExtensionDescriptor;

public class DescriptorMerger extends com.sap.cloud.lm.sl.mta.handlers.v2_0.DescriptorMerger {

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
