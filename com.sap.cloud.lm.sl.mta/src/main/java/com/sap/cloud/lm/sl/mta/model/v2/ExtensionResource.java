package com.sap.cloud.lm.sl.mta.model.v2;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.NamedElement;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;
import com.sap.cloud.lm.sl.mta.model.VisitableElement;
import com.sap.cloud.lm.sl.mta.model.Visitor;
import com.sap.cloud.lm.sl.mta.parsers.v2.ExtensionResourceParser;
import com.sap.cloud.lm.sl.mta.util.YamlElement;

public class ExtensionResource implements VisitableElement, NamedElement, PropertiesContainer, ParametersContainer {

    @YamlElement(ExtensionResourceParser.PARAMETERS)
    private Map<String, Object> parameters;
    @YamlElement(ExtensionResourceParser.NAME)
    private String name;
    @YamlElement(ExtensionResourceParser.PROPERTIES)
    private Map<String, Object> properties;

    protected ExtensionResource() {

    }

    public String getName() {
        return name;
    }

    @Override
    public Map<String, Object> getProperties() {
        return MapUtil.unmodifiable(properties);
    }

    @Override
    public Map<String, Object> getParameters() {
        return MapUtil.unmodifiable(parameters);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = new LinkedHashMap<>(properties);
    }

    @Override
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = new LinkedHashMap<>(parameters);
    }

    @Override
    public void accept(ElementContext context, Visitor visitor) {
        visitor.visit(context, this);
    }

    public static class Builder {

        protected String name;
        protected Map<String, Object> properties;
        protected Map<String, Object> parameters;

        public ExtensionResource build() {
            ExtensionResource result = new ExtensionResource();
            result.setName(name);
            result.setProperties(ObjectUtils.defaultIfNull(properties, Collections.<String, Object> emptyMap()));
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            return result;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setProperties(Map<String, Object> properties) {
            this.properties = properties;
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }

    }

}
