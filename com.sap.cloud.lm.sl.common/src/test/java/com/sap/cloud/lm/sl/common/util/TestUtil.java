package com.sap.cloud.lm.sl.common.util;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

public class TestUtil {

    public static class Expectation {

        public enum Type {
            DEFAULT, RESOURCE, EXCEPTION, DO_NOT_RUN
        }

        private final Type type;
        private final String expectation;

        public Expectation(String expectation) {
            this(Type.DEFAULT, expectation);
        }

        public Expectation(Type type, String expectation) {
            this.type = type;
            this.expectation = expectation;
        }

        public Type getType() {
            return type;
        }

        public String getExpectation() {
            return expectation;
        }

    }

    public static InputStream getResourceAsInputStream(String name, Class<?> resourceClass) {
        return resourceClass.getResourceAsStream(name);
    }

    public static String getResourceAsString(String name, Class<?> resourceClass) {
        try {
            String resource = IOUtils.toString(getResourceAsInputStream(name, resourceClass), StandardCharsets.UTF_8);
            return resource.replace("\r", "");
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static void test(Runnable runnable, Expectation expectation) {
        if (expectation.type.equals(Expectation.Type.DO_NOT_RUN)) {
            return;
        }
        try {
            runnable.run();
        } catch (Exception e) {
            // e.printStackTrace();
            validateException(expectation, e);
        }
    }

    public static <T> void test(Callable<T> callable, Expectation expectation, Class<?> resourceClass) {
        test(callable, expectation, resourceClass, new JsonSerializationOptions(false, false));
    }

    public static <T> void test(Callable<T> callable, Expectation expectation, Class<?> resourceClass,
        JsonSerializationOptions serializationOptions) {
        if (expectation.type.equals(Expectation.Type.DO_NOT_RUN)) {
            return;
        }
        try {
            T result = callable.call();
            String resultAsString = getResultAsString(result, expectation, serializationOptions);
            validateResult(expectation, resultAsString, resourceClass);
        } catch (Exception e) {
            // e.printStackTrace();
            validateException(expectation, e);
        }
    }

    private static void validateResult(Expectation expectation, Object result, Class<?> resourceClass) {
        Object expectedResult = getExpectedResult(expectation, resourceClass);
        assertEquals(expectedResult, result);
    }

    private static Object getExpectedResult(Expectation expectation, Class<?> resourceClass) {
        if (expectation.type.equals(Expectation.Type.RESOURCE)) {
            return getResourceAsString(expectation.expectation, resourceClass);
        }
        return expectation.expectation;
    }

    private static void validateException(Expectation expectation, Exception exception) {
        assertTrue(expectation.type.equals(Expectation.Type.EXCEPTION));
        assertThat("Exception's message doesn't match up", exception.getMessage(), containsString(expectation.expectation));
    }

    private static <T> String getResultAsString(T result, Expectation expectation, JsonSerializationOptions serializationOptions) {
        if (result == null) {
            return null;
        }
        if (result instanceof String || !expectation.type.equals(Expectation.Type.RESOURCE)) {
            return result.toString();
        }
        return JsonUtil.toJson(result, true, serializationOptions.getUseExposeAnnotation(), serializationOptions.getDisableHtmlEscaping());
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
