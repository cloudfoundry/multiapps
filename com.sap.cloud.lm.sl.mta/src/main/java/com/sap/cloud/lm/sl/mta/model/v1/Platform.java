package com.sap.cloud.lm.sl.mta.model.v1;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.NamedElement;
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;
import com.sap.cloud.lm.sl.mta.model.VisitableElement;
import com.sap.cloud.lm.sl.mta.model.Visitor;

/**
 * @see <a href= "https://[My Github Repo]/mta/spec/blob/master/schemas/v1/platform-types-schema.yaml"> Platform descriptor schema</a>
 */
public class Platform implements VisitableElement, NamedElement, PropertiesContainer {

    private String name;
    private String version;
    private String description;
    private Map<String, Object> properties;
    private List<ModuleType> moduleTypes1;
    private List<ResourceType> resourceTypes1;

    protected Platform() {

    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public Map<String, Object> getProperties() {
        return MapUtil.unmodifiable(properties);
    }

    public List<ModuleType> getModuleTypes1() {
        return ListUtil.upcastUnmodifiable(getModuleTypes());
    }

    protected List<? extends ModuleType> getModuleTypes() {
        return moduleTypes1;
    }

    public List<ResourceType> getResourceTypes1() {
        return ListUtil.upcastUnmodifiable(getResourceTypes());
    }

    protected List<? extends ResourceType> getResourceTypes() {
        return resourceTypes1;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = new LinkedHashMap<>(properties);
    }

    public void setModuleTypes1(List<ModuleType> moduleTypes) {
        setModuleTypes(moduleTypes);
    }

    protected void setModuleTypes(List<? extends ModuleType> moduleTypes) {
        this.moduleTypes1 = ListUtil.cast(moduleTypes);
    }

    public void setResourceTypes1(List<ResourceType> resourceTypes) {
        setResourceTypes(resourceTypes);
    }

    protected void setResourceTypes(List<? extends ResourceType> resourceTypes) {
        this.resourceTypes1 = ListUtil.cast(resourceTypes);
    }

    public void accept(Visitor visitor) {
        accept(new ElementContext(this, null), visitor);
    }

    @Override
    public void accept(ElementContext context, Visitor visitor) {
        visitor.visit(context, this);
        for (ModuleType moduleType : getModuleTypes()) {
            moduleType.accept(new ElementContext(moduleType, context), visitor);
        }
        for (ResourceType resourceType : getResourceTypes()) {
            resourceType.accept(new ElementContext(resourceType, context), visitor);
        }
    }

    public static class Builder {

        protected String name;
        protected String version;
        protected String description;
        protected Map<String, Object> properties;
        protected List<ModuleType> moduleTypes1;
        protected List<ResourceType> resourceTypes1;

        public Platform build() {
            Platform result = new Platform();
            result.setName(name);
            result.setVersion(version);
            result.setDescription(description);
            result.setProperties(ObjectUtils.defaultIfNull(properties, Collections.<String, Object> emptyMap()));
            result.setModuleTypes1(ObjectUtils.defaultIfNull(moduleTypes1, Collections.<ModuleType> emptyList()));
            result.setResourceTypes1(ObjectUtils.defaultIfNull(resourceTypes1, Collections.<ResourceType> emptyList()));
            return result;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setProperties(Map<String, Object> properties) {
            this.properties = properties;
        }

        public void setModuleTypes1(List<ModuleType> moduleTypes) {
            setModuleTypes(moduleTypes);
        }

        protected void setModuleTypes(List<? extends ModuleType> moduleTypes) {
            this.moduleTypes1 = ListUtil.cast(moduleTypes);
        }

        public void setResourceTypes1(List<ResourceType> resourceTypes) {
            setResourceTypes(resourceTypes);
        }

        protected void setResourceTypes(List<? extends ResourceType> resourceTypes) {
            this.resourceTypes1 = ListUtil.cast(resourceTypes);
        }

    }

}
