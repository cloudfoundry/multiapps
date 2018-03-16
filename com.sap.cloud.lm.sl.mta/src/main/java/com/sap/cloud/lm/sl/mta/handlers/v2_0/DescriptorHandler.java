package com.sap.cloud.lm.sl.mta.handlers.v2_0;

import java.util.ArrayList;
import java.util.List;

import com.sap.cloud.lm.sl.mta.handlers.v1_0.ModuleComparator;
import com.sap.cloud.lm.sl.mta.model.v2_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionModule;
import com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionRequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v2_0.Module;
import com.sap.cloud.lm.sl.mta.model.v2_0.RequiredDependency;

public class DescriptorHandler extends com.sap.cloud.lm.sl.mta.handlers.v1_0.DescriptorHandler {

    @Override
    protected List<String> getRequiredDependencyNames(com.sap.cloud.lm.sl.mta.model.v1_0.Module module) {
        List<String> names = new ArrayList<>();
        for (RequiredDependency dependency : ((Module) module).getRequiredDependencies2_0()) {
            names.add(dependency.getName());
        }
        return names;
    }

    public RequiredDependency findRequiredDependency(DeploymentDescriptor descriptor, String moduleName, String dependencyName) {
        for (Module module : descriptor.getModules2_0()) {
            if (module.getName()
                .equals(moduleName)) {
                return findRequiredDependency(module, dependencyName);
            }
        }
        return null;
    }

    public ExtensionRequiredDependency findRequiredDependency(ExtensionDescriptor descriptor, String moduleName, String dependencyName) {
        for (ExtensionModule module : descriptor.getModules2_0()) {
            if (module.getName()
                .equals(moduleName)) {
                return findRequiredDependency(module, dependencyName);
            }
        }
        return null;
    }

    public RequiredDependency findRequiredDependency(Module module, String dependencyName) {
        for (RequiredDependency requiredDependency : module.getRequiredDependencies2_0()) {
            if (requiredDependency.getName()
                .equals(dependencyName)) {
                return requiredDependency;
            }
        }
        return null;
    }

    public ExtensionRequiredDependency findRequiredDependency(ExtensionModule module, String dependencyName) {
        for (ExtensionRequiredDependency requiredDependency : module.getRequiredDependencies2_0()) {
            if (requiredDependency.getName()
                .equals(dependencyName)) {
                return requiredDependency;
            }
        }
        return null;
    }

    @Override
    protected ModuleComparator getModuleComparator(String dependencyTypeProperty, String hardDependencyType) {
        return new com.sap.cloud.lm.sl.mta.handlers.v2_0.ModuleComparator(dependencyTypeProperty, hardDependencyType);
    }

}
