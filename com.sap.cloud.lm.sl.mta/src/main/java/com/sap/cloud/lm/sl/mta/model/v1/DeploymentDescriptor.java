package com.sap.cloud.lm.sl.mta.model.v1;

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
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;
import com.sap.cloud.lm.sl.mta.model.VisitableElement;
import com.sap.cloud.lm.sl.mta.model.Visitor;
import com.sap.cloud.lm.sl.mta.parsers.v1.DeploymentDescriptorParser;
import com.sap.cloud.lm.sl.mta.util.YamlElement;
import com.sap.cloud.lm.sl.mta.util.YamlElementOrder;

/**
 * @see <a href="https://[My Github Repo]/mta/spec/blob/master/schemas/v1/mtad-schema.yaml"> MTA Deployment descriptor schema</a>
 */
@YamlElementOrder({ "schemaVersion", "id", "description", "version", "provider", "copyright", "properties", "modules1", "resources1" })
public class DeploymentDescriptor implements Descriptor, VisitableElement, PropertiesContainer {

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
    @YamlElement(DeploymentDescriptorParser.MODULES)
    private List<Module> modules1;
    @YamlElement(DeploymentDescriptorParser.RESOURCES)
    private List<Resource> resources1;
    @YamlElement(DeploymentDescriptorParser.PROPERTIES)
    private Map<String, Object> properties;

    protected DeploymentDescriptor() {

    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getVersion() {
        return version;
    }

    public String getProvider() {
        return provider;
    }

    public String getCopyright() {
        return copyright;
    }

    public List<Module> getModules1() {
        return ListUtil.upcastUnmodifiable(getModules());
    }

    protected List<? extends Module> getModules() {
        return modules1;
    }

    public List<Resource> getResources1() {
        return ListUtil.upcastUnmodifiable(getResources());
    }

    protected List<? extends Resource> getResources() {
        return resources1;
    }

    @Override
    public Map<String, Object> getProperties() {
        return MapUtil.unmodifiable(properties);
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

    public void setModules1(List<Module> modules) {
        setModules(modules);
    }

    protected void setModules(List<? extends Module> modules) {
        this.modules1 = ListUtil.cast(modules);
    }

    public void setResources1(List<Resource> resources) {
        setResources(resources);
    }

    protected void setResources(List<? extends Resource> resources) {
        this.resources1 = ListUtil.cast(resources);
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = new LinkedHashMap<>(properties);
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
        result.setSchemaVersion(getSchemaVersion());
        result.setId(getId());
        result.setProvider(getProvider());
        result.setDescription(getDescription());
        result.setVersion(getVersion());
        result.setCopyright(getCopyright());
        List<Module> clonedModules = new ArrayList<>();
        for (Module module : getModules1()) {
            clonedModules.add(module.copyOf());
        }
        result.setModules1(clonedModules);
        List<Resource> clonedResources = new ArrayList<>();
        for (Resource resource : getResources1()) {
            clonedResources.add(resource.copyOf());
        }
        result.setResources1(clonedResources);
        result.setProperties(getProperties());
        return result.build();
    }

    public static class Builder {

        protected String schemaVersion;
        protected String id;
        protected String description;
        protected String version;
        protected String provider;
        protected String copyright;
        protected List<Module> modules1;
        protected List<Resource> resources1;
        protected Map<String, Object> properties;

        public DeploymentDescriptor build() {
            DeploymentDescriptor result = new DeploymentDescriptor();
            result.setSchemaVersion(schemaVersion);
            result.setId(id);
            result.setProvider(provider);
            result.setDescription(description);
            result.setVersion(version);
            result.setCopyright(copyright);
            result.setProperties(ObjectUtils.defaultIfNull(properties, Collections.<String, Object> emptyMap()));
            result.setModules1(ObjectUtils.defaultIfNull(modules1, Collections.<Module> emptyList()));
            result.setResources1(ObjectUtils.defaultIfNull(resources1, Collections.<Resource> emptyList()));
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

        public void setModules1(List<Module> modules) {
            setModules(modules);
        }

        protected void setModules(List<? extends Module> modules) {
            this.modules1 = ListUtil.cast(modules);
        }

        public void setResources1(List<Resource> resources) {
            setResources(resources);
        }

        protected void setResources(List<? extends Resource> resources) {
            this.resources1 = ListUtil.cast(resources);
        }

        public void setProperties(Map<String, Object> properties) {
            this.properties = properties;
        }

    }

}
