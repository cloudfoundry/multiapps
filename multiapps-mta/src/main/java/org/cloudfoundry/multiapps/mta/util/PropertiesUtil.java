package org.cloudfoundry.multiapps.mta.util;

import static java.text.MessageFormat.format;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.common.util.MiscUtil;
import org.cloudfoundry.multiapps.mta.Messages;
import org.cloudfoundry.multiapps.mta.model.ParametersContainer;
import org.cloudfoundry.multiapps.mta.model.PropertiesContainer;

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
     * @param providers The objects containing the properties
     * @return A list of properties
     */
    public static List<Map<String, Object>> getPropertiesList(Iterable<PropertiesContainer> providers) {
        return StreamSupport.stream(providers.spliterator(), false)
                            .filter(provider -> provider != null && provider.getProperties() != null)
                            .map(PropertiesContainer::getProperties)
                            .collect(Collectors.toList());
    }

    /**
     * Creates a list of parameters from objects implementing the ParametersContainer interface. Parameters are added to the list in the
     * same order as the objects containing them are passed to the method! A NullPointerException is NOT thrown even if a container is null!
     *
     * @param providers The objects containing the parameters
     * @return A list of parameters
     */
    public static List<Map<String, Object>> getParametersList(ParametersContainer... providers) {
        return getParametersList(Arrays.asList(providers));
    }

    /**
     * Creates a list of parameters from objects implementing the ParametersContainer interface. Parameters are added to the list in the
     * same order as the objects containing them are passed to the method! A NullPointerException is NOT thrown even if a container is null!
     *
     * @param providers The objects containing the parameters
     * @return A list of parameters
     */
    public static List<Map<String, Object>> getParametersList(Iterable<ParametersContainer> providers) {
        return StreamSupport.stream(providers.spliterator(), false)
                            .filter(provider -> provider != null && provider.getParameters() != null)
                            .map(ParametersContainer::getParameters)
                            .collect(Collectors.toList());
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
        for (ListIterator<Map<String, Object>> it = propertiesList.listIterator(propertiesList.size()); it.hasPrevious();) {
            Map<String, Object> properties = it.previous();
            if (properties != null) {
                result.putAll(properties);
            }
        }
        return result;
    }

    public static Object getPropertyValue(List<Map<String, Object>> propertiesList, String key, Object defaultValue) {
        return propertiesList.stream()
                             .map(properties -> (properties != null) ? properties.get(key) : null)
                             .filter(Objects::nonNull)
                             .findFirst()
                             .orElse(defaultValue);
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

    public static <T> List<T> getPluralOrSingular(List<Map<String, Object>> propertiesList, String plural, String single) {
        List<T> result = new ArrayList<>();
        Object pluralValues = getPropertyValue(propertiesList, plural, null);
        if (pluralValues != null) {
            validateList(pluralValues, plural);
            result.addAll((List<T>) pluralValues);
            return result;
        }
        Object singleValue = getPropertyValue(propertiesList, single, null);
        if (singleValue != null) {
            result.add((T) singleValue);
        }
        return result;
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
            Map<String, Object> originalMap = MiscUtil.cast(originalObject);
            Map<String, Object> overrideMap = MiscUtil.cast(entry.getValue());
            result.put(entry.getKey(), mergeExtensionProperties(originalMap, overrideMap));
        }
        return result;
    }

    private static void validateTypes(Object override, Object original) {
        if (isMap(original) ^ isMap(override)) {
            throw new ContentException(Messages.INCOMPATIBLE_TYPES,
                                       override.getClass()
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

    protected static void validateList(Object toValidate, String plural) {
        if (!(toValidate instanceof List)) {
            throw new ContentException(Messages.DIFFERENT_TYPE_PROVIDED_INSTEAD_OF_LIST, plural);
        }
    }
}
