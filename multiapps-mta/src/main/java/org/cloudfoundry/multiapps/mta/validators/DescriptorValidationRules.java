package org.cloudfoundry.multiapps.mta.validators;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.model.ElementContext;
import org.cloudfoundry.multiapps.mta.model.ParametersContainer;
import org.cloudfoundry.multiapps.mta.model.PropertiesContainer;

public interface DescriptorValidationRules {

    void postValidate() throws ContentException;

    void validateProperties(ElementContext elementContext, PropertiesContainer propertiesContainer) throws ContentException;

    void validateParameters(ElementContext elementContext, ParametersContainer parametersContainer) throws ContentException;

}
