package org.cloudfoundry.multiapps.mta.resolvers;

import static org.cloudfoundry.multiapps.mta.util.ValidatorUtil.getPrefixedName;

import java.util.Map;

import org.cloudfoundry.multiapps.common.ContentException;

public abstract class PatternResolver<T> implements Resolver<T> {

    protected final String prefix;
    protected final ReferencePattern patternToMatch;

    public PatternResolver(String objectName, String prefix, ReferencePattern placeholderPattern) {
        this.prefix = getPrefixedName(prefix, objectName);
        patternToMatch = placeholderPattern;
    }

    @Override
    public abstract T resolve() throws ContentException;

    protected Map<String, Object> resolve(Map<String, Object> properties, final Map<String, Object> propertyValues) {
        return resolve(properties, propertyValues, null);
    }

    protected Map<String, Object> resolve(Map<String, Object> properties, final Map<String, Object> propertyValues, Boolean isStrict) {
        ProvidedValuesResolver valuesResolver = irrelevant -> propertyValues;
        if (isStrict != null) {
            return new PropertiesResolver(properties, valuesResolver, patternToMatch, prefix, isStrict).resolve();
        }
        return new PropertiesResolver(properties, valuesResolver, patternToMatch, prefix).resolve();
    }

}
