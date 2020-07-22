package org.cloudfoundry.multiapps.mta.resolvers.v2;

import java.util.List;
import java.util.Map;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.builders.v2.ParametersChainBuilder;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.model.ProvidedDependency;
import org.cloudfoundry.multiapps.mta.resolvers.PlaceholderResolver;
import org.cloudfoundry.multiapps.mta.resolvers.PropertiesPlaceholderResolver;
import org.cloudfoundry.multiapps.mta.resolvers.ResolverBuilder;
import org.cloudfoundry.multiapps.mta.util.PropertiesUtil;

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
