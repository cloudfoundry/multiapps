package com.sap.cloud.lm.sl.mta.model.v1_0;

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
import com.sap.cloud.lm.sl.mta.parsers.v1_0.ExtensionModuleParser;
import com.sap.cloud.lm.sl.mta.util.YamlElement;

public class ExtensionModule implements VisitableElement, NamedElement, PropertiesContainer {

    @YamlElement(ExtensionModuleParser.NAME)
    private String name;
    @YamlElement(ExtensionModuleParser.PROPERTIES)
    private Map<String, Object> properties;
    @YamlElement(ExtensionModuleParser.PROVIDES)
    private List<ExtensionProvidedDependency> providedDependencies1_0;

    protected ExtensionModule() {

    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getProperties() {
        return MapUtil.unmodifiable(properties);
    }

    public List<ExtensionProvidedDependency> getProvidedDependencies1_0() {
        return ListUtil.upcastUnmodifiable(getProvidedDependencies());
    }

    protected List<? extends ExtensionProvidedDependency> getProvidedDependencies() {
        return providedDependencies1_0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = new LinkedHashMap<>(properties);
    }

    public void setProvidedDependencies1_0(List<ExtensionProvidedDependency> providedDependencies) {
        setProvidedDependencies(providedDependencies);
    }

    protected void setProvidedDependencies(List<? extends ExtensionProvidedDependency> providedDependencies) {
        this.providedDependencies1_0 = ListUtil.cast(providedDependencies);
    }

    @Override
    public void accept(ElementContext context, Visitor visitor) {
        visitor.visit(context, this);
        for (ExtensionProvidedDependency providedDependency : getProvidedDependencies()) {
            providedDependency.accept(new ElementContext(providedDependency, context), visitor);
        }
    }

    public static class Builder {

        protected String name;
        protected Map<String, Object> properties;
        protected List<ExtensionProvidedDependency> providedDependencies1_0;

        public ExtensionModule build() {
            ExtensionModule result = new ExtensionModule();
            result.setName(name);
            result.setProperties(ObjectUtils.defaultIfNull(properties, Collections.<String, Object> emptyMap()));
            result.setProvidedDependencies1_0(ObjectUtils.defaultIfNull(providedDependencies1_0, Collections.<ExtensionProvidedDependency> emptyList()));
            return result;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setProperties(Map<String, Object> properties) {
            this.properties = properties;
        }

        public void setProvidedDependencies1_0(List<ExtensionProvidedDependency> providedDependencies) {
            setProvidedDependencies(providedDependencies);
        }

        protected void setProvidedDependencies(List<? extends ExtensionProvidedDependency> providedDependencies) {
            this.providedDependencies1_0 = ListUtil.cast(providedDependencies);
        }

    }

}
