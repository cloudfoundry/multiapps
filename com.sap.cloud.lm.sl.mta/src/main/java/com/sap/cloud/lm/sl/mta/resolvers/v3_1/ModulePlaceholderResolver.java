package com.sap.cloud.lm.sl.mta.resolvers.v3_1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.builders.v2_0.ParametersChainBuilder;
import com.sap.cloud.lm.sl.mta.model.SystemParameters;
import com.sap.cloud.lm.sl.mta.model.v3_1.Module;
import com.sap.cloud.lm.sl.mta.model.v3_1.ProvidedDependency;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;

public class ModulePlaceholderResolver extends com.sap.cloud.lm.sl.mta.resolvers.v2_0.ModulePlaceholderResolver {

    protected final Module module3_1;

    public ModulePlaceholderResolver(Module module, String prefix, ParametersChainBuilder parametersChainBuilder,
        SystemParameters systemParameters, ResolverBuilder propertiesResolverBuilder, ResolverBuilder parametersResolverBuilder) {
        super(module, prefix, parametersChainBuilder, systemParameters, propertiesResolverBuilder, parametersResolverBuilder);
        this.module3_1 = module;
    }

    @Override
    public Module resolve() {
        Map<String, Object> mergedParameters = getMergedParameters();
        module3_1.setProperties(getResolvedProperties(mergedParameters));
        module3_1.setParameters(getResolvedParameters(mergedParameters));
        module3_1.setRequiredDependencies2_0(getResolvedRequiredDependencies());
        module3_1.setProvidedDependencies3_1(getResolvedProvidedDependencies3_1());
        return module3_1;
    }

    protected List<ProvidedDependency> getResolvedProvidedDependencies3_1() throws ContentException {
        List<ProvidedDependency> resolved = new ArrayList<ProvidedDependency>();
        for (ProvidedDependency providedDependency : module3_1.getProvidedDependencies3_1()) {
            resolved.add(getProvidedDependencyResolver(providedDependency).resolve());
        }
        return resolved;
    }

    protected ProvidedDependencyPlaceholderResolver getProvidedDependencyResolver(ProvidedDependency providedDependency) {
        return new ProvidedDependencyPlaceholderResolver(module3_1, providedDependency, prefix, parametersChainBuilder, systemParameters,
            propertiesResolverBuilder);
    }
}
