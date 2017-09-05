package com.sap.cloud.lm.sl.mta.mergers.v1_0;

import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.handlers.v1_0.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.Visitor;
import com.sap.cloud.lm.sl.mta.model.v1_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1_0.Module;
import com.sap.cloud.lm.sl.mta.model.v1_0.PlatformModuleType;
import com.sap.cloud.lm.sl.mta.model.v1_0.PlatformResourceType;
import com.sap.cloud.lm.sl.mta.model.v1_0.Resource;
import com.sap.cloud.lm.sl.mta.model.v1_0.Target;

public class TargetMerger extends Visitor {

    protected final Target target;
    protected final DescriptorHandler handler;

    public TargetMerger(Target target, DescriptorHandler handler) {
        this.target = target;
        this.handler = handler;
    }

    public void mergeInto(DeploymentDescriptor descriptor) {
        descriptor.accept(this);
    }

    @Override
    public void visit(ElementContext context, Module element) {
        PlatformModuleType platformElement = handler.findPlatformModuleType(target, element.getType());
        if (platformElement == null) {
            return;
        }
        element.setProperties(MapUtil.merge(platformElement.getProperties(), element.getProperties()));
    }

    @Override
    public void visit(ElementContext context, DeploymentDescriptor element) {
        Target targetElement = target;
        element.setProperties(MapUtil.merge(targetElement.getProperties(), element.getProperties()));
    }

    @Override
    public void visit(ElementContext context, Resource element) {
        if (element.getType() == null) {
            return;
        }
        PlatformResourceType platformElement = handler.findTargetResourceType(target, element.getType());
        if (platformElement == null) {
            return;
        }
        element.setProperties(MapUtil.merge(platformElement.getProperties(), element.getProperties()));
    }

}
