package org.cloudfoundry.multiapps.mta.handlers.v2;

import java.util.List;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.ExtensionDescriptor;
import org.cloudfoundry.multiapps.mta.model.Platform;
import org.cloudfoundry.multiapps.mta.validators.DescriptorValidationRules;
import org.cloudfoundry.multiapps.mta.validators.v2.DefaultDescriptorValidationRules;
import org.cloudfoundry.multiapps.mta.validators.v2.DeploymentDescriptorValidator;
import org.cloudfoundry.multiapps.mta.validators.v2.ExtensionDescriptorValidator;
import org.cloudfoundry.multiapps.mta.validators.v2.MergedDescriptorValidator;

public class DescriptorValidator {

    protected final DescriptorHandler handler;

    public DescriptorValidator() {
        this(new DescriptorHandler());
    }

    public DescriptorValidator(DescriptorHandler handler) {
        this.handler = handler;
    }

    protected DeploymentDescriptorValidator getDeploymentDescriptorValidator(DeploymentDescriptor deploymentDescriptor, Platform platform) {
        return new DeploymentDescriptorValidator(deploymentDescriptor, platform, handler);
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

    public void validateDeploymentDescriptor(DeploymentDescriptor deploymentDescriptor, Platform platform) throws ContentException {
        getDeploymentDescriptorValidator(deploymentDescriptor, platform).validate();
    }

    public void validateExtensionDescriptors(List<ExtensionDescriptor> extensionDescriptors, DeploymentDescriptor deploymentDescriptor)
        throws ContentException {
        for (ExtensionDescriptor extensionDescriptor : extensionDescriptors) {
            getExtensionDescriptorValidator(extensionDescriptor, deploymentDescriptor).validate();
        }
    }

    public void validateMergedDescriptor(DeploymentDescriptor mergedDescriptor) throws ContentException {
        getMergedDescriptorValidator(mergedDescriptor, getDefaultDescriptorValidationRules()).validate();
    }

}
