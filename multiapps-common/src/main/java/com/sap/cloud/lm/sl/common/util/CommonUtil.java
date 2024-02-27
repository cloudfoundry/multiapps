package com.sap.cloud.lm.sl.common.util;

import java.util.Collection;
import java.util.Map;

public class CommonUtil {

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread()
                  .interrupt();
            throw new IllegalStateException("Interrupted!", e);
        }
    }

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

}
