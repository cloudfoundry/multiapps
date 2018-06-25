package com.sap.cloud.lm.sl.mta.model.v3_1;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.Metadata;
import com.sap.cloud.lm.sl.mta.model.ParametersWithMetadataContainer;
import com.sap.cloud.lm.sl.mta.parsers.v3_1.DeploymentDescriptorParser;
import com.sap.cloud.lm.sl.mta.util.MetadataConverter;
import com.sap.cloud.lm.sl.mta.util.YamlAdapter;
import com.sap.cloud.lm.sl.mta.util.YamlElement;
import com.sap.cloud.lm.sl.mta.util.YamlElementOrder;

@YamlElementOrder({ "id", "description", "version", "provider", "copyright", "schemaVersion", "parameters", "parametersMetadata",
    "modules3_1", "resources3_1" })
public class DeploymentDescriptor extends com.sap.cloud.lm.sl.mta.model.v3_0.DeploymentDescriptor
    implements ParametersWithMetadataContainer {

    @YamlElement(DeploymentDescriptorParser.MODULES)
    private List<Module> modules3_1;
    @YamlElement(DeploymentDescriptorParser.RESOURCES)
    private List<Resource> resources3_1;
    @YamlElement(DeploymentDescriptorParser.PARAMETERS_METADATA)
    @YamlAdapter(MetadataConverter.class)
    private Metadata parametersMetadata;

    protected DeploymentDescriptor() {

    }

    public List<Module> getModules3_1() {
        return ListUtil.upcastUnmodifiable(getModules());
    }

    @Override
    protected List<? extends Module> getModules() {
        return modules3_1;
    }

    public List<Resource> getResources3_1() {
        return ListUtil.upcastUnmodifiable(getResources());
    }

    @Override
    protected List<? extends Resource> getResources() {
        return resources3_1;
    }

    public void setModules3_1(List<Module> modules) {
        setModules(modules);
    }

    @Override
    protected void setModules(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.Module> modules) {
        this.modules3_1 = ListUtil.cast(modules);
    }

    public void setResources3_1(List<Resource> resources) {
        setResources(resources);
    }

    @Override
    protected void setResources(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.Resource> resources) {
        this.resources3_1 = ListUtil.cast(resources);
    }

    @Override
    public void setParametersMetadata(Metadata metadata) {
        parametersMetadata = metadata;
    }

    @Override
    public Metadata getParametersMetadata() {
        return parametersMetadata;
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
        for (Module module : getModules3_1()) {
            clonedModules.add(module.copyOf());
        }
        result.setModules3_1(clonedModules);
        List<Resource> clonedResources = new ArrayList<>();
        for (Resource resource : getResources3_1()) {
            clonedResources.add(resource.copyOf());
        }
        result.setResources3_1(clonedResources);
        result.setParametersMetadata(getParametersMetadata());
        return result.build();
    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v3_0.DeploymentDescriptor.Builder {

        protected List<Module> modules3_1;
        protected List<Resource> resources3_1;
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
            result.setModules3_1(getOrDefault(modules3_1, Collections.<Module> emptyList()));
            result.setResources3_1(getOrDefault(resources3_1, Collections.<Resource> emptyList()));
            result.setParameters(getOrDefault(parameters, Collections.<String, Object> emptyMap()));
            result.setParametersMetadata(parametersMetadata);
            return result;
        }

        public void setModules3_1(List<Module> modules) {
            setModules(modules);
        }

        @Override
        protected void setModules(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.Module> modules) {
            this.modules3_1 = ListUtil.cast(modules);
        }

        public void setResources3_1(List<Resource> resources) {
            setResources(resources);
        }

        @Override
        protected void setResources(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.Resource> resources) {
            this.resources3_1 = ListUtil.cast(resources);
        }

        public void setParametersMetadata(Metadata parametersMetadata) {
            this.parametersMetadata = parametersMetadata;
        }

    }

}
