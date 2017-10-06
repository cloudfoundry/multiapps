package com.sap.cloud.lm.sl.mta.model.v3_0;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.Collections;

public class Resource extends com.sap.cloud.lm.sl.mta.model.v2_0.Resource {

    protected Resource() {

    }

    public static class ResourceBuilder extends com.sap.cloud.lm.sl.mta.model.v2_0.Resource.ResourceBuilder {

        @Override
        public Resource build() {
            Resource result = new Resource();
            result.setName(name);
            result.setType(type);
            result.setDescription(description);
            result.setProperties(getOrDefault(properties, Collections.<String, Object> emptyMap()));
            result.setParameters(getOrDefault(parameters, Collections.<String, Object> emptyMap()));
            return result;
        }

    }

    public Resource copyOf() {
        ResourceBuilder result = new ResourceBuilder();
        result.setName(getName());
        result.setType(getType());
        result.setDescription(getDescription());
        result.setProperties(getProperties());
        result.setParameters(getParameters());
        return result.build();
    }
}
