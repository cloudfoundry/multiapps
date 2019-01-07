package com.sap.cloud.lm.sl.mta.mergers.v2;

import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.v2.Platform;
import com.sap.cloud.lm.sl.mta.model.v2.ModuleType;
import com.sap.cloud.lm.sl.mta.model.v2.ResourceType;

public class PlatformMerger extends com.sap.cloud.lm.sl.mta.mergers.v1.PlatformMerger {

    public PlatformMerger(Platform platform, DescriptorHandler handler) {
        super(platform, handler);
    }

    @Override
    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v1.DeploymentDescriptor descriptor) {
        visit(context, (com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor) descriptor);
    }

    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor descriptor) {
        com.sap.cloud.lm.sl.mta.model.v2.Platform v2Platform = (Platform) platform;
        descriptor.setParameters(MapUtil.merge(v2Platform.getParameters(), descriptor.getParameters()));
    }

    @Override
    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v1.Resource resource) {
        visit(context, (com.sap.cloud.lm.sl.mta.model.v2.Resource) resource);
    }

    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v2.Resource resource) {
        if (resource.getType() == null) {
            return;
        }
        ResourceType resourceType = (ResourceType) handler.findResourceType(platform, resource.getType());
        if (resourceType == null) {
            return;
        }
        resource.setParameters(MapUtil.merge(resourceType.getParameters(), resource.getParameters()));
    }

    @Override
    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v1.Module module) {
        visit(context, (com.sap.cloud.lm.sl.mta.model.v2.Module) module);
    }

    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v2.Module module) {
        ModuleType moduleType = (ModuleType) handler.findModuleType(platform, module.getType());
        if (moduleType == null) {
            return;
        }
        module.setProperties(MapUtil.merge(moduleType.getProperties(), module.getProperties()));
        module.setParameters(MapUtil.merge(moduleType.getParameters(), module.getParameters()));
    }

}
