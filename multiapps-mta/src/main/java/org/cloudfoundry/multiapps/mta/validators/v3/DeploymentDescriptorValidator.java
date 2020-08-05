package org.cloudfoundry.multiapps.mta.validators.v3;

import java.util.Map;

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
