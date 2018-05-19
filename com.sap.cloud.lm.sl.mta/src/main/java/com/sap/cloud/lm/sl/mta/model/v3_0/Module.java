package com.sap.cloud.lm.sl.mta.model.v3_0;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.parsers.v3_0.ModuleParser;
import com.sap.cloud.lm.sl.mta.util.YamlElement;

public class Module extends com.sap.cloud.lm.sl.mta.model.v2_0.Module {

    @YamlElement(ModuleParser.REQUIRES)
    private List<RequiredDependency> requiredDependencies3_0;
    @YamlElement(ModuleParser.PROVIDES)
    private List<ProvidedDependency> providedDependencies3_0;

    protected Module() {

    }

    public List<RequiredDependency> getRequiredDependencies3_0() {
        return ListUtil.upcastUnmodifiable(getRequiredDependencies());
    }

    @Override
    protected List<? extends RequiredDependency> getRequiredDependencies() {
        return requiredDependencies3_0;
    }

    public List<ProvidedDependency> getProvidedDependencies3_0() {
        return ListUtil.upcastUnmodifiable(getProvidedDependencies());
    }

    @Override
    protected List<? extends ProvidedDependency> getProvidedDependencies() {
        return providedDependencies3_0;
    }

    public void setRequiredDependencies3_0(List<RequiredDependency> requiredDependencies) {
        setRequiredDependencies(requiredDependencies);
    }

    @Override
    public void setRequiredDependencies(List<? extends com.sap.cloud.lm.sl.mta.model.v2_0.RequiredDependency> requiredDependencies) {
        this.requiredDependencies3_0 = ListUtil.cast(requiredDependencies);
    }

    public void setProvidedDependencies3_0(List<ProvidedDependency> providedDependencies) {
        setProvidedDependencies(providedDependencies);
    }

    @Override
    protected void setProvidedDependencies(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.ProvidedDependency> providedDependencies) {
        this.providedDependencies3_0 = ListUtil.cast(providedDependencies);
    }

    public Module copyOf() {
        ModuleBuilder result = new ModuleBuilder();
        result.setName(getName());
        result.setType(getType());
        result.setPath(getPath());
        result.setDescription(getDescription());
        result.setProperties(getProperties());
        result.setParameters(getParameters());
        List<RequiredDependency> clonedRequiredDependencies = new ArrayList<>();
        for (RequiredDependency requiredDependency : getRequiredDependencies3_0()) {
            clonedRequiredDependencies.add(requiredDependency.copyOf());
        }
        result.setRequiredDependencies3_0(clonedRequiredDependencies);
        List<ProvidedDependency> clonedProvidedDependencies = new ArrayList<>();
        for (ProvidedDependency providedDependency : getProvidedDependencies3_0()) {
            clonedProvidedDependencies.add(providedDependency.copyOf());
        }
        result.setProvidedDependencies3_0(clonedProvidedDependencies);
        return result.build();
    }

    public static class ModuleBuilder extends com.sap.cloud.lm.sl.mta.model.v2_0.Module.ModuleBuilder {

        protected List<RequiredDependency> requiredDependencies3_0;
        protected List<ProvidedDependency> providedDependencies3_0;

        @Override
        public Module build() {
            Module result = new Module();
            result.setName(name);
            result.setType(type);
            result.setPath(path);
            result.setDescription(description);
            result.setProperties(getOrDefault(properties, Collections.<String, Object> emptyMap()));
            result.setParameters(getOrDefault(parameters, Collections.<String, Object> emptyMap()));
            result.setRequiredDependencies3_0(getOrDefault(requiredDependencies3_0, Collections.<RequiredDependency> emptyList()));
            result.setProvidedDependencies3_0(getOrDefault(providedDependencies3_0, Collections.<ProvidedDependency> emptyList()));
            return result;
        }

        protected void setRequiredDependencies(List<? extends com.sap.cloud.lm.sl.mta.model.v2_0.RequiredDependency> requiredDependencies) {
            this.requiredDependencies3_0 = ListUtil.cast(requiredDependencies);
        }

        public void setRequiredDependencies3_0(List<RequiredDependency> requiredDependencies) {
            setRequiredDependencies(requiredDependencies);
        }

        @Override
        protected void setProvidedDependencies(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.ProvidedDependency> providedDependencies) {
            this.providedDependencies3_0 = ListUtil.cast(providedDependencies);
        }

        public void setProvidedDependencies3_0(List<ProvidedDependency> providedDependencies) {
            setProvidedDependencies(providedDependencies);
        }

    }
}
