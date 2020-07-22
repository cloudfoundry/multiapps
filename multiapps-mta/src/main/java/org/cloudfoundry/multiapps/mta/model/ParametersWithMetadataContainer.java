package org.cloudfoundry.multiapps.mta.model;

public interface ParametersWithMetadataContainer extends ParametersContainer {

    Object setParametersMetadata(Metadata metadata);

    Metadata getParametersMetadata();
}
