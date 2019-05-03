package com.sap.cloud.lm.sl.mta.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.common.util.yaml.YamlElement;
import com.sap.cloud.lm.sl.mta.parsers.v2.ExtensionModuleParser;

public class ExtensionModule extends VersionedEntity implements VisitableElement, NamedElement, PropertiesContainer, ParametersContainer {

    @YamlElement(ExtensionModuleParser.NAME)
    private String name;
    @YamlElement(ExtensionModuleParser.PROPERTIES)
    private Map<String, Object> properties = Collections.emptyMap();
    @YamlElement(ExtensionModuleParser.PARAMETERS)
    private Map<String, Object> parameters = Collections.emptyMap();
    @YamlElement(ExtensionModuleParser.REQUIRES)
    private List<ExtensionRequiredDependency> requiredDependencies = Collections.emptyList();
    @YamlElement(ExtensionModuleParser.PROVIDES)
    private List<ExtensionProvidedDependency> providedDependencies = Collections.emptyList();

    public ExtensionModule(int majorSchemaVersion) {
        super(majorSchemaVersion);
    }

    public static ExtensionModule createV2() {
        return new ExtensionModule(2);
    }

    public static ExtensionModule createV3() {
        return new ExtensionModule(3);
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public List<ExtensionRequiredDependency> getRequiredDependencies() {
        return requiredDependencies;
    }

    public List<ExtensionProvidedDependency> getProvidedDependencies() {
        return providedDependencies;
    }

    public ExtensionModule setName(String name) {
        this.name = ObjectUtils.defaultIfNull(name, this.name);
        return this;
    }

    public ExtensionModule setProperties(Map<String, Object> properties) {
        this.properties = ObjectUtils.defaultIfNull(properties, this.properties);
        return this;
    }

    public ExtensionModule setParameters(Map<String, Object> parameters) {
        this.parameters = ObjectUtils.defaultIfNull(parameters, this.parameters);
        return this;
    }

    public ExtensionModule setRequiredDependencies(List<ExtensionRequiredDependency> requiredDependencies) {
        this.requiredDependencies = ObjectUtils.defaultIfNull(requiredDependencies, this.requiredDependencies);
        return this;
    }

    public ExtensionModule setProvidedDependencies(List<ExtensionProvidedDependency> providedDependencies) {
        this.providedDependencies = ObjectUtils.defaultIfNull(providedDependencies, this.providedDependencies);
        return this;
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

}
