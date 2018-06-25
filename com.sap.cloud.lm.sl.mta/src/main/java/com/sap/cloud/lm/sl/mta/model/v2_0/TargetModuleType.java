package com.sap.cloud.lm.sl.mta.model.v2_0;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;

public class TargetModuleType extends com.sap.cloud.lm.sl.mta.model.v1_0.TargetModuleType implements ParametersContainer {

    private Map<String, Object> parameters;

    protected TargetModuleType() {

    }

    @Override
    public Map<String, Object> getParameters() {
        return MapUtil.unmodifiable(parameters);
    }

    @Override
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = new LinkedHashMap<>(parameters);
    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v1_0.TargetModuleType.Builder {

        protected Map<String, Object> parameters;

        @Override
        public TargetModuleType build() {
            TargetModuleType result = new TargetModuleType();
            result.setName(name);
            result.setProperties(getOrDefault(properties, Collections.<String, Object> emptyMap()));
            result.setParameters(getOrDefault(parameters, Collections.<String, Object> emptyMap()));
            return result;
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }

    }

}
