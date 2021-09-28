package org.cloudfoundry.multiapps.mta.handlers.v2;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.cloudfoundry.multiapps.mta.builders.v2.ModuleDependenciesCollector;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.Module;

public class ModulesSorter implements org.cloudfoundry.multiapps.mta.handlers.ModulesSorter {

    protected DeploymentDescriptor descriptor;
    protected DescriptorHandler handler;
    protected String dependencyTypeProperty;
    protected String hardDependencyType;

    public ModulesSorter(DeploymentDescriptor descriptor, DescriptorHandler handler, String dependencyTypeProperty,
                         String hardDependencyType) {
        this.descriptor = descriptor;
        this.handler = handler;
        this.dependencyTypeProperty = dependencyTypeProperty;
        this.hardDependencyType = hardDependencyType;
    }

    public List<Module> sort() {
        return getModulesAndDeploymentDependencies().entrySet()
                                                    .stream()
                                                    .sorted(getModuleComparator(dependencyTypeProperty, hardDependencyType))
                                                    .map(Entry::getKey)
                                                    .collect(Collectors.toList());
    }

    protected Map<Module, Set<String>> getModulesAndDeploymentDependencies() {
        ModuleDependenciesCollector moduleDependenciesCollector = new ModuleDependenciesCollector(handler);
        var result = new LinkedHashMap<Module, Set<String>>();
        for (var module : descriptor.getModules()) {
            result.put(module, moduleDependenciesCollector.collect(descriptor, module));
        }
        return result;
    }

    protected Comparator<Entry<Module, Set<String>>> getModuleComparator(String dependencyTypeProperty, String hardDependencyType) {
        return new ModuleComparator(dependencyTypeProperty, hardDependencyType);
    }

}
