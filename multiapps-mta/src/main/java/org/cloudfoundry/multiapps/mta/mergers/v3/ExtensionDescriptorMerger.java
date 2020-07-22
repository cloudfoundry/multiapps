package org.cloudfoundry.multiapps.mta.mergers.v3;

import org.apache.commons.lang3.ObjectUtils;
import org.cloudfoundry.multiapps.mta.handlers.v3.DescriptorHandler;
import org.cloudfoundry.multiapps.mta.model.ElementContext;
import org.cloudfoundry.multiapps.mta.model.ExtensionDescriptor;
import org.cloudfoundry.multiapps.mta.model.ExtensionHook;
import org.cloudfoundry.multiapps.mta.model.ExtensionRequiredDependency;
import org.cloudfoundry.multiapps.mta.model.ExtensionResource;
import org.cloudfoundry.multiapps.mta.model.Hook;
import org.cloudfoundry.multiapps.mta.model.RequiredDependency;
import org.cloudfoundry.multiapps.mta.model.Resource;
import org.cloudfoundry.multiapps.mta.util.PropertiesUtil;

public class ExtensionDescriptorMerger extends org.cloudfoundry.multiapps.mta.mergers.v2.ExtensionDescriptorMerger {

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
        for (RequiredDependency requiredDependency : hook.getRequiredDependencies()) {
            ExtensionRequiredDependency extensionRequiredDependency = handlerV3.findRequiredDependency(extensionHook,
                                                                                                       requiredDependency.getName());
            if (extensionRequiredDependency != null) {
                merge(requiredDependency, extensionRequiredDependency);
            }
        }
    }
}
