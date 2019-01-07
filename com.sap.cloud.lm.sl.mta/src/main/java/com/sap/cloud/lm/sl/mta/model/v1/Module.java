package com.sap.cloud.lm.sl.mta.model.v1;

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
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;
import com.sap.cloud.lm.sl.mta.model.VisitableElement;
import com.sap.cloud.lm.sl.mta.model.Visitor;
import com.sap.cloud.lm.sl.mta.parsers.v1.ModuleParser;
import com.sap.cloud.lm.sl.mta.util.YamlElement;

public class Module implements VisitableElement, NamedElement, PropertiesContainer {

    @YamlElement(ModuleParser.NAME)
    private String name;
    @YamlElement(ModuleParser.TYPE)
    private String type;
    @YamlElement(ModuleParser.DESCRIPTION)
    private String description;
    @YamlElement(ModuleParser.PROPERTIES)
    private Map<String, Object> properties;
    @YamlElement(ModuleParser.REQUIRES)
    private List<String> requiredDependencies1;
    @YamlElement(ModuleParser.PROVIDES)
    private List<ProvidedDependency> providedDependencies1;

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

    public List<String> getRequiredDependencies1() {
        return ListUtil.unmodifiable(requiredDependencies1);
    }

    public List<ProvidedDependency> getProvidedDependencies1() {
        return ListUtil.upcastUnmodifiable(getProvidedDependencies());
    }

    protected List<? extends ProvidedDependency> getProvidedDependencies() {
        return providedDependencies1;
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

    public void setRequiredDependencies1(List<String> requiredDependencies) {
        this.requiredDependencies1 = new ArrayList<>(requiredDependencies);
    }

    public void setProvidedDependencies1(List<ProvidedDependency> providedDependencies) {
        setProvidedDependencies(providedDependencies);
    }

    protected void setProvidedDependencies(List<? extends ProvidedDependency> providedDependencies) {
        this.providedDependencies1 = ListUtil.cast(providedDependencies);
    }

    @Override
    public void accept(ElementContext context, Visitor visitor) {
        visitor.visit(context, this);
        for (ProvidedDependency providedDependency : getProvidedDependencies()) {
            providedDependency.accept(new ElementContext(providedDependency, context), visitor);
        }
    }

    public Module copyOf() {
        Builder result = new Builder();
        result.setName(getName());
        result.setType(getType());
        result.setDescription(getDescription());
        result.setProperties(getProperties());
        List<String> clonedRequiredDependencies = new ArrayList<>(getRequiredDependencies1());
        result.setRequiredDependencies1(clonedRequiredDependencies);
        List<ProvidedDependency> clonedProvidedDependencies = new ArrayList<>();
        for (ProvidedDependency providedDependency : getProvidedDependencies1()) {
            clonedProvidedDependencies.add(providedDependency.copyOf());
        }
        result.setProvidedDependencies1(clonedProvidedDependencies);
        return result.build();
    }

    public static class Builder {

        protected String name;
        protected String type;
        protected String description;
        protected Map<String, Object> properties;
        protected List<String> requiredDependencies1;
        protected List<ProvidedDependency> providedDependencies1;

        public Module build() {
            Module result = new Module();
            result.setName(name);
            result.setType(type);
            result.setDescription(description);
            result.setProperties(ObjectUtils.defaultIfNull(properties, Collections.<String, Object> emptyMap()));
            result.setRequiredDependencies1(ObjectUtils.defaultIfNull(requiredDependencies1, Collections.<String> emptyList()));
            result.setProvidedDependencies1(ObjectUtils.defaultIfNull(providedDependencies1, Collections.<ProvidedDependency> emptyList()));
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

        public void setProperties(Map<String, Object> properties) {
            this.properties = properties;
        }

        public void setRequiredDependencies1(List<String> requiredDependencies) {
            this.requiredDependencies1 = requiredDependencies;
        }

        public void setProvidedDependencies1(List<ProvidedDependency> providedDependencies) {
            setProvidedDependencies(providedDependencies);
        }

        protected void setProvidedDependencies(List<? extends ProvidedDependency> providedDependencies) {
            this.providedDependencies1 = ListUtil.cast(providedDependencies);
        }

    }

}
