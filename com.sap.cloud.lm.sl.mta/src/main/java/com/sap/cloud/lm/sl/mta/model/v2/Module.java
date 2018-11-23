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
import com.sap.cloud.lm.sl.mta.model.NamedElement;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;
import com.sap.cloud.lm.sl.mta.model.VisitableElement;
import com.sap.cloud.lm.sl.mta.model.Visitor;
import com.sap.cloud.lm.sl.mta.parsers.v2.ModuleParser;
import com.sap.cloud.lm.sl.mta.util.YamlElement;

public class Module implements VisitableElement, NamedElement, PropertiesContainer, ParametersContainer {

    
    @YamlElement(ModuleParser.PATH)
    private String path;
    @YamlElement(ModuleParser.REQUIRES)
    private List<RequiredDependency> requiredDependencies2;
    @YamlElement(ModuleParser.PROVIDES)
    private List<ProvidedDependency> providedDependencies2;
    @YamlElement(ModuleParser.PARAMETERS)
    private Map<String, Object> parameters;
    @YamlElement(ModuleParser.NAME)
    private String name;
    @YamlElement(ModuleParser.TYPE)
    private String type;
    @YamlElement(ModuleParser.DESCRIPTION)
    private String description;
    @YamlElement(ModuleParser.PROPERTIES)
    private Map<String, Object> properties;

    protected Module() {

    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public Map<String, Object> getProperties() {
        return MapUtil.unmodifiable(properties);
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

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = new LinkedHashMap<>(properties);
    }

    public void setRequiredDependencies2(List<RequiredDependency> requiredDependencies) {
        setRequiredDependencies(requiredDependencies);
    }

    protected void setRequiredDependencies(List<? extends RequiredDependency> requiredDependencies) {
        this.requiredDependencies2 = ListUtil.cast(requiredDependencies);
    }

    public void setProvidedDependencies2(List<ProvidedDependency> providedDependencies) {
        setProvidedDependencies(providedDependencies);
    }

    protected void setProvidedDependencies(List<? extends ProvidedDependency> providedDependencies) {
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
        visitor.visit(context, this);
        for (ProvidedDependency providedDependency : getProvidedDependencies()) {
            providedDependency.accept(new ElementContext(providedDependency, context), visitor);
        }
        for (RequiredDependency requiredDependency : getRequiredDependencies()) {
            requiredDependency.accept(new ElementContext(requiredDependency, context), visitor);
        }
    }

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

    public static class Builder {

        protected String name;
        protected String type;
        protected String description;
        protected String path;
        protected Map<String, Object> properties;
        protected Map<String, Object> parameters;
        protected List<RequiredDependency> requiredDependencies2;
        protected List<ProvidedDependency> providedDependencies2;

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

        public void setName(String name) {
            this.name = name;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public void setProperties(Map<String, Object> properties) {
            this.properties = properties;
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }

        protected void setRequiredDependencies(List<? extends com.sap.cloud.lm.sl.mta.model.v2.RequiredDependency> requiredDependencies) {
            this.requiredDependencies2 = ListUtil.cast(requiredDependencies);
        }

        public void setRequiredDependencies2(List<RequiredDependency> requiredDependencies) {
            setRequiredDependencies(requiredDependencies);
        }

        protected void setProvidedDependencies(List<? extends com.sap.cloud.lm.sl.mta.model.v2.ProvidedDependency> providedDependencies) {
            this.providedDependencies2 = ListUtil.cast(providedDependencies);
        }

        public void setProvidedDependencies2(List<ProvidedDependency> providedDependencies) {
            setProvidedDependencies(providedDependencies);
        }

    }

}
