package com.sap.cloud.lm.sl.mta.model.v2;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.NamedElement;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;
import com.sap.cloud.lm.sl.mta.model.VisitableElement;
import com.sap.cloud.lm.sl.mta.model.Visitor;
import com.sap.cloud.lm.sl.mta.parsers.v2.ResourceParser;
import com.sap.cloud.lm.sl.mta.util.YamlElement;

public class Resource implements VisitableElement, NamedElement, PropertiesContainer, ParametersContainer {

    @YamlElement(ResourceParser.PARAMETERS)
    private Map<String, Object> parameters;
    @YamlElement(ResourceParser.NAME)
    private String name;
    @YamlElement(ResourceParser.TYPE)
    private String type;
    @YamlElement(ResourceParser.DESCRIPTION)
    private String description;
    @YamlElement(ResourceParser.GROUPS)
    private List<String> groups;
    @YamlElement(ResourceParser.PROPERTIES)
    private Map<String, Object> properties;

    protected Resource() {

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
 
    public List<String> getGroups() {
        throw new UnsupportedOperationException();
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

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGroups(List<String> groups) {
        throw new UnsupportedOperationException();
    }
    
    public Void setProperties(Map<String, Object> properties) {
        this.properties = new LinkedHashMap<>(properties);
        return null;
    }

    @Override
    public Void setParameters(Map<String, Object> parameters) {
        this.parameters = new LinkedHashMap<>(parameters);
        return null;
    }
    
    @Override
    public void accept(ElementContext context, Visitor visitor) {
        visitor.visit(context, this);
    }

    public Resource copyOf() {
        Builder result = new Builder();
        result.setName(getName());
        result.setType(getType());
        result.setDescription(getDescription());
        result.setProperties(getProperties());
        result.setParameters(getParameters());
        return result.build();
    }

    public static class Builder {

        protected String name;
        protected String type;
        protected String description;
        protected List<String> groups;
        protected Map<String, Object> properties;
        protected Map<String, Object> parameters;

        public Resource build() {
            Resource result = new Resource();
            result.setName(name);
            result.setType(type);
            result.setDescription(description);
            result.setProperties(ObjectUtils.defaultIfNull(properties, Collections.<String, Object> emptyMap()));
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
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

        public void setGroups(List<String> groups) {
            throw new UnsupportedOperationException();
        }
        
        public void setProperties(Map<String, Object> properties) {
            this.properties = properties;
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }
        
    }

}
