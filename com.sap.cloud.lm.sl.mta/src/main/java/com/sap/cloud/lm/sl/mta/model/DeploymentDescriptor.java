package com.sap.cloud.lm.sl.mta.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.common.util.yaml.YamlAdapter;
import com.sap.cloud.lm.sl.common.util.yaml.YamlElement;
import com.sap.cloud.lm.sl.common.util.yaml.YamlElementOrder;
import com.sap.cloud.lm.sl.mta.parsers.v3.DeploymentDescriptorParser;
import com.sap.cloud.lm.sl.mta.util.MetadataConverter;

@YamlElementOrder({ "schemaVersion", "id", "version", "parameters", "modules", "resources" })
public class DeploymentDescriptor extends VersionedEntity implements Descriptor, VisitableElement, ParametersWithMetadataContainer {

    @YamlElement(DeploymentDescriptorParser.SCHEMA_VERSION)
    private String schemaVersion;
    @YamlElement(DeploymentDescriptorParser.ID)
    private String id;
    @YamlElement(DeploymentDescriptorParser.VERSION)
    private String version;
    @YamlElement(DeploymentDescriptorParser.MODULES)
    private List<Module> modules = Collections.emptyList();
    @YamlElement(DeploymentDescriptorParser.RESOURCES)
    private List<Resource> resources = Collections.emptyList();
    @YamlElement(DeploymentDescriptorParser.PARAMETERS)
    private Map<String, Object> parameters = Collections.emptyMap();
    @YamlElement(DeploymentDescriptorParser.PARAMETERS_METADATA)
    @YamlAdapter(MetadataConverter.class)
    private Metadata parametersMetadata = Metadata.DEFAULT_METADATA;

    // Required by Jackson.
    protected DeploymentDescriptor() {
        super(0);
    }

    protected DeploymentDescriptor(int majorSchemaVersion) {
        super(majorSchemaVersion);
    }

    public static DeploymentDescriptor createV2() {
        return new DeploymentDescriptor(2);
    }

    public static DeploymentDescriptor createV3() {
        return new DeploymentDescriptor(3);
    }

    public static DeploymentDescriptor copyOf(DeploymentDescriptor original) {
        DeploymentDescriptor copy = new DeploymentDescriptor(original.majorSchemaVersion);
        copy.schemaVersion = original.schemaVersion;
        copy.id = original.id;
        copy.version = original.version;
        copy.modules = copyModules(original.modules);
        copy.resources = copyResources(original.resources);
        copy.parameters = new TreeMap<>(original.parameters);
        copy.parametersMetadata = original.parametersMetadata;
        return copy;
    }

    private static List<Module> copyModules(List<Module> originals) {
        return originals.stream()
            .map(Module::copyOf)
            .collect(Collectors.toList());
    }

    private static List<Resource> copyResources(List<Resource> originals) {
        return originals.stream()
            .map(Resource::copyOf)
            .collect(Collectors.toList());
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public String getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public List<Module> getModules() {
        return modules;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public Metadata getParametersMetadata() {
        supportedSince(3);
        return parametersMetadata;
    }

    public DeploymentDescriptor setSchemaVersion(String schemaVersion) {
        this.schemaVersion = ObjectUtils.defaultIfNull(schemaVersion, this.schemaVersion);
        return this;
    }

    public DeploymentDescriptor setId(String id) {
        this.id = ObjectUtils.defaultIfNull(id, this.id);
        return this;
    }

    public DeploymentDescriptor setVersion(String version) {
        this.version = ObjectUtils.defaultIfNull(version, this.version);
        return this;
    }

    public DeploymentDescriptor setModules(List<Module> modules) {
        this.modules = ObjectUtils.defaultIfNull(modules, this.modules);
        return this;
    }

    public DeploymentDescriptor setResources(List<Resource> resources) {
        this.resources = ObjectUtils.defaultIfNull(resources, this.resources);
        return this;
    }

    public DeploymentDescriptor setParameters(Map<String, Object> parameters) {
        this.parameters = ObjectUtils.defaultIfNull(parameters, this.parameters);
        return this;
    }

    public DeploymentDescriptor setParametersMetadata(Metadata parametersMetadata) {
        supportedSince(3);
        this.parametersMetadata = ObjectUtils.defaultIfNull(parametersMetadata, this.parametersMetadata);
        return this;
    }

    public void accept(Visitor visitor) {
        accept(new ElementContext(this, null), visitor);
    }

    @Override
    public void accept(ElementContext context, Visitor visitor) {
        visitor.visit(context, this);
        for (Module module : modules) {
            module.accept(new ElementContext(module, context), visitor);
        }
        for (Resource resource : resources) {
            resource.accept(new ElementContext(resource, context), visitor);
        }
    }

}
