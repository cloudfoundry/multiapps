package com.sap.cloud.lm.sl.mta.resolvers.v2;

import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.builders.v2.ParametersChainBuilder;
import com.sap.cloud.lm.sl.mta.model.Module;
import com.sap.cloud.lm.sl.mta.model.ProvidedDependency;
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
        ParametersChainBuilder parametersChainBuilder, ResolverBuilder propertiesResolverBuilder,
        Map<String, String> singularToPluralMapping) {
        super(providedDependency.getName(), prefix, singularToPluralMapping);
        this.parametersChainBuilder = parametersChainBuilder;
        this.module = module;
        this.providedDependency = providedDependency;
        this.propertiesResolverBuilder = propertiesResolverBuilder;
    }

    @Override
    public ProvidedDependency resolve() throws ContentException {
        providedDependency.setProperties(getResolvedProperties());
        providedDependency.setParameters(getResolvedParameters());
        return providedDependency;
    }

    protected Map<String, Object> getResolvedProperties() {
        List<Map<String, Object>> parametersChain = getParametersChain();
        return resolve(providedDependency.getProperties(), PropertiesUtil.mergeProperties(parametersChain));
    }
    
    protected Map<String, Object> getResolvedParameters() {
        List<Map<String, Object>> parametersChain = getParametersChain();
        return resolve(providedDependency.getParameters(), PropertiesUtil.mergeProperties(parametersChain));
    }

    protected List<Map<String, Object>> getParametersChain() {
        String moduleName = module.getName();
        List<Map<String, Object>> parametersChain = parametersChainBuilder.buildModuleChain(moduleName);
        parametersChain.add(0, providedDependency.getParameters());
        addSingularParametersIfNecessary(parametersChain);
        return parametersChain;
    }

    @Override
    protected Map<String, Object> resolve(Map<String, Object> properties, final Map<String, Object> propertyValues, Boolean isStrict) {
        return new PropertiesPlaceholderResolver(this.propertiesResolverBuilder).resolve(properties, propertyValues, prefix);
    }

}
