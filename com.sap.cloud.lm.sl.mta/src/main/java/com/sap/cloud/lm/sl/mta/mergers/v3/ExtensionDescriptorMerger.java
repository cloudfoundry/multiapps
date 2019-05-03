package com.sap.cloud.lm.sl.mta.mergers.v3;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.mta.handlers.v3.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.ExtensionResource;
import com.sap.cloud.lm.sl.mta.model.Resource;

public class ExtensionDescriptorMerger extends com.sap.cloud.lm.sl.mta.mergers.v2.ExtensionDescriptorMerger {

    public ExtensionDescriptorMerger(ExtensionDescriptor extensionDescriptor, DescriptorHandler handler) {
        super(extensionDescriptor, handler);
    }

    @Override
    public void merge(Resource resource, ExtensionResource extension) {
        resource.setActive(ObjectUtils.defaultIfNull(extension.isActive(), resource.isActive()));
        super.merge(resource, extension);
    }

}
