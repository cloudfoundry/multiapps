package com.sap.cloud.lm.sl.mta.resolvers.v2;

import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.builders.v2.ParametersChainBuilder;
import com.sap.cloud.lm.sl.mta.model.SystemParameters;
import com.sap.cloud.lm.sl.mta.model.v2.Resource;
import com.sap.cloud.lm.sl.mta.resolvers.PlaceholderResolver;
import com.sap.cloud.lm.sl.mta.resolvers.PropertiesPlaceholderResolver;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;
import com.sap.cloud.lm.sl.mta.util.PropertiesUtil;

public class ResourcePlaceholderResolver extends PlaceholderResolver<Resource> {

    protected final Resource resource;
    protected final ParametersChainBuilder parametersChainBuilder;
    protected final ResolverBuilder propertiesResolverBuilder;
    protected final ResolverBuilder parametersResolverBuilder;

    public ResourcePlaceholderResolver(Resource resource, String prefix, ParametersChainBuilder parametersChainBuilder,
        SystemParameters systemParameters, ResolverBuilder propertiesResolverBuilder, ResolverBuilder parametersResolverBuilder) {
        super(resource.getName(), prefix, systemParameters);
        this.resource = resource;
        this.parametersChainBuilder = parametersChainBuilder;
        this.propertiesResolverBuilder = propertiesResolverBuilder;
        this.parametersResolverBuilder = parametersResolverBuilder;
    }

    @Override
    public Resource resolve() throws ContentException {
        String resourceName = resource.getName();
        List<Map<String, Object>> parametersChain = parametersChainBuilder.buildResourceChain(resourceName);
        addSingularParametersIfNecessary(parametersChain);
        parametersChain.add(getFullSystemParameters(systemParameters.getResourceParameters()
            .get(resourceName)));
        Map<String, Object> mergedParameters = PropertiesUtil.mergeProperties(parametersChain);
        resource.setProperties(getResolvedProperties(mergedParameters));
        resource.setParameters(getResolvedParameters(mergedParameters));
        return resource;
    }

    protected Map<String, Object> getResolvedProperties(Map<String, Object> mergedParametersChain) throws ContentException {
        return new PropertiesPlaceholderResolver(propertiesResolverBuilder).resolve(resource.getProperties(), mergedParametersChain,
            prefix);
    }

    protected Map<String, Object> getResolvedParameters(Map<String, Object> mergedParametersChain) throws ContentException {
        return new PropertiesPlaceholderResolver(parametersResolverBuilder).resolve(resource.getParameters(), mergedParametersChain,
            prefix);
    }

}
