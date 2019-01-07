package com.sap.cloud.lm.sl.mta.handlers.v2;

import com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2.Platform;
import com.sap.cloud.lm.sl.mta.validators.DescriptorValidationRules;
import com.sap.cloud.lm.sl.mta.validators.v2.DefaultDescriptorValidationRules;
import com.sap.cloud.lm.sl.mta.validators.v2.DeploymentDescriptorValidator;
import com.sap.cloud.lm.sl.mta.validators.v2.ExtensionDescriptorValidator;
import com.sap.cloud.lm.sl.mta.validators.v2.MergedDescriptorValidator;

public class DescriptorValidator {

    protected final DescriptorHandler handler;

    public DescriptorValidator() {
        this(new DescriptorHandler());
    }

    public DescriptorValidator(DescriptorHandler handler) {
        this.handler = handler;
    }

    protected DeploymentDescriptorValidator getDeploymentDescriptorValidator(DeploymentDescriptor deploymentDescriptor,
        Platform platformType) {
        return new DeploymentDescriptorValidator(deploymentDescriptor, platformType, handler);
    }

    protected ExtensionDescriptorValidator getExtensionDescriptorValidator(ExtensionDescriptor extensionDescriptor,
        DeploymentDescriptor deploymentDescriptor) {
        return new ExtensionDescriptorValidator(extensionDescriptor, deploymentDescriptor, handler);
    }


    protected MergedDescriptorValidator getMergedDescriptorValidator(DeploymentDescriptor mergedDescriptor,
        DescriptorValidationRules validationRules) {
        return new MergedDescriptorValidator(mergedDescriptor, validationRules, handler);
    }

    protected DescriptorValidationRules getDefaultDescriptorValidationRules() {
        return new DefaultDescriptorValidationRules();
    }

}
