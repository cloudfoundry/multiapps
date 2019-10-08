package com.sap.cloud.lm.sl.mta.builders.v3;

import com.sap.cloud.lm.sl.mta.handlers.v3.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.Resource;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

public class ResourceDependenciesCollector {

    private final DeploymentDescriptor descriptor;
    private final DescriptorHandler handler;
    private final Set<String> seenResources;

    public ResourceDependenciesCollector(DeploymentDescriptor descriptor, DescriptorHandler handler) {
        this.descriptor = descriptor;
        this.handler = handler;
        seenResources = new HashSet<>();
    }

    public Set<Resource> collect(Resource resource) {
        clearVisitedResources();
        return getDependenciesRecursively(resource);
    }

    private Set<Resource> getDependenciesRecursively(Resource resource) {
        if (visited(resource)) {
            throw new IllegalStateException("Circular dependency detected for resource " + resource.getName());
        }
        markVisited(resource);
        return collectDependenciesRecursively(resource);
    }

    private void clearVisitedResources() {
        seenResources.clear();
    }

    private boolean visited(Resource resource) {
        return seenResources.contains(resource.getName());
    }

    private void markVisited(Resource resource) {
        seenResources.add(resource.getName());
    }

    private Set<Resource> collectDependenciesRecursively(Resource resource) {
        Set<Resource> dependencies = new LinkedHashSet<>();
        for (Resource dependency : getDependencies(resource)) {
            if (notRequiresSelf(resource, dependency)) {
                dependencies.add(dependency);
                dependencies.addAll(getDependenciesRecursively(dependency));
            }
        }
        return dependencies;
    }

    private List<Resource> getDependencies(Resource resource) {
        //TODO change to deployed-after for resources when introduced in mta spec
        return resource.getRequiredDependencies()
                       .stream()
                       .map(dependency -> handler.findResource(descriptor, dependency.getName()))
                       .collect(Collectors.toList());
    }

    private boolean notRequiresSelf(Resource resource, Resource dependency) {
        return dependency != null && !dependency.getName()
                                                .equals(resource.getName());
    }

}
