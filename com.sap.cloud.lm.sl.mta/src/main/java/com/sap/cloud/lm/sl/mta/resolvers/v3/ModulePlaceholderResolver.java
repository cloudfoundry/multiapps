package com.sap.cloud.lm.sl.mta.resolvers.v3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.mta.builders.v2.ParametersChainBuilder;
import com.sap.cloud.lm.sl.mta.model.v3.Module;
import com.sap.cloud.lm.sl.mta.model.v3.ProvidedDependency;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;

public class ModulePlaceholderResolver extends com.sap.cloud.lm.sl.mta.resolvers.v2.ModulePlaceholderResolver {

    protected final Module module3;

    public ModulePlaceholderResolver(Module module, String prefix, ParametersChainBuilder parametersChainBuilder,
        ResolverBuilder propertiesResolverBuilder, ResolverBuilder parametersResolverBuilder, Map<String, String> singularToPluralMapping) {
        super(module, prefix, parametersChainBuilder, propertiesResolverBuilder, parametersResolverBuilder, singularToPluralMapping);
        this.module3 = module;
    }

    @Override
    public Module resolve() {
        Map<String, Object> mergedParameters = getMergedParameters();
        module3.setProperties(getResolvedProperties(mergedParameters));
        module3.setParameters(getResolvedParameters(mergedParameters));
        module3.setRequiredDependencies2(getResolvedRequiredDependencies());
        module3.setProvidedDependencies3(getResolvedProvidedDependencies3());
        return module3;
    }

    protected List<ProvidedDependency> getResolvedProvidedDependencies3() {
        List<ProvidedDependency> resolved = new ArrayList<>();
        for (ProvidedDependency providedDependency : module3.getProvidedDependencies3()) {
            resolved.add(getProvidedDependencyResolver(providedDependency).resolve());
        }
        return resolved;
    }

    protected ProvidedDependencyPlaceholderResolver getProvidedDependencyResolver(ProvidedDependency providedDependency) {
        return new ProvidedDependencyPlaceholderResolver(module3, providedDependency, prefix, parametersChainBuilder,
            propertiesResolverBuilder, singularToPluralMapping);
    }
}
