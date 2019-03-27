package com.sap.cloud.lm.sl.mta.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;

public class Platform implements VisitableElement, NamedElement, ParametersContainer {

    private String name;
    private List<ModuleType> moduleTypes = Collections.emptyList();
    private List<ResourceType> resourceTypes = Collections.emptyList();
    private Map<String, Object> parameters = Collections.emptyMap();

    @Override
    public String getName() {
        return name;
    }

    public List<ModuleType> getModuleTypes() {
        return moduleTypes;
    }

    public List<ResourceType> getResourceTypes() {
        return resourceTypes;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public Platform setName(String name) {
        this.name = ObjectUtils.defaultIfNull(name, this.name);
        return this;
    }

    public Platform setModuleTypes(List<ModuleType> moduleTypes) {
        this.moduleTypes = ObjectUtils.defaultIfNull(moduleTypes, this.moduleTypes);
        return this;
    }

    public Platform setResourceTypes(List<ResourceType> resourceTypes) {
        this.resourceTypes = ObjectUtils.defaultIfNull(resourceTypes, this.resourceTypes);
        return this;
    }

    public Platform setParameters(Map<String, Object> parameters) {
        this.parameters = ObjectUtils.defaultIfNull(parameters, this.parameters);
        return this;
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

}
