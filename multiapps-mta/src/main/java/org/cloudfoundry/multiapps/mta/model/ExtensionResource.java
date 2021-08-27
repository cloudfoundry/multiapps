package org.cloudfoundry.multiapps.mta.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.cloudfoundry.multiapps.common.util.yaml.YamlElement;
import org.cloudfoundry.multiapps.mta.parsers.v3.ExtensionResourceParser;

public class ExtensionResource extends VersionedEntity implements VisitableElement, NamedElement, PropertiesContainer, ParametersContainer {

    @YamlElement(ExtensionResourceParser.NAME)
    private String name;
    @YamlElement(ExtensionResourceParser.ACTIVE)
    private Boolean isActive;
    @YamlElement(ExtensionResourceParser.OPTIONAL)
    private Boolean isOptional;
    @YamlElement(ExtensionResourceParser.PROPERTIES)
    private Map<String, Object> properties = Collections.emptyMap();
    @YamlElement(ExtensionResourceParser.PARAMETERS)
    private Map<String, Object> parameters = Collections.emptyMap();
    @YamlElement(ExtensionResourceParser.REQUIRES)
    private List<ExtensionRequiredDependency> requiredDependencies = Collections.emptyList();

    // Required by Jackson.
    protected ExtensionResource() {
        super(0);
    }

    protected ExtensionResource(int majorSchemaVersion) {
        super(majorSchemaVersion);
    }

    public static ExtensionResource createV2() {
        return new ExtensionResource(2);
    }

    public static ExtensionResource createV3() {
        return new ExtensionResource(3);
    }

    public String getName() {
        return name;
    }

    public Boolean isActive() {
        supportedSince(3);
        return isActive;
    }

    public Boolean isOptional() {
        supportedSince(3);
        return isOptional;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public List<ExtensionRequiredDependency> getRequiredDependencies() {
        supportedSince(3);
        return requiredDependencies;
    }

    public ExtensionResource setName(String name) {
        this.name = ObjectUtils.defaultIfNull(name, this.name);
        return this;
    }

    public ExtensionResource setActive(Boolean isActive) {
        supportedSince(3);
        this.isActive = ObjectUtils.defaultIfNull(isActive, this.isActive);
        return this;
    }

    public ExtensionResource setOptional(Boolean isOptional) {
        supportedSince(3);
        this.isOptional = ObjectUtils.defaultIfNull(isOptional, this.isOptional);
        return this;
    }

    public ExtensionResource setProperties(Map<String, Object> properties) {
        this.properties = ObjectUtils.defaultIfNull(properties, this.properties);
        return this;
    }

    public ExtensionResource setParameters(Map<String, Object> parameters) {
        this.parameters = ObjectUtils.defaultIfNull(parameters, this.parameters);
        return this;
    }

    public ExtensionResource setRequiredDependencies(List<ExtensionRequiredDependency> requiredDependencies) {
        supportedSince(3);
        this.requiredDependencies = ObjectUtils.defaultIfNull(requiredDependencies, this.requiredDependencies);
        return this;
    }

    @Override
    public void accept(ElementContext context, Visitor visitor) {
        visitor.visit(context, this);
        if (majorSchemaVersion > 3) {
            for (ExtensionRequiredDependency requiredDependency : requiredDependencies) {
                visitor.visit(new ElementContext(this, context), requiredDependency);
            }
        }
    }

}
