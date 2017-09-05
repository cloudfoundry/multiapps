package com.sap.cloud.lm.sl.mta.model.v3_0;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.Collections;

public class ResourceType extends com.sap.cloud.lm.sl.mta.model.v2_0.ResourceType {

    protected ResourceType() {

    }

    public static class ResourceTypeBuilder extends com.sap.cloud.lm.sl.mta.model.v2_0.ResourceType.ResourceTypeBuilder {

        @Override
        public ResourceType build() {
            ResourceType result = new ResourceType();
            result.setName(name);
            result.setResourceManager(resourceManager);
            result.setParameters(getOrDefault(parameters, Collections.<String, Object> emptyMap()));
            return result;
        }

    }

}
