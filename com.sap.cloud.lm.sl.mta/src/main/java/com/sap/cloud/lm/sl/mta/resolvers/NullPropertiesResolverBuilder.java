package com.sap.cloud.lm.sl.mta.resolvers;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ContentException;

public class NullPropertiesResolverBuilder extends ResolverBuilder {

    @Override
    public PropertiesResolver build(Map<String, Object> properties, ProvidedValuesResolver<? extends ContentException> valuesResolver,
        ReferencePattern patternToMatch, String prefix, Boolean isStrict) {
        return new NullPropertiesResolver(properties);
    }

    private class NullPropertiesResolver extends PropertiesResolver {

        private Map<String, Object> properties = null;

        public NullPropertiesResolver(Map<String, Object> properties) {
            this.properties = properties;
        }

        @Override
        public Map<String, Object> resolve() throws ContentException {
            return this.properties;
        }

        @Override
        public Object visit(String key, String value) throws ContentException {
            return value;
        }
    }

}
