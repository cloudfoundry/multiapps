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

    protected DeploymentDescriptor descriptor;
    private DescriptorHandler handler;

    public ModuleDependenciesCollector(DeploymentDescriptor descriptor, DescriptorHandler handler) {
        this.descriptor = descriptor;
        this.handler = handler;
    }
    public Set<String> collect(Module module) {
        Set<String> seenModules = new HashSet<>();
        return getDependenciesRecursively(module, seenModules);
    }

    protected List<String> getDependencies(Module module) {
        return module.getRequiredDependencies()
                     .stream()
                     .map(RequiredDependency::getName)
                     .collect(Collectors.toList());
    }


    private Set<String> getDependenciesRecursively(Module module, Set<String> seenModules) {
        if (seenModules.contains(module.getName())) {
            return Collections.emptySet();
        }
        seenModules.add(module.getName());
        return collectDependenciesRecursively(module, seenModules);
    }

    private Set<String> collectDependenciesRecursively(Module module, Set<String> seenModules) {
        Set<String> dependencies = new LinkedHashSet<>();
        for (String dependency : getDependencies(module)) {
            Module moduleProvidingDependency = findModuleSatisfyingDependency(dependency);
            if (notRequiresSelf(module, moduleProvidingDependency)) {
                dependencies.add(moduleProvidingDependency.getName());
                dependencies.addAll(getDependenciesRecursively(moduleProvidingDependency, seenModules));
            }
        }
        return dependencies;
    }

    protected Module findModuleSatisfyingDependency(String dependency) {
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
