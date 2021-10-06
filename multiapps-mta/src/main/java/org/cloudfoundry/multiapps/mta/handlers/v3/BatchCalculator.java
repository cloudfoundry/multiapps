package org.cloudfoundry.multiapps.mta.handlers.v3;

import org.cloudfoundry.multiapps.mta.Messages;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.Resource;
import org.cloudfoundry.multiapps.mta.model.WeightedResource;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class BatchCalculator {

    private final DeploymentDescriptor deploymentDescriptor;

    public BatchCalculator(DeploymentDescriptor deploymentDescriptor) {
        this.deploymentDescriptor = deploymentDescriptor;
    }

    public Map<Integer, List<Resource>> groupByBatches(List<Resource> resources) {
        Set<WeightedResource> result = new TreeSet<>(new ResourceComparator());
        for (Resource resource : resources) {
            int weight = findHeaviestDependencyWeight(resource, 0, deploymentDescriptor);
            result.add(new WeightedResource(resource, weight));
        }
        return convertToMap(result);
    }

    private TreeMap<Integer, List<Resource>> convertToMap(Set<WeightedResource> resourceSet) {
        TreeMap<Integer, List<Resource>> result = new TreeMap<>();
        for (WeightedResource weightedResource : resourceSet) {
            addEntryToMap(weightedResource, result);
        }
        return result;
    }

    private void addEntryToMap(WeightedResource weightedResource, TreeMap<Integer, List<Resource>> result) {
        int currentWeight = weightedResource.getResourceWeight();
        result.putIfAbsent(currentWeight, new ArrayList<>());
        result.get(currentWeight)
              .add(weightedResource.getResource());
    }

    private int findHeaviestDependencyWeight(Resource resource, int maxWeight, DeploymentDescriptor descriptor) {
        if (!haveDependentResources(resource)) {
            return 0;
        }
        for (String resourceName : resource.getProcessedAfter()) {
            Resource dependencyResource = findResourceByName(resourceName, descriptor);
            if (haveDependentResources(dependencyResource) && dependencyResource.getProcessedAfter()
                                                                                .contains(resource.getName())) {
                throw new IllegalStateException(MessageFormat.format(Messages.CIRCULAR_RESOURCE_DEPENDENCIES_DETECTED, resource.getName(),
                                                                     dependencyResource.getName()));
            }
            int dependencyWeight = findHeaviestDependencyWeight(dependencyResource, maxWeight, descriptor);
            if (dependencyWeight > maxWeight) {
                maxWeight = dependencyWeight;
            }
        }
        return maxWeight + 1;
    }

    private Resource findResourceByName(String resourceName, DeploymentDescriptor descriptor) {
        for (Resource resource : descriptor.getResources()) {
            if (resource.getName()
                        .equals(resourceName)) {
                return resource;
            }
        }
        return null;
    }

    private boolean haveDependentResources(Resource resource) {
        return resource.getProcessedAfter() != null && !resource.getProcessedAfter()
                                                                .isEmpty();
    }
}
