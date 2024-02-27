package com.sap.cloud.lm.sl.mta.model.v3;

import java.util.Collections;

import org.apache.commons.lang3.ObjectUtils;

public class ModuleType extends com.sap.cloud.lm.sl.mta.model.v2.ModuleType {

    protected ModuleType() {

    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v2.ModuleType.Builder {

        @Override
        public ModuleType build() {
            ModuleType result = new ModuleType();
            result.setName(name);
            result.setDeployer(deployer);
            result.setProperties(ObjectUtils.defaultIfNull(properties, Collections.<String, Object> emptyMap()));
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            return result;
        }

    }

}
