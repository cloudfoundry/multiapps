package com.sap.cloud.lm.sl.mta.model.v2_0;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;
import com.sap.cloud.lm.sl.mta.parsers.v2_0.ResourceParser;
import com.sap.cloud.lm.sl.mta.util.YamlElement;

public class Resource extends com.sap.cloud.lm.sl.mta.model.v1_0.Resource implements ParametersContainer {

    @YamlElement(ResourceParser.PARAMETERS)
    private Map<String, Object> parameters;

    protected Resource() {

    }

    @Override
    public List<String> getGroups() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> getParameters() {
        return MapUtil.unmodifiable(parameters);
    }

    @Override
    public void setGroups(List<String> groups) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = new LinkedHashMap<>(parameters);
    }

    @Override
    public Resource copyOf() {
        Builder result = new Builder();
        result.setName(getName());
        result.setType(getType());
        result.setDescription(getDescription());
        result.setProperties(getProperties());
        result.setParameters(getParameters());
        return result.build();
    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v1_0.Resource.Builder {

        protected Map<String, Object> parameters;

        @Override
        public Resource build() {
            Resource result = new Resource();
            result.setName(name);
            result.setType(type);
            result.setDescription(description);
            result.setProperties(getOrDefault(properties, Collections.<String, Object> emptyMap()));
            result.setParameters(getOrDefault(parameters, Collections.<String, Object> emptyMap()));
            return result;
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }

        @Override
        public void setGroups(List<String> groups) {
            throw new UnsupportedOperationException();
        }

    }

}
