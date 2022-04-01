package com.sap.cloud.lm.sl.mta.handlers.v3;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.cast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.sap.cloud.lm.sl.mta.builders.v2.ModuleDependenciesCollector;
import com.sap.cloud.lm.sl.mta.model.v2.Module;
import com.sap.cloud.lm.sl.mta.model.v3.DeploymentDescriptor;

public class ModulesSorter extends com.sap.cloud.lm.sl.mta.handlers.v2.ModulesSorter {

    String parallelDeploymentsProperty;

    public ModulesSorter(DeploymentDescriptor descriptor, DescriptorHandler handler, String dependencyTypeProperty,
                         String hardDependencyType, String parallelDeploymentsProperty) {
        super(descriptor, handler, dependencyTypeProperty, hardDependencyType);
        this.parallelDeploymentsProperty = parallelDeploymentsProperty;
    }

    @Override
    public List<? extends Module> sort() {
        DeploymentDescriptor descriptorV3 = cast(descriptor);
        if (hasDeployedAfter(descriptorV3)) {
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
        return descriptor.getModules3()
                         .stream()
                         .anyMatch(this::hasDeployedAfterAttribute);
    }

    private boolean hasDeployedAfterAttribute(Module module) {
        com.sap.cloud.lm.sl.mta.model.v3.Module moduleV3 = cast(module);
        return moduleV3.getDeployedAfter() != null;
    }

    @Override
    protected ModuleDependenciesCollector getDependenciesCollector() {
        return new ModuleDependenciesCollector(descriptor, handler);
    }

    private List<Module> sortUsingDeployedAfter() {
        List<Module> modules = new ArrayList<>(getModules());
        Collections.sort(modules, getModuleComparator());
        return modules;
    }

    protected List<Module> getModules() {
        DeploymentDescriptor descriptorV3 = cast(descriptor);
        List<Module> modules = new ArrayList<>(descriptorV3.getModules3());
        modules.stream()
               .forEach(this::collectDependencies);
        return modules;
    }

    protected void collectDependencies(Module module) {
        getDependenciesCollectorSupportingDeployedAfter().collect(module);
    }

    private ModuleDependenciesCollector getDependenciesCollectorSupportingDeployedAfter() {
        return new com.sap.cloud.lm.sl.mta.builders.v3.ModuleDependenciesCollector(descriptor);
    }

    protected Comparator getModuleComparator() {
        return new ModuleComparator();
    }

}
