package org.cloudfoundry.multiapps.mta.handlers.v3;

import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorHandler;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.ExtensionDescriptor;
import org.cloudfoundry.multiapps.mta.model.Platform;
import org.cloudfoundry.multiapps.mta.validators.DescriptorValidationRules;
import org.cloudfoundry.multiapps.mta.validators.v3.DefaultDescriptorValidationRules;
import org.cloudfoundry.multiapps.mta.validators.v3.DeploymentDescriptorValidator;
import org.cloudfoundry.multiapps.mta.validators.v3.ExtensionDescriptorValidator;

public class DescriptorValidator extends org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorValidator {

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
