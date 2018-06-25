package com.sap.cloud.lm.sl.mta.model.v3_1;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.Collections;

public class TargetModuleType extends com.sap.cloud.lm.sl.mta.model.v3_0.TargetModuleType {

    protected TargetModuleType() {

    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v3_0.TargetModuleType.Builder {

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
