package com.sap.cloud.lm.sl.mta.model.v3;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.parsers.v3.ExtensionModuleParser;
import com.sap.cloud.lm.sl.mta.util.YamlElement;

public class ExtensionModule extends com.sap.cloud.lm.sl.mta.model.v2.ExtensionModule {

    @YamlElement(ExtensionModuleParser.PROVIDES)
    private List<ExtensionProvidedDependency> providedDependencies3;
    @YamlElement(ExtensionModuleParser.REQUIRES)
    private List<ExtensionRequiredDependency> requiredDependencies3;

    protected ExtensionModule() {

    }

    public List<ExtensionRequiredDependency> getRequiredDependencies3() {
        return ListUtil.upcastUnmodifiable(getRequiredDependencies());
    }

    protected List<? extends ExtensionRequiredDependency> getRequiredDependencies() {
        return requiredDependencies3;
    }

    public List<ExtensionProvidedDependency> getProvidedDependencies3() {
        return ListUtil.upcastUnmodifiable(getProvidedDependencies());
    }

    @Override
    protected List<? extends ExtensionProvidedDependency> getProvidedDependencies() {
        return providedDependencies3;
    }

    public void setRequiredDependencies3(List<ExtensionRequiredDependency> requiredDependencies) {
        setRequiredDependencies(requiredDependencies);
    }

    @Override
    protected void setRequiredDependencies(
        List<? extends com.sap.cloud.lm.sl.mta.model.v2.ExtensionRequiredDependency> requiredDependencies) {
        this.requiredDependencies3 = ListUtil.cast(requiredDependencies);
    }

    public void setProvidedDependencies3(List<ExtensionProvidedDependency> providedDependencies) {
        setProvidedDependencies(providedDependencies);
    }

    @Override
    protected void setProvidedDependencies(
        List<? extends com.sap.cloud.lm.sl.mta.model.v2.ExtensionProvidedDependency> providedDependencies) {
        this.providedDependencies3 = ListUtil.cast(providedDependencies);
    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v2.ExtensionModule.Builder {

        protected List<ExtensionRequiredDependency> requiredDependencies3;
        protected List<ExtensionProvidedDependency> providedDependencies3;

        @Override
        public ExtensionModule build() {
            ExtensionModule result = new ExtensionModule();
            result.setName(name);
            result.setProvidedDependencies3(ObjectUtils.defaultIfNull(providedDependencies3, Collections.<ExtensionProvidedDependency> emptyList()));
            result.setRequiredDependencies3(ObjectUtils.defaultIfNull(requiredDependencies3, Collections.<ExtensionRequiredDependency> emptyList()));
            result.setProperties(ObjectUtils.defaultIfNull(properties, Collections.<String, Object> emptyMap()));
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            return result;
        }

        public void setProvidedDependencies3(List<ExtensionProvidedDependency> providedDependencies) {
            setProvidedDependencies(providedDependencies);
        }

        @Override
        protected void setProvidedDependencies(
            List<? extends com.sap.cloud.lm.sl.mta.model.v2.ExtensionProvidedDependency> providedDependencies) {
            this.providedDependencies3 = ListUtil.cast(providedDependencies);
        }

        public void setRequiredDependencies3(List<ExtensionRequiredDependency> requiredDependencies) {
            setRequiredDependencies(requiredDependencies);
        }

        protected void setRequiredDependencies(
            List<? extends com.sap.cloud.lm.sl.mta.model.v2.ExtensionRequiredDependency> requiredDependencies) {
            this.requiredDependencies3 = ListUtil.cast(requiredDependencies);
        }

    }

}
