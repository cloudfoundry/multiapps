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
import com.sap.cloud.lm.sl.mta.parsers.v1.ExtensionDescriptorParser;
import com.sap.cloud.lm.sl.mta.util.YamlElement;
import com.sap.cloud.lm.sl.mta.util.YamlElementOrder;

/**
 * @see <a href="https://[My Github Repo]/mta/spec/blob/master/schemas/v1/mtaext-schema.yaml"> MTA Extension descriptor schema</a>
 */
@YamlElementOrder({ "schemaVersion", "id", "description", "parentId", "provider", "properties", "modules1", "resources1" })
public class ExtensionDescriptor implements Descriptor, VisitableElement, PropertiesContainer {

    @YamlElement(ExtensionDescriptorParser.SCHEMA_VERSION)
    private String schemaVersion;
    @YamlElement(ExtensionDescriptorParser.ID)
    private String id;
    @YamlElement(ExtensionDescriptorParser.EXT_DESCRIPTION)
    private String description;
    @YamlElement(ExtensionDescriptorParser.EXTENDS)
    private String parentId;
    @YamlElement(ExtensionDescriptorParser.PROVIDER)
    private String provider;
    @YamlElement(ExtensionDescriptorParser.TARGET_PLATFORMS)
    private List<String> deployTargets;
    @YamlElement(ExtensionDescriptorParser.MODULES)
    private List<ExtensionModule> modules1;
    @YamlElement(ExtensionDescriptorParser.RESOURCES)
    private List<ExtensionResource> resources1;
    @YamlElement(ExtensionDescriptorParser.PROPERTIES)
    private Map<String, Object> properties;

    protected ExtensionDescriptor() {

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

    public String getParentId() {
        return parentId;
    }

    public String getProvider() {
        return provider;
    }

    public List<String> getDeployTargets() {
        return ListUtil.unmodifiable(deployTargets);
    }

    public List<ExtensionModule> getModules1() {
        return ListUtil.upcastUnmodifiable(getModules());
    }

    protected List<? extends ExtensionModule> getModules() {
        return modules1;
    }

    public List<ExtensionResource> getResources1() {
        return ListUtil.upcastUnmodifiable(getResources());
    }

    protected List<? extends ExtensionResource> getResources() {
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

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setDeployTargets(List<String> targetPlatforms) {
        this.deployTargets = new ArrayList<>(targetPlatforms);
    }

    public void setModules1(List<ExtensionModule> modules) {
        setModules(modules);
    }

    protected void setModules(List<? extends ExtensionModule> modules) {
        this.modules1 = ListUtil.cast(modules);
    }

    public void setResources1(List<ExtensionResource> resources) {
        setResources(resources);
    }

    protected void setResources(List<? extends ExtensionResource> resources) {
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
        for (ExtensionModule module : getModules()) {
            module.accept(new ElementContext(module, context), visitor);
        }
        for (ExtensionResource resource : getResources()) {
            resource.accept(new ElementContext(resource, context), visitor);
        }
    }

    public static class Builder {

        protected String schemaVersion;
        protected String id;
        protected String description;
        protected String parentId;
        protected String provider;
        protected List<String> deployTargets;
        protected List<ExtensionModule> modules1;
        protected List<ExtensionResource> resources1;
        protected Map<String, Object> properties;

        public ExtensionDescriptor build() {
            ExtensionDescriptor result = new ExtensionDescriptor();
            result.setSchemaVersion(schemaVersion);
            result.setId(id);
            result.setDescription(description);
            result.setParentId(parentId);
            result.setProvider(provider);
            result.setDeployTargets(ObjectUtils.defaultIfNull(deployTargets, Collections.<String> emptyList()));
            result.setModules1(ObjectUtils.defaultIfNull(modules1, Collections.<ExtensionModule> emptyList()));
            result.setResources1(ObjectUtils.defaultIfNull(resources1, Collections.<ExtensionResource> emptyList()));
            result.setProperties(ObjectUtils.defaultIfNull(properties, Collections.<String, Object> emptyMap()));
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

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public void setDeployTargets(List<String> deployTargets) {
            this.deployTargets = deployTargets;
        }

        public void setModules1(List<ExtensionModule> modules) {
            setModules(modules);
        }

        protected void setModules(List<? extends ExtensionModule> modules) {
            this.modules1 = ListUtil.cast(modules);
        }

        public void setResources1(List<ExtensionResource> resources) {
            setResources(resources);
        }

        protected void setResources(List<? extends ExtensionResource> resources) {
            this.resources1 = ListUtil.cast(resources);
        }

        public void setProperties(Map<String, Object> properties) {
            this.properties = properties;
        }

    }

}
