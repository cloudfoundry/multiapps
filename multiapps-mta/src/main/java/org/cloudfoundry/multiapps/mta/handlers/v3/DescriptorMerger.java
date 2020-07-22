package org.cloudfoundry.multiapps.mta.handlers.v3;

import org.cloudfoundry.multiapps.mta.mergers.v3.ExtensionDescriptorMerger;
import org.cloudfoundry.multiapps.mta.model.ExtensionDescriptor;

public class DescriptorMerger extends org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorMerger {

    public DescriptorMerger() {
        super(new DescriptorHandler());
    }

    public DescriptorMerger(DescriptorHandler handler) {
        super(handler);
    }

    @Override
    public ExtensionDescriptorMerger getExtensionDescriptorMerger(ExtensionDescriptor extension) {
        return new ExtensionDescriptorMerger(extension, (DescriptorHandler) handler);
    }

}
