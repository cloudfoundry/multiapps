package org.cloudfoundry.multiapps.mta.resolvers;

import static org.cloudfoundry.multiapps.mta.resolvers.ReferencePattern.PLACEHOLDER;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.Messages;

public abstract class PlaceholderResolver<T> extends PatternResolver<T> {

    protected final Map<String, String> singularToPluralMapping;

    public PlaceholderResolver(String objectName, String prefix, Map<String, String> singularToPluralMapping,
                               Set<String> dynamicResolvableParameters) {
        super(objectName, prefix, PLACEHOLDER, dynamicResolvableParameters);
        this.singularToPluralMapping = singularToPluralMapping;
    }

    protected void addSingularParametersIfNecessary(List<Map<String, Object>> parametersList) {
        for (String singular : singularToPluralMapping.keySet()) {
            addSingularParameterIfNecessary(singular, parametersList);
        }
    }

    protected void addSingularParameterIfNecessary(String singular, List<Map<String, Object>> parametersList) {
        if (shouldAddSingularParameter(singular, parametersList)) {
            Object value = getSingularParameter(singularToPluralMapping.get(singular), parametersList);
            if (value != null) {
                Map<String, Object> parameters = new LinkedHashMap<>(parametersList.remove(0));
                parameters.put(singular, value);
                parametersList.add(0, parameters);
            }
        }
    }

    protected boolean shouldAddSingularParameter(String singular, List<Map<String, Object>> parametersList) {
        return parametersList.stream()
                             .noneMatch(parameters -> parameters != null && parameters.containsKey(singular));
    }

    @SuppressWarnings("unchecked")
    protected Object getSingularParameter(String plural, List<Map<String, Object>> parametersList) {
        List<Object> pluralValue = null;
        for (Map<String, Object> parameters : parametersList) {
            if (parameters != null && parameters.containsKey(plural)) {
                validateList(parameters.get(plural), plural);
                pluralValue = (List<Object>) parameters.get(plural);
            }
        }
        return pluralValue == null ? null : pluralValue.get(0);
    }

    private void validateList(Object toValidate, String plural) {
        if (!(toValidate instanceof List)) {
            throw new ContentException(Messages.DIFFERENT_TYPE_PROVIDED_INSTEAD_OF_LIST, plural);
        }
    }
}
