package com.sap.cloud.lm.sl.mta.model.v2_0;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.builders.Builder;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.NamedElement;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;
import com.sap.cloud.lm.sl.mta.model.VisitableElement;
import com.sap.cloud.lm.sl.mta.model.Visitor;
import com.sap.cloud.lm.sl.mta.parsers.v2_0.RequiredDependencyParser;
import com.sap.cloud.lm.sl.mta.util.YamlElement;

public class RequiredDependency implements VisitableElement, NamedElement, PropertiesContainer, ParametersContainer {

    @YamlElement(RequiredDependencyParser.NAME)
    private String name;
    @YamlElement(RequiredDependencyParser.GROUP)
    private String group;
    @YamlElement(RequiredDependencyParser.LIST)
    private String list;
    @YamlElement(RequiredDependencyParser.PROPERTIES)
    private Map<String, Object> properties;
    @YamlElement(RequiredDependencyParser.PARAMETERS)
    private Map<String, Object> parameters;

    protected RequiredDependency() {

    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public String getList() {
        return list;
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

    public void setGroup(String group) {
        this.group = group;
    }

    public void setList(String list) {
        this.list = list;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = new LinkedHashMap<>(properties);
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = new LinkedHashMap<>(parameters);
    }

    @Override
    public void accept(ElementContext context, Visitor visitor) {
        visitor.visit(context, this);
    }

    public static class RequiredDependencyBuilder implements Builder<RequiredDependency> {

        protected String name;
        protected String group;
        protected String list;
        protected Map<String, Object> properties;
        protected Map<String, Object> parameters;

        @Override
        public RequiredDependency build() {
            RequiredDependency result = new RequiredDependency();
            result.setName(name);
            result.setGroup(group);
            result.setList(list);
            result.setProperties(getOrDefault(properties, Collections.<String, Object> emptyMap()));
            result.setParameters(getOrDefault(parameters, Collections.<String, Object> emptyMap()));
            return result;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public void setList(String list) {
            this.list = list;
        }

        public void setProperties(Map<String, Object> properties) {
            this.properties = properties;
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }

    }

    public RequiredDependency copyOf() {
        RequiredDependencyBuilder result = new RequiredDependencyBuilder();
        result.setName(getName());
        result.setGroup(getGroup());
        result.setList(getList());
        result.setProperties(getProperties());
        result.setParameters(getParameters());
        return result.build();
    }

}
