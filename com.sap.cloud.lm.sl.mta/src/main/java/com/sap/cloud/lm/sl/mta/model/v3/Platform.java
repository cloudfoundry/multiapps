package com.sap.cloud.lm.sl.mta.model.v3;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.common.util.ListUtil;

public class Platform extends com.sap.cloud.lm.sl.mta.model.v2.Platform {

    private List<PlatformModuleType> moduleTypes3;
    private List<PlatformResourceType> resourceTypes3;

    protected Platform() {

    }

    public void setResourceTypes3(List<PlatformResourceType> resourceTypes) {
        setResourceTypes(resourceTypes);
    }

    @Override
    protected void setResourceTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1.PlatformResourceType> resourceTypes) {
        this.resourceTypes3 = ListUtil.cast(resourceTypes);
    }

    public List<PlatformResourceType> getResourceTypes3() {
        return ListUtil.upcastUnmodifiable(getResourceTypes());
    }

    @Override
    protected List<? extends PlatformResourceType> getResourceTypes() {
        return resourceTypes3;
    }

    public void setModuleTypes3(List<PlatformModuleType> moduleTypes) {
        setModuleTypes(moduleTypes);
    }

    @Override
    protected void setModuleTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1.PlatformModuleType> moduleTypes) {
        this.moduleTypes3 = ListUtil.cast(moduleTypes);
    }

    public List<PlatformModuleType> getModuleTypes3() {
        return ListUtil.upcastUnmodifiable(getModuleTypes());
    }

    @Override
    protected List<? extends PlatformModuleType> getModuleTypes() {
        return moduleTypes3;
    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v2.Platform.Builder {

        protected List<PlatformModuleType> moduleTypes3;
        protected List<PlatformResourceType> resourceTypes3;

        @Override
        public Platform build() {
            Platform result = new Platform();
            result.setName(name);
            result.setDescription(description);
            result.setModuleTypes3(ObjectUtils.defaultIfNull(moduleTypes3, Collections.<PlatformModuleType> emptyList()));
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            result.setResourceTypes3(ObjectUtils.defaultIfNull(resourceTypes3, Collections.<PlatformResourceType> emptyList()));
            return result;
        }

        public void setModuleTypes3(List<PlatformModuleType> moduleTypes) {
            setModuleTypes(moduleTypes);
        }

        @Override
        protected void setModuleTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1.PlatformModuleType> moduleTypes) {
            this.moduleTypes3 = ListUtil.cast(moduleTypes);
        }

        public void setResourceTypes3(List<PlatformResourceType> resourceTypes) {
            setResourceTypes(resourceTypes);
        }

        @Override
        protected void setResourceTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1.PlatformResourceType> resourceTypes) {
            this.resourceTypes3 = ListUtil.cast(resourceTypes);
        }

    }

}
