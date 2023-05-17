package org.cloudfoundry.multiapps.mta.resolvers;

import java.util.Map;
import java.util.Set;

public class NullPropertiesResolverBuilder extends ResolverBuilder {

    @Override
    public PropertiesResolver build(Map<String, Object> properties, ProvidedValuesResolver valuesResolver, ReferencePattern patternToMatch,
                                    String prefix, Boolean isStrict, Set<String> dynamicResolvableParameters) {
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
