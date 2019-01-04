package com.sap.cloud.lm.sl.mta.mergers.v2;

import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.Visitor;
import com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2.Module;
import com.sap.cloud.lm.sl.mta.model.v2.ModuleType;
import com.sap.cloud.lm.sl.mta.model.v2.Platform;
import com.sap.cloud.lm.sl.mta.model.v2.Resource;
import com.sap.cloud.lm.sl.mta.model.v2.ResourceType;

public class PlatformMerger extends Visitor {

    protected final Platform platform;
    protected final DescriptorHandler handler;

    public PlatformMerger(Platform platform, DescriptorHandler handler) {
        this.platform = platform;
        this.handler = handler;
    }

    public void mergeInto(DeploymentDescriptor descriptor) {
        descriptor.accept(this);
    }

    @Override
    public void visit(ElementContext context, DeploymentDescriptor descriptor) {
        Platform v2Platform = platform;
        descriptor.setParameters(MapUtil.merge(v2Platform.getParameters(), descriptor.getParameters()));
    }

    @Override
    public void visit(ElementContext context, Resource resource) {
        if (resource.getType() == null) {
            return;
        }
        ResourceType resourceType = handler.findResourceType(platform, resource.getType());
        if (resourceType == null) {
            return;
        }
        resource.setParameters(MapUtil.merge(resourceType.getParameters(), resource.getParameters()));
    }

    @Override
    public void visit(ElementContext context, Module module) {
        ModuleType moduleType = handler.findModuleType(platform, module.getType());
        if (moduleType == null) {
            return;
        }
        module.setProperties(MapUtil.merge(moduleType.getProperties(), module.getProperties()));
        module.setParameters(MapUtil.merge(moduleType.getParameters(), module.getParameters()));
    }

}
