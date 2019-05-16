package com.sap.cloud.lm.sl.common.util;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Objects;

public class Tester {

    private final Class<?> testedClass;
    private final JsonSerializationOptions jsonSerializationOptions;

    protected Tester(Class<?> testedClass) {
        this(testedClass, new JsonSerializationOptions(false, false));
    }

    protected Tester(Class<?> testedClass, JsonSerializationOptions jsonSerializationOptions) {
        this.testedClass = testedClass;
        this.jsonSerializationOptions = jsonSerializationOptions;
    }

    public void test(Runnable runnable, Expectation expectation) {
        test(toCallable(runnable), expectation);
    }

    public void test(Callable<?> callable, Expectation expectation) {
        if (expectation.shouldSkipTest()) {
            return;
        }
        try {
            Object result = callable.call();
            validateSuccess(expectation, result);
        } catch (Exception e) {
            validateFailure(expectation, e);
        }
    }

    private void validateSuccess(Expectation expectation, Object result) {
        assertTrue("Expected an exception, but the test finished successfully!", expectation.expectsSuccess());

        if (expectation.type == Expectation.Type.STRING) {
            String expected = expectation.expectation;
            String actual = Objects.toString(result, null);
            assertEquals(expected, actual);
            return;
        }
        if (expectation.type == Expectation.Type.JSON) {
            Object expected = loadResourceAsJsonObject(expectation.expectation);
            Object actual = convertToJsonObject(result);
            // If they're not equal, we want to compare them as JSON strings, because then the differences would be easier to see. That's
            // why we're not using assertEquals here.
            if (actual.equals(expected)) {
                return;
            }
        }

        String expected = loadResourceAsString(expectation.expectation);
        String actual = toJson(result);
        assertEquals(expected, actual);
    }

    private void validateFailure(Expectation expectation, Exception e) {
        if (!expectation.expectsFailure()) {
            e.printStackTrace();
            fail("Test failed: " + e.toString());
        }
        assertThat("Exception's message doesn't match up!", e.getMessage(), containsString(expectation.expectation));
    }

    private Object loadResourceAsJsonObject(String resource) {
        String json = loadResourceAsString(resource);
        return JsonUtil.fromJson(json, Object.class);
    }

    private String loadResourceAsString(String resource) {
        return TestUtil.getResourceAsString(resource, testedClass);
    }

    private Object convertToJsonObject(Object object) {
        String json = JsonUtil.toJson(object);
        return JsonUtil.fromJson(json, Object.class);
    }

    private <T> String toJson(T result) {
        return JsonUtil.toJson(result, true, jsonSerializationOptions.getUseExposeAnnotation(),
            jsonSerializationOptions.getDisableHtmlEscaping());
    }

    private static Callable<Void> toCallable(Runnable runnable) {
        return () -> {
            runnable.run();
            return null;
        };
    }

    public static Tester forClass(Class<?> testedClass) {
        return new Tester(testedClass);
    }

    public static Tester forClass(Class<?> testedClass, JsonSerializationOptions jsonSerializationOptions) {
        return new Tester(testedClass, jsonSerializationOptions);
    }

    public static class Expectation {

        public enum Type {
            STRING, JSON, EXCEPTION, SKIP;
        }

        private final Type type;
        private final String expectation;

        public Expectation(String expectation) {
            this(Type.STRING, expectation);
        }

        public Expectation(Type type, String expectation) {
            this.type = type;
            this.expectation = expectation;
        }

        private boolean expectsSuccess() {
            return type == Type.STRING || type == Type.JSON;
        }

        private boolean expectsFailure() {
            return type == Type.EXCEPTION;
        }

        private boolean shouldSkipTest() {
            return type == Type.SKIP;
        }

        public String toString() {
            return "Expecting test to " + (expectsSuccess() ? "SUCCEED and result in " : "FAIL with ") + expectation;
        }

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
