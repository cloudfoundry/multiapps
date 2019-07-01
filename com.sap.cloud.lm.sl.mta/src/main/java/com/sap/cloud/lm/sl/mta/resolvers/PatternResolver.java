package com.sap.cloud.lm.sl.mta.resolvers;

import static com.sap.cloud.lm.sl.mta.util.ValidatorUtil.getPrefixedName;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ContentException;

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
