package org.cloudfoundry.multiapps.mta.resolvers.v2;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.resolvers.Resolver;
import org.cloudfoundry.multiapps.mta.resolvers.ResolverBuilder;

public class DescriptorReferenceResolver implements Resolver<DeploymentDescriptor> {

    protected final DeploymentDescriptor descriptor;
    protected final ResolverBuilder modulesPropertiesResolverBuilder;
    protected final ResolverBuilder requiredDependenciesPropertiesResolverBuilder;
    protected final Set<String> dynamicResolvableParameters;

    public DescriptorReferenceResolver(DeploymentDescriptor descriptor, ResolverBuilder modulesPropertiesResolverBuilder,
                                       ResolverBuilder requiredDependenciesPropertiesResolverBuilder,
                                       Set<String> dynamicResolvableParameters) {
        this.descriptor = descriptor;
        this.modulesPropertiesResolverBuilder = modulesPropertiesResolverBuilder;
        this.requiredDependenciesPropertiesResolverBuilder = requiredDependenciesPropertiesResolverBuilder;
        this.dynamicResolvableParameters = dynamicResolvableParameters;
    }

    @Override
    public DeploymentDescriptor resolve() throws ContentException {
        descriptor.setModules(getResolvedModules());
        return descriptor;
    }

    protected List<Module> getResolvedModules() {
        return descriptor.getModules()
                         .stream()
                         .map(module -> createModuleResolver(module).resolve())
                         .collect(Collectors.toList());
    }

    protected ModuleReferenceResolver createModuleResolver(Module module) {
        return new ModuleReferenceResolver(descriptor,
                                           module,
                                           "",
                                           modulesPropertiesResolverBuilder,
                                           requiredDependenciesPropertiesResolverBuilder,
                                           dynamicResolvableParameters);
    }

}
