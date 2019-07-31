package com.sap.cloud.lm.sl.mta.handlers.v3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.sap.cloud.lm.sl.mta.builders.v2.ModuleDependenciesCollector;
import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.Module;

public class ModulesSorter extends com.sap.cloud.lm.sl.mta.handlers.v2.ModulesSorter {

    String parallelDeploymentsProperty;

    public ModulesSorter(DeploymentDescriptor descriptor, DescriptorHandler handler, String dependencyTypeProperty,
                         String hardDependencyType, String parallelDeploymentsProperty) {
        super(descriptor, handler, dependencyTypeProperty, hardDependencyType);
        this.parallelDeploymentsProperty = parallelDeploymentsProperty;
    }

    @Override
    public List<Module> sort() {
        if (hasDeployedAfter(descriptor)) {
            return sortUsingDeployedAfter();
        }
        return super.sort();
    }

    private boolean hasDeployedAfter(DeploymentDescriptor descriptorV3) {
        return isParallelDeploymentsEnabled(descriptorV3) || hasDeployedAfterAttribute(descriptorV3);
    }

    private boolean isParallelDeploymentsEnabled(DeploymentDescriptor descriptor) {
        return (Boolean) descriptor.getParameters()
                                   .getOrDefault(parallelDeploymentsProperty, false);
    }

    private boolean hasDeployedAfterAttribute(DeploymentDescriptor descriptor) {
        return descriptor.getModules()
                         .stream()
                         .anyMatch(this::hasDeployedAfterAttribute);
    }

    private boolean hasDeployedAfterAttribute(Module module) {
        return module.getDeployedAfter() != null;
    }

    @Override
    protected ModuleDependenciesCollector getDependenciesCollector() {
        return new ModuleDependenciesCollector(descriptor, handler);
    }

    private List<Module> sortUsingDeployedAfter() {
        List<Module> modules = new ArrayList<>(getModules());
        modules.sort(getModuleComparator());
        return modules;
    }

    protected List<Module> getModules() {
        List<Module> modules = new ArrayList<>(descriptor.getModules());
        modules.forEach(this::collectDependencies);
        return modules;
    }

    protected void collectDependencies(Module module) {
        getDependenciesCollectorSupportingDeployedAfter().collect(module);
    }

    private ModuleDependenciesCollector getDependenciesCollectorSupportingDeployedAfter() {
        return new com.sap.cloud.lm.sl.mta.builders.v3.ModuleDependenciesCollector(descriptor);
    }

    protected Comparator<Module> getModuleComparator() {
        return new ModuleComparator();
    }

}
