package com.sap.cloud.lm.sl.mta.model.v2;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;

public class Target extends com.sap.cloud.lm.sl.mta.model.v1.Target implements ParametersContainer {

    private Map<String, Object> parameters;
    private List<TargetModuleType> platformModuleTypes2;
    private List<TargetResourceType> platformResourceTypes2;

    protected Target() {

    }

    public void setResourceTypes2(List<TargetResourceType> resourceTypes) {
        setResourceTypes(resourceTypes);
    }

    @Override
    protected void setResourceTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1.TargetResourceType> resourceTypes) {
        this.platformResourceTypes2 = ListUtil.cast(resourceTypes);
    }

    public List<TargetResourceType> getResourceTypes2() {
        return ListUtil.upcastUnmodifiable(getResourceTypes());
    }

    @Override
    protected List<? extends TargetResourceType> getResourceTypes() {
        return platformResourceTypes2;
    }

    public void setModuleTypes2(List<TargetModuleType> moduleTypes) {
        setModuleTypes(moduleTypes);
    }

    @Override
    protected void setModuleTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1.TargetModuleType> moduleTypes) {
        this.platformModuleTypes2 = ListUtil.cast(moduleTypes);
    }

    public List<TargetModuleType> getModuleTypes2() {
        return ListUtil.upcastUnmodifiable(getModuleTypes());
    }

    @Override
    protected List<? extends TargetModuleType> getModuleTypes() {
        return platformModuleTypes2;
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

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v1.Target.Builder {

        protected Map<String, Object> parameters;
        protected List<TargetModuleType> platformModuleTypes2;
        protected List<TargetResourceType> platformResourceTypes2;

        @Override
        public Target build() {
            Target result = new Target();
            result.setName(name);
            result.setType(type);
            result.setDescription(description);
            result.setModuleTypes2(ObjectUtils.defaultIfNull(platformModuleTypes2, Collections.<TargetModuleType> emptyList()));
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            result.setResourceTypes2(ObjectUtils.defaultIfNull(platformResourceTypes2, Collections.<TargetResourceType> emptyList()));
            return result;
        }

        public void setModuleTypes2(List<TargetModuleType> moduleTypes) {
            setModuleTypes(moduleTypes);
        }

        @Override
        protected void setModuleTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1.TargetModuleType> moduleTypes) {
            this.platformModuleTypes2 = ListUtil.cast(moduleTypes);
        }

        public void setResourceTypes2(List<TargetResourceType> resourceTypes) {
            setResourceTypes(resourceTypes);
        }

        @Override
        protected void setResourceTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1.TargetResourceType> resourceTypes) {
            this.platformResourceTypes2 = ListUtil.cast(resourceTypes);
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
