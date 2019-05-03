package com.sap.cloud.lm.sl.mta.model;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;

public class ExtensionProvidedDependency extends VersionedEntity
    implements VisitableElement, NamedElement, PropertiesContainer, ParametersContainer {

    private String name;
    private Map<String, Object> properties = Collections.emptyMap();
    private Map<String, Object> parameters = Collections.emptyMap();

    public ExtensionProvidedDependency(int majorSchemaVersion) {
        super(majorSchemaVersion);
    }

    public static ExtensionProvidedDependency createV2() {
        return new ExtensionProvidedDependency(2);
    }

    public static ExtensionProvidedDependency createV3() {
        return new ExtensionProvidedDependency(3);
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

    public ExtensionProvidedDependency setName(String name) {
        this.name = ObjectUtils.defaultIfNull(name, this.name);
        return this;
    }

    public ExtensionProvidedDependency setProperties(Map<String, Object> properties) {
        this.properties = ObjectUtils.defaultIfNull(properties, this.properties);
        return this;
    }

    public ExtensionProvidedDependency setParameters(Map<String, Object> parameters) {
        this.parameters = ObjectUtils.defaultIfNull(parameters, this.parameters);
        return this;
    }

    @Override
    public void accept(ElementContext context, Visitor visitor) {
        visitor.visit(context, this);
    }

}
