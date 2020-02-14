package com.sap.cloud.lm.sl.mta.validators.v3;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.cast;
import static com.sap.cloud.lm.sl.mta.util.ValidatorUtil.getPrefixedName;
import static com.sap.cloud.lm.sl.mta.util.ValidatorUtil.validateModifiableElements;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.Constants;
import com.sap.cloud.lm.sl.mta.Messages;
import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.Metadata;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;
import com.sap.cloud.lm.sl.mta.model.ParametersWithMetadataContainer;
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;
import com.sap.cloud.lm.sl.mta.model.PropertiesWithMetadataContainer;

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
        for (Map.Entry<String, Object> extensionProperty : extensionProperties.entrySet()) {
            boolean isOverwritable = metadata.getOverwritableMetadata(extensionProperty.getKey());
            Object value = properties.get(extensionProperty.getKey());
            if (!isOverwritable && extensionProperty.getValue() != null) {
                if (value == null) {
                    throw new ContentException(Messages.CANNOT_MODIFY_ELEMENT,
                                               elementType,
                                               getPrefixedName(containerName, extensionProperty.getKey()),
                                               extensionDescriptor.getId());
                }
                validateModifiableElements(elementType, containerName, extensionDescriptor.getId(), extensionProperty.getKey(),
                                           extensionProperty.getValue(), value);
            }
        }
    }
}
