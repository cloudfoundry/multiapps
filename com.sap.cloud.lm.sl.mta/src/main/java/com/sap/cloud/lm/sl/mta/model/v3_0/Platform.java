package com.sap.cloud.lm.sl.mta.model.v3_0;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.common.util.ListUtil;

public class Platform extends com.sap.cloud.lm.sl.mta.model.v2_0.Platform {

    private List<PlatformModuleType> moduleTypes3_0;
    private List<PlatformResourceType> resourceTypes3_0;

    protected Platform() {

    }

    public void setResourceTypes3_0(List<PlatformResourceType> resourceTypes) {
        setResourceTypes(resourceTypes);
    }

    @Override
    protected void setResourceTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.PlatformResourceType> resourceTypes) {
        this.resourceTypes3_0 = ListUtil.cast(resourceTypes);
    }

    public List<PlatformResourceType> getResourceTypes3_0() {
        return ListUtil.upcastUnmodifiable(getResourceTypes());
    }

    @Override
    protected List<? extends PlatformResourceType> getResourceTypes() {
        return resourceTypes3_0;
    }

    public void setModuleTypes3_0(List<PlatformModuleType> moduleTypes) {
        setModuleTypes(moduleTypes);
    }

    @Override
    protected void setModuleTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.PlatformModuleType> moduleTypes) {
        this.moduleTypes3_0 = ListUtil.cast(moduleTypes);
    }

    public List<PlatformModuleType> getModuleTypes3_0() {
        return ListUtil.upcastUnmodifiable(getModuleTypes());
    }

    @Override
    protected List<? extends PlatformModuleType> getModuleTypes() {
        return moduleTypes3_0;
    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v2_0.Platform.Builder {

        protected List<PlatformModuleType> moduleTypes3_0;
        protected List<PlatformResourceType> resourceTypes3_0;

        @Override
        public Platform build() {
            Platform result = new Platform();
            result.setName(name);
            result.setDescription(description);
            result.setModuleTypes3_0(ObjectUtils.defaultIfNull(moduleTypes3_0, Collections.<PlatformModuleType> emptyList()));
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            result.setResourceTypes3_0(ObjectUtils.defaultIfNull(resourceTypes3_0, Collections.<PlatformResourceType> emptyList()));
            return result;
        }

        public void setModuleTypes3_0(List<PlatformModuleType> moduleTypes) {
            setModuleTypes(moduleTypes);
        }

        @Override
        protected void setModuleTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.PlatformModuleType> moduleTypes) {
            this.moduleTypes3_0 = ListUtil.cast(moduleTypes);
        }

        public void setResourceTypes3_0(List<PlatformResourceType> resourceTypes) {
            setResourceTypes(resourceTypes);
        }

        @Override
        protected void setResourceTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.PlatformResourceType> resourceTypes) {
            this.resourceTypes3_0 = ListUtil.cast(resourceTypes);
        }

    }

}
