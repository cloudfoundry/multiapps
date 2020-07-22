package org.cloudfoundry.multiapps.common.util;

import org.mockito.ArgumentMatcher;

public class GenericArgumentMatcher<T> implements ArgumentMatcher<T> {

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
