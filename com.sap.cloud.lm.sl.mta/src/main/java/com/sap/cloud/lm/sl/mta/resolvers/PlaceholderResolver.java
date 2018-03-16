package com.sap.cloud.lm.sl.mta.resolvers;

import static com.sap.cloud.lm.sl.mta.resolvers.ReferencePattern.PLACEHOLDER;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.mta.model.SystemParameters;

public abstract class PlaceholderResolver<T> extends PatternResolver<T> {

    protected final SystemParameters systemParameters;

    public PlaceholderResolver(String objectName, String prefix, SystemParameters systemParameters) {
        super(objectName, prefix, PLACEHOLDER);
        this.systemParameters = systemParameters;
    }

    protected void addSingularParametersIfNecessary(List<Map<String, Object>> parametersList) {
        for (String singular : systemParameters.getSingularPluralMapping()
            .keySet()) {
            addSingularParameterIfNecessary(singular, parametersList);
        }
    }

    protected void addSingularParameterIfNecessary(String singular, List<Map<String, Object>> parametersList) {
        if (shouldAddSingularParameter(singular, parametersList)) {
            Object value = getSingularParameter(systemParameters.getSingularPluralMapping()
                .get(singular), parametersList);
            if (value != null) {
                Map<String, Object> parameters = new LinkedHashMap<>(parametersList.remove(0));
                parameters.put(singular, value);
                parametersList.add(0, parameters);
            }
        }
    }

    protected boolean shouldAddSingularParameter(String singular, List<Map<String, Object>> parametersList) {
        for (Map<String, Object> parameters : parametersList) {
            if (parameters != null && parameters.containsKey(singular)) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    protected Object getSingularParameter(String plural, List<Map<String, Object>> parametersList) {
        List<Object> pluralValue = null;
        for (Map<String, Object> parameters : parametersList) {
            if (parameters != null && parameters.containsKey(plural)) {
                pluralValue = (List<Object>) parameters.get(plural);
            }
        }
        return pluralValue == null ? null : pluralValue.get(0);
    }

    protected Map<String, Object> getFullSystemParameters(Map<String, Object> parametersToAdd) {
        Map<String, Object> fullSystemParameters = new HashMap<String, Object>(systemParameters.getGeneralParameters());
        if (parametersToAdd != null && !parametersToAdd.isEmpty()) {
            fullSystemParameters.putAll(parametersToAdd);
        }
        return fullSystemParameters;
    }

}
