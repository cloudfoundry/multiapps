package com.sap.cloud.lm.sl.mta.model.v3_1;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.Collections;
import java.util.List;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.parsers.v3_1.ExtensionResourceParser;
import com.sap.cloud.lm.sl.mta.util.YamlElement;

public class ExtensionResource extends com.sap.cloud.lm.sl.mta.model.v3_0.ExtensionResource {

    @YamlElement(ExtensionResourceParser.ACTIVE)
    private Boolean active = true;
    @YamlElement(ExtensionResourceParser.REQUIRES)
    private List<ExtensionRequiredDependency> requiredDependencies3_1;

    protected ExtensionResource() {

    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getActive() {
        return active;
    }

    protected List<? extends ExtensionRequiredDependency> getRequiredDependencies() {
        return requiredDependencies3_1;
    }

    protected void setRequiredDependencies(List<? extends ExtensionRequiredDependency> requiredDependencies) {
        this.requiredDependencies3_1 = ListUtil.cast(requiredDependencies);
    }

    public List<ExtensionRequiredDependency> getRequiredDependencies3_1() {
        return ListUtil.upcastUnmodifiable(getRequiredDependencies());
    }

    public void setRequiredDependencies3_1(List<ExtensionRequiredDependency> requiredDependencies3_1) {
        this.setRequiredDependencies(requiredDependencies3_1);
    }

    public static class ExtensionResourceBuilder extends com.sap.cloud.lm.sl.mta.model.v3_0.ExtensionResource.Builder {

        protected Boolean active;
        private List<ExtensionRequiredDependency> requiredDependencies;

        @Override
        public ExtensionResource build() {
            ExtensionResource result = new ExtensionResource();
            result.setName(name);
            result.setActive(active);
            result.setProperties(getOrDefault(properties, Collections.<String, Object> emptyMap()));
            result.setParameters(getOrDefault(parameters, Collections.<String, Object> emptyMap()));
            result.setRequiredDependencies3_1(requiredDependencies);
            return result;
        }

        public void setActive(Boolean active) {
            this.active = active;
        }

        public void setRequiredDependencies(List<ExtensionRequiredDependency> requiredDependencies) {
            this.requiredDependencies = requiredDependencies;
        }
    }
}
