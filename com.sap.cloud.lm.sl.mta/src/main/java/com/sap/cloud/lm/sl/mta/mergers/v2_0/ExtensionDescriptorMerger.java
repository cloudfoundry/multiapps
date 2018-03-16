package com.sap.cloud.lm.sl.mta.mergers.v2_0;

import com.sap.cloud.lm.sl.mta.handlers.v2_0.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.v1_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionModule;
import com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionRequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionResource;
import com.sap.cloud.lm.sl.mta.model.v2_0.Module;
import com.sap.cloud.lm.sl.mta.model.v2_0.RequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v2_0.Resource;
import com.sap.cloud.lm.sl.mta.util.PropertiesUtil;

public class ExtensionDescriptorMerger extends com.sap.cloud.lm.sl.mta.mergers.v1_0.ExtensionDescriptorMerger {

    public ExtensionDescriptorMerger(ExtensionDescriptor extensionDescriptor, DescriptorHandler handler) {
        super(extensionDescriptor, handler);
    }

    @Override
    public void visit(ElementContext context, DeploymentDescriptor descriptor) {
        com.sap.cloud.lm.sl.mta.model.v2_0.DeploymentDescriptor descriptorV2 = (com.sap.cloud.lm.sl.mta.model.v2_0.DeploymentDescriptor) descriptor;
        ExtensionDescriptor extensionDescriptorV2 = (ExtensionDescriptor) extensionDescriptor;
        descriptorV2
            .setParameters(PropertiesUtil.mergeExtensionProperties(descriptorV2.getParameters(), extensionDescriptorV2.getParameters()));
    }

    @Override
    public void visit(ElementContext context, RequiredDependency requiredDependency) {
        String containerName = context.getPreviousElementContext()
            .getVisitableElementName();
        ExtensionRequiredDependency extension = ((DescriptorHandler) handler)
            .findRequiredDependency((ExtensionDescriptor) extensionDescriptor, containerName, requiredDependency.getName());
        if (extension != null) {
            merge(requiredDependency, extension);
        }
    }

    protected void merge(RequiredDependency requiredDependency, ExtensionRequiredDependency extension) {
        requiredDependency
            .setParameters(PropertiesUtil.mergeExtensionProperties(requiredDependency.getParameters(), extension.getParameters()));
        requiredDependency
            .setProperties(PropertiesUtil.mergeExtensionProperties(requiredDependency.getProperties(), extension.getProperties()));
    }

    @Override
    public void merge(com.sap.cloud.lm.sl.mta.model.v1_0.Module module, com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionModule extension) {
        merge((Module) module, (ExtensionModule) extension);
    }

    private void merge(Module module, ExtensionModule extension) {
        module.setParameters(PropertiesUtil.mergeExtensionProperties(module.getParameters(), extension.getParameters()));
        super.merge(module, extension);
    }

    @Override
    public void merge(com.sap.cloud.lm.sl.mta.model.v1_0.Resource resource,
        com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionResource extension) {
        merge((Resource) resource, (ExtensionResource) extension);
    }

    private void merge(Resource resource, ExtensionResource extension) {
        resource.setParameters(PropertiesUtil.mergeExtensionProperties(resource.getParameters(), extension.getParameters()));
        super.merge(resource, extension);
    }
}
