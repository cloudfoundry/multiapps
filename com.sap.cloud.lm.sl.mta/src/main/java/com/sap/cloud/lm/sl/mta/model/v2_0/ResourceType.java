package com.sap.cloud.lm.sl.mta.model.v2_0;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;

public class ResourceType extends com.sap.cloud.lm.sl.mta.model.v1_0.ResourceType implements ParametersContainer {

    private Map<String, Object> parameters;

    protected ResourceType() {

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

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = new LinkedHashMap<>(parameters);
    }

    public static class ResourceTypeBuilder extends com.sap.cloud.lm.sl.mta.model.v1_0.ResourceType.ResourceTypeBuilder {

        protected Map<String, Object> parameters;

        @Override
        public ResourceType build() {
            ResourceType result = new ResourceType();
            result.setName(name);
            result.setResourceManager(resourceManager);
            result.setParameters(getOrDefault(parameters, Collections.<String, Object> emptyMap()));
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
