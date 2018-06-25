package com.sap.cloud.lm.sl.mta.model.v3_1;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.Collections;
import java.util.List;

import com.sap.cloud.lm.sl.common.util.ListUtil;

public class Target extends com.sap.cloud.lm.sl.mta.model.v3_0.Target {

    private List<TargetModuleType> platformModuleTypes3_1;
    private List<TargetResourceType> platformResourceTypes3_1;

    protected Target() {

    }

    public void setResourceTypes3_1(List<TargetResourceType> resourceTypes) {
        setResourceTypes(resourceTypes);
    }

    @Override
    protected void setResourceTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.TargetResourceType> resourceTypes) {
        this.platformResourceTypes3_1 = ListUtil.cast(resourceTypes);
    }

    public List<TargetResourceType> getResourceTypes3_1() {
        return ListUtil.upcastUnmodifiable(getResourceTypes());
    }

    @Override
    protected List<? extends TargetResourceType> getResourceTypes() {
        return platformResourceTypes3_1;
    }

    public void setModuleTypes3_1(List<TargetModuleType> moduleTypes) {
        setModuleTypes(moduleTypes);
    }

    @Override
    protected void setModuleTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.TargetModuleType> moduleTypes) {
        this.platformModuleTypes3_1 = ListUtil.cast(moduleTypes);
    }

    public List<TargetModuleType> getModuleTypes3_1() {
        return ListUtil.upcastUnmodifiable(getModuleTypes());
    }

    @Override
    protected List<? extends TargetModuleType> getModuleTypes() {
        return platformModuleTypes3_1;
    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v3_0.Target.Builder {

        protected List<TargetModuleType> platformModuleTypes3_1;
        protected List<TargetResourceType> platformResourceTypes3_1;

        @Override
        public Target build() {
            Target result = new Target();
            result.setName(name);
            result.setType(type);
            result.setDescription(description);
            result.setModuleTypes3_1(getOrDefault(platformModuleTypes3_1, Collections.<TargetModuleType> emptyList()));
            result.setParameters(getOrDefault(parameters, Collections.<String, Object> emptyMap()));
            result.setResourceTypes3_1(getOrDefault(platformResourceTypes3_1, Collections.<TargetResourceType> emptyList()));
            return result;
        }

        public void setModuleTypes3_1(List<TargetModuleType> moduleTypes) {
            setModuleTypes(moduleTypes);
        }

        @Override
        protected void setModuleTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.TargetModuleType> moduleTypes) {
            this.platformModuleTypes3_1 = ListUtil.cast(moduleTypes);
        }

        public void setResourceTypes3_1(List<TargetResourceType> resourceTypes) {
            setResourceTypes(resourceTypes);
        }

        @Override
        protected void setResourceTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.TargetResourceType> resourceTypes) {
            this.platformResourceTypes3_1 = ListUtil.cast(resourceTypes);
        }

    }

}
