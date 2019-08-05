package com.sap.cloud.lm.sl.mta.validators.v3;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.cast;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.message.Constants;
import com.sap.cloud.lm.sl.mta.message.Messages;
import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.Metadata;
import com.sap.cloud.lm.sl.mta.model.Module;
import com.sap.cloud.lm.sl.mta.model.NamedElement;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;
import com.sap.cloud.lm.sl.mta.model.ParametersWithMetadataContainer;
import com.sap.cloud.lm.sl.mta.model.Platform;
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;
import com.sap.cloud.lm.sl.mta.model.PropertiesWithMetadataContainer;
import com.sap.cloud.lm.sl.mta.model.ProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.RequiredDependency;
import com.sap.cloud.lm.sl.mta.model.Resource;
import com.sap.cloud.lm.sl.mta.util.ValidatorUtil;

public class DeploymentDescriptorValidator extends com.sap.cloud.lm.sl.mta.validators.v2.DeploymentDescriptorValidator {

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
        ParametersWithMetadataContainer containerWithMetadata = cast(container);
        validate(containerWithMetadata.getParametersMetadata(), containerWithMetadata.getParameters(), containerName,
                 Constants.PARAMETER_ELEMENT_TYPE_NAME);
    }

    protected void validateProperties(PropertiesContainer container, String containerName) {
        PropertiesWithMetadataContainer containerWithMetadata = cast(container);
        validate(containerWithMetadata.getPropertiesMetadata(), containerWithMetadata.getProperties(), containerName,
                 Constants.PARAMETER_ELEMENT_TYPE_NAME);
    }

    protected void validate(Metadata metadata, Map<String, Object> properties, String containerName, String elementType) {
        for (Map.Entry<String, Object> property : properties.entrySet()) {
            boolean isOverwritable = metadata.getOverwritableMetadata(property.getKey());
            boolean isOptional = metadata.getOptionalMetadata(property.getKey());
            if (property.getValue() == null && !isOverwritable && !isOptional) {
                throw new ContentException(Messages.MANDATORY_ELEMENT_HAS_NO_VALUE, elementType,
                    ValidatorUtil.getPrefixedName(containerName, property.getKey()));
            }
        }
    }
}
