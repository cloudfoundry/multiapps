package com.sap.cloud.lm.sl.mta.validators.v2;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.message.Messages;
import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.Module;
import com.sap.cloud.lm.sl.mta.model.NamedElement;
import com.sap.cloud.lm.sl.mta.model.Platform;
import com.sap.cloud.lm.sl.mta.model.RequiredDependency;
import com.sap.cloud.lm.sl.mta.model.Resource;
import com.sap.cloud.lm.sl.mta.model.VisitableElement;
import com.sap.cloud.lm.sl.mta.model.Visitor;

public class DeploymentDescriptorValidator extends Visitor {

    protected final Platform platform;
    protected final DeploymentDescriptor descriptor;
    protected final DescriptorHandler handler;

    public DeploymentDescriptorValidator(DeploymentDescriptor descriptor, Platform platform, DescriptorHandler handler) {
        this.descriptor = descriptor;
        this.platform = platform;
        this.handler = handler;
    }

    public void validate() throws ContentException {
        descriptor.accept(this);
    }

    @Override
    public void visit(ElementContext context, RequiredDependency requiredDependency) throws ContentException {
        VisitableElement previousElement = context.getPreviousElementContext()
                                                  .getVisitableElement();
        validate((NamedElement) previousElement, requiredDependency.getName());
    }

    protected void validate(NamedElement container, String requiredDependency) {
        if (!canBeResolved(requiredDependency)) {
            throw new ContentException(Messages.UNRESOLVED_MODULE_REQUIRED_DEPENDENCY, requiredDependency, container.getName());
        }
    }

    protected boolean canBeResolved(String dependency) {
        return handler.findResource(descriptor, dependency) != null || handler.findProvidedDependency(descriptor, dependency) != null;
    }

    @Override
    public void visit(ElementContext context, Resource resource) throws ContentException {
        if (isService(resource) && !isSupported(resource) && !isOptional(resource)) {
            throw new ContentException(Messages.UNSUPPORTED_RESOURCE_TYPE, resource.getType(), platform.getName());
        }
    }

    protected boolean isService(Resource resource) {
        return resource.getType() != null;
    }

    protected boolean isSupported(Resource resource) {
        return handler.findResourceType(platform, resource.getType()) != null;
    }

    @Override
    public void visit(ElementContext context, Module module) throws ContentException {
        if (!isSupported(module)) {
            throw new ContentException(Messages.UNSUPPORTED_MODULE_TYPE, module.getType(), platform.getName());
        }
        validateRequiredDependencies(module);
    }

    protected boolean isSupported(Module module) {
        return handler.findModuleType(platform, module.getType()) != null;
    }

    protected void validateRequiredDependencies(Module module) {
        // Do nothing!
    }

    protected boolean isOptional(Resource resource) {
        return false;
    }
}
