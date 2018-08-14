package com.sap.cloud.lm.sl.mta.model.v1_0;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.model.AuditableConfiguration;
import com.sap.cloud.lm.sl.mta.model.ConfigurationIdentifier;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.NamedElement;
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;
import com.sap.cloud.lm.sl.mta.model.VisitableElement;
import com.sap.cloud.lm.sl.mta.model.Visitor;

/**
 * @see <a href="https://[My Github Repo]/mta/spec/blob/master/schemas/v1/platforms-schema.yaml"> Target platform descriptor schema </a>
 */
public class Target implements VisitableElement, NamedElement, PropertiesContainer, AuditableConfiguration {

    private static final String CONFIGURATION_TYPE = "target";
    private String name;
    private String type;
    private String description;
    private Map<String, Object> properties;
    private List<TargetModuleType> platformModuleTypes1_0;
    private List<TargetResourceType> platformResourceTypes1_0;

    protected Target() {

    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public Map<String, Object> getProperties() {
        return MapUtil.unmodifiable(properties);
    }

    public List<TargetModuleType> getModuleTypes1_0() {
        return ListUtil.upcastUnmodifiable(getModuleTypes());
    }

    protected List<? extends TargetModuleType> getModuleTypes() {
        return platformModuleTypes1_0;
    }

    public List<TargetResourceType> getResourceTypes1_0() {
        return ListUtil.upcastUnmodifiable(getResourceTypes());
    }

    protected List<? extends TargetResourceType> getResourceTypes() {
        return platformResourceTypes1_0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = new LinkedHashMap<>(properties);
    }

    public void setModuleTypes1_0(List<TargetModuleType> moduleTypes) {
        setModuleTypes(moduleTypes);
    }

    protected void setModuleTypes(List<? extends TargetModuleType> moduleTypes) {
        this.platformModuleTypes1_0 = ListUtil.cast(moduleTypes);
    }

    public void setResourceTypes1_0(List<TargetResourceType> resourceTypes) {
        setResourceTypes(resourceTypes);
    }

    protected void setResourceTypes(List<? extends TargetResourceType> resourceTypes) {
        this.platformResourceTypes1_0 = ListUtil.cast(resourceTypes);
    }

    public void accept(Visitor visitor) {
        accept(new ElementContext(this, null), visitor);
    }

    @Override
    public void accept(ElementContext context, Visitor visitor) {
        visitor.visit(context, this);
        for (TargetModuleType moduleType : getModuleTypes()) {
            moduleType.accept(new ElementContext(moduleType, context), visitor);
        }
        for (TargetResourceType resourceType : getResourceTypes()) {
            resourceType.accept(new ElementContext(resourceType, context), visitor);
        }
    }

    public static class Builder {

        protected String name;
        protected String type;
        protected String description;
        protected Map<String, Object> properties;
        protected List<TargetModuleType> platformModuleTypes1_0;
        protected List<TargetResourceType> platformResourceTypes1_0;

        public Target build() {
            Target result = new Target();
            result.setName(name);
            result.setType(type);
            result.setDescription(description);
            result.setProperties(ObjectUtils.defaultIfNull(properties, Collections.<String, Object> emptyMap()));
            result.setModuleTypes1_0(ObjectUtils.defaultIfNull(platformModuleTypes1_0, Collections.<TargetModuleType> emptyList()));
            result.setResourceTypes1_0(ObjectUtils.defaultIfNull(platformResourceTypes1_0, Collections.<TargetResourceType> emptyList()));
            return result;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setProperties(Map<String, Object> properties) {
            this.properties = properties;
        }

        public void setModuleTypes1_0(List<TargetModuleType> moduleTypes) {
            setModuleTypes(moduleTypes);
        }

        protected void setModuleTypes(List<? extends TargetModuleType> moduleTypes) {
            this.platformModuleTypes1_0 = ListUtil.cast(moduleTypes);
        }

        public void setResourceTypes1_0(List<TargetResourceType> resourceTypes) {
            setResourceTypes(resourceTypes);
        }

        protected void setResourceTypes(List<? extends TargetResourceType> resourceTypes) {
            this.platformResourceTypes1_0 = ListUtil.cast(resourceTypes);
        }

    }

    @Override
    public String getConfigurationType() {
        return CONFIGURATION_TYPE;
    }

    @Override
    public String getConfigurationName() {
        return name;
    }

    @Override
    public List<ConfigurationIdentifier> getConfigurationIdentifiers() {
        List<ConfigurationIdentifier> configurationIdentifiers = new ArrayList<>();
        configurationIdentifiers.add(new ConfigurationIdentifier("name", name));
        configurationIdentifiers.add(new ConfigurationIdentifier("description", description));
        configurationIdentifiers.add(new ConfigurationIdentifier("type", type));
        return configurationIdentifiers;
    }

}
