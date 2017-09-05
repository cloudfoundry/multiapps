package com.sap.cloud.lm.sl.mta.mergers.v1_0;

import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.handlers.v1_0.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;
import com.sap.cloud.lm.sl.mta.model.Visitor;
import com.sap.cloud.lm.sl.mta.model.v1_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1_0.Module;
import com.sap.cloud.lm.sl.mta.model.v1_0.ModuleType;
import com.sap.cloud.lm.sl.mta.model.v1_0.Resource;
import com.sap.cloud.lm.sl.mta.model.v1_0.ResourceType;
import com.sap.cloud.lm.sl.mta.model.v1_0.Platform;

public class PlatformMerger extends Visitor {

    protected final Platform platformType;
    protected final DescriptorHandler handler;

    public PlatformMerger(Platform platformType, DescriptorHandler handler) {
        this.platformType = platformType;
        this.handler = handler;
    }

    public void mergeInto(DeploymentDescriptor descriptor) {
        descriptor.accept(this);
    }

    @Override
    public void visit(ElementContext context, Module element) {
        ModuleType platformTypeElement = handler.findModuleType(platformType, element.getType());
        if (platformTypeElement == null) {
            return;
        }
        element.setProperties(MapUtil.merge(platformTypeElement.getProperties(), element.getProperties()));
    }

    @Override
    public void visit(ElementContext context, DeploymentDescriptor element) {
        PropertiesContainer platformTypeElement = platformType;
        element.setProperties(MapUtil.merge(platformTypeElement.getProperties(), element.getProperties()));
    }

    @Override
    public void visit(ElementContext context, Resource element) {
        if (element.getType() == null) {
            return;
        }
        ResourceType platformTypeElement = handler.findResourceType(platformType, element.getType());
        if (platformTypeElement == null) {
            return;
        }
        element.setProperties(MapUtil.merge(platformTypeElement.getProperties(), element.getProperties()));
    }
}
