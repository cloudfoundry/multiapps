package com.sap.cloud.lm.sl.mta.handlers.v3;

import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.ExtensionRequiredDependency;
import com.sap.cloud.lm.sl.mta.model.ExtensionResource;
import com.sap.cloud.lm.sl.mta.model.RequiredDependency;
import com.sap.cloud.lm.sl.mta.model.Resource;

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
    public ExtensionRequiredDependency findRequiredDependency(ExtensionDescriptor descriptor, String consumerName, String dependencyName) {
        ExtensionRequiredDependency moduleRequiredDependency = super.findRequiredDependency(descriptor, consumerName, dependencyName);
        if (moduleRequiredDependency != null) {
            return moduleRequiredDependency;
        }
        for (ExtensionResource resource : descriptor.getResources()) {
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
        for (ExtensionRequiredDependency requiredDependency : resource.getRequiredDependencies()) {
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
