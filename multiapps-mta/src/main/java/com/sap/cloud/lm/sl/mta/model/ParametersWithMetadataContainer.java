package com.sap.cloud.lm.sl.mta.model;

public interface ParametersWithMetadataContainer extends ParametersContainer {

    Metadata getParametersMetadata();

    void setParametersMetadata(Metadata metadata);
}
