package org.cloudfoundry.multiapps.mta.model;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.cloudfoundry.multiapps.common.util.yaml.YamlElement;
import org.cloudfoundry.multiapps.mta.parsers.v2.ExtensionRequiredDependencyParser;

public class ExtensionRequiredDependency extends VersionedEntity implements VisitableElement, PropertiesContainer, ParametersContainer {

    @YamlElement(ExtensionRequiredDependencyParser.NAME)
    private String name;
    @YamlElement(ExtensionRequiredDependencyParser.PROPERTIES)
    private Map<String, Object> properties = Collections.emptyMap();
    @YamlElement(ExtensionRequiredDependencyParser.PARAMETERS)
    private Map<String, Object> parameters = Collections.emptyMap();

    // Required by Jackson.
    protected ExtensionRequiredDependency() {
        super(0);
    }

    protected ExtensionRequiredDependency(int majorSchemaVersion) {
        super(majorSchemaVersion);
    }

    public static ExtensionRequiredDependency createV2() {
        return new ExtensionRequiredDependency(2);
    }

    public static ExtensionRequiredDependency createV3() {
        return new ExtensionRequiredDependency(3);
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

    public ExtensionRequiredDependency setName(String name) {
        this.name = ObjectUtils.defaultIfNull(name, this.name);
        return this;
    }

    public ExtensionRequiredDependency setProperties(Map<String, Object> properties) {
        this.properties = ObjectUtils.defaultIfNull(properties, this.properties);
        return this;
    }

    public ExtensionRequiredDependency setParameters(Map<String, Object> parameters) {
        this.parameters = ObjectUtils.defaultIfNull(parameters, this.parameters);
        return this;
    }

    @Override
    public void accept(ElementContext context, Visitor visitor) {
        visitor.visit(context, this);
    }

}
