package com.sap.cloud.lm.sl.mta.mergers.v3_1;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.mta.handlers.v3_1.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.v3_1.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.v3_1.ExtensionResource;
import com.sap.cloud.lm.sl.mta.model.v3_1.Resource;

public class ExtensionDescriptorMerger extends com.sap.cloud.lm.sl.mta.mergers.v2_0.ExtensionDescriptorMerger {

    public ExtensionDescriptorMerger(ExtensionDescriptor extensionDescriptor, DescriptorHandler handler) {
        super(extensionDescriptor, handler);
    }

    @Override
    public void merge(com.sap.cloud.lm.sl.mta.model.v1_0.Resource resource,
        com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionResource extension) {
        merge((Resource) resource, (ExtensionResource) extension);
    }

    private void merge(Resource resource, ExtensionResource extension) {
        resource.setActive(ObjectUtils.defaultIfNull(extension.getActive(), resource.getActive()));
        super.merge(resource, extension);
    }
}
