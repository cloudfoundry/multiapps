package org.cloudfoundry.multiapps.mta.validators.v3;

import java.util.Map;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.Constants;
import org.cloudfoundry.multiapps.mta.Messages;
import org.cloudfoundry.multiapps.mta.model.ElementContext;
import org.cloudfoundry.multiapps.mta.model.Metadata;
import org.cloudfoundry.multiapps.mta.model.ParametersContainer;
import org.cloudfoundry.multiapps.mta.model.ParametersWithMetadataContainer;
import org.cloudfoundry.multiapps.mta.model.PropertiesContainer;
import org.cloudfoundry.multiapps.mta.model.PropertiesWithMetadataContainer;
import org.cloudfoundry.multiapps.mta.util.NameUtil;

public class DefaultDescriptorValidationRules extends org.cloudfoundry.multiapps.mta.validators.v2.DefaultDescriptorValidationRules {

    @Override
    public void validateProperties(ElementContext elementContext, PropertiesContainer propertiesContainer) throws ContentException {
        PropertiesWithMetadataContainer propertiesWithMetadata = (PropertiesWithMetadataContainer) propertiesContainer;
        validateOptionalProperties(propertiesWithMetadata.getProperties(), propertiesWithMetadata.getPropertiesMetadata(), elementContext,
                                   Constants.PROPERTY_ELEMENT_TYPE_NAME);
    }

    @Override
    public void validateParameters(ElementContext elementContext, ParametersContainer parametersContainer) throws ContentException {
        ParametersWithMetadataContainer parametersWithMetadata = (ParametersWithMetadataContainer) parametersContainer;
        validateOptionalProperties(parametersWithMetadata.getParameters(), parametersWithMetadata.getParametersMetadata(), elementContext,
                                   Constants.PARAMETER_ELEMENT_TYPE_NAME);
    }

    protected void validateOptionalProperties(Map<String, Object> properties, Metadata metadata, ElementContext elementContext,
                                              String elementType) {
        for (Map.Entry<String, Object> property : properties.entrySet()) {
            if (!isPropertyValid(property.getValue(), property.getKey(), metadata)) {
                throw new ContentException(Messages.MANDATORY_ELEMENT_HAS_NO_VALUE,
                                           elementType,
                                           NameUtil.getPrefixedName(elementContext.getPrefixedName(), property.getKey()));
            }
        }
    }

    protected boolean isPropertyValid(Object property, String propertyName, Metadata metadata) {
        boolean isOptional = metadata.getOptionalMetadata(propertyName);
        return isOptional || property != null;
    }

}
