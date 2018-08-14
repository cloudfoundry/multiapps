package com.sap.cloud.lm.sl.mta.model.v2_0;

import java.util.Collections;

import org.apache.commons.lang3.ObjectUtils;

public class ExtensionProvidedDependency extends com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionProvidedDependency {

    protected ExtensionProvidedDependency() {

    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionProvidedDependency.Builder {

        @Override
        public ExtensionProvidedDependency build() {
            ExtensionProvidedDependency result = new ExtensionProvidedDependency();
            result.setName(name);
            result.setProperties(ObjectUtils.defaultIfNull(properties, Collections.<String, Object> emptyMap()));
            return result;
        }

    }

}
