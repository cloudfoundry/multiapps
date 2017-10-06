package com.sap.cloud.lm.sl.mta.model;

import java.util.Map;

public interface ParametersContainer {

    Map<String, Object> getParameters();

    void setParameters(Map<String, Object> parameters);
}
