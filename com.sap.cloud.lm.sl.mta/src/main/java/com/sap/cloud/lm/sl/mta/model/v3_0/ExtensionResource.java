package com.sap.cloud.lm.sl.mta.model.v3_0;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.Collections;

public class ExtensionResource extends com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionResource {

    protected ExtensionResource() {

    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionResource.Builder {

        @Override
        public ExtensionResource build() {
            ExtensionResource result = new ExtensionResource();
            result.setName(name);
            result.setProperties(getOrDefault(properties, Collections.<String, Object> emptyMap()));
            result.setParameters(getOrDefault(parameters, Collections.<String, Object> emptyMap()));
            return result;
        }

    }

}
