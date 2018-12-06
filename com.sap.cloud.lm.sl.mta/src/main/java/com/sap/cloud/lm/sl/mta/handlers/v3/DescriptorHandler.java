package com.sap.cloud.lm.sl.mta.handlers.v3;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.cast;

import com.sap.cloud.lm.sl.mta.model.v3.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v3.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.v3.ExtensionRequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v3.ExtensionResource;
import com.sap.cloud.lm.sl.mta.model.v3.RequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v3.Resource;

public class DescriptorHandler extends com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorHandler {

    @Override
    public RequiredDependency findRequiredDependency(com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor descriptor, String consumerName,
        String dependencyName) {
        RequiredDependency moduleRequireDependency = cast(super.findRequiredDependency(descriptor, consumerName, dependencyName));
        if (moduleRequireDependency != null) {
            return moduleRequireDependency;
        }
        DeploymentDescriptor descriptorV3 = cast(descriptor);
        for (Resource resource : descriptorV3.getResources3()) {
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
        ExtensionRequiredDependency moduleRequireDependency = cast(super.findRequiredDependency(descriptor, consumerName, dependencyName));
        if (moduleRequireDependency != null) {
            return moduleRequireDependency;
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
        for (RequiredDependency requiredDependency : resource.getRequiredDependencies3()) {
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
    protected com.sap.cloud.lm.sl.mta.handlers.v2.ModulesSorter getModuleSorter(
        com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor descriptor, String parallelDeploymentProperty, String dependencyTypeProperty,
        String hardDependencyType) {
        DeploymentDescriptor descriptorV3 = cast(descriptor);
        return new com.sap.cloud.lm.sl.mta.handlers.v3.ModulesSorter(descriptorV3, this, dependencyTypeProperty, hardDependencyType,
            parallelDeploymentProperty);
    }

}
