package com.sap.cloud.lm.sl.mta.resolvers;

import static com.sap.cloud.lm.sl.mta.resolvers.ReferencePattern.PLACEHOLDER;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ContentException;

public class PropertiesPlaceholderResolver {

    protected final ResolverBuilder propertiesResolverBuilder;

    public PropertiesPlaceholderResolver(ResolverBuilder propertiesResolverBuilder) {
        this.propertiesResolverBuilder = propertiesResolverBuilder;
    }

    public Map<String, Object> resolve(Map<String, Object> properties, final Map<String, Object> replacementValues, String prefix)
        throws ContentException {
        ProvidedValuesResolver<ContentException> valuesresolver = new ProvidedValuesResolver<ContentException>() {
            @Override
            public Map<String, Object> resolveProvidedValues(String irrelevant) throws ContentException {
                return replacementValues;
            }
        };
        return propertiesResolverBuilder.build(properties, valuesresolver, PLACEHOLDER, prefix, true)
            .resolve();
    }

}
