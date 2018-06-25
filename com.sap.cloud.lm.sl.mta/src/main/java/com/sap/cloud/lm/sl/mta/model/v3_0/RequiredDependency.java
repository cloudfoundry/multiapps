package com.sap.cloud.lm.sl.mta.model.v3_0;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.Collections;

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
            result.setProperties(getOrDefault(properties, Collections.<String, Object> emptyMap()));
            result.setParameters(getOrDefault(parameters, Collections.<String, Object> emptyMap()));
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
