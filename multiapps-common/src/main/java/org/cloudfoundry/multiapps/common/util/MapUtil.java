package org.cloudfoundry.multiapps.common.util;

import java.util.Map;
import java.util.TreeMap;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.common.Messages;

public class MapUtil {

    private MapUtil() {
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> cast(Map<?, ?> map) {
        return map == null ? null : new TreeMap<>((Map<? extends K, ? extends V>) map);
    }

    public static <K, V> void addNonNull(Map<K, V> env, K key, V value) {
        if (value != null) {
            env.put(key, value);
        }
    }

    public static <K, V> Map<K, V> mergeSafely(Map<K, V> original, Map<K, V> override) {
        Map<K, V> result = new TreeMap<>();
        if (original != null) {
            result.putAll(original);
        }
        if (override != null) {
            result.putAll(override);
        }
        return result;
    }

    public static <K, V> Map<K, V> merge(Map<K, V> original, Map<K, V> override) {
        Map<K, V> result = new TreeMap<>();
        result.putAll(original);
        result.putAll(override);
        return result;
    }

    public static Boolean parseBooleanFlag(final Map<String, Object> parameters, String flagName, Boolean defaultValue)
        throws ContentException {
        Object flagValue = parameters.get(flagName);

        if (flagValue == null) {
            return defaultValue;
        }

        if (!(flagValue instanceof Boolean)) {
            throw new ContentException(Messages.COULD_NOT_PARSE_BOOLEAN_FLAG, flagName);
        }

        return ((Boolean) flagValue).booleanValue();
    }
}
