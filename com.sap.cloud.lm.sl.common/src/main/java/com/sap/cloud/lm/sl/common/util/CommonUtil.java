package com.sap.cloud.lm.sl.common.util;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.message.Messages;

public class CommonUtil {

    public static boolean isNullOrEmpty(Object value) {
        return value == null || isEmpty(value);
    }

    public static boolean isEmpty(Object value) {
        if (value instanceof Collection<?>) {
            return ((Collection<?>) value).isEmpty();
        }
        if (value instanceof Map<?, ?>) {
            return ((Map<?, ?>) value).isEmpty();
        }
        if (value instanceof String) {
            return ((String) value).isEmpty();
        }
        return false;
    }

    public static <T> String toCommaDelimitedString(List<T> objects, String prefix) {
        return joinStrings(objects, prefix, ",");
    }

    public static <T> String joinStrings(final List<T> objects, final String prefix, final String delimiter) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (Object object : objects) {
            sb.append(prefix)
                .append(object.toString());
            if (count++ < objects.size() - 1) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    public static String repeat(String string, int times) {
        return new String(new char[times]).replaceAll("\0", string);
    }

    public static <T> T merge(T originalValue, T overrideValue, T defaultValue) {
        if (overrideValue != null) {
            return overrideValue;
        }
        if (originalValue != null) {
            return originalValue;
        }
        return defaultValue;
    }

    public static void replaceAll(StringBuilder builder, String original, String replacement) {
        int index = builder.indexOf(original);
        while (index != -1) {
            builder.replace(index, index + original.length(), replacement);
            index += replacement.length();
            index = builder.indexOf(original, index);
        }
    }

    public static char[] getCharacterRange(char start, char end) {
        if (start > end) {
            return getCharacterRange(end, start);
        }
        char[] range = new char[end - start + 1];
        for (int i = 0; i < range.length; i++) {
            range[i] = start++;
        }
        return range;
    }

    @SuppressWarnings("unchecked")
    public static <E> E cast(Object item) {
        return (E) item;
    }

    public static <E> E getOrDefault(E value, E defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static String abbreviate(String string, int limit) {
        if (string.length() <= limit) {
            return string;
        }
        final String abbreviationMarker = "...";
        int minimumLimit = abbreviationMarker.length() + 1;
        if (limit <= minimumLimit) {
            throw new IllegalArgumentException(MessageFormat.format(Messages.LIMIT_IS_TOO_SMALL, minimumLimit));
        }
        String trimmedString = string.substring(0, limit - abbreviationMarker.length());
        return trimmedString + abbreviationMarker;
    }

    public static int sumOfInts(int... integers) {
        int sum = 0;
        for (int i : integers) {
            sum += i;
        }
        return sum;
    }

}
