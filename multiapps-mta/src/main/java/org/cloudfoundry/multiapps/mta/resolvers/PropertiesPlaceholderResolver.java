package org.cloudfoundry.multiapps.mta.resolvers;

import static org.cloudfoundry.multiapps.mta.resolvers.ReferencePattern.PLACEHOLDER;

import java.util.Map;

public class PropertiesPlaceholderResolver {

    protected final ResolverBuilder propertiesResolverBuilder;

    public PropertiesPlaceholderResolver(ResolverBuilder propertiesResolverBuilder) {
        this.propertiesResolverBuilder = propertiesResolverBuilder;
    }

    public Map<String, Object> resolve(Map<String, Object> properties, final Map<String, Object> replacementValues, String prefix) {
        ProvidedValuesResolver valuesResolver = irrelevant -> replacementValues;
        return propertiesResolverBuilder.build(properties, valuesResolver, PLACEHOLDER, prefix, true)
                                        .resolve();
    }

}
