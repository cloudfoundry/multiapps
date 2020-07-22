package org.cloudfoundry.multiapps.mta.builders.v3;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.ListUtils;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.Module;

public class ModuleDependenciesCollector extends org.cloudfoundry.multiapps.mta.builders.v2.ModuleDependenciesCollector {

    public ModuleDependenciesCollector(DeploymentDescriptor descriptor) {
        super(descriptor, null);
    }

    @Override
    public Set<String> collect(Module module) {
        Set<String> collectedDependencies = super.collect(module);
        List<String> deployedAfter = new ArrayList<>(collectedDependencies);
        module.setDeployedAfter(deployedAfter);
        return collectedDependencies;
    }

    @Override
    protected List<String> getDependencies(Module module) {
        return ListUtils.emptyIfNull(module.getDeployedAfter());
    }

    @Override
    protected Module findModuleSatisfyingDependency(String dependency) {
        return descriptor.getModules()
                         .stream()
                         .filter(module -> module.getName()
                                                 .equals(dependency))
                         .findFirst()
                         .orElse(null);
    }
}