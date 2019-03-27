package com.sap.cloud.lm.sl.mta.model.v2;

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
import com.sap.cloud.lm.sl.mta.parsers.v2.ExtensionModuleParser;
import com.sap.cloud.lm.sl.mta.util.YamlElement;

public class ExtensionModule implements VisitableElement, NamedElement, PropertiesContainer, ParametersContainer {

    @YamlElement(ExtensionModuleParser.PARAMETERS)
    private Map<String, Object> parameters;
    @YamlElement(ExtensionModuleParser.REQUIRES)
    private List<ExtensionRequiredDependency> requiredDependencies2;
    @YamlElement(ExtensionModuleParser.PROVIDES)
    private List<ExtensionProvidedDependency> providedDependencies2;
    @YamlElement(ExtensionModuleParser.NAME)
    private String name;
    @YamlElement(ExtensionModuleParser.PROPERTIES)
    private Map<String, Object> properties;

    protected ExtensionModule() {

    }
    
    public String getName() {
        return name;
    }
    
    public Map<String, Object> getProperties() {
        return MapUtil.unmodifiable(properties);
    }
    
    @Override
    public Map<String, Object> getParameters() {
        return MapUtil.unmodifiable(parameters);
    }

    public List<ExtensionRequiredDependency> getRequiredDependencies2() {
        return ListUtil.upcastUnmodifiable(getRequiredDependencies());
    }

    protected List<? extends ExtensionRequiredDependency> getRequiredDependencies() {
        return requiredDependencies2;
    }

    public List<ExtensionProvidedDependency> getProvidedDependencies2() {
        return ListUtil.upcastUnmodifiable(getProvidedDependencies());
    }

    protected List<? extends ExtensionProvidedDependency> getProvidedDependencies() {
        return providedDependencies2;
    }
    
    public void setName(String name) {
        this.name = name;
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

    public void setRequiredDependencies2(List<ExtensionRequiredDependency> requiredDependencies) {
        setRequiredDependencies(requiredDependencies);
    }

    protected void setRequiredDependencies(
        List<? extends ExtensionRequiredDependency> requiredDependencies) {
        this.requiredDependencies2 = ListUtil.cast(requiredDependencies);
    }

    public void setProvidedDependencies2(List<ExtensionProvidedDependency> providedDependencies) {
        setProvidedDependencies(providedDependencies);
    }

    protected void setProvidedDependencies(
        List<? extends ExtensionProvidedDependency> providedDependencies) {
        this.providedDependencies2 = ListUtil.cast(providedDependencies);
    }

    @Override
    public void accept(ElementContext context, Visitor visitor) {
        visitor.visit(context, this);
        for (ExtensionProvidedDependency providedDependency : getProvidedDependencies()) {
            providedDependency.accept(new ElementContext(providedDependency, context), visitor);
        }
        
        for (ExtensionRequiredDependency requiredDependency : getRequiredDependencies()) {
            requiredDependency.accept(new ElementContext(requiredDependency, context), visitor);
        }
    }

    public static class Builder {

        protected String name;
        protected Map<String, Object> properties;
        protected Map<String, Object> parameters;
        protected List<ExtensionRequiredDependency> requiredDependencies2;
        protected List<ExtensionProvidedDependency> providedDependencies2;

        public ExtensionModule build() {
            ExtensionModule result = new ExtensionModule();
            result.setName(name);
            result.setProvidedDependencies2(ObjectUtils.defaultIfNull(providedDependencies2, Collections.<ExtensionProvidedDependency> emptyList()));
            result.setRequiredDependencies2(ObjectUtils.defaultIfNull(requiredDependencies2, Collections.<ExtensionRequiredDependency> emptyList()));
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

        public void setProvidedDependencies2(List<ExtensionProvidedDependency> providedDependencies) {
            setProvidedDependencies(providedDependencies);
        }

        protected void setProvidedDependencies(
            List<? extends ExtensionProvidedDependency> providedDependencies) {
            this.providedDependencies2 = ListUtil.cast(providedDependencies);
        }

        public void setRequiredDependencies2(List<ExtensionRequiredDependency> requiredDependencies) {
            setRequiredDependencies(requiredDependencies);
        }

        protected void setRequiredDependencies(
            List<? extends ExtensionRequiredDependency> requiredDependencies) {
            this.requiredDependencies2 = ListUtil.cast(requiredDependencies);
        }

    }

}
