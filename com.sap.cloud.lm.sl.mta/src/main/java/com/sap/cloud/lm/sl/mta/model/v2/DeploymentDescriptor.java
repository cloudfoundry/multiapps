package com.sap.cloud.lm.sl.mta.model.v2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;
import com.sap.cloud.lm.sl.mta.parsers.v2.DeploymentDescriptorParser;
import com.sap.cloud.lm.sl.mta.util.YamlElement;
import com.sap.cloud.lm.sl.mta.util.YamlElementOrder;

@YamlElementOrder({ "id", "description", "version", "provider", "copyright", "schemaVersion", "parameters", "modules2", "resources2" })
public class DeploymentDescriptor extends com.sap.cloud.lm.sl.mta.model.v1.DeploymentDescriptor implements ParametersContainer {

    @YamlElement(DeploymentDescriptorParser.MODULES)
    private List<Module> modules2;
    @YamlElement(DeploymentDescriptorParser.RESOURCES)
    private List<Resource> resources2;
    @YamlElement(DeploymentDescriptorParser.PARAMETERS)
    private Map<String, Object> parameters;

    protected DeploymentDescriptor() {

    }

    public List<Module> getModules2() {
        return ListUtil.upcastUnmodifiable(getModules());
    }

    @Override
    protected List<? extends Module> getModules() {
        return modules2;
    }

    public List<Resource> getResources2() {
        return ListUtil.upcastUnmodifiable(getResources());
    }

    @Override
    protected List<? extends Resource> getResources() {
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

    public void setModules2(List<Module> modules) {
        setModules(modules);
    }

    @Override
    protected void setModules(List<? extends com.sap.cloud.lm.sl.mta.model.v1.Module> modules) {
        this.modules2 = ListUtil.cast(modules);
    }

    public void setResources2(List<Resource> resources) {
        setResources(resources);
    }

    @Override
    protected void setResources(List<? extends com.sap.cloud.lm.sl.mta.model.v1.Resource> resources) {
        this.resources2 = ListUtil.cast(resources);
    }

    @Override
    public void setProperties(Map<String, Object> properties) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = new LinkedHashMap<>(parameters);
    }

    @Override
    public DeploymentDescriptor copyOf() {
        Builder result = new Builder();
        result.setId(getId());
        result.setProvider(getProvider());
        result.setDescription(getDescription());
        result.setVersion(getVersion());
        result.setCopyright(getCopyright());
        result.setSchemaVersion(getSchemaVersion());
        result.setParameters(getParameters());
        List<Module> clonedModules = new ArrayList<>();
        for (Module module : getModules2()) {
            clonedModules.add(module.copyOf());
        }
        result.setModules2(clonedModules);
        List<Resource> clonedResources = new ArrayList<>();
        for (Resource resource : getResources2()) {
            clonedResources.add(resource.copyOf());
        }
        result.setResources2(clonedResources);
        return result.build();
    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v1.DeploymentDescriptor.Builder {

        protected List<Module> modules2;
        protected List<Resource> resources2;
        protected Map<String, Object> parameters;

        @Override
        public DeploymentDescriptor build() {
            DeploymentDescriptor result = new DeploymentDescriptor();
            result.setId(id);
            result.setProvider(provider);
            result.setDescription(description);
            result.setVersion(version);
            result.setCopyright(copyright);
            result.setSchemaVersion(schemaVersion);
            result.setModules2(ObjectUtils.defaultIfNull(modules2, Collections.<Module> emptyList()));
            result.setResources2(ObjectUtils.defaultIfNull(resources2, Collections.<Resource> emptyList()));
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            return result;
        }

        public void setModules2(List<Module> modules) {
            setModules(modules);
        }

        @Override
        protected void setModules(List<? extends com.sap.cloud.lm.sl.mta.model.v1.Module> modules) {
            this.modules2 = ListUtil.cast(modules);
        }

        public void setResources2(List<Resource> resources) {
            setResources(resources);
        }

        @Override
        protected void setResources(List<? extends com.sap.cloud.lm.sl.mta.model.v1.Resource> resources) {
            this.resources2 = ListUtil.cast(resources);
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }

        @Override
        public void setProperties(Map<String, Object> properties) {
            throw new UnsupportedOperationException();
        }

    }

}
