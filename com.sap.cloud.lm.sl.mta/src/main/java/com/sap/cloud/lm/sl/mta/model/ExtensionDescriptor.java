package com.sap.cloud.lm.sl.mta.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.common.util.yaml.YamlElement;
import com.sap.cloud.lm.sl.common.util.yaml.YamlElementOrder;
import com.sap.cloud.lm.sl.mta.parsers.v2.ExtensionDescriptorParser;

@YamlElementOrder({ "schemaVersion", "id", "parentId", "parameters", "modules", "resources" })
public class ExtensionDescriptor extends VersionedEntity implements Descriptor, VisitableElement, ParametersContainer {

    @YamlElement(ExtensionDescriptorParser.SCHEMA_VERSION)
    private String schemaVersion;
    @YamlElement(ExtensionDescriptorParser.ID)
    private String id;
    @YamlElement(ExtensionDescriptorParser.EXTENDS)
    private String parentId;
    @YamlElement(ExtensionDescriptorParser.MODULES)
    private List<ExtensionModule> modules = Collections.emptyList();
    @YamlElement(ExtensionDescriptorParser.RESOURCES)
    private List<ExtensionResource> resources = Collections.emptyList();
    @YamlElement(ExtensionDescriptorParser.PARAMETERS)
    private Map<String, Object> parameters = Collections.emptyMap();

    // Required by Jackson.
    protected ExtensionDescriptor() {
        super(0);
    }

    protected ExtensionDescriptor(int majorSchemaVersion) {
        super(majorSchemaVersion);
    }

    public static ExtensionDescriptor createV2() {
        return new ExtensionDescriptor(2);
    }

    public static ExtensionDescriptor createV3() {
        return new ExtensionDescriptor(3);
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public String getId() {
        return id;
    }

    public String getParentId() {
        return parentId;
    }

    public List<ExtensionModule> getModules() {
        return modules;
    }

    public List<ExtensionResource> getResources() {
        return resources;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public ExtensionDescriptor setSchemaVersion(String schemaVersion) {
        this.schemaVersion = ObjectUtils.defaultIfNull(schemaVersion, this.schemaVersion);
        return this;
    }

    public ExtensionDescriptor setId(String id) {
        this.id = ObjectUtils.defaultIfNull(id, this.id);
        return this;
    }

    public ExtensionDescriptor setParentId(String parentId) {
        this.parentId = ObjectUtils.defaultIfNull(parentId, this.parentId);
        return this;
    }

    public ExtensionDescriptor setModules(List<ExtensionModule> modules) {
        this.modules = ObjectUtils.defaultIfNull(modules, this.modules);
        return this;
    }

    public ExtensionDescriptor setResources(List<ExtensionResource> resources) {
        this.resources = ObjectUtils.defaultIfNull(resources, this.resources);
        return this;
    }

    public ExtensionDescriptor setParameters(Map<String, Object> parameters) {
        this.parameters = ObjectUtils.defaultIfNull(parameters, this.parameters);
        return this;
    }

    public void accept(Visitor visitor) {
        accept(new ElementContext(this, null), visitor);
    }

    @Override
    public void accept(ElementContext context, Visitor visitor) {
        visitor.visit(context, this);
        for (ExtensionModule module : getModules()) {
            module.accept(new ElementContext(module, context), visitor);
        }
        for (ExtensionResource resource : getResources()) {
            resource.accept(new ElementContext(resource, context), visitor);
        }
    }

}
