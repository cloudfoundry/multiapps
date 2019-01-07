package com.sap.cloud.lm.sl.mta.validators.v2;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.cast;
import static com.sap.cloud.lm.sl.mta.util.ValidatorUtil.validateModifiableElements;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.message.Constants;
import com.sap.cloud.lm.sl.mta.message.Messages;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;
import com.sap.cloud.lm.sl.mta.model.Visitor;
import com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionModule;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionRequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionResource;
import com.sap.cloud.lm.sl.mta.model.v2.Module;
import com.sap.cloud.lm.sl.mta.model.v2.RequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v2.Resource;

public class ExtensionDescriptorValidator extends Visitor {

    protected final ExtensionDescriptor extensionDescriptor;
    protected final DeploymentDescriptor deploymentDescriptor;
    protected final DescriptorHandler handler;

    public ExtensionDescriptorValidator(ExtensionDescriptor extensionDescriptor, DeploymentDescriptor deploymentDescriptor,
        DescriptorHandler handler) {
        this.extensionDescriptor = extensionDescriptor;
        this.deploymentDescriptor = deploymentDescriptor;
        this.handler = handler;
    }

    public void validate() throws ContentException {
        DeploymentDescriptor deploymentDescriptorV2 = cast(deploymentDescriptor);
        ExtensionDescriptor extensionDescriptorV2 = cast(extensionDescriptor);
        validateParameters(deploymentDescriptorV2, extensionDescriptorV2, "");
        extensionDescriptor.accept(this);
    }

    @Override
    public void visit(ElementContext context, ExtensionRequiredDependency extensionRequiredDependency) throws ContentException {
        String containerName = context.getPreviousElementContext()
            .getVisitableElementName();
        if (!extendsDeploymentDescriptorElement(containerName, extensionRequiredDependency)) {
            throw new ContentException(Messages.UNKNOWN_REQUIRED_DEPENDENCY_IN_MTAEXT, extensionRequiredDependency.getName(), containerName,
                extensionDescriptor.getId());
        }
        RequiredDependency parentContainer = findRequiredDependency(containerName, extensionRequiredDependency);
        validateProperties(parentContainer, extensionRequiredDependency, extensionRequiredDependency.getName());
        validateParameters(parentContainer, extensionRequiredDependency, extensionRequiredDependency.getName());
    }

    protected void validateProperties(final PropertiesContainer parentContainer, final PropertiesContainer extensionContainer,
        final String containerName) {
        validate(parentContainer.getProperties(), extensionContainer.getProperties(), containerName, Constants.PROPERTY_ELEMENT_TYPE_NAME);
    }

    @Override
    public void visit(ElementContext context, ExtensionModule extensionModule) throws ContentException {
        if (!extendsDeploymentDescriptorElement(extensionModule)) {
            throw new ContentException(Messages.UNKNOWN_MODULE_IN_MTAEXT, extensionModule.getName(), extensionDescriptor.getId());
        }
        validateProperties(findModule(extensionModule), extensionModule, extensionModule.getName());
        ExtensionModule extensionModuleV2 = cast(extensionModule);
        Module moduleV2 = cast(findModule(extensionModule));
        validateParameters(moduleV2, extensionModuleV2, extensionModuleV2.getName());
    }

    private boolean extendsDeploymentDescriptorElement(ExtensionModule extensionModule) {
        return findModule(extensionModule) != null;
    }

    protected Module findModule(ExtensionModule extensionModule) {
        return handler.findModule(deploymentDescriptor, extensionModule.getName());
    }

    protected void validate(Map<String, Object> properties, Map<String, Object> extensionProperties, String containerName,
        String elementType) {
        for (String propertyName : extensionProperties.keySet()) {
            Object parentValue = properties.get(propertyName);
            Object value = extensionProperties.get(propertyName);
            validateModifiableElements(elementType, containerName, extensionDescriptor.getId(), propertyName, value, parentValue);
        }
    }

    @Override
    public void visit(ElementContext context, ExtensionResource extensionResource) throws ContentException {
        if (!extendsDeploymentDescriptorElement(extensionResource)) {
            throw new ContentException(Messages.UNKNOWN_RESOURCE_IN_MTAEXT, extensionResource.getName(), extensionDescriptor.getId());
        }

        validateProperties(findResource(extensionResource), extensionResource, extensionResource.getName());
        com.sap.cloud.lm.sl.mta.model.v2.ExtensionResource extensionResourceV2 = cast(extensionResource);
        Resource resourceV2 = cast(findResource(extensionResource));
        validateParameters(resourceV2, extensionResourceV2, extensionResourceV2.getName());
    }

    private boolean extendsDeploymentDescriptorElement(ExtensionResource extensionResource) {
        return findResource(extensionResource) != null;
    }

    protected Resource findResource(ExtensionResource extensionResource) {
        return handler.findResource(deploymentDescriptor, extensionResource.getName());
    }

    protected void validateParameters(ParametersContainer parametersContainer, ParametersContainer extensionParametersContainer,
        String containerName) {
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
