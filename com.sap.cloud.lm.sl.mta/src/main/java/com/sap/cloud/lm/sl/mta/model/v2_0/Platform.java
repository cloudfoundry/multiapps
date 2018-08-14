package com.sap.cloud.lm.sl.mta.model.v2_0;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;

public class Platform extends com.sap.cloud.lm.sl.mta.model.v1_0.Platform implements ParametersContainer {

    private Map<String, Object> parameters;
    private List<PlatformModuleType> moduleTypes2_0;
    private List<PlatformResourceType> resourceTypes2_0;

    protected Platform() {

    }

    @Override
    public String getVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setVersion(String version) {
        throw new UnsupportedOperationException();
    }

    public void setResourceTypes2_0(List<PlatformResourceType> resourceTypes) {
        setResourceTypes(resourceTypes);
    }

    @Override
    protected void setResourceTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.PlatformResourceType> resourceTypes) {
        this.resourceTypes2_0 = ListUtil.cast(resourceTypes);
    }

    public List<PlatformResourceType> getResourceTypes2_0() {
        return ListUtil.upcastUnmodifiable(getResourceTypes());
    }

    @Override
    protected List<? extends PlatformResourceType> getResourceTypes() {
        return resourceTypes2_0;
    }

    public void setModuleTypes2_0(List<PlatformModuleType> moduleTypes) {
        setModuleTypes(moduleTypes);
    }

    @Override
    protected void setModuleTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.PlatformModuleType> moduleTypes) {
        this.moduleTypes2_0 = ListUtil.cast(moduleTypes);
    }

    public List<PlatformModuleType> getModuleTypes2_0() {
        return ListUtil.upcastUnmodifiable(getModuleTypes());
    }

    @Override
    protected List<? extends PlatformModuleType> getModuleTypes() {
        return moduleTypes2_0;
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

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v1_0.Platform.Builder {

        protected Map<String, Object> parameters;
        protected List<PlatformModuleType> moduleTypes2_0;
        protected List<PlatformResourceType> resourceTypes2_0;

        @Override
        public Platform build() {
            Platform result = new Platform();
            result.setName(name);
            result.setDescription(description);
            result.setModuleTypes2_0(ObjectUtils.defaultIfNull(moduleTypes2_0, Collections.<PlatformModuleType> emptyList()));
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            result.setResourceTypes2_0(ObjectUtils.defaultIfNull(resourceTypes2_0, Collections.<PlatformResourceType> emptyList()));
            return result;
        }

        public void setModuleTypes2_0(List<PlatformModuleType> moduleTypes) {
            setModuleTypes(moduleTypes);
        }

        @Override
        protected void setModuleTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.PlatformModuleType> moduleTypes) {
            this.moduleTypes2_0 = ListUtil.cast(moduleTypes);
        }

        public void setResourceTypes2_0(List<PlatformResourceType> resourceTypes) {
            setResourceTypes(resourceTypes);
        }

        @Override
        protected void setResourceTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.PlatformResourceType> resourceTypes) {
            this.resourceTypes2_0 = ListUtil.cast(resourceTypes);
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }

        @Override
        public void setProperties(Map<String, Object> properties) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setVersion(String version) {
            throw new UnsupportedOperationException();
        }

    }

}
