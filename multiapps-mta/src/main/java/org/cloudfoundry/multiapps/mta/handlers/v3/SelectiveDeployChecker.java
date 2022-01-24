package org.cloudfoundry.multiapps.mta.handlers.v3;

import org.apache.commons.collections4.CollectionUtils;
import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.Messages;
import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorHandler;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.Resource;

import java.util.List;
import java.util.stream.Collectors;

public class SelectiveDeployChecker extends org.cloudfoundry.multiapps.mta.handlers.v2.SelectiveDeployChecker {

    private final DeploymentDescriptor deploymentDescriptor;
    private final DescriptorHandler descriptorHandler;

    public SelectiveDeployChecker(DeploymentDescriptor deploymentDescriptor, DescriptorHandler descriptorHandler) {
        this.descriptorHandler = descriptorHandler;
        this.deploymentDescriptor = deploymentDescriptor;
    }

    @Override
    public void check(List<Resource> resourcesForDeployment) {
        List<String> allResourceNames = getAllResourceNames(resourcesForDeployment);
        for (Resource resource : resourcesForDeployment) {
            if (hasDependentResources(resource)) {
                checkDependentResourceNames(resource, allResourceNames);
            }
        }
    }

    private List<String> getAllResourceNames(List<Resource> resourcesForDeployment) {
        return resourcesForDeployment.stream()
                                     .map(Resource::getName)
                                     .collect(Collectors.toList());
    }

    private boolean hasDependentResources(Resource resource) {
        return !CollectionUtils.isEmpty(resource.getProcessedAfter());
    }

    private void checkDependentResourceNames(Resource resource, List<String> allResourceNames) {
        for (String dependentResourceName : resource.getProcessedAfter()) {
            if (descriptorHandler.findResource(deploymentDescriptor, dependentResourceName)
                                 .isActive()
                && !allResourceNames.contains(dependentResourceName)) {
                throw new ContentException(Messages.INAPPROPRIATE_USE_OF_SELECTIVE_DEPLOY, resource.getName());
            }
        }
    }
}
