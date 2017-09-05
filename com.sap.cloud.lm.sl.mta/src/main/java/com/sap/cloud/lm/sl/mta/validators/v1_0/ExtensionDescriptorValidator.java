package com.sap.cloud.lm.sl.mta.validators.v1_0;

import static com.sap.cloud.lm.sl.mta.util.ValidatorUtil.validateModifiableElements;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.handlers.v1_0.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.message.Constants;
import com.sap.cloud.lm.sl.mta.message.Messages;
import com.sap.cloud.lm.sl.mta.model.VisitableElement;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.NamedElement;
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;
import com.sap.cloud.lm.sl.mta.model.Visitor;
import com.sap.cloud.lm.sl.mta.model.v1_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionModule;
import com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionResource;
import com.sap.cloud.lm.sl.mta.model.v1_0.Module;
import com.sap.cloud.lm.sl.mta.model.v1_0.ProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v1_0.Resource;

public class ExtensionDescriptorValidator extends Visitor {

    protected final ExtensionDescriptor extensionDescriptor;
    protected final DeploymentDescriptor deploymentDescriptor;
    protected final DescriptorHandler handler;

    public ExtensionDescriptorValidator(ExtensionDescriptor descriptor, DeploymentDescriptor deploymentDescriptor,
        DescriptorHandler handler) {
        this.extensionDescriptor = descriptor;
        this.deploymentDescriptor = deploymentDescriptor;
        this.handler = handler;
    }

    public void validate() throws ContentException {
        validateProperties(deploymentDescriptor, extensionDescriptor, "");
        extensionDescriptor.accept(this);
    }

    @Override
    public void visit(ElementContext context, ExtensionModule extensionModule) throws ContentException {
        if (!extendsDeploymentDescriptorElement(extensionModule)) {
            throw new ContentException(Messages.UNKNOWN_MODULE_IN_MTAEXT, extensionModule.getName(), extensionDescriptor.getId());
        }
        validateProperties(findModule(extensionModule), extensionModule, extensionModule.getName());
    }

    @Override
    public void visit(ElementContext context, ExtensionProvidedDependency extensionProvidedDependency) throws ContentException {
        VisitableElement container = context.getPreviousElementContext().getVisitableElement();
        if (!extendsDeploymentDescriptorElement(extensionProvidedDependency)) {
            String containerName = container instanceof NamedElement ? ((NamedElement) container).getName() : "";
            throw new ContentException(Messages.UNKNOWN_PROVIDED_DEPENDENCY_IN_MTAEXT, extensionProvidedDependency.getName(), containerName,
                extensionDescriptor.getId());
        }
        validateProperties(findProvidedDependency(extensionProvidedDependency), extensionProvidedDependency,
            extensionProvidedDependency.getName());
    }

    @Override
    public void visit(ElementContext context, ExtensionResource extensionResource) throws ContentException {
        if (!extendsDeploymentDescriptorElement(extensionResource)) {
            throw new ContentException(Messages.UNKNOWN_RESOURCE_IN_MTAEXT, extensionResource.getName(), extensionDescriptor.getId());
        }
        validateProperties(findResource(extensionResource), extensionResource, extensionResource.getName());
    }

    private boolean extendsDeploymentDescriptorElement(ExtensionProvidedDependency extensionProvidedDependency) {
        return findProvidedDependency(extensionProvidedDependency) != null;
    }

    protected ProvidedDependency findProvidedDependency(ExtensionProvidedDependency extensionProvidedDependency) {
        return handler.findProvidedDependency(deploymentDescriptor, extensionProvidedDependency.getName());
    }

    private boolean extendsDeploymentDescriptorElement(ExtensionResource extensionResource) {
        return findResource(extensionResource) != null;
    }

    protected Resource findResource(ExtensionResource extensionResource) {
        return handler.findResource(deploymentDescriptor, extensionResource.getName());
    }

    private boolean extendsDeploymentDescriptorElement(ExtensionModule extensionModule) {
        return findModule(extensionModule) != null;
    }

    protected Module findModule(ExtensionModule extensionModule) {
        return handler.findModule(deploymentDescriptor, extensionModule.getName());
    }

    protected void validateProperties(final PropertiesContainer parentContainer, final PropertiesContainer extensionContainer,
        final String containerName) throws ContentException {
        validate(parentContainer.getProperties(), extensionContainer.getProperties(), containerName, Constants.PROPERTY_ELEMENT_TYPE_NAME);
    }

    protected void validate(Map<String, Object> properties, Map<String, Object> extensionProperties, String containerName,
        String elementType) throws ContentException {
        for (String propertyName : extensionProperties.keySet()) {
            Object parentValue = properties.get(propertyName);
            Object value = extensionProperties.get(propertyName);
            validateModifiableElements(elementType, containerName, extensionDescriptor.getId(), propertyName, value, parentValue);
        }
    }
}
