package org.cloudfoundry.multiapps.mta.resolvers;

import static org.cloudfoundry.multiapps.mta.resolvers.ReferencePattern.PLACEHOLDER;

import java.util.Map;
import java.util.Set;

public class PropertiesPlaceholderResolver {

    protected final ResolverBuilder propertiesResolverBuilder;
    protected final Set<String> dynamicResolvableParameters;

    public PropertiesPlaceholderResolver(ResolverBuilder propertiesResolverBuilder, Set<String> dynamicResolvableParameters) {
        this.propertiesResolverBuilder = propertiesResolverBuilder;
        this.dynamicResolvableParameters = dynamicResolvableParameters;
    }

    public Map<String, Object> resolve(Map<String, Object> properties, final Map<String, Object> replacementValues, String prefix) {
        ProvidedValuesResolver valuesResolver = irrelevant -> replacementValues;
        return propertiesResolverBuilder.build(properties, valuesResolver, PLACEHOLDER, prefix, true, dynamicResolvableParameters)
                                        .resolve();
    }

}
