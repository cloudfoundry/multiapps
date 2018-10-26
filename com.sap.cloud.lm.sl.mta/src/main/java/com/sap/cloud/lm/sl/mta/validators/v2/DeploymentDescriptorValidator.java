package com.sap.cloud.lm.sl.mta.validators.v2;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.NamedElement;
import com.sap.cloud.lm.sl.mta.model.VisitableElement;
import com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2.Platform;
import com.sap.cloud.lm.sl.mta.model.v2.RequiredDependency;

public class DeploymentDescriptorValidator extends com.sap.cloud.lm.sl.mta.validators.v1.DeploymentDescriptorValidator {

    public DeploymentDescriptorValidator(DeploymentDescriptor descriptor, Platform platformType, DescriptorHandler handler) {
        super(descriptor, platformType, handler);
    }

    @Override
    protected void validateRequiredDependencies(com.sap.cloud.lm.sl.mta.model.v1.Module module) {
        // Do nothing!
    }

    @Override
    public void visit(ElementContext context, RequiredDependency requiredDependency) throws ContentException {
        VisitableElement previousElement = context.getPreviousElementContext()
            .getVisitableElement();
        super.validate((NamedElement) previousElement, requiredDependency.getName());
    }
}
