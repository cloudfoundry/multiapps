package com.sap.cloud.lm.sl.mta.model.v2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.model.Descriptor;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;
import com.sap.cloud.lm.sl.mta.model.VisitableElement;
import com.sap.cloud.lm.sl.mta.model.Visitor;
import com.sap.cloud.lm.sl.mta.parsers.v2.DeploymentDescriptorParser;
import com.sap.cloud.lm.sl.mta.util.YamlElement;
import com.sap.cloud.lm.sl.mta.util.YamlElementOrder;

@YamlElementOrder({ "id", "description", "version", "provider", "copyright", "schemaVersion", "parameters", "modules2", "resources2" })
public class DeploymentDescriptor implements Descriptor, VisitableElement, PropertiesContainer, ParametersContainer {

    @YamlElement(DeploymentDescriptorParser.MODULES)
    private List<Module> modules2;
    @YamlElement(DeploymentDescriptorParser.RESOURCES)
    private List<Resource> resources2;
    @YamlElement(DeploymentDescriptorParser.PARAMETERS)
    private Map<String, Object> parameters;
    @YamlElement(DeploymentDescriptorParser.SCHEMA_VERSION)
    private String schemaVersion;
    @YamlElement(DeploymentDescriptorParser.ID)
    private String id;
    @YamlElement(DeploymentDescriptorParser.DESCRIPTION)
    private String description;
    @YamlElement(DeploymentDescriptorParser.VERSION)
    private String version;
    @YamlElement(DeploymentDescriptorParser.PROVIDER)
    private String provider;
    @YamlElement(DeploymentDescriptorParser.COPYRIGHT)
    private String copyright;
    @YamlElement(DeploymentDescriptorParser.PROPERTIES)
    private Map<String, Object> properties;

    protected DeploymentDescriptor() {

    }

    @Override
    public String getId() {
        return id;
    }

    public List<Module> getModules2() {
        return ListUtil.upcastUnmodifiable(getModules());
    }

    protected List<? extends Module> getModules() {
        return modules2;
    }

    public List<Resource> getResources2() {
        return ListUtil.upcastUnmodifiable(getResources());
    }

    protected List<? extends Resource> getResources() {
        return resources2;
    }

    @Override
    public Map<String, Object> getProperties() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> getParameters() {
        return MapUtil.unmodifiable(parameters);
    }

    public void setModules2(List<Module> modules) {
        setModules(modules);
    }

    protected void setModules(List<? extends Module> modules) {
        this.modules2 = ListUtil.cast(modules);
    }

    public void setResources2(List<Resource> resources) {
        setResources(resources);
    }

    protected void setResources(List<? extends Resource> resources) {
        this.resources2 = ListUtil.cast(resources);
    }

    @Override
    public void setProperties(Map<String, Object> properties) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = new LinkedHashMap<>(parameters);
    }

    public void accept(Visitor visitor) {
        accept(new ElementContext(this, null), visitor);
    }

    @Override
    public void accept(ElementContext context, Visitor visitor) {
        visitor.visit(context, this);
        for (Module module : getModules()) {
            module.accept(new ElementContext(module, context), visitor);
        }
        for (Resource resource : getResources()) {
            resource.accept(new ElementContext(resource, context), visitor);
        }
    }

    public DeploymentDescriptor copyOf() {
        Builder result = new Builder();
        result.setId(getId());
        result.setProvider(getProvider());
        result.setDescription(getDescription());
        result.setVersion(getVersion());
        result.setCopyright(getCopyright());
        result.setSchemaVersion(getSchemaVersion());
        result.setParameters(getParameters());
        List<Module> clonedModules = new ArrayList<>();
        for (Module module : getModules2()) {
            clonedModules.add(module.copyOf());
        }
        result.setModules2(clonedModules);
        List<Resource> clonedResources = new ArrayList<>();
        for (Resource resource : getResources2()) {
            clonedResources.add(resource.copyOf());
        }
        result.setResources2(clonedResources);
        return result.build();
    }

    public static class Builder {

        protected String schemaVersion;
        protected String id;
        protected String description;
        protected String version;
        protected String provider;
        protected String copyright;
        protected List<Module> modules2;
        protected List<Resource> resources2;
        protected Map<String, Object> parameters;

        public DeploymentDescriptor build() {
            DeploymentDescriptor result = new DeploymentDescriptor();
            result.setId(id);
            result.setProvider(provider);
            result.setDescription(description);
            result.setVersion(version);
            result.setCopyright(copyright);
            result.setSchemaVersion(schemaVersion);
            result.setModules2(ObjectUtils.defaultIfNull(modules2, Collections.<Module> emptyList()));
            result.setResources2(ObjectUtils.defaultIfNull(resources2, Collections.<Resource> emptyList()));
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            return result;
        }

        public void setSchemaVersion(String schemaVersion) {
            this.schemaVersion = schemaVersion;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public void setCopyright(String copyright) {
            this.copyright = copyright;
        }

        public void setModules2(List<Module> modules) {
            setModules(modules);
        }

        protected void setModules(List<? extends Module> modules) {
            this.modules2 = ListUtil.cast(modules);
        }

        public void setResources2(List<Resource> resources) {
            setResources(resources);
        }

        protected void setResources(List<? extends Resource> resources) {
            this.resources2 = ListUtil.cast(resources);
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }

        public void setProperties(Map<String, Object> properties) {
            throw new UnsupportedOperationException();
        }

    }

    public String getProvider() {
        return provider;
    }

    public String getDescription() {
        return description;
    }

    public String getVersion() {
        return version;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

}
