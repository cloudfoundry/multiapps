package com.sap.cloud.lm.sl.mta.model.v3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.Metadata;
import com.sap.cloud.lm.sl.mta.model.ParametersWithMetadataContainer;
import com.sap.cloud.lm.sl.mta.parsers.v3.DeploymentDescriptorParser;
import com.sap.cloud.lm.sl.mta.util.MetadataConverter;
import com.sap.cloud.lm.sl.mta.util.YamlAdapter;
import com.sap.cloud.lm.sl.mta.util.YamlElement;
import com.sap.cloud.lm.sl.mta.util.YamlElementOrder;

@YamlElementOrder({ "id", "description", "version", "provider", "copyright", "schemaVersion", "parameters", "parametersMetadata",
    "modules3", "resources3" })
public class DeploymentDescriptor extends com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor implements ParametersWithMetadataContainer {

    @YamlElement(DeploymentDescriptorParser.MODULES)
    private List<Module> modules3;
    @YamlElement(DeploymentDescriptorParser.RESOURCES)
    private List<Resource> resources3;
    @YamlElement(DeploymentDescriptorParser.PARAMETERS_METADATA)
    @YamlAdapter(MetadataConverter.class)
    private Metadata parametersMetadata;

    protected DeploymentDescriptor() {

    }

    @Override
    public void setParametersMetadata(Metadata metadata) {
        parametersMetadata = metadata;
    }

    @Override
    public Metadata getParametersMetadata() {
        return parametersMetadata;
    }

    public List<Module> getModules3() {
        return ListUtil.upcastUnmodifiable(getModules());
    }

    @Override
    protected List<? extends Module> getModules() {
        return modules3;
    }

    public List<Resource> getResources3() {
        return ListUtil.upcastUnmodifiable(getResources());
    }

    @Override
    protected List<? extends Resource> getResources() {
        return resources3;
    }

    public void setModules3(List<Module> modules) {
        setModules(modules);
    }

    @Override
    protected void setModules(List<? extends com.sap.cloud.lm.sl.mta.model.v1.Module> modules) {
        this.modules3 = ListUtil.cast(modules);
    }

    public void setResources3(List<Resource> resources) {
        setResources(resources);
    }

    @Override
    protected void setResources(List<? extends com.sap.cloud.lm.sl.mta.model.v1.Resource> resources) {
        this.resources3 = ListUtil.cast(resources);
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
        result.setParametersMetadata(getParametersMetadata());
        List<Module> clonedModules = new ArrayList<>();
        for (Module module : getModules3()) {
            clonedModules.add(module.copyOf());
        }
        result.setModules3(clonedModules);
        List<Resource> clonedResources = new ArrayList<>();
        for (Resource resource : getResources3()) {
            clonedResources.add(resource.copyOf());
        }
        result.setResources3(clonedResources);
        return result.build();
    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor.Builder {

        protected List<Module> modules3;
        protected List<Resource> resources3;
        protected Metadata parametersMetadata;

        @Override
        public DeploymentDescriptor build() {
            DeploymentDescriptor result = new DeploymentDescriptor();
            result.setId(id);
            result.setProvider(provider);
            result.setDescription(description);
            result.setVersion(version);
            result.setCopyright(copyright);
            result.setSchemaVersion(schemaVersion);
            result.setModules3(ObjectUtils.defaultIfNull(modules3, Collections.<Module> emptyList()));
            result.setResources3(ObjectUtils.defaultIfNull(resources3, Collections.<Resource> emptyList()));
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            result.setParametersMetadata(ObjectUtils.defaultIfNull(parametersMetadata, Metadata.DEFAULT_METADATA));
            return result;
        }

        public void setParametersMetadata(Metadata parametersMetadata) {
            this.parametersMetadata = parametersMetadata;
        }

        public void setModules3(List<Module> modules) {
            setModules(modules);
        }

        @Override
        protected void setModules(List<? extends com.sap.cloud.lm.sl.mta.model.v1.Module> modules) {
            this.modules3 = ListUtil.cast(modules);
        }

        public void setResources3(List<Resource> resources) {
            setResources(resources);
        }

        @Override
        protected void setResources(List<? extends com.sap.cloud.lm.sl.mta.model.v1.Resource> resources) {
            this.resources3 = ListUtil.cast(resources);
        }

    }

}
