package com.sap.cloud.lm.sl.mta.model.v3_0;

import java.util.Collections;

import org.apache.commons.lang3.ObjectUtils;

public class RequiredDependency extends com.sap.cloud.lm.sl.mta.model.v2_0.RequiredDependency {

    protected RequiredDependency() {

    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v2_0.RequiredDependency.Builder {

        @Override
        public RequiredDependency build() {
            RequiredDependency result = new RequiredDependency();
            result.setName(name);
            result.setGroup(group);
            result.setList(list);
            result.setProperties(ObjectUtils.defaultIfNull(properties, Collections.<String, Object> emptyMap()));
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            return result;
        }

    }

    @Override
    public RequiredDependency copyOf() {
        Builder result = new Builder();
        result.setName(getName());
        result.setGroup(getGroup());
        result.setList(getList());
        result.setProperties(getProperties());
        result.setParameters(getParameters());
        return result.build();
    }

}
