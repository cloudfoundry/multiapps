package com.sap.cloud.lm.sl.mta.resolvers.v3;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.builders.v2.ParametersChainBuilder;
import com.sap.cloud.lm.sl.mta.model.Hook;
import com.sap.cloud.lm.sl.mta.model.Module;
import com.sap.cloud.lm.sl.mta.model.RequiredDependency;
import com.sap.cloud.lm.sl.mta.resolvers.PlaceholderResolver;
import com.sap.cloud.lm.sl.mta.resolvers.PropertiesPlaceholderResolver;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;
import com.sap.cloud.lm.sl.mta.resolvers.v2.RequiredDependencyPlaceholderResolver;

public class HookPlaceholderResolver extends PlaceholderResolver<Hook> {

    protected final Module module;
    protected final Hook hook;
    protected final ParametersChainBuilder parametersChainBuilder;
    protected final ResolverBuilder propertiesResolverBuilder;
    protected final ResolverBuilder parametersResolverBuilder;
    protected final Map<String, Object> mergedParameters;

    public HookPlaceholderResolver(Module module, Hook hook, String prefix, ParametersChainBuilder parametersChainBuilder,
                                   ResolverBuilder propertiesResolverBuilder, ResolverBuilder parametersResolverBuilder,
                                   Map<String, String> singularToPluralMapping, Map<String, Object> mergedParameters) {
        super(hook.getName(), prefix, singularToPluralMapping);
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
        return new PropertiesPlaceholderResolver(parametersResolverBuilder).resolve(hook.getParameters(), mergedParameters, prefix);
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
                                                         singularToPluralMapping);
    }

}
