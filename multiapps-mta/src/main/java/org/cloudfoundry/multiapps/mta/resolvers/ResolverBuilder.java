package org.cloudfoundry.multiapps.mta.resolvers;

import java.util.Map;
import java.util.Set;

public class ResolverBuilder {

    public PropertiesResolver build(Map<String, Object> properties, ProvidedValuesResolver valuesResolver, ReferencePattern patternToMatch,
                                    String prefix, Boolean isStrict, Set<String> dynamicResolvableParameters) {
        return new PropertiesResolver(properties, valuesResolver, patternToMatch, prefix, isStrict, dynamicResolvableParameters);
    }

}
