package com.sap.cloud.lm.sl.mta.model.v3_1;

import java.util.Collections;

import org.apache.commons.lang3.ObjectUtils;

public class PlatformModuleType extends com.sap.cloud.lm.sl.mta.model.v3_0.PlatformModuleType {

    protected PlatformModuleType() {

    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v3_0.PlatformModuleType.Builder {

        @Override
        public PlatformModuleType build() {
            PlatformModuleType result = new PlatformModuleType();
            result.setName(name);
            result.setDeployer(deployer);
            result.setProperties(ObjectUtils.defaultIfNull(properties, Collections.<String, Object> emptyMap()));
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            return result;
        }

    }

}
