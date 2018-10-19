package com.sap.cloud.lm.sl.mta.handlers.v2;

import java.util.Comparator;

import com.sap.cloud.lm.sl.mta.builders.v2.ModuleDependenciesCollector;
import com.sap.cloud.lm.sl.mta.model.v1.DeploymentDescriptor;

public class ModulesSorter extends com.sap.cloud.lm.sl.mta.handlers.v1.ModulesSorter {

    public ModulesSorter(DeploymentDescriptor descriptor, DescriptorHandler handler, String dependencyTypeProperty,
        String hardDependencyType) {
        super(descriptor, handler, dependencyTypeProperty, hardDependencyType);
    }

    @Override
    protected ModuleDependenciesCollector getDependenciesCollector() {
        return new ModuleDependenciesCollector(descriptor, handler);
    }

    @Override
    protected Comparator getModuleComparator(String dependencyTypeProperty, String hardDependencyType) {
        return new ModuleComparator(dependencyTypeProperty, hardDependencyType);
    }

}
