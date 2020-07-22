package com.sap.cloud.lm.sl.mta.model;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;

public class ModuleType implements VisitableElement, NamedElement, PropertiesContainer, ParametersContainer {

    private String name;
    private Map<String, Object> properties = Collections.emptyMap();
    private Map<String, Object> parameters = Collections.emptyMap();

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, Object> getProperties() {
        return properties;
    }

    @Override
    public Map<String, Object> getParameters() {
        return parameters;
    }

    public ModuleType setName(String name) {
        this.name = ObjectUtils.defaultIfNull(name, this.name);
        return this;
    }

    @Override
    public ModuleType setProperties(Map<String, Object> properties) {
        this.properties = ObjectUtils.defaultIfNull(properties, this.properties);
        return this;
    }

    @Override
    public ModuleType setParameters(Map<String, Object> parameters) {
        this.parameters = ObjectUtils.defaultIfNull(parameters, this.parameters);
        return this;
    }

    @Override
    public void accept(ElementContext context, Visitor visitor) {
        visitor.visit(context, this);
    }

}
