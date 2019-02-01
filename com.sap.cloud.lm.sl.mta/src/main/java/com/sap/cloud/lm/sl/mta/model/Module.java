package com.sap.cloud.lm.sl.mta.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;

import com.google.gson.annotations.JsonAdapter;
import com.sap.cloud.lm.sl.common.model.json.MapWithNumbersAdapterFactory;
import com.sap.cloud.lm.sl.mta.parsers.v3.ModuleParser;
import com.sap.cloud.lm.sl.mta.util.MetadataConverter;
import com.sap.cloud.lm.sl.common.util.yaml.YamlAdapter;
import com.sap.cloud.lm.sl.common.util.yaml.YamlElement;

public class Module extends VersionedEntity
    implements VisitableElement, NamedElement, PropertiesWithMetadataContainer, ParametersWithMetadataContainer {

    @YamlElement(ModuleParser.NAME)
    private String name;
    @YamlElement(ModuleParser.TYPE)
    private String type;
    @YamlElement(ModuleParser.PATH)
    private String path;
    @YamlElement(ModuleParser.DESCRIPTION)
    private String description;
    @YamlElement(ModuleParser.PROPERTIES)
    @JsonAdapter(MapWithNumbersAdapterFactory.class)
    private Map<String, Object> properties = Collections.emptyMap();
    @YamlElement(ModuleParser.PARAMETERS)
    @JsonAdapter(MapWithNumbersAdapterFactory.class)
    private Map<String, Object> parameters = Collections.emptyMap();
    @YamlElement(ModuleParser.REQUIRES)
    private List<RequiredDependency> requiredDependencies = Collections.emptyList();
    @YamlElement(ModuleParser.PROVIDES)
    private List<ProvidedDependency> providedDependencies = Collections.emptyList();
    @YamlElement(ModuleParser.PROPERTIES_METADATA)
    @YamlAdapter(MetadataConverter.class)
    private Metadata propertiesMetadata = Metadata.DEFAULT_METADATA;
    @YamlElement(ModuleParser.PARAMETERS_METADATA)
    @YamlAdapter(MetadataConverter.class)
    private Metadata parametersMetadata = Metadata.DEFAULT_METADATA;
    @YamlElement(ModuleParser.DEPLOYED_AFTER)
    private List<String> deployedAfter;

    protected Module(int majorSchemaVersion) {
        super(majorSchemaVersion);
    }

    public static Module copyOf(Module original) {
        Module copy = new Module(original.majorSchemaVersion);
        copy.name = original.name;
        copy.type = original.type;
        copy.path = original.path;
        copy.description = original.description;
        copy.properties = new TreeMap<>(original.properties);
        copy.parameters = new TreeMap<>(original.parameters);
        copy.requiredDependencies = copyRequiredDependencies(original.requiredDependencies);
        copy.providedDependencies = copyProvidedDependencies(original.providedDependencies);
        copy.propertiesMetadata = original.propertiesMetadata;
        copy.parametersMetadata = original.parametersMetadata;
        copy.deployedAfter = original.deployedAfter == null ? null : new ArrayList<>(original.deployedAfter);
        return copy;
    }

    private static List<RequiredDependency> copyRequiredDependencies(List<RequiredDependency> originals) {
        return originals.stream()
            .map(RequiredDependency::copyOf)
            .collect(Collectors.toList());
    }

    private static List<ProvidedDependency> copyProvidedDependencies(List<ProvidedDependency> originals) {
        return originals.stream()
            .map(ProvidedDependency::copyOf)
            .collect(Collectors.toList());
    }

    public static Module createV2() {
        return new Module(2);
    }

    public static Module createV3() {
        return new Module(3);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getPath() {
        return path;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public List<RequiredDependency> getRequiredDependencies() {
        return requiredDependencies;
    }

    public List<ProvidedDependency> getProvidedDependencies() {
        return providedDependencies;
    }

    public Metadata getPropertiesMetadata() {
        supportedSince(3);
        return propertiesMetadata;
    }

    public Metadata getParametersMetadata() {
        supportedSince(3);
        return parametersMetadata;
    }

    public List<String> getDeployedAfter() {
        supportedSince(3);
        return deployedAfter;
    }

    public Module setName(String name) {
        this.name = ObjectUtils.defaultIfNull(name, this.name);
        return this;
    }

    public Module setType(String type) {
        this.type = ObjectUtils.defaultIfNull(type, this.type);
        return this;
    }

    public Module setPath(String path) {
        this.path = ObjectUtils.defaultIfNull(path, this.path);
        return this;
    }

    public Module setDescription(String description) {
        this.description = ObjectUtils.defaultIfNull(description, this.description);
        return this;
    }

    public Module setProperties(Map<String, Object> properties) {
        this.properties = ObjectUtils.defaultIfNull(properties, this.properties);
        return this;
    }

    public Module setParameters(Map<String, Object> parameters) {
        this.parameters = ObjectUtils.defaultIfNull(parameters, this.parameters);
        return this;
    }

    public Module setRequiredDependencies(List<RequiredDependency> requiredDependencies) {
        this.requiredDependencies = ObjectUtils.defaultIfNull(requiredDependencies, this.requiredDependencies);
        return this;
    }

    public Module setProvidedDependencies(List<ProvidedDependency> providedDependencies) {
        this.providedDependencies = ObjectUtils.defaultIfNull(providedDependencies, this.providedDependencies);
        return this;
    }

    public Module setPropertiesMetadata(Metadata propertiesMetadata) {
        supportedSince(3);
        this.propertiesMetadata = ObjectUtils.defaultIfNull(propertiesMetadata, this.propertiesMetadata);
        return this;
    }

    public Module setParametersMetadata(Metadata parametersMetadata) {
        supportedSince(3);
        this.parametersMetadata = ObjectUtils.defaultIfNull(parametersMetadata, this.parametersMetadata);
        return this;
    }

    public Module setDeployedAfter(List<String> deployedAfter) {
        supportedSince(3);
        this.deployedAfter = ObjectUtils.defaultIfNull(deployedAfter, this.deployedAfter);
        return this;
    }

    @Override
    public void accept(ElementContext context, Visitor visitor) {
        visitor.visit(context, this);
        for (ProvidedDependency providedDependency : providedDependencies) {
            providedDependency.accept(new ElementContext(providedDependency, context), visitor);
        }
        for (RequiredDependency requiredDependency : requiredDependencies) {
            requiredDependency.accept(new ElementContext(requiredDependency, context), visitor);
        }
    }

}
