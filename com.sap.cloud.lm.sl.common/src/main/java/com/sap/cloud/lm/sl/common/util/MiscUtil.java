package com.sap.cloud.lm.sl.common.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class MiscUtil {

    private MiscUtil() {
    }

    public static URL getURL(String s) throws MalformedURLException {
        return URI.create(s)
                  .toURL();
    }

    /**
     * Outlines the first character of the string that does not match the given pattern, by surrounding it with square braces []. If the
     * string matches the pattern, it is not changed.
     * 
     * @return the string with the outlined character
     */
    public static String outlineProblematicCharacter(String pattern, String value) {
        int problematicCharacterIndex = findProblematicCharacter(pattern, value);
        if (problematicCharacterIndex < 0) {
            return value;
        }
        return outlineCharacter(problematicCharacterIndex, value);
    }

    private static String outlineCharacter(int i, String value) {
        return value.substring(0, i) + "[" + value.charAt(i) + "]" + value.substring(i + 1);
    }

    private static int findProblematicCharacter(String pattern, String value) {
        for (int i = 0; i < value.length(); i++) {
            if (!value.substring(0, i + 1)
                      .matches(pattern)) {
                return i;
            }
        }
        return -1;
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread()
                  .interrupt();
            throw new IllegalStateException("Interrupted!", e);
        }
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
