package org.cloudfoundry.multiapps.mta.builders.v2;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorHandler;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.model.RequiredDependency;

public class ModuleDependenciesCollector {

    private final DescriptorHandler handler;

    public ModuleDependenciesCollector(DescriptorHandler handler) {
        this.handler = handler;
    }

    public Set<String> collect(DeploymentDescriptor descriptor, Module module) {
        Set<String> seenModules = new HashSet<>();
        return getDependenciesRecursively(descriptor, module, seenModules);
    }

    private Set<String> getDependenciesRecursively(DeploymentDescriptor descriptor, Module module, Set<String> seenModules) {
        if (seenModules.contains(module.getName())) {
            return Collections.emptySet();
        }
        seenModules.add(module.getName());
        return collectDependenciesRecursively(descriptor, module, seenModules);
    }

    private Set<String> collectDependenciesRecursively(DeploymentDescriptor descriptor, Module module, Set<String> seenModules) {
        Set<String> dependencies = new LinkedHashSet<>();
        for (String dependency : getDependencies(module)) {
            Module moduleProvidingDependency = findModuleSatisfyingDependency(descriptor, dependency);
            if (notRequiresSelf(module, moduleProvidingDependency)) {
                dependencies.add(moduleProvidingDependency.getName());
                dependencies.addAll(getDependenciesRecursively(descriptor, moduleProvidingDependency, seenModules));
            }
        }
        return dependencies;
    }

    protected List<String> getDependencies(Module module) {
        return module.getRequiredDependencies()
                     .stream()
                     .map(RequiredDependency::getName)
                     .collect(Collectors.toList());
    }

    protected Module findModuleSatisfyingDependency(DeploymentDescriptor descriptor, String dependency) {
        return descriptor.getModules()
                         .stream()
                         .filter(module -> handler.findProvidedDependency(module, dependency) != null)
                         .findFirst()
                         .orElse(null);
    }

    private boolean notRequiresSelf(Module module, Module requiredModule) {
        return requiredModule != null && !requiredModule.getName()
                                                        .equals(module.getName());
    }

}
