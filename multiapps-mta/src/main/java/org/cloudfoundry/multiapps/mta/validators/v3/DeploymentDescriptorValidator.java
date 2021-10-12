package org.cloudfoundry.multiapps.mta.validators.v3;

import java.text.MessageFormat;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.common.util.MiscUtil;
import org.cloudfoundry.multiapps.mta.Constants;
import org.cloudfoundry.multiapps.mta.Messages;
import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorHandler;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.ElementContext;
import org.cloudfoundry.multiapps.mta.model.Metadata;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.model.NamedElement;
import org.cloudfoundry.multiapps.mta.model.ParametersContainer;
import org.cloudfoundry.multiapps.mta.model.ParametersWithMetadataContainer;
import org.cloudfoundry.multiapps.mta.model.Platform;
import org.cloudfoundry.multiapps.mta.model.PropertiesContainer;
import org.cloudfoundry.multiapps.mta.model.PropertiesWithMetadataContainer;
import org.cloudfoundry.multiapps.mta.model.ProvidedDependency;
import org.cloudfoundry.multiapps.mta.model.RequiredDependency;
import org.cloudfoundry.multiapps.mta.model.Resource;
import org.cloudfoundry.multiapps.mta.util.NameUtil;

public class DeploymentDescriptorValidator extends org.cloudfoundry.multiapps.mta.validators.v2.DeploymentDescriptorValidator {

    public DeploymentDescriptorValidator(DeploymentDescriptor descriptor, Platform platform, DescriptorHandler handler) {
        super(descriptor, platform, handler);
    }

    @Override
    public void visit(ElementContext context, RequiredDependency requiredDependency) throws ContentException {
        super.visit(context, requiredDependency);
        validateParameters(requiredDependency, requiredDependency.getName());
        validateProperties(requiredDependency, requiredDependency.getName());
    }

    @Override
    protected void validate(NamedElement container, String requiredDependency) {
        if (!canBeResolved(requiredDependency)) {
            if (container instanceof Module) {
                throw new ContentException(Messages.UNRESOLVED_MODULE_REQUIRED_DEPENDENCY, requiredDependency, container.getName());
            }
            if (container instanceof Resource) {
                throw new ContentException(Messages.UNRESOLVED_RESOURCE_REQUIRED_DEPENDENCY, requiredDependency, container.getName());
            }
            throw new IllegalStateException("Required reference container not supported");
        }
    }

    @Override
    public void visit(ElementContext context, Resource resource) throws ContentException {
        super.visit(context, resource);
        validateParameters(resource, resource.getName());
        validateProperties(resource, resource.getName());
        checkForCircularDependency(resource, resource.getName(), descriptor.getResources()
                                                                           .size());
    }

    @Override
    protected boolean isOptional(Resource resource) {
        return resource.isOptional();
    }

    @Override
    public void visit(ElementContext context, Module module) throws ContentException {
        super.visit(context, module);
        validateParameters(module, module.getName());
        validateProperties(module, module.getName());
    }

    @Override
    public void visit(ElementContext context, ProvidedDependency providedDependency) throws ContentException {
        super.visit(context, providedDependency);
        validateProperties(providedDependency, providedDependency.getName());
    }

    private void checkForCircularDependency(Resource resource, String resourceName, int initialDependencyCount) {
        if (!hasDependentResources(resource) || initialDependencyCount == 0) {
            return;
        }
        for (String dependentResourceName : resource.getProcessedAfter()) {
            checkIfResourceExists(dependentResourceName);
            Resource dependentResource = handler.findResource(descriptor, dependentResourceName);
            if (isSelfRequired(resource)) {
                throw new IllegalStateException(MessageFormat.format(Messages.SELF_REQUIRED_RESOURCE, resource.getName()));
            }
            if (hasDependentResources(dependentResource) && dependentResource.getProcessedAfter()
                                                                             .contains(resourceName)) {
                throw new IllegalStateException(MessageFormat.format(Messages.CIRCULAR_RESOURCE_DEPENDENCIES_DETECTED,
                                                                     dependentResource.getName(), resourceName));
            }
            checkForCircularDependency(dependentResource, resourceName, initialDependencyCount - 1);
        }
    }

    private boolean hasDependentResources(Resource resource) {
        return !CollectionUtils.isEmpty(resource.getProcessedAfter());
    }

    private boolean isSelfRequired(Resource resource) {
        return resource.getProcessedAfter()
                       .contains(resource.getName());
    }

    private void checkIfResourceExists(String resourceName) {
        if (handler.findResource(descriptor, resourceName) == null) {
            throw new ContentException(MessageFormat.format(Messages.RESOURCE_DOES_NOT_EXIST, resourceName));
        }
    }

    protected void validateParameters(ParametersContainer container, String containerName) {
        ParametersWithMetadataContainer containerWithMetadata = MiscUtil.cast(container);
        validate(containerWithMetadata.getParametersMetadata(), containerWithMetadata.getParameters(), containerName,
                 Constants.PARAMETER_ELEMENT_TYPE_NAME);
    }

    protected void validateProperties(PropertiesContainer container, String containerName) {
        PropertiesWithMetadataContainer containerWithMetadata = MiscUtil.cast(container);
        validate(containerWithMetadata.getPropertiesMetadata(), containerWithMetadata.getProperties(), containerName,
                 Constants.PARAMETER_ELEMENT_TYPE_NAME);
    }

    protected void validate(Metadata metadata, Map<String, Object> properties, String containerName, String elementType) {
        for (Map.Entry<String, Object> property : properties.entrySet()) {
            boolean isOverwritable = metadata.getOverwritableMetadata(property.getKey());
            boolean isOptional = metadata.getOptionalMetadata(property.getKey());
            if (property.getValue() == null && !isOverwritable && !isOptional) {
                throw new ContentException(Messages.MANDATORY_ELEMENT_HAS_NO_VALUE,
                                           elementType,
                                           NameUtil.getPrefixedName(containerName, property.getKey()));
            }
        }
    }
}
