package com.sap.cloud.lm.sl.mta.model.v3_1;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.Collections;
import java.util.List;

import com.sap.cloud.lm.sl.common.util.ListUtil;

public class Target extends com.sap.cloud.lm.sl.mta.model.v3_0.Target {

    private List<PlatformModuleType> platformModuleTypes3_1;
    private List<PlatformResourceType> platformResourceTypes3_1;

    protected Target() {

    }

    public void setResourceTypes3_1(List<PlatformResourceType> resourceTypes) {
        setResourceTypes(resourceTypes);
    }

    @Override
    protected void setResourceTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.PlatformResourceType> resourceTypes) {
        this.platformResourceTypes3_1 = ListUtil.cast(resourceTypes);
    }

    public List<PlatformResourceType> getResourceTypes3_1() {
        return ListUtil.upcastUnmodifiable(getResourceTypes());
    }

    @Override
    protected List<? extends PlatformResourceType> getResourceTypes() {
        return platformResourceTypes3_1;
    }

    public void setModuleTypes3_1(List<PlatformModuleType> moduleTypes) {
        setModuleTypes(moduleTypes);
    }

    @Override
    protected void setModuleTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.PlatformModuleType> moduleTypes) {
        this.platformModuleTypes3_1 = ListUtil.cast(moduleTypes);
    }

    public List<PlatformModuleType> getModuleTypes3_1() {
        return ListUtil.upcastUnmodifiable(getModuleTypes());
    }

    @Override
    protected List<? extends PlatformModuleType> getModuleTypes() {
        return platformModuleTypes3_1;
    }

    public static class TargetBuilder extends com.sap.cloud.lm.sl.mta.model.v3_0.Target.TargetBuilder {

        protected List<PlatformModuleType> platformModuleTypes3_1;
        protected List<PlatformResourceType> platformResourceTypes3_1;

        @Override
        public Target build() {
            Target result = new Target();
            result.setName(name);
            result.setType(type);
            result.setDescription(description);
            result.setModuleTypes3_1(getOrDefault(platformModuleTypes3_1, Collections.<PlatformModuleType> emptyList()));
            result.setParameters(getOrDefault(parameters, Collections.<String, Object> emptyMap()));
            result.setResourceTypes3_1(getOrDefault(platformResourceTypes3_1, Collections.<PlatformResourceType> emptyList()));
            return result;
        }

        public void setModuleTypes3_1(List<PlatformModuleType> moduleTypes) {
            setModuleTypes(moduleTypes);
        }

        @Override
        protected void setModuleTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.PlatformModuleType> moduleTypes) {
            this.platformModuleTypes3_1 = ListUtil.cast(moduleTypes);
        }

        public void setResourceTypes3_1(List<PlatformResourceType> resourceTypes) {
            setResourceTypes(resourceTypes);
        }

        @Override
        protected void setResourceTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.PlatformResourceType> resourceTypes) {
            this.platformResourceTypes3_1 = ListUtil.cast(resourceTypes);
        }

    }

}
