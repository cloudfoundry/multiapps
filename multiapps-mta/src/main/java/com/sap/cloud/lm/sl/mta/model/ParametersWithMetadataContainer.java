package com.sap.cloud.lm.sl.mta.model;

public interface ParametersWithMetadataContainer extends ParametersContainer {

    Object setParametersMetadata(Metadata metadata);

    Metadata getParametersMetadata();
}
