package com.sap.cloud.lm.sl.mta.resolvers.v2_0;

import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.builders.v2_0.ParametersChainBuilder;
import com.sap.cloud.lm.sl.mta.model.SystemParameters;
import com.sap.cloud.lm.sl.mta.model.v2_0.Module;
import com.sap.cloud.lm.sl.mta.model.v2_0.ProvidedDependency;
import com.sap.cloud.lm.sl.mta.resolvers.PlaceholderResolver;
import com.sap.cloud.lm.sl.mta.resolvers.PropertiesPlaceholderResolver;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;
import com.sap.cloud.lm.sl.mta.util.PropertiesUtil;

public class ProvidedDependencyPlaceholderResolver extends PlaceholderResolver<ProvidedDependency> {

    protected final ParametersChainBuilder parametersChainBuilder;
    protected final Module module;
    protected final ProvidedDependency providedDependency;
    protected final ResolverBuilder propertiesResolverBuilder;

    public ProvidedDependencyPlaceholderResolver(Module module, ProvidedDependency providedDependency, String prefix,
        ParametersChainBuilder parametersChainBuilder, SystemParameters systemParameters, ResolverBuilder propertiesResolverBuilder) {
        super(providedDependency.getName(), prefix, systemParameters);
        this.parametersChainBuilder = parametersChainBuilder;
        this.module = module;
        this.providedDependency = providedDependency;
        this.propertiesResolverBuilder = propertiesResolverBuilder;
    }

    @Override
    public ProvidedDependency resolve() throws ContentException {
        providedDependency.setProperties(getResolvedProperties());
        return providedDependency;
    }

    protected Map<String, Object> getResolvedProperties() throws ContentException {
        List<Map<String, Object>> parametersChain = getParametersChain();
        return resolve(providedDependency.getProperties(), PropertiesUtil.mergeProperties(parametersChain));
    }

    protected List<Map<String, Object>> getParametersChain() {
        String moduleName = module.getName();
        List<Map<String, Object>> parametersChain = parametersChainBuilder.buildModuleChain(moduleName);
        addSingularParametersIfNecessary(parametersChain);
        parametersChain.add(getFullSystemParameters(systemParameters.getModuleParameters()
            .get(moduleName)));
        return parametersChain;
    }

    @Override
    protected Map<String, Object> resolve(Map<String, Object> properties, final Map<String, Object> propertyValues, Boolean isStrict)
        throws ContentException {
        return new PropertiesPlaceholderResolver(this.propertiesResolverBuilder).resolve(properties, propertyValues, prefix);
    }

}
