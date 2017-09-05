package com.sap.cloud.lm.sl.mta.mergers.v2_0;

import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.handlers.v2_0.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.v2_0.PlatformModuleType;
import com.sap.cloud.lm.sl.mta.model.v2_0.PlatformResourceType;
import com.sap.cloud.lm.sl.mta.model.v2_0.Target;

public class TargetMerger extends com.sap.cloud.lm.sl.mta.mergers.v1_0.TargetMerger {

    public TargetMerger(Target target, DescriptorHandler handler) {
        super(target, handler);
    }

    @Override
    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v1_0.Module element) {
        visit(context, (com.sap.cloud.lm.sl.mta.model.v2_0.Module) element);
    }

    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v2_0.Module element) {
        PlatformModuleType platformElement = (PlatformModuleType) handler.findPlatformModuleType(target, element.getType());
        if (platformElement == null) {
            return;
        }
        element.setProperties(MapUtil.merge(platformElement.getProperties(), element.getProperties()));
        element.setParameters(MapUtil.merge(platformElement.getParameters(), element.getParameters()));
    }

    @Override
    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v1_0.DeploymentDescriptor element) {
        visit(context, (com.sap.cloud.lm.sl.mta.model.v2_0.DeploymentDescriptor) element);
    }

    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v2_0.DeploymentDescriptor element) {
        Target targetElement = (Target) target;
        element.setParameters(MapUtil.merge(targetElement.getParameters(), element.getParameters()));
    }

    @Override
    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v1_0.Resource element) {
        visit(context, (com.sap.cloud.lm.sl.mta.model.v2_0.Resource) element);
    }

    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v2_0.Resource element) {
        if (element.getType() == null) {
            return;
        }
        PlatformResourceType platformElement = (PlatformResourceType) handler.findTargetResourceType(target, element.getType());
        if (platformElement == null) {
            return;
        }
        element.setParameters(MapUtil.merge(platformElement.getParameters(), element.getParameters()));
    }

}
