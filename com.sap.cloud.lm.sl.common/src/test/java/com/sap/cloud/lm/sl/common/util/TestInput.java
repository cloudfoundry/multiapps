package com.sap.cloud.lm.sl.common.util;

import com.fasterxml.jackson.core.type.TypeReference;

public class TestInput {

    protected static <I> I loadJsonInput(String resourceLocation, TypeReference<I> type, Class<?> runnerClass) {
        return JsonUtil.fromJson(TestUtil.getResourceAsString(resourceLocation, runnerClass), type);
    }

    protected static <I> I loadJsonInput(String resourceLocation, Class<I> type, Class<?> runnerClass) {
        return JsonUtil.fromJson(TestUtil.getResourceAsString(resourceLocation, runnerClass), type);
    }

}
