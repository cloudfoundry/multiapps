package com.sap.cloud.lm.sl.mta.handlers.v2;

import com.sap.cloud.lm.sl.mta.handlers.ModulesSorter;
import com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionModule;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionRequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v2.Module;
import com.sap.cloud.lm.sl.mta.model.v2.RequiredDependency;

public class DescriptorHandler extends com.sap.cloud.lm.sl.mta.handlers.v1.DescriptorHandler {

    public RequiredDependency findRequiredDependency(DeploymentDescriptor descriptor, String moduleName, String dependencyName) {
        for (Module module : descriptor.getModules2()) {
            if (module.getName()
                .equals(moduleName)) {
                return findRequiredDependency(module, dependencyName);
            }
        }
        return null;
    }

    public ExtensionRequiredDependency findRequiredDependency(ExtensionDescriptor descriptor, String moduleName, String dependencyName) {
        for (ExtensionModule module : descriptor.getModules2()) {
            if (module.getName()
                .equals(moduleName)) {
                return findRequiredDependency(module, dependencyName);
            }
        }
        return null;
    }

    public RequiredDependency findRequiredDependency(Module module, String dependencyName) {
        for (RequiredDependency requiredDependency : module.getRequiredDependencies2()) {
            if (requiredDependency.getName()
                .equals(dependencyName)) {
                return requiredDependency;
            }
        }
        return null;
    }

    public ExtensionRequiredDependency findRequiredDependency(ExtensionModule module, String dependencyName) {
        for (ExtensionRequiredDependency requiredDependency : module.getRequiredDependencies2()) {
            if (requiredDependency.getName()
                .equals(dependencyName)) {
                return requiredDependency;
            }
        }
        return null;
    }

    @Override
    protected ModulesSorter getModuleSorter(com.sap.cloud.lm.sl.mta.model.v1.DeploymentDescriptor descriptor,
        String parallelDeploymentProperty, String dependencyTypeProperty, String hardDependencyType) {
        return new com.sap.cloud.lm.sl.mta.handlers.v2.ModulesSorter(descriptor, this, dependencyTypeProperty, hardDependencyType);
    }

}
