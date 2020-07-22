package org.cloudfoundry.multiapps.mta.resolvers.v2;

import java.util.List;
import java.util.Map;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.builders.v2.ParametersChainBuilder;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.model.RequiredDependency;
import org.cloudfoundry.multiapps.mta.resolvers.PlaceholderResolver;
import org.cloudfoundry.multiapps.mta.resolvers.PropertiesPlaceholderResolver;
import org.cloudfoundry.multiapps.mta.resolvers.ResolverBuilder;
import org.cloudfoundry.multiapps.mta.util.PropertiesUtil;

public class RequiredDependencyPlaceholderResolver extends PlaceholderResolver<RequiredDependency> {

    protected final ParametersChainBuilder parametersChainBuilder;
    protected final Module module;
    protected final RequiredDependency requiredDependency;
    protected final ResolverBuilder propertiesResolverBuilder;
    protected final ResolverBuilder parametersResolverBuilder;

    public RequiredDependencyPlaceholderResolver(Module module, RequiredDependency requiredDependency, String prefix,
                                                 ParametersChainBuilder parametersChainBuilder, ResolverBuilder propertiesResolverBuilder,
                                                 ResolverBuilder parametersResolverBuilder, Map<String, String> singularToPluralMapping) {
        super(requiredDependency.getName(), prefix, singularToPluralMapping);
        this.parametersChainBuilder = parametersChainBuilder;
        this.module = module;
        this.requiredDependency = requiredDependency;
        this.propertiesResolverBuilder = propertiesResolverBuilder;
        this.parametersResolverBuilder = parametersResolverBuilder;
    }

    @Override
    public RequiredDependency resolve() throws ContentException {
        String moduleName = module.getName();
        List<Map<String, Object>> parametersChain = parametersChainBuilder.buildModuleChain(moduleName);
        parametersChain.add(0, requiredDependency.getParameters());
        addSingularParametersIfNecessary(parametersChain);
        Map<String, Object> mergedParameters = PropertiesUtil.mergeProperties(parametersChain);
        requiredDependency.setParameters(getResolvedParameters(mergedParameters));
        requiredDependency.setProperties(getResolvedProperties(mergedParameters));
        return requiredDependency;
    }

    protected Map<String, Object> getResolvedParameters(Map<String, Object> mergedParameters) {
        return new PropertiesPlaceholderResolver(propertiesResolverBuilder).resolve(requiredDependency.getParameters(), mergedParameters,
                                                                                    prefix);
    }

    protected Map<String, Object> getResolvedProperties(Map<String, Object> mergedParameters) {
        return new PropertiesPlaceholderResolver(parametersResolverBuilder).resolve(requiredDependency.getProperties(), mergedParameters,
                                                                                    prefix);
    }

}
