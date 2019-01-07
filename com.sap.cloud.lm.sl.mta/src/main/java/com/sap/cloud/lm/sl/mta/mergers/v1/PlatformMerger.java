package com.sap.cloud.lm.sl.mta.mergers.v1;

import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.handlers.v1.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;
import com.sap.cloud.lm.sl.mta.model.Visitor;
import com.sap.cloud.lm.sl.mta.model.v1.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1.Module;
import com.sap.cloud.lm.sl.mta.model.v1.Platform;
import com.sap.cloud.lm.sl.mta.model.v1.Resource;

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
    public void visit(ElementContext context, Module module) {
        PropertiesContainer moduleType = handler.findModuleType(platform, module.getType());
        if (moduleType == null) {
            return;
        }
        module.setProperties(MapUtil.merge(moduleType.getProperties(), module.getProperties()));
    }

    @Override
    public void visit(ElementContext context, DeploymentDescriptor descriptor) {
        descriptor.setProperties(MapUtil.merge(platform.getProperties(), descriptor.getProperties()));
    }

    @Override
    public void visit(ElementContext context, Resource resource) {
        if (resource.getType() == null) {
            return;
        }
        PropertiesContainer resourceType = handler.findResourceType(platform, resource.getType());
        if (resourceType == null) {
            return;
        }
        resource.setProperties(MapUtil.merge(resourceType.getProperties(), resource.getProperties()));
    }
}
