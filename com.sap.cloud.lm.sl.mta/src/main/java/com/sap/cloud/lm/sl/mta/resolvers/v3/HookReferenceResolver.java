package com.sap.cloud.lm.sl.mta.resolvers.v3;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.Hook;
import com.sap.cloud.lm.sl.mta.model.Module;
import com.sap.cloud.lm.sl.mta.model.RequiredDependency;
import com.sap.cloud.lm.sl.mta.resolvers.Resolver;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;
import com.sap.cloud.lm.sl.mta.resolvers.v2.ModulePropertiesReferenceResolver;
import com.sap.cloud.lm.sl.mta.resolvers.v2.RequiredDependencyReferenceResolver;

public class HookReferenceResolver implements Resolver<Hook> {

    protected final Hook hook;
    protected final DeploymentDescriptor descriptor;
    protected final Module module;
    protected final String prefix;
    protected final ResolverBuilder propertiesResolverBuilder;
    protected final ResolverBuilder requiredDepencenciesPropertiesResolverBuilder;

    public HookReferenceResolver(Hook hook, DeploymentDescriptor descriptor, Module module, String prefix,
                                 ResolverBuilder propertiesResolverBuilder, ResolverBuilder requiredDepencenciesPropertiesResolverBuilder) {
        this.hook = hook;
        this.descriptor = descriptor;
        this.module = module;
        this.prefix = prefix;
        this.propertiesResolverBuilder = propertiesResolverBuilder;
        this.requiredDepencenciesPropertiesResolverBuilder = requiredDepencenciesPropertiesResolverBuilder;
    }

    @Override
    public Hook resolve() {
        hook.setParameters(getResolvedHookParameters(hook));
        hook.setRequiredDependencies(getResolvedHookDependencies(hook));
        return hook;
    }

    private Map<String, Object> getResolvedHookParameters(Hook hook) {
        return createModulePropertiesReferenceResolver(hook.getParameters()).resolve();
    }

    protected ModulePropertiesReferenceResolver createModulePropertiesReferenceResolver(Map<String, Object> properties) {
        return new ModulePropertiesReferenceResolver(descriptor, module, properties, prefix, propertiesResolverBuilder);
    }

    private List<RequiredDependency> getResolvedHookDependencies(Hook hook) {
        return hook.getRequiredDependencies()
                   .stream()
                   .map(requiredDependency -> getRequiredDependencyResolver(requiredDependency).resolve())
                   .collect(Collectors.toList());
    }

    protected RequiredDependencyReferenceResolver getRequiredDependencyResolver(RequiredDependency requiredDependency) {
        return new RequiredDependencyReferenceResolver(descriptor,
                                                       module,
                                                       requiredDependency,
                                                       prefix,
                                                       requiredDepencenciesPropertiesResolverBuilder);
    }
}
