package com.sap.cloud.lm.sl.mta.handlers.v3;

import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.Platform;
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
    protected DeploymentDescriptorValidator getDeploymentDescriptorValidator(DeploymentDescriptor deploymentDescriptor, Platform platform) {
        return new DeploymentDescriptorValidator(deploymentDescriptor, platform, handler);
    }

    @Override
    protected ExtensionDescriptorValidator getExtensionDescriptorValidator(ExtensionDescriptor extensionDescriptor,
                                                                           DeploymentDescriptor deploymentDescriptor) {
        return new ExtensionDescriptorValidator(extensionDescriptor, deploymentDescriptor, handler);
    }

    @Override
    protected DescriptorValidationRules getDefaultDescriptorValidationRules() {
        return new DefaultDescriptorValidationRules();
    }

}
