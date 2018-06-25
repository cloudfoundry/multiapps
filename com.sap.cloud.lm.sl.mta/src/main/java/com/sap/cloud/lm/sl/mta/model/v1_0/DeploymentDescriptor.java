package com.sap.cloud.lm.sl.mta.model.v1_0;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.IdentifiableElement;
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;
import com.sap.cloud.lm.sl.mta.model.VisitableElement;
import com.sap.cloud.lm.sl.mta.model.Visitor;
import com.sap.cloud.lm.sl.mta.parsers.v1_0.DeploymentDescriptorParser;
import com.sap.cloud.lm.sl.mta.util.YamlElement;
import com.sap.cloud.lm.sl.mta.util.YamlElementOrder;

/**
 * @see <a href="https://[My Github Repo]/mta/spec/blob/master/schemas/v1/mtad-schema.yaml"> MTA Deployment descriptor schema</a>
 */
@YamlElementOrder({ "id", "description", "version", "provider", "copyright", "schemaVersion", "properties", "modules1_0", "resources1_0" })
public class DeploymentDescriptor implements VisitableElement, IdentifiableElement, PropertiesContainer {

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
    @YamlElement(DeploymentDescriptorParser.SCHEMA_VERSION)
    private String schemaVersion;
    @YamlElement(DeploymentDescriptorParser.MODULES)
    private List<Module> modules1_0;
    @YamlElement(DeploymentDescriptorParser.RESOURCES)
    private List<Resource> resources1_0;
    @YamlElement(DeploymentDescriptorParser.PROPERTIES)
    private Map<String, Object> properties;

    protected DeploymentDescriptor() {

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

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public List<Module> getModules1_0() {
        return ListUtil.upcastUnmodifiable(getModules());
    }

    protected List<? extends Module> getModules() {
        return modules1_0;
    }

    public List<Resource> getResources1_0() {
        return ListUtil.upcastUnmodifiable(getResources());
    }

    protected List<? extends Resource> getResources() {
        return resources1_0;
    }

    @Override
    public Map<String, Object> getProperties() {
        return MapUtil.unmodifiable(properties);
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

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public void setModules1_0(List<Module> modules) {
        setModules(modules);
    }

    protected void setModules(List<? extends Module> modules) {
        this.modules1_0 = ListUtil.cast(modules);
    }

    public void setResources1_0(List<Resource> resources) {
        setResources(resources);
    }

    protected void setResources(List<? extends Resource> resources) {
        this.resources1_0 = ListUtil.cast(resources);
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
        result.setId(getId());
        result.setProvider(getProvider());
        result.setDescription(getDescription());
        result.setVersion(getVersion());
        result.setCopyright(getCopyright());
        result.setSchemaVersion(getSchemaVersion());
        List<Module> clonedModules = new ArrayList<>();
        for (Module module : getModules1_0()) {
            clonedModules.add(module.copyOf());
        }
        result.setModules1_0(clonedModules);
        List<Resource> clonedResources = new ArrayList<>();
        for (Resource resource : getResources1_0()) {
            clonedResources.add(resource.copyOf());
        }
        result.setResources1_0(clonedResources);
        result.setProperties(getProperties());
        return result.build();
    }

    public static class Builder {

        protected String id;
        protected String description;
        protected String version;
        protected String provider;
        protected String copyright;
        protected String schemaVersion;
        protected List<Module> modules1_0;
        protected List<Resource> resources1_0;
        protected Map<String, Object> properties;

        public DeploymentDescriptor build() {
            DeploymentDescriptor result = new DeploymentDescriptor();
            result.setId(id);
            result.setProvider(provider);
            result.setDescription(description);
            result.setVersion(version);
            result.setCopyright(copyright);
            result.setSchemaVersion(schemaVersion);
            result.setProperties(getOrDefault(properties, Collections.<String, Object> emptyMap()));
            result.setModules1_0(getOrDefault(modules1_0, Collections.<Module> emptyList()));
            result.setResources1_0(getOrDefault(resources1_0, Collections.<Resource> emptyList()));
            return result;
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

        public void setSchemaVersion(String schemaVersion) {
            this.schemaVersion = schemaVersion;
        }

        public void setModules1_0(List<Module> modules) {
            setModules(modules);
        }

        protected void setModules(List<? extends Module> modules) {
            this.modules1_0 = ListUtil.cast(modules);
        }

        public void setResources1_0(List<Resource> resources) {
            setResources(resources);
        }

        protected void setResources(List<? extends Resource> resources) {
            this.resources1_0 = ListUtil.cast(resources);
        }

        public void setProperties(Map<String, Object> properties) {
            this.properties = properties;
        }

    }

}
