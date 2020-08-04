package org.cloudfoundry.multiapps.mta.builders.v3;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.ListUtils;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.Module;

public class ModuleDependenciesCollector extends org.cloudfoundry.multiapps.mta.builders.v2.ModuleDependenciesCollector {

    public ModuleDependenciesCollector() {
        super(null);
    }

    @Override
    public Set<String> collect(DeploymentDescriptor descriptor, Module module) {
        Set<String> collectedDependencies = super.collect(descriptor, module);
        List<String> deployedAfter = new ArrayList<>(collectedDependencies);
        module.setDeployedAfter(deployedAfter);
        return collectedDependencies;
    }

    @Override
    protected List<String> getDependencies(Module module) {
        return ListUtils.emptyIfNull(module.getDeployedAfter());
    }

    @Override
    protected Module findModuleSatisfyingDependency(DeploymentDescriptor descriptor, String dependency) {
        return descriptor.getModules()
                         .stream()
                         .filter(module -> module.getName()
                                                 .equals(dependency))
                         .findFirst()
                         .orElse(null);
    }
}