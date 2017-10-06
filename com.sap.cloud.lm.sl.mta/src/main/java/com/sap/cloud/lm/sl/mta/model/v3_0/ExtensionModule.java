package com.sap.cloud.lm.sl.mta.model.v3_0;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.Collections;
import java.util.List;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.parsers.v3_0.ExtensionModuleParser;
import com.sap.cloud.lm.sl.mta.util.YamlElement;

public class ExtensionModule extends com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionModule {

    @YamlElement(ExtensionModuleParser.PROVIDES)
    private List<ExtensionProvidedDependency> providedDependencies3_0;
    @YamlElement(ExtensionModuleParser.REQUIRES)
    private List<ExtensionRequiredDependency> requiredDependencies3_0;

    protected ExtensionModule() {

    }

    public List<ExtensionRequiredDependency> getRequiredDependencies3_0() {
        return ListUtil.upcastUnmodifiable(getRequiredDependencies());
    }

    protected List<? extends ExtensionRequiredDependency> getRequiredDependencies() {
        return requiredDependencies3_0;
    }

    public List<ExtensionProvidedDependency> getProvidedDependencies3_0() {
        return ListUtil.upcastUnmodifiable(getProvidedDependencies());
    }

    @Override
    protected List<? extends ExtensionProvidedDependency> getProvidedDependencies() {
        return providedDependencies3_0;
    }

    public void setRequiredDependencies3_0(List<ExtensionRequiredDependency> requiredDependencies) {
        setRequiredDependencies(requiredDependencies);
    }

    @Override
    protected void setRequiredDependencies(
        List<? extends com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionRequiredDependency> requiredDependencies) {
        this.requiredDependencies3_0 = ListUtil.cast(requiredDependencies);
    }

    public void setProvidedDependencies3_0(List<ExtensionProvidedDependency> providedDependencies) {
        setProvidedDependencies(providedDependencies);
    }

    @Override
    protected void setProvidedDependencies(
        List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionProvidedDependency> providedDependencies) {
        this.providedDependencies3_0 = ListUtil.cast(providedDependencies);
    }

    public static class ExtensionModuleBuilder extends com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionModule.ExtensionModuleBuilder {

        protected List<ExtensionRequiredDependency> requiredDependencies3_0;
        protected List<ExtensionProvidedDependency> providedDependencies3_0;

        @Override
        public ExtensionModule build() {
            ExtensionModule result = new ExtensionModule();
            result.setName(name);
            result.setProvidedDependencies3_0(getOrDefault(providedDependencies3_0, Collections.<ExtensionProvidedDependency> emptyList()));
            result.setRequiredDependencies3_0(getOrDefault(requiredDependencies3_0, Collections.<ExtensionRequiredDependency> emptyList()));
            result.setProperties(getOrDefault(properties, Collections.<String, Object> emptyMap()));
            result.setParameters(getOrDefault(parameters, Collections.<String, Object> emptyMap()));
            return result;
        }

        public void setProvidedDependencies3_0(List<ExtensionProvidedDependency> providedDependencies) {
            setProvidedDependencies(providedDependencies);
        }

        @Override
        protected void setProvidedDependencies(
            List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionProvidedDependency> providedDependencies) {
            this.providedDependencies3_0 = ListUtil.cast(providedDependencies);
        }

        public void setRequiredDependencies3_0(List<ExtensionRequiredDependency> requiredDependencies) {
            setRequiredDependencies(requiredDependencies);
        }

        protected void setRequiredDependencies(
            List<? extends com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionRequiredDependency> requiredDependencies) {
            this.requiredDependencies3_0 = ListUtil.cast(requiredDependencies);
        }

    }

}
