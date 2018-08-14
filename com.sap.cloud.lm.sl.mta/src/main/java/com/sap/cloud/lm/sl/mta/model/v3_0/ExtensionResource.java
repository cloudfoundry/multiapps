package com.sap.cloud.lm.sl.mta.model.v3_0;

import java.util.Collections;

import org.apache.commons.lang3.ObjectUtils;

public class ExtensionResource extends com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionResource {

    protected ExtensionResource() {

    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionResource.Builder {

        @Override
        public ExtensionResource build() {
            ExtensionResource result = new ExtensionResource();
            result.setName(name);
            result.setProperties(ObjectUtils.defaultIfNull(properties, Collections.<String, Object> emptyMap()));
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            return result;
        }

    }

}
