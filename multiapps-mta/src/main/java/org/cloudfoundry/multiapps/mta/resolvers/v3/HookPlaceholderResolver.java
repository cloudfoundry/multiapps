package org.cloudfoundry.multiapps.mta.resolvers.v3;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.builders.v2.ParametersChainBuilder;
import org.cloudfoundry.multiapps.mta.model.Hook;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.model.RequiredDependency;
import org.cloudfoundry.multiapps.mta.resolvers.PlaceholderResolver;
import org.cloudfoundry.multiapps.mta.resolvers.PropertiesPlaceholderResolver;
import org.cloudfoundry.multiapps.mta.resolvers.ResolverBuilder;
import org.cloudfoundry.multiapps.mta.resolvers.v2.RequiredDependencyPlaceholderResolver;

public class HookPlaceholderResolver extends PlaceholderResolver<Hook> {

    protected final Module module;
    protected final Hook hook;
    protected final ParametersChainBuilder parametersChainBuilder;
    protected final ResolverBuilder propertiesResolverBuilder;
    protected final ResolverBuilder parametersResolverBuilder;
    protected final Map<String, Object> mergedParameters;

    public HookPlaceholderResolver(Module module, Hook hook, String prefix, ParametersChainBuilder parametersChainBuilder,
                                   ResolverBuilder propertiesResolverBuilder, ResolverBuilder parametersResolverBuilder,
                                   Map<String, String> singularToPluralMapping, Map<String, Object> mergedParameters,
                                   Set<String> dynamicResolvableParameters) {
        super(hook.getName(), prefix, singularToPluralMapping, dynamicResolvableParameters);
        this.module = module;
        this.hook = hook;
        this.parametersChainBuilder = parametersChainBuilder;
        this.propertiesResolverBuilder = propertiesResolverBuilder;
        this.parametersResolverBuilder = parametersResolverBuilder;
        this.mergedParameters = mergedParameters;
    }

    @Override
    public Hook resolve() throws ContentException {
        hook.setParameters(getResolvedHookParameters(hook, mergedParameters));
        hook.setRequiredDependencies(getResolvedHookDependencies(hook));
        return hook;
    }

    private Map<String, Object> getResolvedHookParameters(Hook hook, Map<String, Object> mergedParameters) {
        return new PropertiesPlaceholderResolver(parametersResolverBuilder, dynamicResolvableParameters).resolve(hook.getParameters(),
                                                                                                              mergedParameters, prefix);
    }

    private List<RequiredDependency> getResolvedHookDependencies(Hook hook) {
        return hook.getRequiredDependencies()
                   .stream()
                   .map(requiredDependency -> getRequiredDependencyResolver(requiredDependency).resolve())
                   .collect(Collectors.toList());
    }

    protected RequiredDependencyPlaceholderResolver getRequiredDependencyResolver(RequiredDependency requiredDependency) {
        return new RequiredDependencyPlaceholderResolver(module,
                                                         requiredDependency,
                                                         prefix,
                                                         parametersChainBuilder,
                                                         propertiesResolverBuilder,
                                                         parametersResolverBuilder,
                                                         singularToPluralMapping,
                                                         dynamicResolvableParameters);
    }

}
