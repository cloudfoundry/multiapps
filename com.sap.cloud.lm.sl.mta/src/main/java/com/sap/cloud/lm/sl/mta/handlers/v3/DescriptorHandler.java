package com.sap.cloud.lm.sl.mta.handlers.v3;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.cast;

import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.RequiredDependency;
import com.sap.cloud.lm.sl.mta.model.Resource;
import com.sap.cloud.lm.sl.mta.model.v3.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.v3.ExtensionRequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v3.ExtensionResource;

public class DescriptorHandler extends com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorHandler {

    @Override
    public RequiredDependency findRequiredDependency(DeploymentDescriptor descriptor, String consumerName, String dependencyName) {
        RequiredDependency moduleRequiredDependency = super.findRequiredDependency(descriptor, consumerName, dependencyName);
        if (moduleRequiredDependency != null) {
            return moduleRequiredDependency;
        }
        for (Resource resource : descriptor.getResources()) {
            if (resource.getName()
                .equals(consumerName)) {
                return findRequiredDependency(resource, dependencyName);
            }
        }
        return null;
    }

    @Override
    public ExtensionRequiredDependency findRequiredDependency(com.sap.cloud.lm.sl.mta.model.v2.ExtensionDescriptor descriptor,
        String consumerName, String dependencyName) {
        ExtensionRequiredDependency moduleRequiredDependency = cast(super.findRequiredDependency(descriptor, consumerName, dependencyName));
        if (moduleRequiredDependency != null) {
            return moduleRequiredDependency;
        }
        ExtensionDescriptor descriptorV3 = cast(descriptor);
        for (ExtensionResource resource : descriptorV3.getResources3()) {
            if (resource.getName()
                .equals(consumerName)) {
                return findRequiredDependency(resource, dependencyName);
            }
        }
        return null;
    }

    public RequiredDependency findRequiredDependency(Resource resource, String dependencyName) {
        for (RequiredDependency requiredDependency : resource.getRequiredDependencies()) {
            if (requiredDependency.getName()
                .equals(dependencyName)) {
                return requiredDependency;
            }
        }
        return null;
    }

    public ExtensionRequiredDependency findRequiredDependency(ExtensionResource resource, String dependencyName) {
        for (ExtensionRequiredDependency requiredDependency : resource.getRequiredDependencies3()) {
            if (requiredDependency.getName()
                .equals(dependencyName)) {
                return requiredDependency;
            }
        }
        return null;
    }

    @Override
    protected com.sap.cloud.lm.sl.mta.handlers.v2.ModulesSorter getModuleSorter(DeploymentDescriptor descriptor,
        String parallelDeploymentProperty, String dependencyTypeProperty, String hardDependencyType) {
        return new com.sap.cloud.lm.sl.mta.handlers.v3.ModulesSorter(descriptor, this, dependencyTypeProperty, hardDependencyType,
            parallelDeploymentProperty);
    }

}
