package org.cloudfoundry.multiapps.mta.validators.v2;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ObjectUtils;
import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.Constants;
import org.cloudfoundry.multiapps.mta.Messages;
import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorHandler;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.ElementContext;
import org.cloudfoundry.multiapps.mta.model.ExtensionDescriptor;
import org.cloudfoundry.multiapps.mta.model.ExtensionModule;
import org.cloudfoundry.multiapps.mta.model.ExtensionProvidedDependency;
import org.cloudfoundry.multiapps.mta.model.ExtensionRequiredDependency;
import org.cloudfoundry.multiapps.mta.model.ExtensionResource;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.model.NamedElement;
import org.cloudfoundry.multiapps.mta.model.ParametersContainer;
import org.cloudfoundry.multiapps.mta.model.PropertiesContainer;
import org.cloudfoundry.multiapps.mta.model.ProvidedDependency;
import org.cloudfoundry.multiapps.mta.model.RequiredDependency;
import org.cloudfoundry.multiapps.mta.model.Resource;
import org.cloudfoundry.multiapps.mta.model.VisitableElement;
import org.cloudfoundry.multiapps.mta.model.Visitor;
import org.cloudfoundry.multiapps.mta.util.NameUtil;

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
        validateParameters(deploymentDescriptor, extensionDescriptor, "");
        extensionDescriptor.accept(this);
    }

    @Override
    public void visit(ElementContext context, ExtensionRequiredDependency extensionRequiredDependency) throws ContentException {
        String containerName = context.getPreviousElementContext()
                                      .getVisitableElementName();
        if (!extendsDeploymentDescriptorElement(containerName, extensionRequiredDependency)) {
            throw new ContentException(Messages.UNKNOWN_REQUIRED_DEPENDENCY_IN_MTAEXT,
                                       extensionRequiredDependency.getName(),
                                       containerName,
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
        Module module = findModule(extensionModule);
        validateProperties(module, extensionModule, extensionModule.getName());
        validateParameters(module, extensionModule, extensionModule.getName());
    }

    private boolean extendsDeploymentDescriptorElement(ExtensionModule extensionModule) {
        return findModule(extensionModule) != null;
    }

    protected Module findModule(ExtensionModule extensionModule) {
        return handler.findModule(deploymentDescriptor, extensionModule.getName());
    }

    protected void validate(Map<String, Object> properties, Map<String, Object> extensionProperties, String containerName,
                            String elementType) {
        for (Entry<String, Object> extensionProperty : extensionProperties.entrySet()) {
            String propertyName = extensionProperty.getKey();
            Object parentValue = properties.get(propertyName);
            Object value = extensionProperty.getValue();
            validateModifiableElements(elementType, containerName, extensionDescriptor.getId(), propertyName, value, parentValue);
        }
    }

    protected void validateModifiableElements(String elementType, String elementPrefix, String containerName, String key, Object value,
                                              Object parentValue)
        throws ContentException {
        if (!ObjectUtils.isEmpty(parentValue) && !parentValue.equals(value)) {
            throw new ContentException(Messages.CANNOT_MODIFY_ELEMENT,
                                       elementType,
                                       NameUtil.getPrefixedName(elementPrefix, key),
                                       containerName);
        }
    }

    @Override
    public void visit(ElementContext context, ExtensionResource extensionResource) throws ContentException {
        if (!extendsDeploymentDescriptorElement(extensionResource)) {
            throw new ContentException(Messages.UNKNOWN_RESOURCE_IN_MTAEXT, extensionResource.getName(), extensionDescriptor.getId());
        }
        Resource resource = findResource(extensionResource);
        validateProperties(resource, extensionResource, extensionResource.getName());
        validateParameters(resource, extensionResource, extensionResource.getName());
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

    private boolean extendsDeploymentDescriptorElement(String containerName, ExtensionRequiredDependency extensionRequiredDependency) {
        return findRequiredDependency(containerName, extensionRequiredDependency) != null;
    }

    protected RequiredDependency findRequiredDependency(String containerName, ExtensionRequiredDependency extensionRequiredDependency) {
        return handler.findRequiredDependency(deploymentDescriptor, containerName, extensionRequiredDependency.getName());
    }

    @Override
    public void visit(ElementContext context, ExtensionProvidedDependency extensionProvidedDependency) throws ContentException {
        VisitableElement container = context.getPreviousElementContext()
                                            .getVisitableElement();
        if (!extendsDeploymentDescriptorElement(extensionProvidedDependency)) {
            String containerName = container instanceof NamedElement ? ((NamedElement) container).getName() : "";
            throw new ContentException(Messages.UNKNOWN_PROVIDED_DEPENDENCY_IN_MTAEXT,
                                       extensionProvidedDependency.getName(),
                                       containerName,
                                       extensionDescriptor.getId());
        }
        ProvidedDependency providedDependency = findProvidedDependency(extensionProvidedDependency);
        validateProperties(providedDependency, extensionProvidedDependency, extensionProvidedDependency.getName());
        validateParameters(providedDependency, extensionProvidedDependency, extensionProvidedDependency.getName());
    }

    private boolean extendsDeploymentDescriptorElement(ExtensionProvidedDependency extensionProvidedDependency) {
        return findProvidedDependency(extensionProvidedDependency) != null;
    }

    protected ProvidedDependency findProvidedDependency(ExtensionProvidedDependency extensionProvidedDependency) {
        return handler.findProvidedDependency(deploymentDescriptor, extensionProvidedDependency.getName());
    }

}
