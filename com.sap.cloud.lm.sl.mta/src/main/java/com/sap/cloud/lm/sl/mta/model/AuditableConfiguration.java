package com.sap.cloud.lm.sl.mta.model;

import java.util.List;

public interface AuditableConfiguration {

    public String getConfigurationType();

    public String getConfiguratioName();

    public List<ConfigurationIdentifier> getConfigurationIdentifiers();

}
