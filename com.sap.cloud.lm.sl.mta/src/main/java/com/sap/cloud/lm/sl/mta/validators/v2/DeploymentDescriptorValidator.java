package com.sap.cloud.lm.sl.mta.validators.v2;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.message.Messages;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.NamedElement;
import com.sap.cloud.lm.sl.mta.model.VisitableElement;
import com.sap.cloud.lm.sl.mta.model.Visitor;
import com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2.Platform;
import com.sap.cloud.lm.sl.mta.model.v2.RequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v2.Resource;

public class DeploymentDescriptorValidator extends Visitor {

    protected final Platform platformType;
    protected final DeploymentDescriptor descriptor;
    protected final DescriptorHandler handler;

    public DeploymentDescriptorValidator(DeploymentDescriptor descriptor, Platform platformType, DescriptorHandler handler) {
        this.descriptor = descriptor;
        this.platformType = platformType;
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
        return handler.findDependency(descriptor, dependency) != null;
    }

    protected boolean isOptional(Resource resource) {
        return false;
    }
}
