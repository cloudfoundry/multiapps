package com.sap.cloud.lm.sl.mta.model;

import java.util.Map;

public interface PropertiesContainer {

    Map<String, Object> getProperties();

    Object setProperties(Map<String, Object> properties);

}
