package com.sap.cloud.lm.sl.mta.model.v1_0;

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
    private List<PlatformModuleType> moduleTypes1_0;
    private List<PlatformResourceType> resourceTypes1_0;

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

    public List<PlatformModuleType> getModuleTypes1_0() {
        return ListUtil.upcastUnmodifiable(getModuleTypes());
    }

    protected List<? extends PlatformModuleType> getModuleTypes() {
        return moduleTypes1_0;
    }

    public List<PlatformResourceType> getResourceTypes1_0() {
        return ListUtil.upcastUnmodifiable(getResourceTypes());
    }

    protected List<? extends PlatformResourceType> getResourceTypes() {
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

    public void setModuleTypes1_0(List<PlatformModuleType> moduleTypes) {
        setModuleTypes(moduleTypes);
    }

    protected void setModuleTypes(List<? extends PlatformModuleType> moduleTypes) {
        this.moduleTypes1_0 = ListUtil.cast(moduleTypes);
    }

    public void setResourceTypes1_0(List<PlatformResourceType> resourceTypes) {
        setResourceTypes(resourceTypes);
    }

    protected void setResourceTypes(List<? extends PlatformResourceType> resourceTypes) {
        this.resourceTypes1_0 = ListUtil.cast(resourceTypes);
    }

    public void accept(Visitor visitor) {
        accept(new ElementContext(this, null), visitor);
    }

    @Override
    public void accept(ElementContext context, Visitor visitor) {
        visitor.visit(context, this);
        for (PlatformModuleType moduleType : getModuleTypes()) {
            moduleType.accept(new ElementContext(moduleType, context), visitor);
        }
        for (PlatformResourceType resourceType : getResourceTypes()) {
            resourceType.accept(new ElementContext(resourceType, context), visitor);
        }
    }

    public static class Builder {

        protected String name;
        protected String version;
        protected String description;
        protected Map<String, Object> properties;
        protected List<PlatformModuleType> moduleTypes1_0;
        protected List<PlatformResourceType> resourceTypes1_0;

        public Platform build() {
            Platform result = new Platform();
            result.setName(name);
            result.setVersion(version);
            result.setDescription(description);
            result.setProperties(ObjectUtils.defaultIfNull(properties, Collections.<String, Object> emptyMap()));
            result.setModuleTypes1_0(ObjectUtils.defaultIfNull(moduleTypes1_0, Collections.<PlatformModuleType> emptyList()));
            result.setResourceTypes1_0(ObjectUtils.defaultIfNull(resourceTypes1_0, Collections.<PlatformResourceType> emptyList()));
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

        public void setModuleTypes1_0(List<PlatformModuleType> moduleTypes) {
            setModuleTypes(moduleTypes);
        }

        protected void setModuleTypes(List<? extends PlatformModuleType> moduleTypes) {
            this.moduleTypes1_0 = ListUtil.cast(moduleTypes);
        }

        public void setResourceTypes1_0(List<PlatformResourceType> resourceTypes) {
            setResourceTypes(resourceTypes);
        }

        protected void setResourceTypes(List<? extends PlatformResourceType> resourceTypes) {
            this.resourceTypes1_0 = ListUtil.cast(resourceTypes);
        }

    }

}
