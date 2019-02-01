package com.sap.cloud.lm.sl.mta.model.v3;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.common.util.yaml.YamlElement;
import com.sap.cloud.lm.sl.common.util.yaml.YamlElementOrder;
import com.sap.cloud.lm.sl.mta.parsers.v3.ExtensionDescriptorParser;

@YamlElementOrder({ "schemaVersion", "id", "parentId", "description", "provider", "parameters", "modules3", "resources3" })
public class ExtensionDescriptor extends com.sap.cloud.lm.sl.mta.model.v2.ExtensionDescriptor {

    @YamlElement(ExtensionDescriptorParser.MODULES)
    private List<ExtensionModule> modules3;
    @YamlElement(ExtensionDescriptorParser.RESOURCES)
    private List<ExtensionResource> resources3;

    protected ExtensionDescriptor() {

    }

    public List<ExtensionModule> getModules3() {
        return ListUtil.upcastUnmodifiable(getModules());
    }

    @Override
    public List<? extends ExtensionModule> getModules() {
        return modules3;
    }

    public List<ExtensionResource> getResources3() {
        return ListUtil.upcastUnmodifiable(getResources());
    }

    @Override
    public List<? extends ExtensionResource> getResources() {
        return resources3;
    }

    public void setModules3(List<ExtensionModule> modules) {
        setModules(modules);
    }

    @Override
    protected void setModules(List<? extends com.sap.cloud.lm.sl.mta.model.v2.ExtensionModule> modules) {
        this.modules3 = ListUtil.cast(modules);
    }

    public void setResources3(List<ExtensionResource> resources) {
        setResources(resources);
    }

    @Override
    protected void setResources(List<? extends com.sap.cloud.lm.sl.mta.model.v2.ExtensionResource> resources) {
        this.resources3 = ListUtil.cast(resources);
    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v2.ExtensionDescriptor.Builder {

        protected List<ExtensionModule> modules3;
        protected List<ExtensionResource> resources3;

        @Override
        public ExtensionDescriptor build() {
            ExtensionDescriptor result = new ExtensionDescriptor();
            result.setId(id);
            result.setDescription(description);
            result.setParentId(parentId);
            result.setProvider(provider);
            result.setSchemaVersion(schemaVersion);
            result.setResources3(ObjectUtils.defaultIfNull(resources3, Collections.<ExtensionResource> emptyList()));
            result.setDeployTargets(ObjectUtils.defaultIfNull(deployTargets, Collections.<String> emptyList()));
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            result.setModules3(ObjectUtils.defaultIfNull(modules3, Collections.<ExtensionModule> emptyList()));
            return result;
        }

        public void setModules3(List<ExtensionModule> modules) {
            setModules(modules);
        }

        @Override
        protected void setModules(List<? extends com.sap.cloud.lm.sl.mta.model.v2.ExtensionModule> modules) {
            this.modules3 = ListUtil.cast(modules);
        }

        public void setResources3(List<ExtensionResource> resources) {
            setResources(resources);
        }

        @Override
        protected void setResources(List<? extends com.sap.cloud.lm.sl.mta.model.v2.ExtensionResource> resources) {
            this.resources3 = ListUtil.cast(resources);
        }

    }

}
