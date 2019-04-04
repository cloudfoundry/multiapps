package com.sap.cloud.lm.sl.mta.validators;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;

public interface DescriptorValidationRules {

    public void postValidate() throws ContentException;

    public void validateProperties(ElementContext elementContext, PropertiesContainer propertiesContainer) throws ContentException;

    public void validateParameters(ElementContext elementContext, ParametersContainer parametersContainer) throws ContentException;

}
