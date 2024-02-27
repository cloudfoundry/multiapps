package com.sap.cloud.lm.sl.common.util;

import java.lang.reflect.Type;

public class TestInput {

    protected static <I> I loadJsonInput(String resourceLocation, Type resourceClass, Class<?> runnerClass) throws Exception {
        return JsonUtil.fromJson(TestUtil.getResourceAsString(resourceLocation, runnerClass), resourceClass);
    }

}
