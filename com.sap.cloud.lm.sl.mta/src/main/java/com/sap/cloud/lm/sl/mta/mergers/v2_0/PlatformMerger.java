package com.sap.cloud.lm.sl.mta.mergers.v2_0;

import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.handlers.v2_0.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.v2_0.Platform;
import com.sap.cloud.lm.sl.mta.model.v2_0.PlatformModuleType;
import com.sap.cloud.lm.sl.mta.model.v2_0.PlatformResourceType;

public class PlatformMerger extends com.sap.cloud.lm.sl.mta.mergers.v1_0.PlatformMerger {

    public PlatformMerger(Platform platform, DescriptorHandler handler) {
        super(platform, handler);
    }

    @Override
    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v1_0.DeploymentDescriptor descriptor) {
        visit(context, (com.sap.cloud.lm.sl.mta.model.v2_0.DeploymentDescriptor) descriptor);
    }

    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v2_0.DeploymentDescriptor descriptor) {
        com.sap.cloud.lm.sl.mta.model.v2_0.Platform v2Platform = (Platform) platform;
        descriptor.setParameters(MapUtil.merge(v2Platform.getParameters(), descriptor.getParameters()));
    }

    @Override
    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v1_0.Resource resource) {
        visit(context, (com.sap.cloud.lm.sl.mta.model.v2_0.Resource) resource);
    }

    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v2_0.Resource resource) {
        if (resource.getType() == null) {
            return;
        }
        PlatformResourceType resourceType = (PlatformResourceType) handler.findPlatformResourceType(platform, resource.getType());
        if (resourceType == null) {
            return;
        }
        resource.setParameters(MapUtil.merge(resourceType.getParameters(), resource.getParameters()));
    }

    @Override
    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v1_0.Module module) {
        visit(context, (com.sap.cloud.lm.sl.mta.model.v2_0.Module) module);
    }

    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v2_0.Module module) {
        PlatformModuleType moduleType = (PlatformModuleType) handler.findPlatformModuleType(platform, module.getType());
        if (moduleType == null) {
            return;
        }
        module.setProperties(MapUtil.merge(moduleType.getProperties(), module.getProperties()));
        module.setParameters(MapUtil.merge(moduleType.getParameters(), module.getParameters()));
    }

}
