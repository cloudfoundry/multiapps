package com.sap.cloud.lm.sl.mta.validators.v3;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.cast;
import static com.sap.cloud.lm.sl.mta.util.ValidatorUtil.getPrefixedName;
import static com.sap.cloud.lm.sl.mta.util.ValidatorUtil.validateModifiableElements;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.message.Constants;
import com.sap.cloud.lm.sl.mta.message.Messages;
import com.sap.cloud.lm.sl.mta.model.Metadata;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;
import com.sap.cloud.lm.sl.mta.model.ParametersWithMetadataContainer;
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;
import com.sap.cloud.lm.sl.mta.model.PropertiesWithMetadataContainer;
import com.sap.cloud.lm.sl.mta.model.v3.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v3.ExtensionDescriptor;

public class ExtensionDescriptorValidator extends com.sap.cloud.lm.sl.mta.validators.v2.ExtensionDescriptorValidator {

    public ExtensionDescriptorValidator(ExtensionDescriptor extensionDescriptor, DeploymentDescriptor deploymentDescriptor,
                                        DescriptorHandler handler) {
        super(extensionDescriptor, deploymentDescriptor, handler);
    }

    @Override
    protected void validateParameters(ParametersContainer container, ParametersContainer extensionContainer, String containerName) {
        ParametersWithMetadataContainer containerWithMetadata = cast(container);
        validate(containerWithMetadata.getParametersMetadata(), containerWithMetadata.getParameters(), extensionContainer.getParameters(),
                 containerName, Constants.PARAMETER_ELEMENT_TYPE_NAME);
    }

    @Override
    protected void validateProperties(PropertiesContainer container, PropertiesContainer extensionContainer, String containerName) {
        PropertiesWithMetadataContainer containerWithMetadata = cast(container);
        validate(containerWithMetadata.getPropertiesMetadata(), containerWithMetadata.getProperties(), extensionContainer.getProperties(),
                 containerName, Constants.PROPERTY_ELEMENT_TYPE_NAME);
    }

    protected void validate(Metadata metadata, Map<String, Object> properties, Map<String, Object> extensionProperties,
                            String containerName, String elementType) {
        for (String propertyName : extensionProperties.keySet()) {
            boolean isOverwritable = metadata.getOverwritableMetadata(propertyName);
            Object value = properties.get(propertyName);
            Object extensionValue = extensionProperties.get(propertyName);
            if (!isOverwritable && extensionValue != null) {
                if (value == null) {
                    throw new ContentException(Messages.CANNOT_MODIFY_ELEMENT,
                                               elementType,
                                               getPrefixedName(containerName, propertyName),
                                               extensionDescriptor.getId());
                }
                validateModifiableElements(elementType, containerName, extensionDescriptor.getId(), propertyName, extensionValue, value);
            }
        }
    }
}
