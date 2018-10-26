package com.sap.cloud.lm.sl.mta.validators.v2;

import java.util.Set;
import java.util.TreeSet;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.message.Messages;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;

public class DefaultDescriptorValidationRules extends com.sap.cloud.lm.sl.mta.validators.v1.DefaultDescriptorValidationRules {

    protected final Set<String> emptyParameters = new TreeSet<>();

    @Override
    public void validateParameters(ParametersContainer parametersContainer, ElementContext elementContext) throws ContentException {
        checkForEmptyFields(parametersContainer.getParameters(), elementContext, emptyParameters);
    }

    @Override
    public void postValidate() throws ContentException {
        super.postValidate();
        if (emptyParameters.size() != 0) {
            throw new ContentException(Messages.UNRESOLVED_PARAMETERS, emptyParameters);
        }
    }
}
