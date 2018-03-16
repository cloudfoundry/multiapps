package com.sap.cloud.lm.sl.mta.resolvers.v3_1;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.cast;
import static com.sap.cloud.lm.sl.mta.util.ValidatorUtil.getPrefixedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.handlers.v3_1.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.v3_1.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v3_1.RequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v3_1.Resource;
import com.sap.cloud.lm.sl.mta.resolvers.Resolver;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;
import com.sap.cloud.lm.sl.mta.resolvers.v2_0.RequiredDependencyReferenceResolver;

public class ResourceReferenceResolver implements Resolver<Resource, ContentException> {

    protected final DeploymentDescriptor descriptor;
    protected final Resource resource;
    protected final String prefix;
    protected final ResolverBuilder resourcesPropertiesResolverBuilder;
    protected final ResolverBuilder requiredDepencenciesPropertiesResolverBuilder;

    public ResourceReferenceResolver(DeploymentDescriptor descriptor, Resource resource, String prefix,
        ResolverBuilder resourcesPropertiesResolverBuilder, ResolverBuilder requiredDepencenciesPropertiesResolverBuilder) {
        this.descriptor = descriptor;
        this.resource = resource;
        this.resourcesPropertiesResolverBuilder = resourcesPropertiesResolverBuilder;
        this.requiredDepencenciesPropertiesResolverBuilder = requiredDepencenciesPropertiesResolverBuilder;
        this.prefix = getPrefixedName(prefix, resource.getName());
    }

    @Override
    public Resource resolve() throws ContentException {
        resource.setProperties(getResolvedProperties());
        resource.setParameters(getResolvedParameters());
        resource.setRequiredDependencies3_1(getResolvedDependencies());
        return resource;
    }

    private List<RequiredDependency> getResolvedDependencies() {
        List<RequiredDependency> result = new ArrayList<>();
        for (RequiredDependency requiredDependency : resource.getRequiredDependencies3_1()) {
            result.add(resolveRequiredDependency(requiredDependency));
        }
        return result;
    }

    protected RequiredDependency resolveRequiredDependency(RequiredDependency dependency) {
        RequiredDependencyReferenceResolver resolver = createRequiredDependencyResolver(dependency);
        return cast(resolver.resolve());
    }

    protected RequiredDependencyReferenceResolver createRequiredDependencyResolver(RequiredDependency requiredDependency) {
        return new RequiredDependencyReferenceResolver(descriptor, resource, requiredDependency, prefix, new DescriptorHandler(),
            requiredDepencenciesPropertiesResolverBuilder);
    }

    private Map<String, Object> getResolvedProperties() throws ContentException {
        return createResourcePropertiesReferenceResolver(resource.getProperties()).resolve();
    }

    private Map<String, Object> getResolvedParameters() throws ContentException {
        return createResourcePropertiesReferenceResolver(resource.getParameters()).resolve();
    }

    protected ResourcePropertiesReferenceResolver createResourcePropertiesReferenceResolver(Map<String, Object> properties) {
        return new ResourcePropertiesReferenceResolver(descriptor, resource, properties, prefix, resourcesPropertiesResolverBuilder);
    }

}
