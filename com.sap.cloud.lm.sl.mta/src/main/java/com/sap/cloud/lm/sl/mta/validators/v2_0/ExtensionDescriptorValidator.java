package com.sap.cloud.lm.sl.mta.validators.v2_0;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.cast;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.handlers.v2_0.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.message.Constants;
import com.sap.cloud.lm.sl.mta.message.Messages;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;
import com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionModule;
import com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionResource;
import com.sap.cloud.lm.sl.mta.model.v2_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionRequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v2_0.Module;
import com.sap.cloud.lm.sl.mta.model.v2_0.RequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v2_0.Resource;

public class ExtensionDescriptorValidator extends com.sap.cloud.lm.sl.mta.validators.v1_0.ExtensionDescriptorValidator {

    public ExtensionDescriptorValidator(ExtensionDescriptor extensionDescriptor, DeploymentDescriptor deploymentDescriptor,
        DescriptorHandler handler) {
        super(extensionDescriptor, deploymentDescriptor, handler);
    }

    @Override
    public void validate() throws ContentException {
        DeploymentDescriptor deploymentDescriptorV2 = cast(deploymentDescriptor);
        ExtensionDescriptor extensionDescriptorV2 = cast(extensionDescriptor);
        validateParameters(deploymentDescriptorV2, extensionDescriptorV2, "");
        extensionDescriptor.accept(this);
    }

    @Override
    public void visit(ElementContext context, ExtensionRequiredDependency extensionRequiredDependency) throws ContentException {
        String containerName = context.getPreviousElementContext().getVisitableElementName();
        if (!extendsDeploymentDescriptorElement(containerName, extensionRequiredDependency)) {
            throw new ContentException(Messages.UNKNOWN_REQUIRED_DEPENDENCY_IN_MTAEXT, extensionRequiredDependency.getName(), containerName,
                extensionDescriptor.getId());
        }
        RequiredDependency parentContainer = findRequiredDependency(containerName, extensionRequiredDependency);
        validateProperties(parentContainer, extensionRequiredDependency, extensionRequiredDependency.getName());
        validateParameters(parentContainer, extensionRequiredDependency, extensionRequiredDependency.getName());
    }

    @Override
    public void visit(ElementContext context, ExtensionModule extensionModule) throws ContentException {
        super.visit(context, extensionModule);
        com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionModule extensionModuleV2 = cast(extensionModule);
        Module moduleV2 = cast(findModule(extensionModule));
        validateParameters(moduleV2, extensionModuleV2, extensionModuleV2.getName());
    }

    @Override
    public void visit(ElementContext context, ExtensionResource extensionResource) throws ContentException {
        super.visit(context, extensionResource);
        com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionResource extensionResourceV2 = cast(extensionResource);
        Resource resourceV2 = cast(findResource(extensionResource));
        validateParameters(resourceV2, extensionResourceV2, extensionResourceV2.getName());
    }

    protected void validateParameters(ParametersContainer parametersContainer, ParametersContainer extensionParametersContainer,
        String containerName) throws ContentException {
        validate(parametersContainer.getParameters(), extensionParametersContainer.getParameters(), containerName,
            Constants.PARAMETER_ELEMENT_TYPE_NAME);
    }

    private boolean extendsDeploymentDescriptorElement(String containerName, ExtensionRequiredDependency extenstionRequiredDependency) {
        return findRequiredDependency(containerName, extenstionRequiredDependency) != null;
    }

    protected RequiredDependency findRequiredDependency(String containerName, ExtensionRequiredDependency extensionRequiredDependency) {
        return ((DescriptorHandler) handler).findRequiredDependency((DeploymentDescriptor) deploymentDescriptor, containerName,
            extensionRequiredDependency.getName());
    }

}
