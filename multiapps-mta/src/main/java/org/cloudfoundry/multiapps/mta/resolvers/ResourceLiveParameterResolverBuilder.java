package org.cloudfoundry.multiapps.mta.resolvers;

import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.Resource;
import org.cloudfoundry.multiapps.mta.resolvers.v2.ResourceLiveParameterResolver;

import java.util.Map;

public class ResourceLiveParameterResolverBuilder {

    private final DeploymentDescriptor descriptor;
    private final Map<String, String> idleToLiveParameterPairs;

    public ResourceLiveParameterResolverBuilder(DeploymentDescriptor descriptor, Map<String, String> idleToLiveParameterPairs) {
        this.descriptor = descriptor;
        this.idleToLiveParameterPairs = idleToLiveParameterPairs;
    }

    public ResourceLiveParameterResolver build(Resource resource) {
        return new ResourceLiveParameterResolver(descriptor, resource, idleToLiveParameterPairs);
    }
}
