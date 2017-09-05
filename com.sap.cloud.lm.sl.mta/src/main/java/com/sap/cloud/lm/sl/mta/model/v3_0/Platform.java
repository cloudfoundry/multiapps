package com.sap.cloud.lm.sl.mta.model.v3_0;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.Collections;
import java.util.List;

import com.sap.cloud.lm.sl.common.util.ListUtil;

public class Platform extends com.sap.cloud.lm.sl.mta.model.v2_0.Platform {

    private List<ModuleType> moduleTypes3_0;
    private List<ResourceType> resourceTypes3_0;

    protected Platform() {

    }

    public void setResourceTypes3_0(List<ResourceType> resourceTypes) {
        setResourceTypes(resourceTypes);
    }

    @Override
    protected void setResourceTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.ResourceType> resourceTypes) {
        this.resourceTypes3_0 = ListUtil.cast(resourceTypes);
    }

    public List<ResourceType> getResourceTypes3_0() {
        return ListUtil.upcastUnmodifiable(getResourceTypes());
    }

    @Override
    protected List<? extends ResourceType> getResourceTypes() {
        return resourceTypes3_0;
    }

    public void setModuleTypes3_0(List<ModuleType> moduleTypes) {
        setModuleTypes(moduleTypes);
    }

    @Override
    protected void setModuleTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.ModuleType> moduleTypes) {
        this.moduleTypes3_0 = ListUtil.cast(moduleTypes);
    }

    public List<ModuleType> getModuleTypes3_0() {
        return ListUtil.upcastUnmodifiable(getModuleTypes());
    }

    @Override
    protected List<? extends ModuleType> getModuleTypes() {
        return moduleTypes3_0;
    }

    public static class PlatformBuilder extends com.sap.cloud.lm.sl.mta.model.v2_0.Platform.PlatformBuilder {

        protected List<ModuleType> moduleTypes3_0;
        protected List<ResourceType> resourceTypes3_0;

        @Override
        public Platform build() {
            Platform result = new Platform();
            result.setName(name);
            result.setDescription(description);
            result.setModuleTypes3_0(getOrDefault(moduleTypes3_0, Collections.<ModuleType> emptyList()));
            result.setParameters(getOrDefault(parameters, Collections.<String, Object> emptyMap()));
            result.setResourceTypes3_0(getOrDefault(resourceTypes3_0, Collections.<ResourceType> emptyList()));
            return result;
        }

        public void setModuleTypes3_0(List<ModuleType> moduleTypes) {
            setModuleTypes(moduleTypes);
        }

        @Override
        protected void setModuleTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.ModuleType> moduleTypes) {
            this.moduleTypes3_0 = ListUtil.cast(moduleTypes);
        }

        public void setResourceTypes3_0(List<ResourceType> resourceTypes) {
            setResourceTypes(resourceTypes);
        }

        @Override
        protected void setResourceTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.ResourceType> resourceTypes) {
            this.resourceTypes3_0 = ListUtil.cast(resourceTypes);
        }

    }

}
