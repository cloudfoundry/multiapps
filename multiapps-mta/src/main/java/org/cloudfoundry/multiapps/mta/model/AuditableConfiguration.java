package org.cloudfoundry.multiapps.mta.model;

import java.util.List;

public interface AuditableConfiguration {

    String getConfigurationType();

    String getConfigurationName();

    List<ConfigurationIdentifier> getConfigurationIdentifiers();

}
