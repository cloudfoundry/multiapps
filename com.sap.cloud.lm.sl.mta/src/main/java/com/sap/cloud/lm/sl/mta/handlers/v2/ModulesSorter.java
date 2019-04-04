package com.sap.cloud.lm.sl.mta.handlers.v2;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.sap.cloud.lm.sl.mta.builders.v2.ModuleDependenciesCollector;
import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.Module;

public class ModulesSorter implements com.sap.cloud.lm.sl.mta.handlers.ModulesSorter {

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
        Map<Module, Set<String>> modulesAndDeploymentDependencies = getModulesAndDeploymentDependencies();
        List<Entry<Module, Set<String>>> modulesAndDeploymentDependenciesSorted = modulesAndDeploymentDependencies.entrySet()
            .stream()
            .collect(Collectors.toList());

        Collections.sort(modulesAndDeploymentDependenciesSorted, getModuleComparator(dependencyTypeProperty, hardDependencyType));

        return modulesAndDeploymentDependenciesSorted.stream()
            .map(Entry::getKey)
            .collect(Collectors.toList());
    }
    
    protected Map<Module, Set<String>> getModulesAndDeploymentDependencies() {
        Map<Module, Set<String>> result = new LinkedHashMap<>();
        for (Module module : descriptor.getModules()) {
            result.put(module, getDependencies(module));
        }
        return result;
    }
    
    protected Set<String> getDependencies(Module module) {
        return getDependenciesCollector().collect(module);
    }
    
    protected ModuleDependenciesCollector getDependenciesCollector() {
        return new ModuleDependenciesCollector(descriptor, handler);
    }

    protected Comparator<Entry<Module, Set<String>>> getModuleComparator(String dependencyTypeProperty, String hardDependencyType) {
        return new ModuleComparator(dependencyTypeProperty, hardDependencyType);
    }

}
