package com.sap.cloud.lm.sl.mta.model.v3;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.common.util.ListUtil;

public class Target extends com.sap.cloud.lm.sl.mta.model.v2.Target {

    private List<TargetModuleType> platformModuleTypes3;
    private List<TargetResourceType> platformResourceTypes3;

    protected Target() {

    }

    public void setResourceTypes3(List<TargetResourceType> resourceTypes) {
        setResourceTypes(resourceTypes);
    }

    @Override
    protected void setResourceTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1.TargetResourceType> resourceTypes) {
        this.platformResourceTypes3 = ListUtil.cast(resourceTypes);
    }

    public List<TargetResourceType> getResourceTypes3() {
        return ListUtil.upcastUnmodifiable(getResourceTypes());
    }

    @Override
    protected List<? extends TargetResourceType> getResourceTypes() {
        return platformResourceTypes3;
    }

    public void setModuleTypes3(List<TargetModuleType> moduleTypes) {
        setModuleTypes(moduleTypes);
    }

    @Override
    protected void setModuleTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1.TargetModuleType> moduleTypes) {
        this.platformModuleTypes3 = ListUtil.cast(moduleTypes);
    }

    public List<TargetModuleType> getModuleTypes3() {
        return ListUtil.upcastUnmodifiable(getModuleTypes());
    }

    @Override
    protected List<? extends TargetModuleType> getModuleTypes() {
        return platformModuleTypes3;
    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v2.Target.Builder {

        protected List<TargetModuleType> platformModuleTypes3;
        protected List<TargetResourceType> platformResourceTypes3;

        @Override
        public Target build() {
            Target result = new Target();
            result.setName(name);
            result.setType(type);
            result.setDescription(description);
            result.setModuleTypes3(ObjectUtils.defaultIfNull(platformModuleTypes3, Collections.<TargetModuleType> emptyList()));
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            result.setResourceTypes3(ObjectUtils.defaultIfNull(platformResourceTypes3, Collections.<TargetResourceType> emptyList()));
            return result;
        }

        public void setModuleTypes3(List<TargetModuleType> moduleTypes) {
            setModuleTypes(moduleTypes);
        }

        @Override
        protected void setModuleTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1.TargetModuleType> moduleTypes) {
            this.platformModuleTypes3 = ListUtil.cast(moduleTypes);
        }

        public void setResourceTypes3(List<TargetResourceType> resourceTypes) {
            setResourceTypes(resourceTypes);
        }

        @Override
        protected void setResourceTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1.TargetResourceType> resourceTypes) {
            this.platformResourceTypes3 = ListUtil.cast(resourceTypes);
        }

    }

}
