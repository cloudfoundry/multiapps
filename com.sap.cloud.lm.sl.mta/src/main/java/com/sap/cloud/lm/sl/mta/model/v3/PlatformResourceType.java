package com.sap.cloud.lm.sl.mta.model.v3;

import java.util.Collections;

import org.apache.commons.lang3.ObjectUtils;

public class PlatformResourceType extends com.sap.cloud.lm.sl.mta.model.v2.PlatformResourceType {

    protected PlatformResourceType() {

    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v2.PlatformResourceType.Builder {

        @Override
        public PlatformResourceType build() {
            PlatformResourceType result = new PlatformResourceType();
            result.setName(name);
            result.setResourceManager(resourceManager);
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            return result;
        }

    }

}
