package com.sap.cloud.lm.sl.mta.handlers.v3_1;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.cast;

import com.sap.cloud.lm.sl.mta.model.v2_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.v3_1.ExtensionRequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v3_1.ExtensionResource;
import com.sap.cloud.lm.sl.mta.model.v3_1.RequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v3_1.Resource;

public class DescriptorHandler extends com.sap.cloud.lm.sl.mta.handlers.v2_0.DescriptorHandler {

    @Override
    public RequiredDependency findRequiredDependency(DeploymentDescriptor descriptor, String consumerName, String dependencyName) {
        RequiredDependency moduleRequireDependency = cast(super.findRequiredDependency(descriptor, consumerName, dependencyName));
        if (moduleRequireDependency != null) {
            return moduleRequireDependency;
        }
        com.sap.cloud.lm.sl.mta.model.v3_1.DeploymentDescriptor descriptor3_1 = cast(descriptor);
        for (Resource resource : descriptor3_1.getResources3_1()) {
            if (resource.getName()
                .equals(consumerName)) {
                return findRequiredDependency(resource, dependencyName);
            }
        }
        return null;
    }

    @Override
    public ExtensionRequiredDependency findRequiredDependency(ExtensionDescriptor descriptor, String consumerName, String dependencyName) {
        ExtensionRequiredDependency moduleRequireDependency = cast(super.findRequiredDependency(descriptor, consumerName, dependencyName));
        if (moduleRequireDependency != null) {
            return moduleRequireDependency;
        }
        com.sap.cloud.lm.sl.mta.model.v3_1.ExtensionDescriptor descriptor3_1 = cast(descriptor);
        for (ExtensionResource resource : descriptor3_1.getResources3_1()) {
            if (resource.getName()
                .equals(consumerName)) {
                return findRequiredDependency(resource, dependencyName);
            }
        }
        return null;
    }

    public RequiredDependency findRequiredDependency(Resource resource, String dependencyName) {
        for (RequiredDependency requiredDependency : resource.getRequiredDependencies3_1()) {
            if (requiredDependency.getName()
                .equals(dependencyName)) {
                return requiredDependency;
            }
        }
        return null;
    }

    public ExtensionRequiredDependency findRequiredDependency(ExtensionResource resource, String dependencyName) {
        for (ExtensionRequiredDependency requiredDependency : resource.getRequiredDependencies3_1()) {
            if (requiredDependency.getName()
                .equals(dependencyName)) {
                return requiredDependency;
            }
        }
        return null;
    }
}
