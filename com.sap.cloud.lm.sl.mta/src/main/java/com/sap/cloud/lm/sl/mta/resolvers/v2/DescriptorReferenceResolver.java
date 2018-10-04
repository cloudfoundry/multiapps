package com.sap.cloud.lm.sl.mta.resolvers.v2;

import java.util.ArrayList;
import java.util.List;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2.Module;
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
        descriptor.setModules2(getResolvedModules());
        return descriptor;
    }

    protected List<Module> getResolvedModules() throws ContentException {
        List<Module> resolvedModules = new ArrayList<Module>();
        for (Module module : descriptor.getModules2()) {
            resolvedModules.add(createModuleResolver(module).resolve());
        }
        return resolvedModules;
    }

    protected ModuleReferenceResolver createModuleResolver(Module module) {
        return new ModuleReferenceResolver(descriptor, module, "", modulesPropertiesResolverBuilder,
            requiredDepencenciesPropertiesResolverBuilder);
    }

}
