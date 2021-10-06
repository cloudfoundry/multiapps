package org.cloudfoundry.multiapps.mta.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.cloudfoundry.multiapps.common.util.yaml.YamlAdapter;
import org.cloudfoundry.multiapps.common.util.yaml.YamlElement;
import org.cloudfoundry.multiapps.mta.parsers.v3.ResourceParser;
import org.cloudfoundry.multiapps.mta.util.MetadataConverter;

public class Resource extends VersionedEntity
    implements VisitableElement, NamedParametersContainer, NamedElement, PropertiesWithMetadataContainer, ParametersWithMetadataContainer {

    @YamlElement(ResourceParser.NAME)
    private String name;
    @YamlElement(ResourceParser.TYPE)
    private String type;
    @YamlElement(ResourceParser.DESCRIPTION)
    private String description;
    @YamlElement(ResourceParser.PROPERTIES)
    private Map<String, Object> properties = Collections.emptyMap();
    @YamlElement(ResourceParser.PARAMETERS)
    private Map<String, Object> parameters = Collections.emptyMap();
    @YamlElement(ResourceParser.ACTIVE)
    private boolean isActive = true;
    @YamlElement(ResourceParser.OPTIONAL)
    private boolean isOptional;
    @YamlElement(ResourceParser.PROPERTIES_METADATA)
    @YamlAdapter(MetadataConverter.class)
    private Metadata propertiesMetadata = Metadata.DEFAULT_METADATA;
    @YamlElement(ResourceParser.PARAMETERS_METADATA)
    @YamlAdapter(MetadataConverter.class)
    private Metadata parametersMetadata = Metadata.DEFAULT_METADATA;
    @YamlElement(ResourceParser.REQUIRES)
    private List<RequiredDependency> requiredDependencies = Collections.emptyList();
    @YamlElement(ResourceParser.PROCESSED_AFTER)
    private List<String> processedAfter;

    // Required by Jackson.
    protected Resource() {
        super(0);
    }

    protected Resource(int majorSchemaVersion) {
        super(majorSchemaVersion);
    }

    public static Resource createV2() {
        return new Resource(2);
    }

    public static Resource createV3() {
        return new Resource(3);
    }

    public static Resource copyOf(Resource original) {
        Resource copy = new Resource(original.majorSchemaVersion);
        copy.name = original.name;
        copy.type = original.type;
        copy.description = original.description;
        copy.properties = new TreeMap<>(original.properties);
        copy.parameters = new TreeMap<>(original.parameters);
        copy.isActive = original.isActive;
        copy.isOptional = original.isOptional;
        copy.propertiesMetadata = original.propertiesMetadata;
        copy.parametersMetadata = original.parametersMetadata;
        copy.requiredDependencies = copyRequiredDependencies(original.requiredDependencies);
        return copy;
    }

    private static List<RequiredDependency> copyRequiredDependencies(List<RequiredDependency> originals) {
        return originals.stream()
                        .map(RequiredDependency::copyOf)
                        .collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
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

    public List<String> getProcessedAfter() {
        return processedAfter;
    }

    public boolean isActive() {
        supportedSince(3);
        return isActive;
    }

    public boolean isOptional() {
        supportedSince(3);
        return isOptional;
    }

    public Metadata getPropertiesMetadata() {
        supportedSince(3);
        return propertiesMetadata;
    }

    public Metadata getParametersMetadata() {
        supportedSince(3);
        return parametersMetadata;
    }

    public List<RequiredDependency> getRequiredDependencies() {
        supportedSince(3);
        return requiredDependencies;
    }

    public Resource setName(String name) {
        this.name = ObjectUtils.defaultIfNull(name, this.name);
        return this;
    }

    public Resource setType(String type) {
        this.type = ObjectUtils.defaultIfNull(type, this.type);
        return this;
    }

    public Resource setDescription(String description) {
        this.description = ObjectUtils.defaultIfNull(description, this.description);
        return this;
    }

    public Resource setProperties(Map<String, Object> properties) {
        this.properties = ObjectUtils.defaultIfNull(properties, this.properties);
        return this;
    }

    public Resource setParameters(Map<String, Object> parameters) {
        this.parameters = ObjectUtils.defaultIfNull(parameters, this.parameters);
        return this;
    }

    public Resource setProcessAfter(List<String> processedAfter) {
        this.processedAfter = ObjectUtils.defaultIfNull(processedAfter, this.processedAfter);
        return this;
    }

    public Resource setActive(Boolean isActive) {
        supportedSince(3);
        this.isActive = ObjectUtils.defaultIfNull(isActive, this.isActive);
        return this;
    }

    public Resource setOptional(Boolean isOptional) {
        supportedSince(3);
        this.isOptional = ObjectUtils.defaultIfNull(isOptional, this.isOptional);
        return this;
    }

    public Resource setPropertiesMetadata(Metadata propertiesMetadata) {
        supportedSince(3);
        this.propertiesMetadata = ObjectUtils.defaultIfNull(propertiesMetadata, this.propertiesMetadata);
        return this;
    }

    public Resource setParametersMetadata(Metadata parametersMetadata) {
        supportedSince(3);
        this.parametersMetadata = ObjectUtils.defaultIfNull(parametersMetadata, this.parametersMetadata);
        return this;
    }

    public Resource setRequiredDependencies(List<RequiredDependency> requiredDependencies) {
        supportedSince(3);
        this.requiredDependencies = ObjectUtils.defaultIfNull(requiredDependencies, this.requiredDependencies);
        return this;
    }

    @Override
    public void accept(ElementContext context, Visitor visitor) {
        visitor.visit(context, this);
        if (majorSchemaVersion > 3) {
            for (RequiredDependency requiredDependency : requiredDependencies) {
                visitor.visit(new ElementContext(this, context), requiredDependency);
            }
        }
    }

}
