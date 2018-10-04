package com.sap.cloud.lm.sl.mta.model.v2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;
import com.sap.cloud.lm.sl.mta.model.Visitor;
import com.sap.cloud.lm.sl.mta.parsers.v2.ModuleParser;
import com.sap.cloud.lm.sl.mta.util.YamlElement;

public class Module extends com.sap.cloud.lm.sl.mta.model.v1.Module implements ParametersContainer {

    @YamlElement(ModuleParser.PATH)
    private String path;
    @YamlElement(ModuleParser.REQUIRES)
    private List<RequiredDependency> requiredDependencies2;
    @YamlElement(ModuleParser.PROVIDES)
    private List<ProvidedDependency> providedDependencies2;
    @YamlElement(ModuleParser.PARAMETERS)
    private Map<String, Object> parameters;

    protected Module() {

    }

    @Override
    public List<String> getRequiredDependencies1() {
        throw new UnsupportedOperationException();
    }

    public List<RequiredDependency> getRequiredDependencies2() {
        return ListUtil.upcastUnmodifiable(getRequiredDependencies());
    }

    protected List<? extends RequiredDependency> getRequiredDependencies() {
        return requiredDependencies2;
    }

    public List<ProvidedDependency> getProvidedDependencies2() {
        return ListUtil.upcastUnmodifiable(getProvidedDependencies());
    }

    @Override
    protected List<? extends ProvidedDependency> getProvidedDependencies() {
        return providedDependencies2;
    }

    public String getPath() {
        return path;
    }

    @Override
    public Map<String, Object> getParameters() {
        return MapUtil.unmodifiable(parameters);
    }

    @Override
    public void setRequiredDependencies1(List<String> requiredDependencies) {
        throw new UnsupportedOperationException();
    }

    public void setRequiredDependencies2(List<RequiredDependency> requiredDependencies) {
        setRequiredDependencies(requiredDependencies);
    }

    protected void setRequiredDependencies(List<? extends com.sap.cloud.lm.sl.mta.model.v2.RequiredDependency> requiredDependencies) {
        this.requiredDependencies2 = ListUtil.cast(requiredDependencies);
    }

    public void setProvidedDependencies2(List<ProvidedDependency> providedDependencies) {
        setProvidedDependencies(providedDependencies);
    }

    @Override
    protected void setProvidedDependencies(List<? extends com.sap.cloud.lm.sl.mta.model.v1.ProvidedDependency> providedDependencies) {
        this.providedDependencies2 = ListUtil.cast(providedDependencies);
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
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

    @Override
    public Module copyOf() {
        Builder result = new Builder();
        result.setName(getName());
        result.setType(getType());
        result.setPath(getPath());
        result.setDescription(getDescription());
        result.setProperties(getProperties());
        result.setParameters(getParameters());
        List<RequiredDependency> clonedRequiredDependencies = new ArrayList<>();
        for (RequiredDependency requiredDependency : getRequiredDependencies2()) {
            clonedRequiredDependencies.add(requiredDependency.copyOf());
        }
        result.setRequiredDependencies2(clonedRequiredDependencies);
        List<ProvidedDependency> clonedProvidedDependencies = new ArrayList<>();
        for (ProvidedDependency providedDependency : getProvidedDependencies2()) {
            clonedProvidedDependencies.add(providedDependency.copyOf());
        }
        result.setProvidedDependencies2(clonedProvidedDependencies);
        return result.build();
    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v1.Module.Builder {

        protected String path;
        protected Map<String, Object> parameters;
        protected List<RequiredDependency> requiredDependencies2;
        protected List<ProvidedDependency> providedDependencies2;

        @Override
        public Module build() {
            Module result = new Module();
            result.setName(name);
            result.setType(type);
            result.setPath(path);
            result.setDescription(description);
            result.setProperties(ObjectUtils.defaultIfNull(properties, Collections.<String, Object> emptyMap()));
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            result.setRequiredDependencies2(ObjectUtils.defaultIfNull(requiredDependencies2, Collections.<RequiredDependency> emptyList()));
            result.setProvidedDependencies2(ObjectUtils.defaultIfNull(providedDependencies2, Collections.<ProvidedDependency> emptyList()));
            return result;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }

        protected void setRequiredDependencies(List<? extends com.sap.cloud.lm.sl.mta.model.v2.RequiredDependency> requiredDependencies) {
            this.requiredDependencies2 = ListUtil.cast(requiredDependencies);
        }

        @Override
        public void setRequiredDependencies1(List<String> requiredDependencies) {
            throw new UnsupportedOperationException();
        }

        public void setRequiredDependencies2(List<RequiredDependency> requiredDependencies) {
            setRequiredDependencies(requiredDependencies);
        }

        @Override
        protected void setProvidedDependencies(List<? extends com.sap.cloud.lm.sl.mta.model.v1.ProvidedDependency> providedDependencies) {
            this.providedDependencies2 = ListUtil.cast(providedDependencies);
        }

        public void setProvidedDependencies2(List<ProvidedDependency> providedDependencies) {
            setProvidedDependencies(providedDependencies);
        }

    }

}
