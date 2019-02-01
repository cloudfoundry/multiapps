package com.sap.cloud.lm.sl.mta.model;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.ObjectUtils;

import com.google.gson.annotations.JsonAdapter;
import com.sap.cloud.lm.sl.common.model.json.MapWithNumbersAdapterFactory;
import com.sap.cloud.lm.sl.mta.parsers.v3.RequiredDependencyParser;
import com.sap.cloud.lm.sl.mta.util.MetadataConverter;
import com.sap.cloud.lm.sl.common.util.yaml.YamlAdapter;
import com.sap.cloud.lm.sl.common.util.yaml.YamlElement;

public class RequiredDependency extends VersionedEntity
    implements VisitableElement, NamedElement, PropertiesWithMetadataContainer, ParametersWithMetadataContainer {

    @YamlElement(RequiredDependencyParser.NAME)
    private String name;
    @YamlElement(RequiredDependencyParser.LIST)
    private String list;
    @YamlElement(RequiredDependencyParser.GROUP)
    private String group;
    @YamlElement(RequiredDependencyParser.PROPERTIES)
    @JsonAdapter(MapWithNumbersAdapterFactory.class)
    private Map<String, Object> properties = Collections.emptyMap();
    @YamlElement(RequiredDependencyParser.PARAMETERS)
    @JsonAdapter(MapWithNumbersAdapterFactory.class)
    private Map<String, Object> parameters = Collections.emptyMap();
    @YamlElement(RequiredDependencyParser.PROPERTIES_METADATA)
    @YamlAdapter(MetadataConverter.class)
    private Metadata propertiesMetadata = Metadata.DEFAULT_METADATA;
    @YamlElement(RequiredDependencyParser.PARAMETERS_METADATA)
    @YamlAdapter(MetadataConverter.class)
    private Metadata parametersMetadata = Metadata.DEFAULT_METADATA;

    protected RequiredDependency(int majorSchemaVersion) {
        super(majorSchemaVersion);
    }

    public static RequiredDependency createV2() {
        return new RequiredDependency(2);
    }

    public static RequiredDependency createV3() {
        return new RequiredDependency(3);
    }

    public static RequiredDependency copyOf(RequiredDependency original) {
        RequiredDependency copy = new RequiredDependency(original.majorSchemaVersion);
        copy.name = original.name;
        copy.list = original.list;
        copy.group = original.group;
        copy.properties = new TreeMap<>(original.properties);
        copy.parameters = new TreeMap<>(original.parameters);
        copy.propertiesMetadata = original.propertiesMetadata;
        copy.parametersMetadata = original.parametersMetadata;
        return copy;
    }

    public String getName() {
        return name;
    }

    public String getList() {
        return list;
    }

    public String getGroup() {
        return group;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public Metadata getPropertiesMetadata() {
        supportedSince(3);
        return propertiesMetadata;
    }

    public Metadata getParametersMetadata() {
        supportedSince(3);
        return parametersMetadata;
    }

    public RequiredDependency setName(String name) {
        this.name = ObjectUtils.defaultIfNull(name, this.name);
        return this;
    }

    public RequiredDependency setList(String list) {
        this.list = ObjectUtils.defaultIfNull(list, this.list);
        return this;
    }

    public RequiredDependency setGroup(String group) {
        this.group = ObjectUtils.defaultIfNull(group, this.group);
        return this;
    }

    public RequiredDependency setProperties(Map<String, Object> properties) {
        this.properties = ObjectUtils.defaultIfNull(properties, this.properties);
        return this;
    }

    public RequiredDependency setParameters(Map<String, Object> parameters) {
        this.parameters = ObjectUtils.defaultIfNull(parameters, this.parameters);
        return this;
    }

    public RequiredDependency setPropertiesMetadata(Metadata propertiesMetadata) {
        supportedSince(3);
        this.propertiesMetadata = ObjectUtils.defaultIfNull(propertiesMetadata, this.propertiesMetadata);
        return this;
    }

    public RequiredDependency setParametersMetadata(Metadata parametersMetadata) {
        supportedSince(3);
        this.parametersMetadata = ObjectUtils.defaultIfNull(parametersMetadata, this.parametersMetadata);
        return this;
    }

    @Override
    public void accept(ElementContext context, Visitor visitor) {
        visitor.visit(context, this);
    }

}
