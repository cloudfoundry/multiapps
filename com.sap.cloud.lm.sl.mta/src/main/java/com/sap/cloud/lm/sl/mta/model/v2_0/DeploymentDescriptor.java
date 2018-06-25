package com.sap.cloud.lm.sl.mta.model.v2_0;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;
import com.sap.cloud.lm.sl.mta.parsers.v2_0.DeploymentDescriptorParser;
import com.sap.cloud.lm.sl.mta.util.YamlElement;
import com.sap.cloud.lm.sl.mta.util.YamlElementOrder;

@YamlElementOrder({ "id", "description", "version", "provider", "copyright", "schemaVersion", "parameters", "modules2_0", "resources2_0" })
public class DeploymentDescriptor extends com.sap.cloud.lm.sl.mta.model.v1_0.DeploymentDescriptor implements ParametersContainer {

    @YamlElement(DeploymentDescriptorParser.MODULES)
    private List<Module> modules2_0;
    @YamlElement(DeploymentDescriptorParser.RESOURCES)
    private List<Resource> resources2_0;
    @YamlElement(DeploymentDescriptorParser.PARAMETERS)
    private Map<String, Object> parameters;

    protected DeploymentDescriptor() {

    }

    public List<Module> getModules2_0() {
        return ListUtil.upcastUnmodifiable(getModules());
    }

    @Override
    protected List<? extends Module> getModules() {
        return modules2_0;
    }

    public List<Resource> getResources2_0() {
        return ListUtil.upcastUnmodifiable(getResources());
    }

    @Override
    protected List<? extends Resource> getResources() {
        return resources2_0;
    }

    @Override
    public Map<String, Object> getProperties() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> getParameters() {
        return MapUtil.unmodifiable(parameters);
    }

    public void setModules2_0(List<Module> modules) {
        setModules(modules);
    }

    @Override
    protected void setModules(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.Module> modules) {
        this.modules2_0 = ListUtil.cast(modules);
    }

    public void setResources2_0(List<Resource> resources) {
        setResources(resources);
    }

    @Override
    protected void setResources(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.Resource> resources) {
        this.resources2_0 = ListUtil.cast(resources);
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
        for (Module module : getModules2_0()) {
            clonedModules.add(module.copyOf());
        }
        result.setModules2_0(clonedModules);
        List<Resource> clonedResources = new ArrayList<>();
        for (Resource resource : getResources2_0()) {
            clonedResources.add(resource.copyOf());
        }
        result.setResources2_0(clonedResources);
        return result.build();
    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v1_0.DeploymentDescriptor.Builder {

        protected List<Module> modules2_0;
        protected List<Resource> resources2_0;
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
            result.setModules2_0(getOrDefault(modules2_0, Collections.<Module> emptyList()));
            result.setResources2_0(getOrDefault(resources2_0, Collections.<Resource> emptyList()));
            result.setParameters(getOrDefault(parameters, Collections.<String, Object> emptyMap()));
            return result;
        }

        public void setModules2_0(List<Module> modules) {
            setModules(modules);
        }

        @Override
        protected void setModules(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.Module> modules) {
            this.modules2_0 = ListUtil.cast(modules);
        }

        public void setResources2_0(List<Resource> resources) {
            setResources(resources);
        }

        @Override
        protected void setResources(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.Resource> resources) {
            this.resources2_0 = ListUtil.cast(resources);
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
