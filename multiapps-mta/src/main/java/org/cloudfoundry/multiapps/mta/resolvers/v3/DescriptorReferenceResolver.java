package org.cloudfoundry.multiapps.mta.resolvers.v3;

import java.util.List;
import java.util.stream.Collectors;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.model.Resource;
import org.cloudfoundry.multiapps.mta.resolvers.ResolverBuilder;

public class DescriptorReferenceResolver extends org.cloudfoundry.multiapps.mta.resolvers.v2.DescriptorReferenceResolver {

    protected final ResolverBuilder resourcePropertiesResolverBuilder;

    public DescriptorReferenceResolver(DeploymentDescriptor descriptor, ResolverBuilder modulesPropertiesResolverBuilder,
                                       ResolverBuilder resourcePropertiesResolverBuilder,
                                       ResolverBuilder requiredDependenciesPropertiesResolverBuilder) {
        super(descriptor, modulesPropertiesResolverBuilder, requiredDependenciesPropertiesResolverBuilder);
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
        return new ResourceReferenceResolver(descriptor,
                                             resource,
                                             "",
                                             resourcePropertiesResolverBuilder,
                                             requiredDependenciesPropertiesResolverBuilder);
    }

    @Override
    protected ModuleReferenceResolver createModuleResolver(Module module) {
        return new ModuleReferenceResolver(descriptor,
                                           module,
                                           "",
                                           modulesPropertiesResolverBuilder,
                                           requiredDependenciesPropertiesResolverBuilder);
    }

}
