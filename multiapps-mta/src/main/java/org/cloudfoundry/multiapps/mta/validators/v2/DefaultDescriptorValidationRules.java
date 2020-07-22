package org.cloudfoundry.multiapps.mta.validators.v2;

import static org.cloudfoundry.multiapps.mta.util.ValidatorUtil.getPrefixedName;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.Messages;
import org.cloudfoundry.multiapps.mta.model.ElementContext;
import org.cloudfoundry.multiapps.mta.model.ParametersContainer;
import org.cloudfoundry.multiapps.mta.model.PropertiesContainer;
import org.cloudfoundry.multiapps.mta.validators.DescriptorValidationRules;

public class DefaultDescriptorValidationRules implements DescriptorValidationRules {

    protected final Set<String> emptyProperties = new TreeSet<>();
    protected final Set<String> emptyParameters = new TreeSet<>();

    @Override
    public void validateProperties(ElementContext elementContext, PropertiesContainer propertiesContainer) throws ContentException {
        checkForEmptyFields(propertiesContainer.getProperties(), elementContext, emptyProperties);
    }

    @Override
    public void validateParameters(ElementContext elementContext, ParametersContainer parametersContainer) throws ContentException {
        checkForEmptyFields(parametersContainer.getParameters(), elementContext, emptyParameters);
    }

    protected void checkForEmptyFields(Map<String, Object> properties, ElementContext elementContext, Set<String> emptyFields) {
        properties.entrySet()
                  .stream()
                  .filter(property -> property.getValue() == null)
                  .map(property -> getPrefixedName(elementContext.getPrefixedName(), property.getKey()))
                  .forEach(emptyFields::add);
    }

    @Override
    public void postValidate() throws ContentException {
        if (!emptyProperties.isEmpty()) {
            throw new ContentException(Messages.UNRESOLVED_PROPERTIES, emptyProperties);
        }

        if (!emptyParameters.isEmpty()) {
            throw new ContentException(Messages.UNRESOLVED_PARAMETERS, emptyParameters);
        }
    }

}
