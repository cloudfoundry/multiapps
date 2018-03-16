package com.sap.cloud.lm.sl.mta.model.v3_0;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.parsers.v3_0.DeploymentDescriptorParser;
import com.sap.cloud.lm.sl.mta.util.YamlElement;
import com.sap.cloud.lm.sl.mta.util.YamlElementOrder;

@YamlElementOrder({ "id", "description", "version", "provider", "copyright", "schemaVersion", "parameters", "modules3_0", "resources3_0" })
public class DeploymentDescriptor extends com.sap.cloud.lm.sl.mta.model.v2_0.DeploymentDescriptor {

    @YamlElement(DeploymentDescriptorParser.MODULES)
    private List<Module> modules3_0;
    @YamlElement(DeploymentDescriptorParser.RESOURCES)
    private List<Resource> resources3_0;

    protected DeploymentDescriptor() {

    }

    public List<Module> getModules3_0() {
        return ListUtil.upcastUnmodifiable(getModules());
    }

    @Override
    protected List<? extends Module> getModules() {
        return modules3_0;
    }

    public List<Resource> getResources3_0() {
        return ListUtil.upcastUnmodifiable(getResources());
    }

    @Override
    protected List<? extends Resource> getResources() {
        return resources3_0;
    }

    public void setModules3_0(List<Module> modules) {
        setModules(modules);
    }

    @Override
    protected void setModules(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.Module> modules) {
        this.modules3_0 = ListUtil.cast(modules);
    }

    public void setResources3_0(List<Resource> resources) {
        setResources(resources);
    }

    @Override
    protected void setResources(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.Resource> resources) {
        this.resources3_0 = ListUtil.cast(resources);
    }

    public DeploymentDescriptor copyOf() {
        DeploymentDescriptorBuilder result = new DeploymentDescriptorBuilder();
        result.setId(getId());
        result.setProvider(getProvider());
        result.setDescription(getDescription());
        result.setVersion(getVersion());
        result.setCopyright(getCopyright());
        result.setSchemaVersion(getSchemaVersion());
        result.setParameters(getParameters());
        List<Module> clonedModules = new ArrayList<>();
        for (Module module : getModules3_0()) {
            clonedModules.add(module.copyOf());
        }
        result.setModules3_0(clonedModules);
        List<Resource> clonedResources = new ArrayList<>();
        for (Resource resource : getResources3_0()) {
            clonedResources.add(resource.copyOf());
        }
        result.setResources3_0(clonedResources);
        return result.build();
    }

    public static class DeploymentDescriptorBuilder
        extends com.sap.cloud.lm.sl.mta.model.v2_0.DeploymentDescriptor.DeploymentDescriptorBuilder {

        protected List<Module> modules3_0;
        protected List<Resource> resources3_0;

        @Override
        public DeploymentDescriptor build() {
            DeploymentDescriptor result = new DeploymentDescriptor();
            result.setId(id);
            result.setProvider(provider);
            result.setDescription(description);
            result.setVersion(version);
            result.setCopyright(copyright);
            result.setSchemaVersion(schemaVersion);
            result.setModules3_0(getOrDefault(modules3_0, Collections.<Module> emptyList()));
            result.setResources3_0(getOrDefault(resources3_0, Collections.<Resource> emptyList()));
            result.setParameters(getOrDefault(parameters, Collections.<String, Object> emptyMap()));
            return result;
        }

        public void setModules3_0(List<Module> modules) {
            setModules(modules);
        }

        @Override
        protected void setModules(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.Module> modules) {
            this.modules3_0 = ListUtil.cast(modules);
        }

        public void setResources3_0(List<Resource> resources) {
            setResources(resources);
        }

        @Override
        protected void setResources(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.Resource> resources) {
            this.resources3_0 = ListUtil.cast(resources);
        }

    }

}
