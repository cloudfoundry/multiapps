package com.sap.cloud.lm.sl.common.util;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

public class TestUtil {

    public static class Expectation {

        public enum Type {
            DIRECT, RESOURCE, EXCEPTION, SKIP,
        }

        private final Type type;
        private final String expectation;

        public Expectation(String expectation) {
            this(Type.DIRECT, expectation);
        }

        public Expectation(Type type, String expectation) {
            this.type = type;
            this.expectation = expectation;
        }

        private boolean expectsSuccess() {
            return type == Type.DIRECT || type == Type.RESOURCE;
        }

        private boolean expectsFailure() {
            return type == Type.EXCEPTION;
        }

        private boolean shouldSkipTest() {
            return type == Type.SKIP;
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
        test(toCallable(runnable), expectation, null);
    }

    public static <T> void test(Callable<T> callable, Expectation expectation, Class<?> resourceClass) {
        test(callable, expectation, resourceClass, new JsonSerializationOptions(false, false));
    }

    public static <T> void test(Callable<T> callable, Expectation expectation, Class<?> resourceClass,
        JsonSerializationOptions serializationOptions) {
        if (expectation.shouldSkipTest()) {
            return;
        }
        try {
            T result = callable.call();
            validateResult(expectation, result, resourceClass, serializationOptions);
        } catch (Exception e) {
            validateException(expectation, e);
        }
    }

    private static Callable<Void> toCallable(Runnable runnable) {
        return () -> {
            runnable.run();
            return null;
        };
    }

    private static void validateResult(Expectation expectation, Object result, Class<?> resourceClass,
        JsonSerializationOptions serializationOptions) {
        assertTrue("Expected an exception, but the test finished successfully!", expectation.expectsSuccess());
        String resultAsString = getResultAsString(result, expectation, serializationOptions);
        Object expectedResult = getExpectedResult(expectation, resourceClass);
        assertEquals(expectedResult, resultAsString);
    }

    private static <T> String getResultAsString(T result, Expectation expectation, JsonSerializationOptions serializationOptions) {
        if (result == null) {
            return null;
        }
        if (result instanceof String || expectation.type == Expectation.Type.DIRECT) {
            return result.toString();
        }
        return serializeToJson(result, serializationOptions);
    }

    private static <T> String serializeToJson(T result, JsonSerializationOptions serializationOptions) {
        return JsonUtil.toJson(result, true, serializationOptions.getUseExposeAnnotation(), serializationOptions.getDisableHtmlEscaping());
    }

    private static Object getExpectedResult(Expectation expectation, Class<?> resourceClass) {
        if (expectation.type == Expectation.Type.RESOURCE) {
            return getResourceAsString(expectation.expectation, resourceClass);
        }
        return expectation.expectation;
    }

    private static void validateException(Expectation expectation, Exception e) {
        if (!expectation.expectsFailure()) {
            e.printStackTrace();
            fail(e.toString());
        }
        assertThat("Exception's message doesn't match up!", e.getMessage(), containsString(expectation.expectation));
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
