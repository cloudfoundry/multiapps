package com.sap.cloud.lm.sl.mta.handlers.v3;

import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.v3.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v3.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.v3.Platform;
import com.sap.cloud.lm.sl.mta.validators.DescriptorValidationRules;
import com.sap.cloud.lm.sl.mta.validators.v3.DefaultDescriptorValidationRules;
import com.sap.cloud.lm.sl.mta.validators.v3.DeploymentDescriptorValidator;
import com.sap.cloud.lm.sl.mta.validators.v3.ExtensionDescriptorValidator;

public class DescriptorValidator extends com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorValidator {

    public DescriptorValidator() {
        super(new DescriptorHandler());
    }

    public DescriptorValidator(DescriptorHandler handler) {
        super(handler);
    }

    @Override
    protected DeploymentDescriptorValidator getDeploymentDescriptorValidator(
        com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor deploymentDescriptor,
        com.sap.cloud.lm.sl.mta.model.v2.Platform platformType) {
        return new DeploymentDescriptorValidator((DeploymentDescriptor) deploymentDescriptor, (Platform) platformType,
            (DescriptorHandler) handler);
    }

    @Override
    protected ExtensionDescriptorValidator getExtensionDescriptorValidator(
        com.sap.cloud.lm.sl.mta.model.v2.ExtensionDescriptor extensionDescriptor,
        com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor deploymentDescriptor) {
        return new ExtensionDescriptorValidator((ExtensionDescriptor) extensionDescriptor, (DeploymentDescriptor) deploymentDescriptor,
            (DescriptorHandler) handler);
    }

    @Override
    protected DescriptorValidationRules getDefaultDescriptorValidationRules() {
        return new DefaultDescriptorValidationRules();
    }

}
