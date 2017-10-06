package com.sap.cloud.lm.sl.mta.model.v1_0;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.builders.Builder;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.NamedElement;
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;
import com.sap.cloud.lm.sl.mta.model.VisitableElement;
import com.sap.cloud.lm.sl.mta.model.Visitor;
import com.sap.cloud.lm.sl.mta.parsers.v1_0.ExtensionResourceParser;
import com.sap.cloud.lm.sl.mta.util.YamlElement;

public class ExtensionResource implements VisitableElement, NamedElement, PropertiesContainer {

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

    public void setName(String name) {
        this.name = name;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = new LinkedHashMap<>(properties);
    }

    @Override
    public void accept(ElementContext context, Visitor visitor) {
        visitor.visit(context, this);
    }

    public static class ExtensionResourceBuilder implements Builder<ExtensionResource> {

        protected String name;
        protected Map<String, Object> properties;

        @Override
        public ExtensionResource build() {
            ExtensionResource result = new ExtensionResource();
            result.setName(name);
            result.setProperties(getOrDefault(properties, Collections.<String, Object> emptyMap()));
            return result;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setProperties(Map<String, Object> properties) {
            this.properties = properties;
        }

    }

}
