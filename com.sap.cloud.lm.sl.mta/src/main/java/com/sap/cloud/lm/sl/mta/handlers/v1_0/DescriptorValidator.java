package com.sap.cloud.lm.sl.mta.handlers.v1_0;

import java.util.List;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.common.util.Pair;
import com.sap.cloud.lm.sl.mta.model.v1_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1_0.Platform;
import com.sap.cloud.lm.sl.mta.model.v1_0.Target;
import com.sap.cloud.lm.sl.mta.validators.DescriptorValidationRules;
import com.sap.cloud.lm.sl.mta.validators.v1_0.DefaultDescriptorValidationRules;
import com.sap.cloud.lm.sl.mta.validators.v1_0.DeploymentDescriptorValidator;
import com.sap.cloud.lm.sl.mta.validators.v1_0.ExtensionDescriptorValidator;
import com.sap.cloud.lm.sl.mta.validators.v1_0.MergedDescriptorValidator;

public class DescriptorValidator {

    protected final DescriptorHandler handler;

    public DescriptorValidator() {
        this(new DescriptorHandler());
    }

    public DescriptorValidator(DescriptorHandler handler) {
        this.handler = handler;
    }

    public void validateDeploymentDescriptor(DeploymentDescriptor deploymentDescriptor, Platform platform) throws ContentException {
        getDeploymentDescriptorValidator(deploymentDescriptor, platform).validate();
    }

    protected DeploymentDescriptorValidator getDeploymentDescriptorValidator(DeploymentDescriptor deploymentDescriptor, Platform platform) {
        return new DeploymentDescriptorValidator(deploymentDescriptor, platform, handler);
    }

    public void validateExtensionDescriptors(List<ExtensionDescriptor> extensionDescriptors, DeploymentDescriptor deploymentDescriptor)
        throws ContentException {
        for (ExtensionDescriptor extensionDescriptor : extensionDescriptors) {
            getExtensionDescriptorValidator(extensionDescriptor, deploymentDescriptor).validate();
        }
    }

    protected ExtensionDescriptorValidator getExtensionDescriptorValidator(ExtensionDescriptor extensionDescriptor,
        DeploymentDescriptor deploymentDescriptor) {
        return new ExtensionDescriptorValidator(extensionDescriptor, deploymentDescriptor, handler);
    }

    public void validateMergedDescriptor(Pair<DeploymentDescriptor, List<String>> mergedDescriptor,
        DescriptorValidationRules validationRules, Target target) throws ContentException {
        getMergedDescriptorValidator(mergedDescriptor, validationRules).validate(target.getName());
    }

    public void validateMergedDescriptor(Pair<DeploymentDescriptor, List<String>> mergedDescriptor, Target target) throws ContentException {
        getMergedDescriptorValidator(mergedDescriptor, getDefaultDescriptorValidationRules()).validate(target.getName());
    }

    protected MergedDescriptorValidator getMergedDescriptorValidator(Pair<DeploymentDescriptor, List<String>> mergedDescriptor,
        DescriptorValidationRules validationRules) {
        return new MergedDescriptorValidator(mergedDescriptor, validationRules, handler);
    }

    protected DescriptorValidationRules getDefaultDescriptorValidationRules() {
        return new DefaultDescriptorValidationRules();
    }

}
