package org.cloudfoundry.multiapps.mta.resolvers;

import java.util.Map;
import java.util.Set;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.util.NameUtil;

public abstract class PatternResolver<T> implements Resolver<T> {

    protected final String prefix;
    protected final ReferencePattern patternToMatch;
    protected final Set<String> dynamicResolvableParameters;

    public PatternResolver(String objectName, String prefix, ReferencePattern placeholderPattern, Set<String> dynamicResolvableParameters) {
        this.prefix = NameUtil.getPrefixedName(prefix, objectName);
        this.patternToMatch = placeholderPattern;
        this.dynamicResolvableParameters = dynamicResolvableParameters;
    }

    @Override
    public abstract T resolve() throws ContentException;

    protected Map<String, Object> resolve(Map<String, Object> properties, final Map<String, Object> propertyValues) {
        return resolve(properties, propertyValues, null);
    }

    protected Map<String, Object> resolve(Map<String, Object> properties, final Map<String, Object> propertyValues, Boolean isStrict) {
        ProvidedValuesResolver valuesResolver = irrelevant -> propertyValues;
        if (isStrict != null) {
            return new PropertiesResolver(properties, valuesResolver, patternToMatch, prefix, isStrict, dynamicResolvableParameters).resolve();
        }
        return new PropertiesResolver(properties, valuesResolver, patternToMatch, prefix, dynamicResolvableParameters).resolve();
    }

}
