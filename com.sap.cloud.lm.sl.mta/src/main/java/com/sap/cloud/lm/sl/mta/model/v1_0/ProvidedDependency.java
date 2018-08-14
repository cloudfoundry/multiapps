package com.sap.cloud.lm.sl.mta.model.v1_0;

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
import com.sap.cloud.lm.sl.mta.parsers.v1_0.ProvidedDependencyParser;
import com.sap.cloud.lm.sl.mta.util.YamlElement;

public class ProvidedDependency implements VisitableElement, NamedElement, PropertiesContainer {

    @YamlElement(ProvidedDependencyParser.NAME)
    private String name;
    @YamlElement(ProvidedDependencyParser.GROUPS)
    private List<String> groups;
    @YamlElement(ProvidedDependencyParser.PROPERTIES)
    private Map<String, Object> properties;

    protected ProvidedDependency() {

    }

    public String getName() {
        return name;
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

    public void setGroups(List<String> groups) {
        this.groups = new ArrayList<>(groups);
    }

    @Override
    public void setProperties(Map<String, Object> properties) {
        this.properties = new LinkedHashMap<>(properties);
    }

    @Override
    public void accept(ElementContext context, Visitor visitor) {
        visitor.visit(context, this);
    }

    public ProvidedDependency copyOf() {
        Builder result = new Builder();
        result.setName(getName());
        result.setGroups(getGroups());
        result.setProperties(getProperties());
        return result.build();
    }

    public static class Builder {

        protected String name;
        protected List<String> groups;
        protected Map<String, Object> properties;

        public ProvidedDependency build() {
            ProvidedDependency result = new ProvidedDependency();
            result.setName(name);
            result.setGroups(ObjectUtils.defaultIfNull(groups, Collections.<String> emptyList()));
            result.setProperties(ObjectUtils.defaultIfNull(properties, Collections.<String, Object> emptyMap()));
            return result;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setGroups(List<String> groups) {
            this.groups = groups;
        }

        public void setProperties(Map<String, Object> properties) {
            this.properties = properties;
        }

    }

}
