package com.sap.cloud.lm.sl.mta.model.v3_0;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.Collections;

public class ProvidedDependency extends com.sap.cloud.lm.sl.mta.model.v2_0.ProvidedDependency {

    protected ProvidedDependency() {

    }

    public static class ProvidedDependencyBuilder extends com.sap.cloud.lm.sl.mta.model.v2_0.ProvidedDependency.ProvidedDependencyBuilder {

        @Override
        public ProvidedDependency build() {
            ProvidedDependency result = new ProvidedDependency();
            result.setName(name);
            result.setPublic(getOrDefault(isPublic, false));
            result.setProperties(getOrDefault(properties, Collections.<String, Object> emptyMap()));
            return result;
        }

    }

    public ProvidedDependency copyOf() {
        ProvidedDependencyBuilder result = new ProvidedDependencyBuilder();
        result.setName(getName());
        result.setPublic(isPublic());
        result.setProperties(getProperties());
        return result.build();
    }

}
