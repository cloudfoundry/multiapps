package org.cloudfoundry.multiapps.mta.mergers;

import org.cloudfoundry.multiapps.common.util.MapUtil;
import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorHandler;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.ElementContext;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.model.ModuleType;
import org.cloudfoundry.multiapps.mta.model.Platform;
import org.cloudfoundry.multiapps.mta.model.Resource;
import org.cloudfoundry.multiapps.mta.model.ResourceType;
import org.cloudfoundry.multiapps.mta.model.Visitor;

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
        descriptor.setParameters(MapUtil.merge(platform.getParameters(), descriptor.getParameters()));
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
