package com.sap.cloud.lm.sl.mta.validators.v2;

import static com.sap.cloud.lm.sl.mta.util.ValidatorUtil.getPrefixedName;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.message.Messages;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;
import com.sap.cloud.lm.sl.mta.validators.DescriptorValidationRules;

public class DefaultDescriptorValidationRules implements DescriptorValidationRules {

    protected final Set<String> emptyProperties = new TreeSet<>();
    protected final Set<String> emptyParameters = new TreeSet<>();

    @Override
    public void validateProperties(PropertiesContainer propertiesContainer, ElementContext elementContext) throws ContentException {
        checkForEmptyFields(propertiesContainer.getProperties(), elementContext, emptyProperties);
    }

    @Override
    public void validateParameters(ParametersContainer parametersContainer, ElementContext elementContext) throws ContentException {
        checkForEmptyFields(parametersContainer.getParameters(), elementContext, emptyParameters);
    }

    protected void checkForEmptyFields(Map<String, Object> properties, ElementContext elementContext, Set<String> emptyFields) {
        for (String propertyName : properties.keySet()) {
            if (properties.get(propertyName) == null) {
                emptyFields.add(getPrefixedName(elementContext.getPrefixedName(), propertyName));
            }
        }
    }

    @Override
    public void postValidate() throws ContentException {
        if (emptyProperties.size() != 0) {
            throw new ContentException(Messages.UNRESOLVED_PROPERTIES, emptyProperties);
        }

        if (emptyParameters.size() != 0) {
            throw new ContentException(Messages.UNRESOLVED_PARAMETERS, emptyParameters);
        }
    }

}
