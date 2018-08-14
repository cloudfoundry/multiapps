package com.sap.cloud.lm.sl.mta.model.v2_0;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;

public class TargetResourceType extends com.sap.cloud.lm.sl.mta.model.v1_0.TargetResourceType implements ParametersContainer {

    private Map<String, Object> parameters;

    protected TargetResourceType() {

    }

    @Override
    public Map<String, Object> getProperties() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> getParameters() {
        return MapUtil.unmodifiable(parameters);
    }

    @Override
    public void setProperties(Map<String, Object> properties) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = new LinkedHashMap<>(parameters);
    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v1_0.TargetResourceType.Builder {

        protected Map<String, Object> parameters;

        @Override
        public TargetResourceType build() {
            TargetResourceType result = new TargetResourceType();
            result.setName(name);
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            return result;
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }

        @Override
        public void setProperties(Map<String, Object> properties) {
            throw new UnsupportedOperationException();
        }

    }

}
