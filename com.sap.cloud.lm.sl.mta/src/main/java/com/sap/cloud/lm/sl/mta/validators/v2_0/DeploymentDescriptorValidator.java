package com.sap.cloud.lm.sl.mta.validators.v2_0;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.handlers.v2_0.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.NamedElement;
import com.sap.cloud.lm.sl.mta.model.VisitableElement;
import com.sap.cloud.lm.sl.mta.model.v2_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2_0.Platform;
import com.sap.cloud.lm.sl.mta.model.v2_0.RequiredDependency;

public class DeploymentDescriptorValidator extends com.sap.cloud.lm.sl.mta.validators.v1_0.DeploymentDescriptorValidator {

    public DeploymentDescriptorValidator(DeploymentDescriptor descriptor, Platform platformType, DescriptorHandler handler) {
        super(descriptor, platformType, handler);
    }

    @Override
    protected void validateRequiredDependencies(com.sap.cloud.lm.sl.mta.model.v1_0.Module module) throws ContentException {
        // Do nothing!
    }

    @Override
    public void visit(ElementContext context, RequiredDependency requiredDependency) throws ContentException {
        VisitableElement previousElement = context.getPreviousElementContext()
            .getVisitableElement();
        super.validate((NamedElement) previousElement, requiredDependency.getName());
    }
}
