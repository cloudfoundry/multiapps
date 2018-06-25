package com.sap.cloud.lm.sl.mta.model.v3_0;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.Collections;

public class TargetResourceType extends com.sap.cloud.lm.sl.mta.model.v2_0.TargetResourceType {

    protected TargetResourceType() {

    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v2_0.TargetResourceType.Builder {

        @Override
        public TargetResourceType build() {
            TargetResourceType result = new TargetResourceType();
            result.setName(name);
            result.setParameters(getOrDefault(parameters, Collections.<String, Object> emptyMap()));
            return result;
        }

    }

}
