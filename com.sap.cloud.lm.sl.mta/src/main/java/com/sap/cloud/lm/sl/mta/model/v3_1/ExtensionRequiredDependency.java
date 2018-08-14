package com.sap.cloud.lm.sl.mta.model.v3_1;

import java.util.Collections;

import org.apache.commons.lang3.ObjectUtils;

public class ExtensionRequiredDependency extends com.sap.cloud.lm.sl.mta.model.v3_0.ExtensionRequiredDependency {

    protected ExtensionRequiredDependency() {

    }

    public static class ExtensionRequiredDependencyBuilder
        extends com.sap.cloud.lm.sl.mta.model.v3_0.ExtensionRequiredDependency.Builder {

        @Override
        public ExtensionRequiredDependency build() {
            ExtensionRequiredDependency result = new ExtensionRequiredDependency();
            result.setName(name);
            result.setProperties(ObjectUtils.defaultIfNull(properties, Collections.<String, Object> emptyMap()));
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            return result;
        }

    }

}
