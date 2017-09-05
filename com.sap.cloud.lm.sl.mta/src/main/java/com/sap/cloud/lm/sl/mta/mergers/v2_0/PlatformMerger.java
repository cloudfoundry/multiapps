package com.sap.cloud.lm.sl.mta.mergers.v2_0;

import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.handlers.v2_0.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.v2_0.ModuleType;
import com.sap.cloud.lm.sl.mta.model.v2_0.ResourceType;
import com.sap.cloud.lm.sl.mta.model.v2_0.Platform;

public class PlatformMerger extends com.sap.cloud.lm.sl.mta.mergers.v1_0.PlatformMerger {

    public PlatformMerger(Platform platformType, DescriptorHandler handler) {
        super(platformType, handler);
    }

    @Override
    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v1_0.DeploymentDescriptor element) {
        visit(context, (com.sap.cloud.lm.sl.mta.model.v2_0.DeploymentDescriptor) element);
    }

    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v2_0.DeploymentDescriptor element) {
        Platform platformTypeElement = (Platform) platformType;
        element.setParameters(MapUtil.merge(platformTypeElement.getParameters(), element.getParameters()));
    }

    @Override
    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v1_0.Resource element) {
        visit(context, (com.sap.cloud.lm.sl.mta.model.v2_0.Resource) element);
    }

    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v2_0.Resource element) {
        if (element.getType() == null) {
            return;
        }
        ResourceType platformTypeElement = (ResourceType) handler.findResourceType(platformType, element.getType());
        if (platformTypeElement == null) {
            return;
        }
        element.setParameters(MapUtil.merge(platformTypeElement.getParameters(), element.getParameters()));
    }

    @Override
    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v1_0.Module element) {
        visit(context, (com.sap.cloud.lm.sl.mta.model.v2_0.Module) element);
    }

    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v2_0.Module element) {
        ModuleType platformTypeElement = (ModuleType) handler.findModuleType(platformType, element.getType());
        if (platformTypeElement == null) {
            return;
        }
        element.setProperties(MapUtil.merge(platformTypeElement.getProperties(), element.getProperties()));
        element.setParameters(MapUtil.merge(platformTypeElement.getParameters(), element.getParameters()));
    }
}
