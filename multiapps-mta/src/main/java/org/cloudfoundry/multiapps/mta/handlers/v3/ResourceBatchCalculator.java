package org.cloudfoundry.multiapps.mta.handlers.v3;

import org.apache.commons.collections4.CollectionUtils;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ResourceBatchCalculator extends org.cloudfoundry.multiapps.mta.handlers.v2.ResourceBatchCalculator {

    private final DeploymentDescriptor deploymentDescriptor;
    private DescriptorHandler descriptorHandler;

    public ResourceBatchCalculator(DeploymentDescriptor deploymentDescriptor) {
        this.deploymentDescriptor = deploymentDescriptor;
    }

    @Override
    public Map<Integer, List<Resource>> groupResourcesByWeight(List<Resource> resources) {
        Map<Integer, List<Resource>> result = new TreeMap<>();
        initializeDescriptorHandler();
        for (Resource resource : resources) {
            int weight = findHeaviestDependencyWeight(resource);
            result.computeIfAbsent(weight, t -> new ArrayList<>())
                  .add(resource);
        }
        return result;
    }

    private int findHeaviestDependencyWeight(Resource resource) {
        if (CollectionUtils.isEmpty(resource.getProcessedAfter())) {
            return 0;
        }
        int heaviestWeight = 0;
        for (String resourceName : resource.getProcessedAfter()) {
            Resource dependencyResource = descriptorHandler.findResource(deploymentDescriptor, resourceName);
            int dependencyWeight = findHeaviestDependencyWeight(dependencyResource);
            if (dependencyWeight > heaviestWeight) {
                heaviestWeight = dependencyWeight;
            }
        }
        return heaviestWeight + 1;
    }

    protected void initializeDescriptorHandler() {
        this.descriptorHandler = new DescriptorHandler();
    }
}
