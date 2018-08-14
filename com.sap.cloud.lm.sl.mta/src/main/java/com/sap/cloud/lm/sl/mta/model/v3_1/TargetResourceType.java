package com.sap.cloud.lm.sl.mta.model.v3_1;

import java.util.Collections;

import org.apache.commons.lang3.ObjectUtils;

public class TargetResourceType extends com.sap.cloud.lm.sl.mta.model.v3_0.TargetResourceType {

    protected TargetResourceType() {

    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v3_0.TargetResourceType.Builder {

        @Override
        public TargetResourceType build() {
            TargetResourceType result = new TargetResourceType();
            result.setName(name);
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            return result;
        }

    }

}
