package com.sap.cloud.lm.sl.mta.model.v3_0;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.Collections;

public class ExtensionRequiredDependency extends com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionRequiredDependency {

    protected ExtensionRequiredDependency() {

    }

    public static class ExtensionRequiredDependencyBuilder
        extends com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionRequiredDependency.ExtensionRequiredDependencyBuilder {

        @Override
        public ExtensionRequiredDependency build() {
            ExtensionRequiredDependency result = new ExtensionRequiredDependency();
            result.setName(name);
            result.setProperties(getOrDefault(properties, Collections.<String, Object> emptyMap()));
            result.setParameters(getOrDefault(parameters, Collections.<String, Object> emptyMap()));
            return result;
        }

    }

}
