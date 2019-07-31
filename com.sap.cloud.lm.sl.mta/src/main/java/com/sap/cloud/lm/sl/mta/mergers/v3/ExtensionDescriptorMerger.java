package com.sap.cloud.lm.sl.mta.mergers.v3;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.mta.handlers.v3.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.ExtensionHook;
import com.sap.cloud.lm.sl.mta.model.ExtensionRequiredDependency;
import com.sap.cloud.lm.sl.mta.model.ExtensionResource;
import com.sap.cloud.lm.sl.mta.model.Hook;
import com.sap.cloud.lm.sl.mta.model.Resource;
import com.sap.cloud.lm.sl.mta.util.PropertiesUtil;

public class ExtensionDescriptorMerger extends com.sap.cloud.lm.sl.mta.mergers.v2.ExtensionDescriptorMerger {

    private final DescriptorHandler handlerV3;

    public ExtensionDescriptorMerger(ExtensionDescriptor extensionDescriptor, DescriptorHandler handler) {
        super(extensionDescriptor, handler);
        this.handlerV3 = handler;
    }

    @Override
    public void merge(Resource resource, ExtensionResource extension) {
        resource.setActive(ObjectUtils.defaultIfNull(extension.isActive(), resource.isActive()));
        super.merge(resource, extension);
    }

    @Override
    public void visit(ElementContext context, Hook hook) {
        String moduleContainerName = context.getPreviousElementContext()
                                            .getVisitableElementName();
        ExtensionHook extensionHook = handlerV3.findHook(extensionDescriptor, moduleContainerName, hook.getName());
        if (extensionHook != null) {
            merge(hook, extensionHook);
        }
    }

    private void merge(Hook hook, ExtensionHook extensionHook) {
        hook.setParameters(PropertiesUtil.mergeExtensionProperties(hook.getParameters(), extensionHook.getParameters()));
        mergeHookRequiredDependencies(hook, extensionHook);
    }

    private void mergeHookRequiredDependencies(Hook hook, ExtensionHook extensionHook) {
        hook.getRequiredDependencies()
            .forEach(requiredDependency -> {
                ExtensionRequiredDependency extensionRequiredDependency = handlerV3.findRequiredDependency(extensionHook,
                                                                                                           requiredDependency.getName());
                if (extensionRequiredDependency != null) {
                    merge(requiredDependency, extensionRequiredDependency);
                }
            });
    }

}
