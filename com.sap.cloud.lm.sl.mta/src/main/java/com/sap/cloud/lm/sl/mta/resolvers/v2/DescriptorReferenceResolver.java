package com.sap.cloud.lm.sl.mta.resolvers.v2;

import java.util.List;
import java.util.stream.Collectors;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.Module;
import com.sap.cloud.lm.sl.mta.resolvers.Resolver;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;

public class DescriptorReferenceResolver implements Resolver<DeploymentDescriptor> {

    protected final DeploymentDescriptor descriptor;
    protected final ResolverBuilder modulesPropertiesResolverBuilder;
    protected final ResolverBuilder requiredDepencenciesPropertiesResolverBuilder;

    public DescriptorReferenceResolver(DeploymentDescriptor descriptor, ResolverBuilder modulesPropertiesResolverBuilder,
                                       ResolverBuilder requiredDepencenciesPropertiesResolverBuilder) {
        this.descriptor = descriptor;
        this.modulesPropertiesResolverBuilder = modulesPropertiesResolverBuilder;
        this.requiredDepencenciesPropertiesResolverBuilder = requiredDepencenciesPropertiesResolverBuilder;
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
                                           requiredDepencenciesPropertiesResolverBuilder);
    }

}
