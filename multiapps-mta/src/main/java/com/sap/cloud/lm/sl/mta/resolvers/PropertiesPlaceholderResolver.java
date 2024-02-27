package com.sap.cloud.lm.sl.mta.resolvers;

import static com.sap.cloud.lm.sl.mta.resolvers.ReferencePattern.PLACEHOLDER;

import java.util.Map;

public class PropertiesPlaceholderResolver {

    protected final ResolverBuilder propertiesResolverBuilder;

    public PropertiesPlaceholderResolver(ResolverBuilder propertiesResolverBuilder) {
        this.propertiesResolverBuilder = propertiesResolverBuilder;
    }

    public Map<String, Object> resolve(Map<String, Object> properties, final Map<String, Object> replacementValues, String prefix) {
        ProvidedValuesResolver valuesResolver = new ProvidedValuesResolver() {
            @Override
            public Map<String, Object> resolveProvidedValues(String irrelevant) {
                return replacementValues;
            }
        };
        return propertiesResolverBuilder.build(properties, valuesResolver, PLACEHOLDER, prefix, true)
                                        .resolve();
    }

}
