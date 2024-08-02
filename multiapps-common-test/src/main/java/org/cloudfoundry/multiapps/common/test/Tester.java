package org.cloudfoundry.multiapps.common.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.function.FailableCallable;
import org.apache.commons.lang3.function.FailableRunnable;
import org.cloudfoundry.multiapps.common.util.JsonUtil;
import org.junit.jupiter.api.Assertions;

public class Tester {

    private final Class<?> testedClass;

    protected Tester(Class<?> testedClass) {
        this.testedClass = testedClass;
    }

    public <E extends Exception> void test(FailableRunnable<E> runnable, Expectation expectation) {
        test(toCallable(runnable), expectation);
    }

    public <E extends Exception> void test(FailableCallable<?, E> callable, Expectation expectation) {
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

    @SuppressWarnings("unchecked")
    private void validateSuccess(Expectation expectation, Object result) {
        assertTrue(expectation.expectsSuccess(), "Expected an exception, but the test finished successfully!");

        if (expectation.type == Expectation.Type.STRING) {
            assertEquals(expectation.getExpectationAsString(), Objects.toString(result, null));
            return;
        }

        if (expectation.type == Expectation.Type.SET) {
            validateSuccessForSetExpectation((Set<Object>) expectation.expectation, (Set<Object>) result);
            return;
        }

        Object expected = loadResourceAsJsonObject(expectation.getExpectationAsString());
        Object actual = toJsonObject(result);
        // If they're not equal, we want to compare them as JSON strings, because then the differences would be easier to see. That's
        // why we're not using assertEquals here.
        if (actual.equals(expected)) {
            return;
        }
        expected = loadResourceAsString(expectation.getExpectationAsString());
        actual = toJson(result);
        assertEquals(expected, actual);
    }

    private void validateSuccessForSetExpectation(Set<Object> expectedSet, Set<Object> resultSet) {
        if (expectedSet == null || resultSet == null) {
            Assertions.assertTrue(expectedSet == null && resultSet == null,
                                  "Only " + (expectedSet == null ? "EXPECTED" : "RESULT") + " set is null.");
        } else {
            List<Object> expectedButMissing = expectedSet.stream()
                                                         .filter(element -> !resultSet.contains(element))
                                                         .collect(Collectors.toList());
            List<Object> notExpectedButPresent = resultSet.stream()
                                                          .filter(element -> !expectedSet.contains(element))
                                                          .collect(Collectors.toList());
            String failMessage = "";
            if (expectedButMissing.size() > 0) {
                failMessage += String.format("EXPECTED set %s contains elements not present in RESULT %s : %s\n", expectedSet, resultSet,
                                             expectedButMissing);
            }
            if (notExpectedButPresent.size() > 0) {
                failMessage += String.format("RESULT set %s contains elements not present in EXPECTED %s : %s\n", resultSet, expectedSet,
                                             notExpectedButPresent);
            }
            if (!failMessage.isEmpty()) {
                Assertions.fail(failMessage);
            }
        }
    }

    private void validateFailure(Expectation expectation, Exception e) {
        if (!expectation.expectsFailure()) {
            e.printStackTrace();
            fail("Test failed: " + e.toString());
        }
        String exceptionMessage = e.getMessage();
        assertTrue(exceptionMessage.contains(expectation.getExpectationAsString()),
                   MessageFormat.format("Exception's message doesn't match up! Expected [{0}] to contain [{1}]!", exceptionMessage,
                                        expectation.getExpectationAsString()));
    }

    private Object loadResourceAsJsonObject(String resource) {
        String json = loadResourceAsString(resource);
        return JsonUtil.fromJson(json, Object.class);
    }

    private String loadResourceAsString(String resource) {
        return TestUtil.getResourceAsString(resource, testedClass);
    }

    private Object toJsonObject(Object object) {
        String json = toJson(object);
        return JsonUtil.fromJson(json, Object.class);
    }

    private String toJson(Object object) {
        String json = JsonUtil.toJson(object, true);
        return TestUtil.removeCarriageReturns(json);
    }

    private static <E extends Exception> FailableCallable<Void, E> toCallable(FailableRunnable<E> runnable) {
        return () -> {
            runnable.run();
            return null;
        };
    }

    public static Tester forClass(Class<?> testedClass) {
        return new Tester(testedClass);
    }

    public static class Expectation {

        public enum Type {
            STRING, JSON, EXCEPTION, SKIP, SET
        }

        private final Type type;
        private final Object expectation;

        public Expectation(String expectation) {
            this(Type.STRING, expectation);
        }

        public Expectation(Type type, Object expectation) {
            this.type = type;
            this.expectation = expectation;
        }

        private boolean expectsSuccess() {
            return type == Type.STRING || type == Type.JSON || type == Type.SET;
        }

        private boolean expectsFailure() {
            return type == Type.EXCEPTION;
        }

        private boolean shouldSkipTest() {
            return type == Type.SKIP;
        }

        private String getExpectationAsString() {
            return (String) expectation;
        }

        public String toString() {
            return "Expecting test to " + (expectsSuccess() ? "SUCCEED and result in " : "FAIL with ") + expectation;
        }

    }

}
