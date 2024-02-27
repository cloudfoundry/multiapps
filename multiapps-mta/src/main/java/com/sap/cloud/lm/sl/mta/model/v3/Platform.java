package com.sap.cloud.lm.sl.mta.model.v3;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.common.util.ListUtil;

public class Platform extends com.sap.cloud.lm.sl.mta.model.v2.Platform {

    private List<ModuleType> moduleTypes3;
    private List<ResourceType> resourceTypes3;

    protected Platform() {

    }

    public List<ResourceType> getResourceTypes3() {
        return ListUtil.upcastUnmodifiable(getResourceTypes());
    }

    public void setResourceTypes3(List<ResourceType> resourceTypes) {
        setResourceTypes(resourceTypes);
    }

    @Override
    protected List<? extends ResourceType> getResourceTypes() {
        return resourceTypes3;
    }

    @Override
    protected void setResourceTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v2.ResourceType> resourceTypes) {
        this.resourceTypes3 = ListUtil.cast(resourceTypes);
    }

    public List<ModuleType> getModuleTypes3() {
        return ListUtil.upcastUnmodifiable(getModuleTypes());
    }

    public void setModuleTypes3(List<ModuleType> moduleTypes) {
        setModuleTypes(moduleTypes);
    }

    @Override
    protected List<? extends ModuleType> getModuleTypes() {
        return moduleTypes3;
    }

    @Override
    protected void setModuleTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v2.ModuleType> moduleTypes) {
        this.moduleTypes3 = ListUtil.cast(moduleTypes);
    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v2.Platform.Builder {

        protected List<ModuleType> moduleTypes3;
        protected List<ResourceType> resourceTypes3;

        @Override
        public Platform build() {
            Platform result = new Platform();
            result.setName(name);
            result.setDescription(description);
            result.setModuleTypes3(ObjectUtils.defaultIfNull(moduleTypes3, Collections.<ModuleType> emptyList()));
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            result.setResourceTypes3(ObjectUtils.defaultIfNull(resourceTypes3, Collections.<ResourceType> emptyList()));
            return result;
        }

        public void setModuleTypes3(List<ModuleType> moduleTypes) {
            setModuleTypes(moduleTypes);
        }

        @Override
        protected void setModuleTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v2.ModuleType> moduleTypes) {
            this.moduleTypes3 = ListUtil.cast(moduleTypes);
        }

        public void setResourceTypes3(List<ResourceType> resourceTypes) {
            setResourceTypes(resourceTypes);
        }

        @Override
        protected void setResourceTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v2.ResourceType> resourceTypes) {
            this.resourceTypes3 = ListUtil.cast(resourceTypes);
        }

    }

}
