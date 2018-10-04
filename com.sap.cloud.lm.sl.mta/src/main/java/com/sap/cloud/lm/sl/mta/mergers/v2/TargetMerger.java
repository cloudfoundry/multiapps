package com.sap.cloud.lm.sl.mta.mergers.v2;

import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.v2.Target;
import com.sap.cloud.lm.sl.mta.model.v2.TargetModuleType;
import com.sap.cloud.lm.sl.mta.model.v2.TargetResourceType;

public class TargetMerger extends com.sap.cloud.lm.sl.mta.mergers.v1.TargetMerger {

    public TargetMerger(Target target, DescriptorHandler handler) {
        super(target, handler);
    }

    @Override
    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v1.Module module) {
        visit(context, (com.sap.cloud.lm.sl.mta.model.v2.Module) module);
    }

    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v2.Module module) {
        TargetModuleType moduleType = (TargetModuleType) handler.findTargetModuleType(target, module.getType());
        if (moduleType == null) {
            return;
        }
        module.setProperties(MapUtil.merge(moduleType.getProperties(), module.getProperties()));
        module.setParameters(MapUtil.merge(moduleType.getParameters(), module.getParameters()));
    }

    @Override
    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v1.DeploymentDescriptor descriptor) {
        visit(context, (com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor) descriptor);
    }

    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor descriptor) {
        Target v2Target = (Target) target;
        descriptor.setParameters(MapUtil.merge(v2Target.getParameters(), descriptor.getParameters()));
    }

    @Override
    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v1.Resource resource) {
        visit(context, (com.sap.cloud.lm.sl.mta.model.v2.Resource) resource);
    }

    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v2.Resource resource) {
        if (resource.getType() == null) {
            return;
        }
        TargetResourceType resourceType = (TargetResourceType) handler.findTargetResourceType(target, resource.getType());
        if (resourceType == null) {
            return;
        }
        resource.setParameters(MapUtil.merge(resourceType.getParameters(), resource.getParameters()));
    }

}
