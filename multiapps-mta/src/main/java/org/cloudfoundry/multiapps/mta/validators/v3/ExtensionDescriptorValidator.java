package org.cloudfoundry.multiapps.mta.validators.v3;

import java.util.Map;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.common.util.MiscUtil;
import org.cloudfoundry.multiapps.mta.Constants;
import org.cloudfoundry.multiapps.mta.Messages;
import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorHandler;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.ExtensionDescriptor;
import org.cloudfoundry.multiapps.mta.model.Metadata;
import org.cloudfoundry.multiapps.mta.model.ParametersContainer;
import org.cloudfoundry.multiapps.mta.model.ParametersWithMetadataContainer;
import org.cloudfoundry.multiapps.mta.model.PropertiesContainer;
import org.cloudfoundry.multiapps.mta.model.PropertiesWithMetadataContainer;
import org.cloudfoundry.multiapps.mta.util.NameUtil;

public class ExtensionDescriptorValidator extends org.cloudfoundry.multiapps.mta.validators.v2.ExtensionDescriptorValidator {

    public ExtensionDescriptorValidator(ExtensionDescriptor extensionDescriptor, DeploymentDescriptor deploymentDescriptor,
                                        DescriptorHandler handler) {
        super(extensionDescriptor, deploymentDescriptor, handler);
    }

    @Override
    protected void validateParameters(ParametersContainer container, ParametersContainer extensionContainer, String containerName) {
        ParametersWithMetadataContainer containerWithMetadata = MiscUtil.cast(container);
        validate(containerWithMetadata.getParametersMetadata(), containerWithMetadata.getParameters(), extensionContainer.getParameters(),
                 containerName, Constants.PARAMETER_ELEMENT_TYPE_NAME);
    }

    @Override
    protected void validateProperties(PropertiesContainer container, PropertiesContainer extensionContainer, String containerName) {
        PropertiesWithMetadataContainer containerWithMetadata = MiscUtil.cast(container);
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
                                               NameUtil.getPrefixedName(containerName, extensionProperty.getKey()),
                                               extensionDescriptor.getId());
                }
                validateModifiableElements(elementType, containerName, extensionDescriptor.getId(), extensionProperty.getKey(),
                                           extensionProperty.getValue(), value);
            }
        }
    }
}
