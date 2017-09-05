package com.sap.cloud.lm.sl.mta.model.v1_0;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.builders.Builder;
import com.sap.cloud.lm.sl.mta.model.VisitableElement;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.ExtensionElement;
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;
import com.sap.cloud.lm.sl.mta.model.Visitor;
import com.sap.cloud.lm.sl.mta.parsers.v1_0.ExtensionDescriptorParser;
import com.sap.cloud.lm.sl.mta.util.YamlElement;
import com.sap.cloud.lm.sl.mta.util.YamlElementOrder;

/**
 * @see <a href="https://github.wdf.sap.corp/mta/spec/blob/master/schemas/v1/mtaext-schema.yaml"> MTA Extension descriptor schema</a>
 */
@YamlElementOrder({ "id", "description", "parentId", "provider", "schemaVersion", "properties", "modules1_0", "resources1_0" })
public class ExtensionDescriptor implements VisitableElement, ExtensionElement, PropertiesContainer {

    @YamlElement(ExtensionDescriptorParser.ID)
    private String id;
    @YamlElement(ExtensionDescriptorParser.EXT_DESCRIPTION)
    private String description;
    @YamlElement(ExtensionDescriptorParser.EXTENDS)
    private String parentId;
    @YamlElement(ExtensionDescriptorParser.PROVIDER)
    private String provider;
    @YamlElement(ExtensionDescriptorParser.SCHEMA_VERSION)
    private String schemaVersion;
    @YamlElement(ExtensionDescriptorParser.TARGET_PLATFORMS)
    private List<String> deployTargets;
    @YamlElement(ExtensionDescriptorParser.MODULES)
    private List<ExtensionModule> modules1_0;
    @YamlElement(ExtensionDescriptorParser.RESOURCES)
    private List<ExtensionResource> resources1_0;
    @YamlElement(ExtensionDescriptorParser.PROPERTIES)
    private Map<String, Object> properties;

    protected ExtensionDescriptor() {

    }

    @Override
    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String getParentId() {
        return parentId;
    }

    public String getProvider() {
        return provider;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public List<String> getDeployTargets() {
        return ListUtil.unmodifiable(deployTargets);
    }

    public List<ExtensionModule> getModules1_0() {
        return ListUtil.upcastUnmodifiable(getModules());
    }

    protected List<? extends ExtensionModule> getModules() {
        return modules1_0;
    }

    public List<ExtensionResource> getResources1_0() {
        return ListUtil.upcastUnmodifiable(getResources());
    }

    protected List<? extends ExtensionResource> getResources() {
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

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public void setDeployTargets(List<String> targetPlatforms) {
        this.deployTargets = new ArrayList<>(targetPlatforms);
    }

    public void setModules1_0(List<ExtensionModule> modules) {
        setModules(modules);
    }

    protected void setModules(List<? extends ExtensionModule> modules) {
        this.modules1_0 = ListUtil.cast(modules);
    }

    public void setResources1_0(List<ExtensionResource> resources) {
        setResources(resources);
    }

    protected void setResources(List<? extends ExtensionResource> resources) {
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
        for (ExtensionModule module : getModules()) {
            module.accept(new ElementContext(module, context), visitor);
        }
        for (ExtensionResource resource : getResources()) {
            resource.accept(new ElementContext(resource, context), visitor);
        }
    }

    public static class ExtensionDescriptorBuilder implements Builder<ExtensionDescriptor> {

        protected String id;
        protected String description;
        protected String parentId;
        protected String provider;
        protected String schemaVersion;
        protected List<String> deployTargets;
        protected List<ExtensionModule> modules1_0;
        protected List<ExtensionResource> resources1_0;
        protected Map<String, Object> properties;

        @Override
        public ExtensionDescriptor build() {
            ExtensionDescriptor result = new ExtensionDescriptor();
            result.setId(id);
            result.setDescription(description);
            result.setParentId(parentId);
            result.setProvider(provider);
            result.setSchemaVersion(schemaVersion);
            result.setDeployTargets(getOrDefault(deployTargets, Collections.<String> emptyList()));
            result.setModules1_0(getOrDefault(modules1_0, Collections.<ExtensionModule> emptyList()));
            result.setResources1_0(getOrDefault(resources1_0, Collections.<ExtensionResource> emptyList()));
            result.setProperties(getOrDefault(properties, Collections.<String, Object> emptyMap()));
            return result;
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

        public void setSchemaVersion(String schemaVersion) {
            this.schemaVersion = schemaVersion;
        }

        public void setDeployTargets(List<String> deployTargets) {
            this.deployTargets = deployTargets;
        }

        public void setModules1_0(List<ExtensionModule> modules) {
            setModules(modules);
        }

        protected void setModules(List<? extends ExtensionModule> modules) {
            this.modules1_0 = ListUtil.cast(modules);
        }

        public void setResources1_0(List<ExtensionResource> resources) {
            setResources(resources);
        }

        protected void setResources(List<? extends ExtensionResource> resources) {
            this.resources1_0 = ListUtil.cast(resources);
        }

        public void setProperties(Map<String, Object> properties) {
            this.properties = properties;
        }

    }

}
