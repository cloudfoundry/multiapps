package com.sap.cloud.lm.sl.mta.model.v2_0;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;

public class Target extends com.sap.cloud.lm.sl.mta.model.v1_0.Target implements ParametersContainer {

    private Map<String, Object> parameters;
    private List<PlatformModuleType> platformModuleTypes2_0;
    private List<PlatformResourceType> platformResourceTypes2_0;

    protected Target() {

    }

    public void setResourceTypes2_0(List<PlatformResourceType> resourceTypes) {
        setResourceTypes(resourceTypes);
    }

    @Override
    protected void setResourceTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.PlatformResourceType> resourceTypes) {
        this.platformResourceTypes2_0 = ListUtil.cast(resourceTypes);
    }

    public List<PlatformResourceType> getResourceTypes2_0() {
        return ListUtil.upcastUnmodifiable(getResourceTypes());
    }

    @Override
    protected List<? extends PlatformResourceType> getResourceTypes() {
        return platformResourceTypes2_0;
    }

    public void setModuleTypes2_0(List<PlatformModuleType> moduleTypes) {
        setModuleTypes(moduleTypes);
    }

    @Override
    protected void setModuleTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.PlatformModuleType> moduleTypes) {
        this.platformModuleTypes2_0 = ListUtil.cast(moduleTypes);
    }

    public List<PlatformModuleType> getModuleTypes2_0() {
        return ListUtil.upcastUnmodifiable(getModuleTypes());
    }

    @Override
    protected List<? extends PlatformModuleType> getModuleTypes() {
        return platformModuleTypes2_0;
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

    public static class TargetBuilder extends com.sap.cloud.lm.sl.mta.model.v1_0.Target.TargetBuilder {

        protected Map<String, Object> parameters;
        protected List<PlatformModuleType> platformModuleTypes2_0;
        protected List<PlatformResourceType> platformResourceTypes2_0;

        @Override
        public Target build() {
            Target result = new Target();
            result.setName(name);
            result.setType(type);
            result.setDescription(description);
            result.setModuleTypes2_0(getOrDefault(platformModuleTypes2_0, Collections.<PlatformModuleType> emptyList()));
            result.setParameters(getOrDefault(parameters, Collections.<String, Object> emptyMap()));
            result.setResourceTypes2_0(getOrDefault(platformResourceTypes2_0, Collections.<PlatformResourceType> emptyList()));
            return result;
        }

        public void setModuleTypes2_0(List<PlatformModuleType> moduleTypes) {
            setModuleTypes(moduleTypes);
        }

        @Override
        protected void setModuleTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.PlatformModuleType> moduleTypes) {
            this.platformModuleTypes2_0 = ListUtil.cast(moduleTypes);
        }

        public void setResourceTypes2_0(List<PlatformResourceType> resourceTypes) {
            setResourceTypes(resourceTypes);
        }

        @Override
        protected void setResourceTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.PlatformResourceType> resourceTypes) {
            this.platformResourceTypes2_0 = ListUtil.cast(resourceTypes);
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
