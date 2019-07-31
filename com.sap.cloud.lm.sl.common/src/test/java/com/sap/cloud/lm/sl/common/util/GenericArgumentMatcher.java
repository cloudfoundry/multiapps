package com.sap.cloud.lm.sl.common.util;

import org.mockito.ArgumentMatcher;

public class GenericArgumentMatcher<T> extends ArgumentMatcher<T> {

    private T expectedObject;

    protected GenericArgumentMatcher(T expectedObject) {
        this.expectedObject = expectedObject;
    }

    @Override
    public boolean matches(Object actualObject) {
        String actualJson = JsonUtil.toJson(actualObject);
        String expectedJson = JsonUtil.toJson(expectedObject);
        return expectedJson.equals(actualJson);
    }

    public static <T> GenericArgumentMatcher<T> forObject(T expectedObject) {
        return new GenericArgumentMatcher<>(expectedObject);
    }

}
