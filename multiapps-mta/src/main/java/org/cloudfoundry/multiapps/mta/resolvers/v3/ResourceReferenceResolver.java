package org.cloudfoundry.multiapps.mta.resolvers.v3;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.handlers.v3.DescriptorHandler;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.RequiredDependency;
import org.cloudfoundry.multiapps.mta.model.Resource;
import org.cloudfoundry.multiapps.mta.resolvers.Resolver;
import org.cloudfoundry.multiapps.mta.resolvers.ResolverBuilder;
import org.cloudfoundry.multiapps.mta.resolvers.v2.RequiredDependencyReferenceResolver;
import org.cloudfoundry.multiapps.mta.util.NameUtil;

public class ResourceReferenceResolver implements Resolver<Resource> {

    protected final DeploymentDescriptor descriptor;
    protected final Resource resource;
    protected final String prefix;
    protected final ResolverBuilder resourcesPropertiesResolverBuilder;
    protected final ResolverBuilder requiredDependenciesPropertiesResolverBuilder;
    protected final Set<String> dynamicResolvableParameters;

    public ResourceReferenceResolver(DeploymentDescriptor descriptor, Resource resource, String prefix,
                                     ResolverBuilder resourcesPropertiesResolverBuilder,
                                     ResolverBuilder requiredDependenciesPropertiesResolverBuilder, Set<String> dynamicResolvableParameters) {
        this.descriptor = descriptor;
        this.resource = resource;
        this.resourcesPropertiesResolverBuilder = resourcesPropertiesResolverBuilder;
        this.requiredDependenciesPropertiesResolverBuilder = requiredDependenciesPropertiesResolverBuilder;
        this.prefix = NameUtil.getPrefixedName(prefix, resource.getName());
        this.dynamicResolvableParameters = dynamicResolvableParameters;
    }

    @Override
    public Resource resolve() throws ContentException {
        resource.setProperties(getResolvedProperties());
        resource.setParameters(getResolvedParameters());
        resource.setRequiredDependencies(getResolvedDependencies());
        return resource;
    }

    private List<RequiredDependency> getResolvedDependencies() {
        return resource.getRequiredDependencies()
                       .stream()
                       .map(this::resolveRequiredDependency)
                       .collect(Collectors.toList());
    }

    protected RequiredDependency resolveRequiredDependency(RequiredDependency dependency) {
        RequiredDependencyReferenceResolver resolver = createRequiredDependencyResolver(dependency);
        return resolver.resolve();
    }

    protected RequiredDependencyReferenceResolver createRequiredDependencyResolver(RequiredDependency requiredDependency) {
        return new RequiredDependencyReferenceResolver(descriptor,
                                                       resource,
                                                       requiredDependency,
                                                       prefix,
                                                       new DescriptorHandler(),
                                                       requiredDependenciesPropertiesResolverBuilder,
                                                       dynamicResolvableParameters);
    }

    private Map<String, Object> getResolvedProperties() {
        return createResourcePropertiesReferenceResolver(resource.getProperties()).resolve();
    }

    private Map<String, Object> getResolvedParameters() {
        return createResourcePropertiesReferenceResolver(resource.getParameters()).resolve();
    }

    protected ResourcePropertiesReferenceResolver createResourcePropertiesReferenceResolver(Map<String, Object> properties) {
        return new ResourcePropertiesReferenceResolver(descriptor,
                                                       resource,
                                                       properties,
                                                       prefix,
                                                       resourcesPropertiesResolverBuilder,
                                                       dynamicResolvableParameters);
    }

}
