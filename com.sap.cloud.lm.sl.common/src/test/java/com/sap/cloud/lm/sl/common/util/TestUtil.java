package com.sap.cloud.lm.sl.common.util;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;

public class TestUtil {

    public static final String RESOURCE_PREFIX = "R";
    public static final String DO_NOT_RUN_TEST = "S";
    public static final String EXCEPTION_PREFIX = "E";

    public static final String NO_CONTENT = "";

    public static final char PREFIX_SEPARATOR = ':';

    public static void test(Runnable runnable, String expected) {
        try {
            if (!expected.equals(DO_NOT_RUN_TEST)) {
                runnable.run();
                validateResult(expected, null, NO_CONTENT);
            }
        } catch (Exception e) {
            // e.printStackTrace();
            validateException(expected, e);
        }
    }

    private static <T> void test(Callable<T> callable, String expected, Class<?> resourceClass,
        JsonSerializationOptions serializationOptions, boolean shouldConvertResultToJson) {
        try {
            if (!expected.equals(DO_NOT_RUN_TEST)) {
                T result = callable.call();
                String resultAsString = getResultAsString(result, shouldConvertResultToJson, expected, serializationOptions);
                validateResult(expected, resourceClass, resultAsString);
            }
        } catch (Exception e) {
            // e.printStackTrace();
            validateException(expected, e);
        }
    }

    public static <T> void test(Callable<T> callable, String expectedResult, String expectedExceptionMessage, String expectedExceptionCause,
        Class<?> resourceClass, JsonSerializationOptions serializationOptions, boolean shouldConvertResultToJson) {
        try {
            if (expectedResult.equals(DO_NOT_RUN_TEST) || expectedExceptionMessage.equals(DO_NOT_RUN_TEST)
                || expectedExceptionCause.equals(DO_NOT_RUN_TEST)) {
                return;
            }
            T result = callable.call();
            String resultAsString = getResultAsString(result, shouldConvertResultToJson, expectedResult, serializationOptions);
            validateResult(expectedResult, resourceClass, resultAsString);
        } catch (Exception e) {
            // e.printStackTrace();
            validateException(expectedExceptionMessage, expectedExceptionCause, e);
        }
    }

    public static <T> void test(Callable<T> callable, String expected, Class<?> resourceClass, boolean shouldConvertResultToJson) {
        test(callable, expected, resourceClass, new JsonSerializationOptions(false, false), shouldConvertResultToJson);
    }

    public static <T> void test(Callable<T> callable, String expected, Class<?> resourceClass) {
        test(callable, expected, resourceClass, new JsonSerializationOptions(false, false), true);
    }

    public static <T> void test(Callable<T> callable, String expected, Class<?> resourceClass,
        JsonSerializationOptions serializationOptions) {
        test(callable, expected, resourceClass, serializationOptions, true);
    }

    public static <T> void test(Callable<T> callable, String expectedResult, String expectedExceptionMessage, String expectedExceptionCause,
        Class<?> resourceClass) {
        test(callable, expectedResult, expectedExceptionMessage, expectedExceptionCause, resourceClass,
            new JsonSerializationOptions(false, false), true);
    }

    private static void validateResult(String expected, Class<?> resourceClass, String actual) throws IOException {
        String expectedContent = null;
        if (getPrefix(expected).equals(RESOURCE_PREFIX)) {
            expectedContent = getResourceAsString(getContent(expected), resourceClass);
        } else {
            expectedContent = getContent(expected);
        }
        assertEquals(expectedContent, actual);
    }

    private static void validateException(String expected, Exception exception) {
        String stackTrace = Arrays.asList(exception.getStackTrace())
            .subList(0, 5)
            .toString();
        assertEquals(exception.getMessage() + stackTrace, getPrefix(expected), EXCEPTION_PREFIX);
        assertThat("exception's message doesn't match up", exception.getMessage(), containsString(getContent(expected)));
    }

    private static void validateException(String expectedMessage, String expectedCause, Exception exception) {
        String stackTrace = Arrays.asList(exception.getStackTrace())
            .subList(0, 5)
            .toString();
        assertEquals(exception.getMessage() + stackTrace, getPrefix(expectedMessage), EXCEPTION_PREFIX);
        assertEquals(exception.getCause()
            .toString(), getPrefix(expectedCause), EXCEPTION_PREFIX);
        assertThat("exception's message match up", exception.getMessage(), containsString(getContent(expectedMessage)));
        assertThat("exception's cause match up", exception.getCause()
            .toString(), containsString(getContent(expectedCause)));
    }

    private static String getContent(String expected) {
        return expected.substring(expected.indexOf(PREFIX_SEPARATOR) + 1);
    }

    private static boolean hasPrefix(String expected) {
        return expected.indexOf(PREFIX_SEPARATOR) != -1;
    }

    private static String getPrefix(String expected) {
        if (hasPrefix(expected)) {
            return expected.substring(0, expected.indexOf(PREFIX_SEPARATOR));
        }
        return NO_CONTENT;
    }

    private static <T> String getResultAsString(T result, boolean shouldConvertToJson, String expected,
        JsonSerializationOptions serializationOptions) {
        if (shouldConvertToJson) {
            return JsonUtil.toJson(result, getPrefix(expected).equals(RESOURCE_PREFIX), serializationOptions.getUseExposeAnnotation(),
                serializationOptions.getDisableHtmlEscaping());
        }
        return (String) result;
    }

    public static String getResourceAsString(String name, Class<?> resourceClass) throws IOException {
        String resource = IOUtils.toString(resourceClass.getResourceAsStream(name), StandardCharsets.UTF_8);
        return resource.replace("\r", "");
    }

    public static class JsonSerializationOptions {

        private boolean useExposeAnnotation;
        private boolean disableHtmlEscaping;

        public JsonSerializationOptions(boolean useExposeAnnotation, boolean disableHtmlEscaping) {
            this.useExposeAnnotation = useExposeAnnotation;
            this.disableHtmlEscaping = disableHtmlEscaping;
        }

        public boolean getUseExposeAnnotation() {
            return useExposeAnnotation;
        }

        public boolean getDisableHtmlEscaping() {
            return disableHtmlEscaping;
        }

    }

}
