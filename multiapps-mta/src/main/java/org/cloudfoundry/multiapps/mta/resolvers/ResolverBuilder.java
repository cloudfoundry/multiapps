package org.cloudfoundry.multiapps.mta.resolvers;

import java.util.Map;

public class ResolverBuilder {

    public PropertiesResolver build(Map<String, Object> properties, ProvidedValuesResolver valuesResolver, ReferencePattern patternToMatch,
                                    String prefix, Boolean isStrict) {
        return new PropertiesResolver(properties, valuesResolver, patternToMatch, prefix, isStrict);
    }

}
