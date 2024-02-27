package com.sap.cloud.lm.sl.mta.handlers.v3;

import com.sap.cloud.lm.sl.mta.mergers.v3.ExtensionDescriptorMerger;
import com.sap.cloud.lm.sl.mta.model.v3.ExtensionDescriptor;

public class DescriptorMerger extends com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorMerger {

    public DescriptorMerger() {
        super(new DescriptorHandler());
    }

    public DescriptorMerger(DescriptorHandler handler) {
        super(handler);
    }

    @Override
    public ExtensionDescriptorMerger getExtensionDescriptorMerger(com.sap.cloud.lm.sl.mta.model.v2.ExtensionDescriptor extension) {
        return new ExtensionDescriptorMerger((ExtensionDescriptor) extension, (DescriptorHandler) handler);
    }

}
