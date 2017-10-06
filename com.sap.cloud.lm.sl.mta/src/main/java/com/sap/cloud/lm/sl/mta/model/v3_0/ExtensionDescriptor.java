package com.sap.cloud.lm.sl.mta.model.v3_0;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.Collections;
import java.util.List;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.parsers.v3_0.ExtensionDescriptorParser;
import com.sap.cloud.lm.sl.mta.util.YamlElement;
import com.sap.cloud.lm.sl.mta.util.YamlElementOrder;

@YamlElementOrder({ "id", "description", "parentId", "provider", "schemaVersion", "parameters", "modules3_0", "resources3_0" })
public class ExtensionDescriptor extends com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionDescriptor {

    @YamlElement(ExtensionDescriptorParser.MODULES)
    private List<ExtensionModule> modules3_0;
    @YamlElement(ExtensionDescriptorParser.RESOURCES)
    private List<ExtensionResource> resources3_0;

    protected ExtensionDescriptor() {

    }

    public List<ExtensionModule> getModules3_0() {
        return ListUtil.upcastUnmodifiable(getModules());
    }

    @Override
    public List<? extends ExtensionModule> getModules() {
        return modules3_0;
    }

    public List<ExtensionResource> getResources3_0() {
        return ListUtil.upcastUnmodifiable(getResources());
    }

    @Override
    public List<? extends ExtensionResource> getResources() {
        return resources3_0;
    }

    public void setModules3_0(List<ExtensionModule> modules) {
        setModules(modules);
    }

    @Override
    protected void setModules(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionModule> modules) {
        this.modules3_0 = ListUtil.cast(modules);
    }

    public void setResources3_0(List<ExtensionResource> resources) {
        setResources(resources);
    }

    @Override
    protected void setResources(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionResource> resources) {
        this.resources3_0 = ListUtil.cast(resources);
    }

    public static class ExtensionDescriptorBuilder
        extends com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionDescriptor.ExtensionDescriptorBuilder {

        protected List<ExtensionModule> modules3_0;
        protected List<ExtensionResource> resources3_0;

        @Override
        public ExtensionDescriptor build() {
            ExtensionDescriptor result = new ExtensionDescriptor();
            result.setId(id);
            result.setDescription(description);
            result.setParentId(parentId);
            result.setProvider(provider);
            result.setSchemaVersion(schemaVersion);
            result.setResources3_0(getOrDefault(resources3_0, Collections.<ExtensionResource> emptyList()));
            result.setDeployTargets(getOrDefault(deployTargets, Collections.<String> emptyList()));
            result.setParameters(getOrDefault(parameters, Collections.<String, Object> emptyMap()));
            result.setModules3_0(getOrDefault(modules3_0, Collections.<ExtensionModule> emptyList()));
            return result;
        }

        public void setModules3_0(List<ExtensionModule> modules) {
            setModules(modules);
        }

        @Override
        protected void setModules(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionModule> modules) {
            this.modules3_0 = ListUtil.cast(modules);
        }

        public void setResources3_0(List<ExtensionResource> resources) {
            setResources(resources);
        }

        @Override
        protected void setResources(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionResource> resources) {
            this.resources3_0 = ListUtil.cast(resources);
        }

    }

}
