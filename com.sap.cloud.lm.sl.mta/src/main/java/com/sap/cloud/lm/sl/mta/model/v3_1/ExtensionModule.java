package com.sap.cloud.lm.sl.mta.model.v3_1;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.Collections;
import java.util.List;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.parsers.v3_1.ExtensionModuleParser;
import com.sap.cloud.lm.sl.mta.util.YamlElement;

public class ExtensionModule extends com.sap.cloud.lm.sl.mta.model.v3_0.ExtensionModule {

    @YamlElement(ExtensionModuleParser.PROVIDES)
    private List<ExtensionProvidedDependency> providedDependencies3_1;
    @YamlElement(ExtensionModuleParser.REQUIRES)
    private List<ExtensionRequiredDependency> requiredDependencies3_1;

    protected ExtensionModule() {

    }

    public List<ExtensionRequiredDependency> getRequiredDependencies3_1() {
        return ListUtil.upcastUnmodifiable(getRequiredDependencies());
    }

    protected List<? extends ExtensionRequiredDependency> getRequiredDependencies() {
        return requiredDependencies3_1;
    }

    public List<ExtensionProvidedDependency> getProvidedDependencies3_1() {
        return ListUtil.upcastUnmodifiable(getProvidedDependencies());
    }

    @Override
    protected List<? extends ExtensionProvidedDependency> getProvidedDependencies() {
        return providedDependencies3_1;
    }

    public void setRequiredDependencies3_1(List<ExtensionRequiredDependency> requiredDependencies) {
        setRequiredDependencies(requiredDependencies);
    }

    @Override
    protected void setRequiredDependencies(
        List<? extends com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionRequiredDependency> requiredDependencies) {
        this.requiredDependencies3_1 = ListUtil.cast(requiredDependencies);
    }

    public void setProvidedDependencies3_1(List<ExtensionProvidedDependency> providedDependencies) {
        setProvidedDependencies(providedDependencies);
    }

    @Override
    protected void setProvidedDependencies(
        List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionProvidedDependency> providedDependencies) {
        this.providedDependencies3_1 = ListUtil.cast(providedDependencies);
    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v3_0.ExtensionModule.Builder {

        protected List<ExtensionRequiredDependency> requiredDependencies3_1;
        protected List<ExtensionProvidedDependency> providedDependencies3_1;

        @Override
        public ExtensionModule build() {
            ExtensionModule result = new ExtensionModule();
            result.setName(name);
            result.setProvidedDependencies3_1(getOrDefault(providedDependencies3_1, Collections.<ExtensionProvidedDependency> emptyList()));
            result.setRequiredDependencies3_1(getOrDefault(requiredDependencies3_1, Collections.<ExtensionRequiredDependency> emptyList()));
            result.setProperties(getOrDefault(properties, Collections.<String, Object> emptyMap()));
            result.setParameters(getOrDefault(parameters, Collections.<String, Object> emptyMap()));
            return result;
        }

        public void setProvidedDependencies3_1(List<ExtensionProvidedDependency> providedDependencies) {
            setProvidedDependencies(providedDependencies);
        }

        @Override
        protected void setProvidedDependencies(
            List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionProvidedDependency> providedDependencies) {
            this.providedDependencies3_1 = ListUtil.cast(providedDependencies);
        }

        public void setRequiredDependencies3_1(List<ExtensionRequiredDependency> requiredDependencies) {
            setRequiredDependencies(requiredDependencies);
        }

        protected void setRequiredDependencies(
            List<? extends com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionRequiredDependency> requiredDependencies) {
            this.requiredDependencies3_1 = ListUtil.cast(requiredDependencies);
        }

    }

}
