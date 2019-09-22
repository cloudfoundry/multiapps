package com.sap.cloud.lm.sl.mta.resolvers;

import java.util.Map;

public class NullPropertiesResolverBuilder extends ResolverBuilder {

    @Override
    public PropertiesResolver build(Map<String, Object> properties, ProvidedValuesResolver valuesResolver, ReferencePattern patternToMatch,
                                    String prefix, Boolean isStrict) {
        return new NullPropertiesResolver(properties);
    }

    private static class NullPropertiesResolver extends PropertiesResolver {

        private Map<String, Object> properties;

        NullPropertiesResolver(Map<String, Object> properties) {
            this.properties = properties;
        }

        @Override
        public Map<String, Object> resolve() {
            return this.properties;
        }

    }

}
