package com.sap.cloud.lm.sl.mta.validators.v3;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.cast;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.message.Constants;
import com.sap.cloud.lm.sl.mta.message.Messages;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.Metadata;
import com.sap.cloud.lm.sl.mta.model.NamedElement;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;
import com.sap.cloud.lm.sl.mta.model.ParametersWithMetadataContainer;
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;
import com.sap.cloud.lm.sl.mta.model.PropertiesWithMetadataContainer;
import com.sap.cloud.lm.sl.mta.model.v2.Module;
import com.sap.cloud.lm.sl.mta.model.v2.ProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v2.RequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v2.Resource;
import com.sap.cloud.lm.sl.mta.model.v3.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v3.Platform;
import com.sap.cloud.lm.sl.mta.util.ValidatorUtil;

public class DeploymentDescriptorValidator extends com.sap.cloud.lm.sl.mta.validators.v2.DeploymentDescriptorValidator {

    public DeploymentDescriptorValidator(DeploymentDescriptor descriptor, Platform platformType, DescriptorHandler handler) {
        super(descriptor, platformType, handler);
    }

    @Override
    public void visit(ElementContext context, RequiredDependency requiredDependency) throws ContentException {
        super.visit(context, requiredDependency);
        com.sap.cloud.lm.sl.mta.model.v3.RequiredDependency requiredDependencyV3 = cast(requiredDependency);
        validateParameters(requiredDependencyV3, requiredDependencyV3.getName());
        validateProperties(requiredDependencyV3, requiredDependencyV3.getName());
    }

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
        com.sap.cloud.lm.sl.mta.model.v3.Resource resourceV3 = cast(resource);
        validateParameters(resourceV3, resourceV3.getName());
        validateProperties(resourceV3, resourceV3.getName());
    }

    @Override
    protected boolean isOptional(Resource resource) {
        com.sap.cloud.lm.sl.mta.model.v3.Resource resourceV3 = cast(resource);
        return resourceV3.isOptional();
    }

    @Override
    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v2.Module module) throws ContentException {
        super.visit(context, module);
        com.sap.cloud.lm.sl.mta.model.v3.Module moduleV3 = cast(module);
        validateParameters(moduleV3, moduleV3.getName());
        validateProperties(moduleV3, moduleV3.getName());
    }

    @Override
    public void visit(ElementContext context, ProvidedDependency providedDependency) throws ContentException {
        super.visit(context, providedDependency);
        com.sap.cloud.lm.sl.mta.model.v3.ProvidedDependency providedDependencyV3 = cast(providedDependency);
        validateProperties(providedDependencyV3, providedDependencyV3.getName());
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
        for (String propertyName : properties.keySet()) {
            boolean isOverwritable = metadata.getOverwritableMetadata(propertyName);
            boolean isOptional = metadata.getOptionalMetadata(propertyName);
            if (properties.get(propertyName) == null && !isOverwritable && !isOptional) {
                throw new ContentException(Messages.MANDATORY_ELEMENT_HAS_NO_VALUE,
                                           elementType,
                                           ValidatorUtil.getPrefixedName(containerName, propertyName));
            }
        }
    }
}
