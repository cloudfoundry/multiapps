package com.sap.cloud.lm.sl.mta.model.v2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.common.util.yaml.YamlElement;
import com.sap.cloud.lm.sl.common.util.yaml.YamlElementOrder;
import com.sap.cloud.lm.sl.mta.model.Descriptor;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;
import com.sap.cloud.lm.sl.mta.model.VisitableElement;
import com.sap.cloud.lm.sl.mta.model.Visitor;
import com.sap.cloud.lm.sl.mta.parsers.v2.ExtensionDescriptorParser;

@YamlElementOrder({ "schemaVersion", "id", "parentId", "description", "provider", "parameters", "modules2", "resources2" })
public class ExtensionDescriptor implements Descriptor, VisitableElement, PropertiesContainer, ParametersContainer {

    @YamlElement(ExtensionDescriptorParser.DESCRIPTION)
    private String description;
    @YamlElement(ExtensionDescriptorParser.PROVIDER)
    private String provider;
    @YamlElement(ExtensionDescriptorParser.MODULES)
    private List<ExtensionModule> modules2;
    @YamlElement(ExtensionDescriptorParser.RESOURCES)
    private List<ExtensionResource> resources2;
    @YamlElement(ExtensionDescriptorParser.PARAMETERS)
    private Map<String, Object> parameters;
    @YamlElement(ExtensionDescriptorParser.SCHEMA_VERSION)
    private String schemaVersion;
    @YamlElement(ExtensionDescriptorParser.ID)
    private String id;
    @YamlElement(ExtensionDescriptorParser.EXT_DESCRIPTION)
    private String extensionDescription;
    @YamlElement(ExtensionDescriptorParser.EXTENDS)
    private String parentId;
    @YamlElement(ExtensionDescriptorParser.EXT_PROVIDER)
    private String extensionProvider;
    @YamlElement(ExtensionDescriptorParser.TARGET_PLATFORMS)
    private List<String> deployTargets;
    @YamlElement(ExtensionDescriptorParser.PROPERTIES)
    private Map<String, Object> properties;

    protected ExtensionDescriptor() {

    }

    /**
     * @deprecated Use {@link #getDescription()} instead.
     */
    @Deprecated
    public String getExtensionDescription() {
        return getDescription();
    }

    /**
     * @deprecated Use {@link #getProvider()} instead.
     */
    @Deprecated
    public String getExtensionProvider() {
        return getProvider();
    }

    public String getDescription() {
        return description;
    }

    public String getProvider() {
        return provider;
    }

    @Override
    public String getId() {
        return id;
    }
    
    public String getParentId() {
        return parentId;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }
    
    public List<ExtensionModule> getModules2() {
        return ListUtil.upcastUnmodifiable(getModules());
    }

    public List<? extends ExtensionModule> getModules() {
        return modules2;
    }

    public List<ExtensionResource> getResources2() {
        return ListUtil.upcastUnmodifiable(getResources());
    }

    public List<? extends ExtensionResource> getResources() {
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

    /**
     * @deprecated Use {@link #setDescription(String)} instead.
     */
    @Deprecated
    public void setExtensionDescription(String extensionDescription) {
        setDescription(extensionDescription);
    }

    /**
     * @deprecated Use {@link #setProvider(String)} instead.
     */
    @Deprecated
    public void setExtensionProvider(String extensionProvider) {
        setProvider(extensionProvider);
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

    public void setModules2(List<ExtensionModule> modules) {
        setModules(modules);
    }

    protected void setModules(List<? extends ExtensionModule> modules) {
        this.modules2 = ListUtil.cast(modules);
    }

    public void setResources2(List<ExtensionResource> resources) {
        setResources(resources);
    }

    protected void setResources(List<? extends ExtensionResource> resources) {
        this.resources2 = ListUtil.cast(resources);
    }

    @Override
    public Void setProperties(Map<String, Object> properties) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Void setParameters(Map<String, Object> parameters) {
        this.parameters = new LinkedHashMap<>(parameters);
        return null;
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

        protected String extensionDescription;
        protected String extensionProvider;
        protected String schemaVersion;
        protected String id;
        protected String description;
        protected String parentId;
        protected String provider;
        protected List<String> deployTargets;
        protected List<ExtensionModule> modules2;
        protected List<ExtensionResource> resources2;
        protected Map<String, Object> parameters;

        public ExtensionDescriptor build() {
            ExtensionDescriptor result = new ExtensionDescriptor();
            result.setId(id);
            result.setDescription(description);
            result.setParentId(parentId);
            result.setProvider(provider);
            result.setSchemaVersion(schemaVersion);
            result.setResources2(ObjectUtils.defaultIfNull(resources2, Collections.<ExtensionResource> emptyList()));
            result.setDeployTargets(ObjectUtils.defaultIfNull(deployTargets, Collections.<String> emptyList()));
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            result.setModules2(ObjectUtils.defaultIfNull(modules2, Collections.<ExtensionModule> emptyList()));
            return result;
        }

        /**
         * @deprecated Use {@link #setDescription(String)} instead.
         */
        @Deprecated
        public void setExtensionDescription(String extensionDescription) {
            setDescription(extensionDescription);
        }

        /**
         * @deprecated Use {@link #setProvider(String)} instead.
         */
        @Deprecated
        public void setExtensionProvider(String extensionProvider) {
            setProvider(extensionProvider);
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

        public void setModules2(List<ExtensionModule> modules) {
            setModules(modules);
        }

        protected void setModules(List<? extends ExtensionModule> modules) {
            this.modules2 = ListUtil.cast(modules);
        }

        public void setResources2(List<ExtensionResource> resources) {
            setResources(resources);
        }

        protected void setResources(List<? extends ExtensionResource> resources) {
            this.resources2 = ListUtil.cast(resources);
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }

        public void setProperties(Map<String, Object> properties) {
            throw new UnsupportedOperationException();
        }

    }

}
