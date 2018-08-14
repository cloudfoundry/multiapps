package com.sap.cloud.lm.sl.mta.model.v3_0;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.common.util.ListUtil;

public class Target extends com.sap.cloud.lm.sl.mta.model.v2_0.Target {

    private List<TargetModuleType> platformModuleTypes3_0;
    private List<TargetResourceType> platformResourceTypes3_0;

    protected Target() {

    }

    public void setResourceTypes3_0(List<TargetResourceType> resourceTypes) {
        setResourceTypes(resourceTypes);
    }

    @Override
    protected void setResourceTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.TargetResourceType> resourceTypes) {
        this.platformResourceTypes3_0 = ListUtil.cast(resourceTypes);
    }

    public List<TargetResourceType> getResourceTypes3_0() {
        return ListUtil.upcastUnmodifiable(getResourceTypes());
    }

    @Override
    protected List<? extends TargetResourceType> getResourceTypes() {
        return platformResourceTypes3_0;
    }

    public void setModuleTypes3_0(List<TargetModuleType> moduleTypes) {
        setModuleTypes(moduleTypes);
    }

    @Override
    protected void setModuleTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.TargetModuleType> moduleTypes) {
        this.platformModuleTypes3_0 = ListUtil.cast(moduleTypes);
    }

    public List<TargetModuleType> getModuleTypes3_0() {
        return ListUtil.upcastUnmodifiable(getModuleTypes());
    }

    @Override
    protected List<? extends TargetModuleType> getModuleTypes() {
        return platformModuleTypes3_0;
    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v2_0.Target.Builder {

        protected List<TargetModuleType> platformModuleTypes3_0;
        protected List<TargetResourceType> platformResourceTypes3_0;

        @Override
        public Target build() {
            Target result = new Target();
            result.setName(name);
            result.setType(type);
            result.setDescription(description);
            result.setModuleTypes3_0(ObjectUtils.defaultIfNull(platformModuleTypes3_0, Collections.<TargetModuleType> emptyList()));
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            result.setResourceTypes3_0(ObjectUtils.defaultIfNull(platformResourceTypes3_0, Collections.<TargetResourceType> emptyList()));
            return result;
        }

        public void setModuleTypes3_0(List<TargetModuleType> moduleTypes) {
            setModuleTypes(moduleTypes);
        }

        @Override
        protected void setModuleTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.TargetModuleType> moduleTypes) {
            this.platformModuleTypes3_0 = ListUtil.cast(moduleTypes);
        }

        public void setResourceTypes3_0(List<TargetResourceType> resourceTypes) {
            setResourceTypes(resourceTypes);
        }

        @Override
        protected void setResourceTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.TargetResourceType> resourceTypes) {
            this.platformResourceTypes3_0 = ListUtil.cast(resourceTypes);
        }

    }

}
