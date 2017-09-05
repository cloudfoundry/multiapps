package com.sap.cloud.lm.sl.mta.model.v1_0;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.builders.Builder;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.NamedElement;
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;
import com.sap.cloud.lm.sl.mta.model.VisitableElement;
import com.sap.cloud.lm.sl.mta.model.Visitor;

/**
 * @see <a href= "https://github.wdf.sap.corp/mta/spec/blob/master/schemas/v1/platform-types-schema.yaml"> Platform descriptor schema</a>
 */
public class Platform implements VisitableElement, NamedElement, PropertiesContainer {

    private String name;
    private String version;
    private String description;
    private Map<String, Object> properties;
    private List<ModuleType> moduleTypes1_0;
    private List<ResourceType> resourceTypes1_0;

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

    public List<ModuleType> getModuleTypes1_0() {
        return ListUtil.upcastUnmodifiable(getModuleTypes());
    }

    protected List<? extends ModuleType> getModuleTypes() {
        return moduleTypes1_0;
    }

    public List<ResourceType> getResourceTypes1_0() {
        return ListUtil.upcastUnmodifiable(getResourceTypes());
    }

    protected List<? extends ResourceType> getResourceTypes() {
        return resourceTypes1_0;
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

    public void setModuleTypes1_0(List<ModuleType> moduleTypes) {
        setModuleTypes(moduleTypes);
    }

    protected void setModuleTypes(List<? extends ModuleType> moduleTypes) {
        this.moduleTypes1_0 = ListUtil.cast(moduleTypes);
    }

    public void setResourceTypes1_0(List<ResourceType> resourceTypes) {
        setResourceTypes(resourceTypes);
    }

    protected void setResourceTypes(List<? extends ResourceType> resourceTypes) {
        this.resourceTypes1_0 = ListUtil.cast(resourceTypes);
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

    public static class PlatformBuilder implements Builder<Platform> {

        protected String name;
        protected String version;
        protected String description;
        protected Map<String, Object> properties;
        protected List<ModuleType> moduleTypes1_0;
        protected List<ResourceType> resourceTypes1_0;

        @Override
        public Platform build() {
            Platform result = new Platform();
            result.setName(name);
            result.setVersion(version);
            result.setDescription(description);
            result.setProperties(getOrDefault(properties, Collections.<String, Object> emptyMap()));
            result.setModuleTypes1_0(getOrDefault(moduleTypes1_0, Collections.<ModuleType> emptyList()));
            result.setResourceTypes1_0(getOrDefault(resourceTypes1_0, Collections.<ResourceType> emptyList()));
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

        public void setModuleTypes1_0(List<ModuleType> moduleTypes) {
            setModuleTypes(moduleTypes);
        }

        protected void setModuleTypes(List<? extends ModuleType> moduleTypes) {
            this.moduleTypes1_0 = ListUtil.cast(moduleTypes);
        }

        public void setResourceTypes1_0(List<ResourceType> resourceTypes) {
            setResourceTypes(resourceTypes);
        }

        protected void setResourceTypes(List<? extends ResourceType> resourceTypes) {
            this.resourceTypes1_0 = ListUtil.cast(resourceTypes);
        }

    }

}
