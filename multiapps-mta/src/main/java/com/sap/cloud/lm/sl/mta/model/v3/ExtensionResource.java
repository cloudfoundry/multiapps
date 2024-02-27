package com.sap.cloud.lm.sl.mta.model.v3;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.parsers.v3.ExtensionResourceParser;
import com.sap.cloud.lm.sl.mta.util.YamlElement;

public class ExtensionResource extends com.sap.cloud.lm.sl.mta.model.v2.ExtensionResource {

    @YamlElement(ExtensionResourceParser.ACTIVE)
    private Boolean isActive;
    @YamlElement(ExtensionResourceParser.REQUIRES)
    private List<ExtensionRequiredDependency> requiredDependencies3;

    protected ExtensionResource() {

    }

    public void setActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean isActive() {
        return isActive;
    }

    protected List<? extends ExtensionRequiredDependency> getRequiredDependencies() {
        return requiredDependencies3;
    }

    protected void setRequiredDependencies(List<? extends ExtensionRequiredDependency> requiredDependencies) {
        this.requiredDependencies3 = ListUtil.cast(requiredDependencies);
    }

    public List<ExtensionRequiredDependency> getRequiredDependencies3() {
        return ListUtil.upcastUnmodifiable(getRequiredDependencies());
    }

    public void setRequiredDependencies3(List<ExtensionRequiredDependency> requiredDependencies) {
        this.setRequiredDependencies(requiredDependencies);
    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v2.ExtensionResource.Builder {

        protected Boolean isActive;
        private List<ExtensionRequiredDependency> requiredDependencies;

        @Override
        public ExtensionResource build() {
            ExtensionResource result = new ExtensionResource();
            result.setName(name);
            result.setActive(isActive);
            result.setProperties(ObjectUtils.defaultIfNull(properties, Collections.<String, Object> emptyMap()));
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            result.setRequiredDependencies3(requiredDependencies);
            return result;
        }

        public void setActive(Boolean active) {
            this.isActive = active;
        }

        public void setRequiredDependencies(List<ExtensionRequiredDependency> requiredDependencies) {
            this.requiredDependencies = requiredDependencies;
        }

    }

}
