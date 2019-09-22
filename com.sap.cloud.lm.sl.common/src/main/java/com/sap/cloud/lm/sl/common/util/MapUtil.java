package com.sap.cloud.lm.sl.common.util;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.tuple.Pair;

public class MapUtil {

    private MapUtil() {
    }

    public static <K, V> Map<K, V> upcast(Map<? extends K, ? extends V> map) {
        return map == null ? null : new TreeMap<>(map);
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

    public static <K, V> Map<K, V> asMap(K key, V value) {
        Map<K, V> result = new TreeMap<>();
        result.put(key, value);
        return result;
    }

    // Use this method until adoption of Java 9
    @SafeVarargs
    public static <K, V> Map<K, V> of(Pair<K, V>... keysValues) {
        Map<K, V> result = new TreeMap<>();
        for (Pair<K, V> keyValue : keysValues) {
            result.put(keyValue.getKey(), keyValue.getValue());
        }
        return result;
    }
}
