package com.sap.cloud.lm.sl.mta.mergers.v3;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.mta.handlers.v3.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.v3.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.v3.ExtensionResource;
import com.sap.cloud.lm.sl.mta.model.v3.Resource;

public class ExtensionDescriptorMerger extends com.sap.cloud.lm.sl.mta.mergers.v2.ExtensionDescriptorMerger {

    public ExtensionDescriptorMerger(ExtensionDescriptor extensionDescriptor, DescriptorHandler handler) {
        super(extensionDescriptor, handler);
    }

    @Override
    public void merge(com.sap.cloud.lm.sl.mta.model.v1.Resource resource,
        com.sap.cloud.lm.sl.mta.model.v1.ExtensionResource extension) {
        merge((Resource) resource, (ExtensionResource) extension);
    }

    private void merge(Resource resource, ExtensionResource extension) {
        resource.setActive(ObjectUtils.defaultIfNull(extension.isActive(), resource.isActive()));
        super.merge(resource, extension);
    }
}
