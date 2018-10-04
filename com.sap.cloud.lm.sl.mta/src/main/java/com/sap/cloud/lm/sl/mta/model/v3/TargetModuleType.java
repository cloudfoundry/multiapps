package com.sap.cloud.lm.sl.mta.model.v3;

import java.util.Collections;

import org.apache.commons.lang3.ObjectUtils;

public class TargetModuleType extends com.sap.cloud.lm.sl.mta.model.v2.TargetModuleType {

    protected TargetModuleType() {

    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v2.TargetModuleType.Builder {

        @Override
        public TargetModuleType build() {
            TargetModuleType result = new TargetModuleType();
            result.setName(name);
            result.setProperties(ObjectUtils.defaultIfNull(properties, Collections.<String, Object> emptyMap()));
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            return result;
        }

    }

}
