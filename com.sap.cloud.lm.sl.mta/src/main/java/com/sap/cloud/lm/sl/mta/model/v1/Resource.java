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
import com.sap.cloud.lm.sl.mta.parsers.v1.ResourceParser;
import com.sap.cloud.lm.sl.mta.util.YamlElement;

public class Resource implements VisitableElement, NamedElement, PropertiesContainer {

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
        return ListUtil.unmodifiable(groups);
    }

    @Override
    public Map<String, Object> getProperties() {
        return MapUtil.unmodifiable(properties);
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
        this.groups = new ArrayList<>(groups);
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = new LinkedHashMap<>(properties);
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
        result.setGroups(getGroups());
        return result.build();
    }

    public static class Builder {

        protected String name;
        protected String type;
        protected String description;
        protected List<String> groups;
        protected Map<String, Object> properties;

        public Resource build() {
            Resource result = new Resource();
            result.setName(name);
            result.setType(type);
            result.setDescription(description);
            result.setGroups(ObjectUtils.defaultIfNull(groups, Collections.<String> emptyList()));
            result.setProperties(ObjectUtils.defaultIfNull(properties, Collections.<String, Object> emptyMap()));
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
            this.groups = groups;
        }

        public void setProperties(Map<String, Object> properties) {
            this.properties = properties;
        }

    }

}
