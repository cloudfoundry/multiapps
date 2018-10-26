package com.sap.cloud.lm.sl.mta.util;

import static java.text.MessageFormat.format;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.common.util.CommonUtil;
import com.sap.cloud.lm.sl.mta.message.Messages;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;

public class PropertiesUtil {

    /**
     * Creates a list of properties from objects implementing the PropertiesContainer interface. Properties are added to the list in the
     * same order as the objects containing them are passed to the method! A NullPointerException is NOT thrown even if a container is null!
     * 
     * @param containers The objects containing the properties
     * @return A list of properties
     */
    public static List<Map<String, Object>> getPropertiesList(PropertiesContainer... containers) {
        return getPropertiesList(Arrays.asList(containers));
    }

    /**
     * Creates a list of properties from objects implementing the PropertiesContainer interface. Properties are added to the list in the
     * same order as the objects containing them are passed to the method! A NullPointerException is NOT thrown even if a container is null!
     * 
     * @param containers The objects containing the properties
     * @return A list of properties
     */
    public static List<Map<String, Object>> getPropertiesList(Iterable<PropertiesContainer> providers) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (PropertiesContainer provider : providers) {
            if (provider != null && provider.getProperties() != null) {
                result.add(provider.getProperties());
            }
        }
        return result;
    }

    /**
     * Creates a list of parameters from objects implementing the ParametersContainer interface. Parameters are added to the list in the
     * same order as the objects containing them are passed to the method! A NullPointerException is NOT thrown even if a container is null!
     * 
     * @param containers The objects containing the parameters
     * @return A list of parameters
     */
    public static List<Map<String, Object>> getParametersList(ParametersContainer... providers) {
        return getParametersList(Arrays.asList(providers));
    }

    /**
     * Creates a list of parameters from objects implementing the ParametersContainer interface. Parameters are added to the list in the
     * same order as the objects containing them are passed to the method! A NullPointerException is NOT thrown even if a container is null!
     * 
     * @param containers The objects containing the parameters
     * @return A list of parameters
     */
    public static List<Map<String, Object>> getParametersList(Iterable<ParametersContainer> providers) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (ParametersContainer provider : providers) {
            if (provider != null && provider.getParameters() != null) {
                result.add(provider.getParameters());
            }
        }
        return result;
    }

    /**
     * Merges a list of property maps into a single map. If a property is seen more than once in the list, the value that is nearer to the
     * start of the list is put in the resulting map.
     * 
     * @param propertiesList The list of property maps
     * @return A single properties map
     */
    public static Map<String, Object> mergeProperties(List<Map<String, Object>> propertiesList) {
        Map<String, Object> result = new TreeMap<>();
        // Add properties from the original list to the result in reverse order:
        for (int i = propertiesList.size() - 1; i >= 0; i--) {
            Map<String, Object> properties = propertiesList.get(i);
            if (properties != null) {
                result.putAll(properties);
            }
        }
        return result;
    }

    public static Object getPropertyValue(List<Map<String, Object>> propertiesList, String key, Object defaultValue) {
        for (Map<String, Object> properties : propertiesList) {
            Object value = (properties != null) ? properties.get(key) : null;
            if (value != null) {
                return value;
            }
        }
        return defaultValue;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getRequiredParameter(Map<String, Object> parameters, String parameterName) throws ContentException {
        if (!canGetParameter(parameters, parameterName)) {
            throw new ContentException(format(Messages.COULD_NOT_FIND_REQUIRED_PROPERTY, parameterName));
        }
        return (T) parameters.get(parameterName);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getOptionalParameter(Map<String, Object> parameters, String parameterName) {
        if (!canGetParameter(parameters, parameterName)) {
            return null;
        }
        return (T) parameters.get(parameterName);
    }

    private static boolean canGetParameter(Map<String, Object> parameters, String parameterName) {
        return parameters != null && parameters.containsKey(parameterName);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> getAll(List<Map<String, Object>> propertiesList, String single, String plural) {
        List<T> result = new ArrayList<>();
        T value = (T) getPropertyValue(propertiesList, single, null);
        List<T> values = (List<T>) getPropertyValue(propertiesList, plural, null);
        if (value != null) {
            result.add(value);
        }
        if (values != null) {
            result.addAll(values);
        }
        return result;
    }

    public static PropertiesContainer asPropertiesProvider(final Map<String, Object> properties) {
        return new PropertiesContainer() {
            @Override
            public Map<String, Object> getProperties() {
                return properties;
            }

            @Override
            public void setProperties(Map<String, Object> properties) {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static ParametersContainer asParametersProvider(final Map<String, Object> parameters) {
        return new ParametersContainer() {
            @Override
            public Map<String, Object> getParameters() {
                return parameters;
            }

            @Override
            public void setParameters(Map<String, Object> parameters) {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static Map<String, Object> mergeExtensionProperties(Map<String, Object> original, Map<String, Object> override) {

        Map<String, Object> result = new TreeMap<>(original);
        for (Entry<String, Object> entry : override.entrySet()) {
            Object originalObject = original.get(entry.getKey());
            if (shouldOverwrite(entry.getValue(), originalObject)) {
                result.put(entry.getKey(), entry.getValue());
                continue;
            }
            validateTypes(entry.getValue(), originalObject);
            Map<String, Object> originalMap = CommonUtil.cast(originalObject);
            Map<String, Object> overrideMap = CommonUtil.cast(entry.getValue());
            result.put(entry.getKey(), mergeExtensionProperties(originalMap, overrideMap));
        }
        return result;
    }

    private static void validateTypes(Object override, Object original) {
        if (isMap(original) ^ isMap(override)) {
            throw new ContentException(Messages.INCOMPATIBLE_TYPES, override.getClass()
                .getName(),
                original.getClass()
                    .getName());
        }
    }

    private static boolean shouldOverwrite(Object override, Object original) {
        if (override == null || original == null) {
            return true;
        }
        return !isMap(original) && !isMap(override);
    }

    private static boolean isMap(Object o) {
        return o instanceof Map;
    }
}
