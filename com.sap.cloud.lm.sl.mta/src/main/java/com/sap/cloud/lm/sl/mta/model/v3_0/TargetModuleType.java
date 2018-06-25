package com.sap.cloud.lm.sl.mta.model.v3_0;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.Collections;

public class TargetModuleType extends com.sap.cloud.lm.sl.mta.model.v2_0.TargetModuleType {

    protected TargetModuleType() {

    }

    public static class TargetModuleTypeBuilder extends com.sap.cloud.lm.sl.mta.model.v2_0.TargetModuleType.TargetModuleTypeBuilder {

        @Override
        public TargetModuleType build() {
            TargetModuleType result = new TargetModuleType();
            result.setName(name);
            result.setProperties(getOrDefault(properties, Collections.<String, Object> emptyMap()));
            result.setParameters(getOrDefault(parameters, Collections.<String, Object> emptyMap()));
            return result;
        }

    }

}
