package com.sap.cloud.lm.sl.mta.model.v3;

import java.util.Collections;

import org.apache.commons.lang3.ObjectUtils;

public class ResourceType extends com.sap.cloud.lm.sl.mta.model.v2.ResourceType {

    protected ResourceType() {

    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v2.ResourceType.Builder {

        @Override
        public ResourceType build() {
            ResourceType result = new ResourceType();
            result.setName(name);
            result.setResourceManager(resourceManager);
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            return result;
        }

    }

}
