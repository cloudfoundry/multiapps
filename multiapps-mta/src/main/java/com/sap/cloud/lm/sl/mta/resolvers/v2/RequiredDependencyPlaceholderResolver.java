package com.sap.cloud.lm.sl.mta.resolvers.v2;

import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.builders.v2.ParametersChainBuilder;
import com.sap.cloud.lm.sl.mta.model.SystemParameters;
import com.sap.cloud.lm.sl.mta.model.v2.Module;
import com.sap.cloud.lm.sl.mta.model.v2.RequiredDependency;
import com.sap.cloud.lm.sl.mta.resolvers.PlaceholderResolver;
import com.sap.cloud.lm.sl.mta.resolvers.PropertiesPlaceholderResolver;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;
import com.sap.cloud.lm.sl.mta.util.PropertiesUtil;

public class RequiredDependencyPlaceholderResolver extends PlaceholderResolver<RequiredDependency> {

    protected final ParametersChainBuilder parametersChainBuilder;
    protected final Module module;
    protected final RequiredDependency requiredDependency;
    protected final ResolverBuilder propertiesResolverBuilder;
    protected final ResolverBuilder parametersResolverBuilder;

    public RequiredDependencyPlaceholderResolver(Module module, RequiredDependency requiredDependency, String prefix,
                                                 ParametersChainBuilder parametersChainBuilder, SystemParameters systemParameters,
                                                 ResolverBuilder propertiesResolverBuilder, ResolverBuilder parametersResolverBuilder) {
        super(requiredDependency.getName(), prefix, systemParameters);
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
        parametersChain.add(getFullSystemParameters(systemParameters.getModuleParameters()
                                                                    .get(moduleName)));
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
