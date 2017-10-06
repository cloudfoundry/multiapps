package com.sap.cloud.lm.sl.mta.model.v3_0;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.Collections;

public class PlatformModuleType extends com.sap.cloud.lm.sl.mta.model.v2_0.PlatformModuleType {

    protected PlatformModuleType() {

    }

    public static class PlatformModuleTypeBuilder extends com.sap.cloud.lm.sl.mta.model.v2_0.PlatformModuleType.PlatformModuleTypeBuilder {

        @Override
        public PlatformModuleType build() {
            PlatformModuleType result = new PlatformModuleType();
            result.setName(name);
            result.setProperties(getOrDefault(properties, Collections.<String, Object> emptyMap()));
            result.setParameters(getOrDefault(parameters, Collections.<String, Object> emptyMap()));
            return result;
        }

    }

}
