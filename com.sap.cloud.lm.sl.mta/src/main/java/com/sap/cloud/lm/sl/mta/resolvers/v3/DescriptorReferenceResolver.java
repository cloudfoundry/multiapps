package com.sap.cloud.lm.sl.mta.resolvers.v3;

import java.util.List;
import java.util.stream.Collectors;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.Module;
import com.sap.cloud.lm.sl.mta.model.Resource;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;

public class DescriptorReferenceResolver extends com.sap.cloud.lm.sl.mta.resolvers.v2.DescriptorReferenceResolver {

    protected final ResolverBuilder resourcePropertiesResolverBuilder;

    public DescriptorReferenceResolver(DeploymentDescriptor descriptor, ResolverBuilder modulesPropertiesResolverBuilder,
        ResolverBuilder resourcePropertiesResolverBuilder, ResolverBuilder requiredDepencenciesPropertiesResolverBuilder) {
        super(descriptor, modulesPropertiesResolverBuilder, requiredDepencenciesPropertiesResolverBuilder);
        this.resourcePropertiesResolverBuilder = resourcePropertiesResolverBuilder;
    }

    @Override
    public DeploymentDescriptor resolve() throws ContentException {
        descriptor.setResources(getResolvedResources());
        return super.resolve();
    }

    private List<Resource> getResolvedResources() {
        return descriptor.getResources()
            .stream()
            .map(resource -> createResourceResolver(resource).resolve())
            .collect(Collectors.toList());
    }

    protected ResourceReferenceResolver createResourceResolver(Resource resource) {
        return new ResourceReferenceResolver(descriptor, resource, "", resourcePropertiesResolverBuilder,
            requiredDepencenciesPropertiesResolverBuilder);
    }

    @Override
    protected ModuleReferenceResolver createModuleResolver(Module module) {
        return new ModuleReferenceResolver(descriptor, module, "", modulesPropertiesResolverBuilder,
            requiredDepencenciesPropertiesResolverBuilder);
    }

}
