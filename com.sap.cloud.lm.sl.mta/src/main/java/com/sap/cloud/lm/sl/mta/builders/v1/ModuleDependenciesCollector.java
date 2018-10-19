package com.sap.cloud.lm.sl.mta.builders.v1;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.sap.cloud.lm.sl.mta.handlers.v1.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.v1.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1.Module;

public class ModuleDependenciesCollector {

    private DescriptorHandler handler;
    protected DeploymentDescriptor descriptor;
    protected Set<String> seenModules = new HashSet<>();

    public ModuleDependenciesCollector(DeploymentDescriptor descriptor, DescriptorHandler handler) {
        this.descriptor = descriptor;
        this.handler = handler;
    }

    public Set<String> collect(Module module) {
        clearVisitedModules();
        return getDependenciesRecursively(module);
    }

    private void clearVisitedModules() {
        seenModules.clear();
    }

    private Set<String> getDependenciesRecursively(Module module) {
        if (visited(module)) {
            return Collections.emptySet();
        }
        markVisited(module);
        return collectDependenciesRecursively(module);
    }

    private boolean visited(Module module) {
        return seenModules.contains(module.getName());
    }

    private void markVisited(Module module) {
        seenModules.add(module.getName());
    }

    private Set<String> collectDependenciesRecursively(Module module) {
        Set<String> dependencies = new LinkedHashSet<>();
        for (String dependency : getDependencies(module)) {
            Module moduleProvidingDependency = findModuleSatisfyingDependency(dependency);
            if (notRequiresSelf(module, moduleProvidingDependency)) {
                dependencies.add(moduleProvidingDependency.getName());
                dependencies.addAll(getDependenciesRecursively(moduleProvidingDependency));
            }
        }
        return dependencies;
    }

    protected List<String> getDependencies(Module module) {
        return module.getRequiredDependencies1();
    }

    protected Module findModuleSatisfyingDependency(String dependency) {
        return descriptor.getModules1()
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
