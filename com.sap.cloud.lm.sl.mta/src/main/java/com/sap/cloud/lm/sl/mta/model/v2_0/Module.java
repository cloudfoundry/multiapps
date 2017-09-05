package com.sap.cloud.lm.sl.mta.model.v2_0;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;
import com.sap.cloud.lm.sl.mta.model.Visitor;
import com.sap.cloud.lm.sl.mta.parsers.v2_0.ModuleParser;
import com.sap.cloud.lm.sl.mta.util.YamlElement;

public class Module extends com.sap.cloud.lm.sl.mta.model.v1_0.Module implements ParametersContainer {

    @YamlElement(ModuleParser.PATH)
    private String path;
    @YamlElement(ModuleParser.REQUIRES)
    private List<RequiredDependency> requiredDependencies2_0;
    @YamlElement(ModuleParser.PROVIDES)
    private List<ProvidedDependency> providedDependencies2_0;
    @YamlElement(ModuleParser.PARAMETERS)
    private Map<String, Object> parameters;

    protected Module() {

    }

    @Override
    public List<String> getRequiredDependencies1_0() {
        throw new UnsupportedOperationException();
    }

    public List<RequiredDependency> getRequiredDependencies2_0() {
        return ListUtil.upcastUnmodifiable(getRequiredDependencies());
    }

    protected List<? extends RequiredDependency> getRequiredDependencies() {
        return requiredDependencies2_0;
    }

    public List<ProvidedDependency> getProvidedDependencies2_0() {
        return ListUtil.upcastUnmodifiable(getProvidedDependencies());
    }

    @Override
    protected List<? extends ProvidedDependency> getProvidedDependencies() {
        return providedDependencies2_0;
    }

    public String getPath() {
        return path;
    }

    @Override
    public Map<String, Object> getParameters() {
        return MapUtil.unmodifiable(parameters);
    }

    @Override
    public void setRequiredDependencies1_0(List<String> requiredDependencies) {
        throw new UnsupportedOperationException();
    }

    public void setRequiredDependencies2_0(List<RequiredDependency> requiredDependencies) {
        setRequiredDependencies(requiredDependencies);
    }

    protected void setRequiredDependencies(List<? extends com.sap.cloud.lm.sl.mta.model.v2_0.RequiredDependency> requiredDependencies) {
        this.requiredDependencies2_0 = ListUtil.cast(requiredDependencies);
    }

    public void setProvidedDependencies2_0(List<ProvidedDependency> providedDependencies) {
        setProvidedDependencies(providedDependencies);
    }

    @Override
    protected void setProvidedDependencies(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.ProvidedDependency> providedDependencies) {
        this.providedDependencies2_0 = ListUtil.cast(providedDependencies);
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = new LinkedHashMap<>(parameters);
    }

    @Override
    public void accept(ElementContext context, Visitor visitor) {
        super.accept(context, visitor);
        for (RequiredDependency requiredDependency : getRequiredDependencies()) {
            requiredDependency.accept(new ElementContext(requiredDependency, context), visitor);
        }
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
        for (RequiredDependency requiredDependency : getRequiredDependencies2_0()) {
            clonedRequiredDependencies.add(requiredDependency.copyOf());
        }
        result.setRequiredDependencies2_0(clonedRequiredDependencies);
        List<ProvidedDependency> clonedProvidedDependencies = new ArrayList<>();
        for (ProvidedDependency providedDependency : getProvidedDependencies2_0()) {
            clonedProvidedDependencies.add(providedDependency.copyOf());
        }
        result.setProvidedDependencies2_0(clonedProvidedDependencies);
        return result.build();
    }

    public static class ModuleBuilder extends com.sap.cloud.lm.sl.mta.model.v1_0.Module.ModuleBuilder {

        protected String path;
        protected Map<String, Object> parameters;
        protected List<RequiredDependency> requiredDependencies2_0;
        protected List<ProvidedDependency> providedDependencies2_0;

        @Override
        public Module build() {
            Module result = new Module();
            result.setName(name);
            result.setType(type);
            result.setPath(path);
            result.setDescription(description);
            result.setProperties(getOrDefault(properties, Collections.<String, Object> emptyMap()));
            result.setParameters(getOrDefault(parameters, Collections.<String, Object> emptyMap()));
            result.setRequiredDependencies2_0(getOrDefault(requiredDependencies2_0, Collections.<RequiredDependency> emptyList()));
            result.setProvidedDependencies2_0(getOrDefault(providedDependencies2_0, Collections.<ProvidedDependency> emptyList()));
            return result;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }

        protected void setRequiredDependencies(List<? extends com.sap.cloud.lm.sl.mta.model.v2_0.RequiredDependency> requiredDependencies) {
            this.requiredDependencies2_0 = ListUtil.cast(requiredDependencies);
        }

        @Override
        public void setRequiredDependencies1_0(List<String> requiredDependencies) {
            throw new UnsupportedOperationException();
        }

        public void setRequiredDependencies2_0(List<RequiredDependency> requiredDependencies) {
            setRequiredDependencies(requiredDependencies);
        }

        @Override
        protected void setProvidedDependencies(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.ProvidedDependency> providedDependencies) {
            this.providedDependencies2_0 = ListUtil.cast(providedDependencies);
        }

        public void setProvidedDependencies2_0(List<ProvidedDependency> providedDependencies) {
            setProvidedDependencies(providedDependencies);
        }

    }

}
