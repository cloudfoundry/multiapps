package org.cloudfoundry.multiapps.mta.resolvers.v2;

import java.util.List;
import java.util.Map;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.builders.v2.ParametersChainBuilder;
import org.cloudfoundry.multiapps.mta.model.Resource;
import org.cloudfoundry.multiapps.mta.resolvers.PlaceholderResolver;
import org.cloudfoundry.multiapps.mta.resolvers.PropertiesPlaceholderResolver;
import org.cloudfoundry.multiapps.mta.resolvers.ResolverBuilder;
import org.cloudfoundry.multiapps.mta.util.PropertiesUtil;

public class ResourcePlaceholderResolver extends PlaceholderResolver<Resource> {

    protected final Resource resource;
    protected final ParametersChainBuilder parametersChainBuilder;
    protected final ResolverBuilder propertiesResolverBuilder;
    protected final ResolverBuilder parametersResolverBuilder;

    public ResourcePlaceholderResolver(Resource resource, String prefix, ParametersChainBuilder parametersChainBuilder,
                                       ResolverBuilder propertiesResolverBuilder, ResolverBuilder parametersResolverBuilder,
                                       Map<String, String> singularToPluralMapping) {
        super(resource.getName(), prefix, singularToPluralMapping);
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
        Map<String, Object> mergedParameters = PropertiesUtil.mergeProperties(parametersChain);
        resource.setProperties(getResolvedProperties(mergedParameters));
        resource.setParameters(getResolvedParameters(mergedParameters));
        return resource;
    }

    protected Map<String, Object> getResolvedProperties(Map<String, Object> mergedParametersChain) {
        return new PropertiesPlaceholderResolver(propertiesResolverBuilder).resolve(resource.getProperties(), mergedParametersChain,
                                                                                    prefix);
    }

    protected Map<String, Object> getResolvedParameters(Map<String, Object> mergedParametersChain) {
        return new PropertiesPlaceholderResolver(parametersResolverBuilder).resolve(resource.getParameters(), mergedParametersChain,
                                                                                    prefix);
    }

}
