package com.sap.cloud.lm.sl.mta.model;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.common.util.yaml.YamlElement;
import com.sap.cloud.lm.sl.mta.parsers.v3.ProvidedDependencyParser;

public class ProvidedDependency extends VersionedEntity
    implements VisitableElement, NamedElement, PropertiesWithMetadataContainer, ParametersWithMetadataContainer {

    @YamlElement(ProvidedDependencyParser.NAME)
    private String name;
    @YamlElement(ProvidedDependencyParser.PUBLIC)
    private boolean isPublic;
    @YamlElement(ProvidedDependencyParser.PROPERTIES)
    private Map<String, Object> properties = Collections.emptyMap();
    @YamlElement(ProvidedDependencyParser.PARAMETERS)
    private Map<String, Object> parameters = Collections.emptyMap();
    @YamlElement(ProvidedDependencyParser.PROPERTIES_METADATA)
    private Metadata propertiesMetadata = Metadata.DEFAULT_METADATA;
    @YamlElement(ProvidedDependencyParser.PARAMETERS_METADATA)
    private Metadata parametersMetadata = Metadata.DEFAULT_METADATA;

    // Required by Jackson.
    protected ProvidedDependency() {
        super(0);
    }

    protected ProvidedDependency(int majorSchemaVersion) {
        super(majorSchemaVersion);
    }

    public static ProvidedDependency createV2() {
        return new ProvidedDependency(2).setPublic(true);
    }

    public static ProvidedDependency createV3() {
        return new ProvidedDependency(3);
    }

    public static ProvidedDependency copyOf(ProvidedDependency original) {
        ProvidedDependency copy = new ProvidedDependency(original.majorSchemaVersion);
        copy.name = original.name;
        copy.isPublic = original.isPublic;
        copy.properties = new TreeMap<>(original.properties);
        copy.parameters = new TreeMap<>(original.parameters);
        copy.propertiesMetadata = original.propertiesMetadata;
        copy.parametersMetadata = original.parametersMetadata;
        return copy;
    }

    public String getName() {
        return name;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public ProvidedDependency setName(String name) {
        this.name = ObjectUtils.defaultIfNull(name, this.name);
        return this;
    }

    public ProvidedDependency setPublic(Boolean isPublic) {
        this.isPublic = ObjectUtils.defaultIfNull(isPublic, this.isPublic);
        return this;
    }

    public ProvidedDependency setProperties(Map<String, Object> properties) {
        this.properties = ObjectUtils.defaultIfNull(properties, this.properties);
        return this;
    }

    public ProvidedDependency setParameters(Map<String, Object> parameters) {
        this.parameters = ObjectUtils.defaultIfNull(parameters, this.parameters);
        return this;
    }

    public Metadata getPropertiesMetadata() {
        supportedSince(3);
        return propertiesMetadata;
    }

    public Metadata getParametersMetadata() {
        supportedSince(3);
        return parametersMetadata;
    }

    public ProvidedDependency setPropertiesMetadata(Metadata propertiesMetadata) {
        supportedSince(3);
        this.propertiesMetadata = ObjectUtils.defaultIfNull(propertiesMetadata, this.propertiesMetadata);
        return this;
    }

    public ProvidedDependency setParametersMetadata(Metadata parametersMetadata) {
        supportedSince(3);
        this.parametersMetadata = ObjectUtils.defaultIfNull(parametersMetadata, this.parametersMetadata);
        return this;
    }

    @Override
    public void accept(ElementContext context, Visitor visitor) {
        visitor.visit(context, this);
    }

}
