package com.sap.cloud.lm.sl.mta.handlers.v2;

import java.util.List;

import com.sap.cloud.lm.sl.common.util.Pair;
import com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2.Platform;
import com.sap.cloud.lm.sl.mta.validators.DescriptorValidationRules;
import com.sap.cloud.lm.sl.mta.validators.v2.DefaultDescriptorValidationRules;
import com.sap.cloud.lm.sl.mta.validators.v2.DeploymentDescriptorValidator;
import com.sap.cloud.lm.sl.mta.validators.v2.ExtensionDescriptorValidator;
import com.sap.cloud.lm.sl.mta.validators.v2.MergedDescriptorValidator;

public class DescriptorValidator extends com.sap.cloud.lm.sl.mta.handlers.v1.DescriptorValidator {

    public DescriptorValidator() {
        super(new DescriptorHandler());
    }

    public DescriptorValidator(DescriptorHandler handler) {
        super(handler);
    }

    @Override
    protected DeploymentDescriptorValidator getDeploymentDescriptorValidator(
        com.sap.cloud.lm.sl.mta.model.v1.DeploymentDescriptor deploymentDescriptor,
        com.sap.cloud.lm.sl.mta.model.v1.Platform platformType) {
        return new DeploymentDescriptorValidator((DeploymentDescriptor) deploymentDescriptor, (Platform) platformType,
            (DescriptorHandler) handler);
    }

    @Override
    protected ExtensionDescriptorValidator getExtensionDescriptorValidator(
        com.sap.cloud.lm.sl.mta.model.v1.ExtensionDescriptor extensionDescriptor,
        com.sap.cloud.lm.sl.mta.model.v1.DeploymentDescriptor deploymentDescriptor) {
        return new ExtensionDescriptorValidator((ExtensionDescriptor) extensionDescriptor, (DeploymentDescriptor) deploymentDescriptor,
            (DescriptorHandler) handler);
    }

    @Override
    protected MergedDescriptorValidator getMergedDescriptorValidator(
        Pair<com.sap.cloud.lm.sl.mta.model.v1.DeploymentDescriptor, List<String>> mergedDescriptor,
        DescriptorValidationRules validationRules) {
        return new MergedDescriptorValidator(mergedDescriptor, validationRules, (DescriptorHandler) handler);
    }

    @Override
    protected DescriptorValidationRules getDefaultDescriptorValidationRules() {
        return new DefaultDescriptorValidationRules();
    }

}
