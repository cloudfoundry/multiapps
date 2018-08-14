package com.sap.cloud.lm.sl.mta.model.v3_0;

import java.util.Collections;

import org.apache.commons.lang3.ObjectUtils;

public class Resource extends com.sap.cloud.lm.sl.mta.model.v2_0.Resource {

    protected Resource() {

    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v2_0.Resource.Builder {

        @Override
        public Resource build() {
            Resource result = new Resource();
            result.setName(name);
            result.setType(type);
            result.setDescription(description);
            result.setProperties(ObjectUtils.defaultIfNull(properties, Collections.<String, Object> emptyMap()));
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            return result;
        }

    }

    @Override
    public Resource copyOf() {
        Builder result = new Builder();
        result.setName(getName());
        result.setType(getType());
        result.setDescription(getDescription());
        result.setProperties(getProperties());
        result.setParameters(getParameters());
        return result.build();
    }
}
