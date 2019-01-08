package com.sap.cloud.lm.sl.mta.model.v2;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.NamedElement;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;
import com.sap.cloud.lm.sl.mta.model.VisitableElement;
import com.sap.cloud.lm.sl.mta.model.Visitor;

public class Platform implements VisitableElement, NamedElement, PropertiesContainer, ParametersContainer {

    
    private Map<String, Object> parameters;
    private List<ModuleType> moduleTypes2;
    private List<ResourceType> resourceTypes2;
    private String name;
    private String description;

    protected Platform() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setResourceTypes2(List<ResourceType> resourceTypes) {
        setResourceTypes(resourceTypes);
    }

    protected void setResourceTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v2.ResourceType> resourceTypes) {
        this.resourceTypes2 = ListUtil.cast(resourceTypes);
    }

    public List<ResourceType> getResourceTypes2() {
        return ListUtil.upcastUnmodifiable(getResourceTypes());
    }

    protected List<? extends ResourceType> getResourceTypes() {
        return resourceTypes2;
    }

    public void setModuleTypes2(List<ModuleType> moduleTypes) {
        setModuleTypes(moduleTypes);
    }

    protected void setModuleTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v2.ModuleType> moduleTypes) {
        this.moduleTypes2 = ListUtil.cast(moduleTypes);
    }

    public List<ModuleType> getModuleTypes2() {
        return ListUtil.upcastUnmodifiable(getModuleTypes());
    }

    protected List<? extends ModuleType> getModuleTypes() {
        return moduleTypes2;
    }

    @Override
    public Map<String, Object> getProperties() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> getParameters() {
        return MapUtil.unmodifiable(parameters);
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
        for (ModuleType moduleType : getModuleTypes()) {
            moduleType.accept(new ElementContext(moduleType, context), visitor);
        }
        for (ResourceType resourceType : getResourceTypes()) {
            resourceType.accept(new ElementContext(resourceType, context), visitor);
        }
    }

    public static class Builder {

        protected String name;
        protected String description;
        protected Map<String, Object> parameters;
        protected List<ModuleType> moduleTypes2;
        protected List<ResourceType> resourceTypes2;

        public Platform build() {
            Platform result = new Platform();
            result.setName(name);
            result.setDescription(description);
            result.setModuleTypes2(ObjectUtils.defaultIfNull(moduleTypes2, Collections.<ModuleType> emptyList()));
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            result.setResourceTypes2(ObjectUtils.defaultIfNull(resourceTypes2, Collections.<ResourceType> emptyList()));
            return result;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setModuleTypes2(List<ModuleType> moduleTypes) {
            setModuleTypes(moduleTypes);
        }

        protected void setModuleTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v2.ModuleType> moduleTypes) {
            this.moduleTypes2 = ListUtil.cast(moduleTypes);
        }

        public void setResourceTypes2(List<ResourceType> resourceTypes) {
            setResourceTypes(resourceTypes);
        }

        protected void setResourceTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v2.ResourceType> resourceTypes) {
            this.resourceTypes2 = ListUtil.cast(resourceTypes);
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }

    }

}
