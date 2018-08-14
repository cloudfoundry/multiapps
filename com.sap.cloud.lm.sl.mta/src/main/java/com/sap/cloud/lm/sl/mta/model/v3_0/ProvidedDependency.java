package com.sap.cloud.lm.sl.mta.model.v3_0;

import java.util.Collections;

import org.apache.commons.lang3.ObjectUtils;

public class ProvidedDependency extends com.sap.cloud.lm.sl.mta.model.v2_0.ProvidedDependency {

    protected ProvidedDependency() {

    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v2_0.ProvidedDependency.Builder {

        @Override
        public ProvidedDependency build() {
            ProvidedDependency result = new ProvidedDependency();
            result.setName(name);
            result.setPublic(ObjectUtils.defaultIfNull(isPublic, false));
            result.setProperties(ObjectUtils.defaultIfNull(properties, Collections.<String, Object> emptyMap()));
            return result;
        }

    }

    @Override
    public ProvidedDependency copyOf() {
        Builder result = new Builder();
        result.setName(getName());
        result.setPublic(isPublic());
        result.setProperties(getProperties());
        return result.build();
    }

}
