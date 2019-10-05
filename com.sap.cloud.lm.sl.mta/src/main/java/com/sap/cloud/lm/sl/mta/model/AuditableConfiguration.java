package com.sap.cloud.lm.sl.mta.model;

import java.util.List;

public interface AuditableConfiguration {

    String getConfigurationType();

    String getConfigurationName();

    List<ConfigurationIdentifier> getConfigurationIdentifiers();

}
