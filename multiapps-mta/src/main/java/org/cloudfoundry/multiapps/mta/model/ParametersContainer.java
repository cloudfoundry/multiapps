package org.cloudfoundry.multiapps.mta.model;

import java.util.Map;

public interface ParametersContainer {

    Map<String, Object> getParameters();

    Object setParameters(Map<String, Object> parameters);

}
