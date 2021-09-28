package org.cloudfoundry.multiapps.mta.handlers.v3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.Module;

public class ModulesSorter extends org.cloudfoundry.multiapps.mta.handlers.v2.ModulesSorter {

    private String parallelDeploymentsProperty;

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

    private List<Module> sortUsingDeployedAfter() {
        List<Module> modules = new ArrayList<>(descriptor.getModules());
        modules.sort(getModuleComparator());
        return modules;
    }

    protected Comparator<Module> getModuleComparator() {
        return new ModuleComparator();
    }

}
