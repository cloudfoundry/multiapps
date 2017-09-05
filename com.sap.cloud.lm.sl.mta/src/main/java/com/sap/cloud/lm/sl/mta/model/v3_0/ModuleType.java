package com.sap.cloud.lm.sl.mta.model.v3_0;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.Collections;

public class ModuleType extends com.sap.cloud.lm.sl.mta.model.v2_0.ModuleType {

    protected ModuleType() {

    }

    public static class ModuleTypeBuilder extends com.sap.cloud.lm.sl.mta.model.v2_0.ModuleType.ModuleTypeBuilder {

        @Override
        public ModuleType build() {
            ModuleType result = new ModuleType();
            result.setName(name);
            result.setDeployer(deployer);
            result.setProperties(getOrDefault(properties, Collections.<String, Object> emptyMap()));
            result.setParameters(getOrDefault(parameters, Collections.<String, Object> emptyMap()));
            return result;
        }

    }

}
