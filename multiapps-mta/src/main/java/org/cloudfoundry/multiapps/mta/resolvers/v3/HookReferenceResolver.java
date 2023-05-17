package org.cloudfoundry.multiapps.mta.resolvers.v3;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.Hook;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.model.RequiredDependency;
import org.cloudfoundry.multiapps.mta.resolvers.Resolver;
import org.cloudfoundry.multiapps.mta.resolvers.ResolverBuilder;
import org.cloudfoundry.multiapps.mta.resolvers.v2.ModulePropertiesReferenceResolver;
import org.cloudfoundry.multiapps.mta.resolvers.v2.RequiredDependencyReferenceResolver;

public class HookReferenceResolver implements Resolver<Hook> {

    protected final Hook hook;
    protected final DeploymentDescriptor descriptor;
    protected final Module module;
    protected final String prefix;
    protected final ResolverBuilder propertiesResolverBuilder;
    protected final ResolverBuilder requiredDependenciesPropertiesResolverBuilder;
    protected final Set<String> dynamicResolvableParameters;

    public HookReferenceResolver(Hook hook, DeploymentDescriptor descriptor, Module module, String prefix,
                                 ResolverBuilder propertiesResolverBuilder, ResolverBuilder requiredDependenciesPropertiesResolverBuilder,
                                 Set<String> dynamicResolvableParameters) {
        this.hook = hook;
        this.descriptor = descriptor;
        this.module = module;
        this.prefix = prefix;
        this.propertiesResolverBuilder = propertiesResolverBuilder;
        this.requiredDependenciesPropertiesResolverBuilder = requiredDependenciesPropertiesResolverBuilder;
        this.dynamicResolvableParameters = dynamicResolvableParameters;
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
        return new ModulePropertiesReferenceResolver(descriptor,
                                                     module,
                                                     properties,
                                                     prefix,
                                                     propertiesResolverBuilder,
                                                     dynamicResolvableParameters);
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
                                                       requiredDependenciesPropertiesResolverBuilder,
                                                       dynamicResolvableParameters);
    }
}
