package com.sap.cloud.lm.sl.mta.mergers.v3;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.mta.handlers.v3.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.Resource;
import com.sap.cloud.lm.sl.mta.model.v3.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.v3.ExtensionResource;

public class ExtensionDescriptorMerger extends com.sap.cloud.lm.sl.mta.mergers.v2.ExtensionDescriptorMerger {

    public ExtensionDescriptorMerger(ExtensionDescriptor extensionDescriptor, DescriptorHandler handler) {
        super(extensionDescriptor, handler);
    }

    @Override
    public void merge(Resource resource, com.sap.cloud.lm.sl.mta.model.v2.ExtensionResource extension) {
        merge(resource, (ExtensionResource) extension);
    }

    private void merge(Resource resource, ExtensionResource extension) {
        resource.setActive(ObjectUtils.defaultIfNull(extension.isActive(), resource.isActive()));
        super.merge(resource, extension);
    }
}
