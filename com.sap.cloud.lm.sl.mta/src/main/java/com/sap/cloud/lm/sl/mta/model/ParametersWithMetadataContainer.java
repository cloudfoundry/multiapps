package com.sap.cloud.lm.sl.mta.model;

public interface ParametersWithMetadataContainer extends ParametersContainer {

    void setParametersMetadata(Metadata metadata);

    Metadata getParametersMetadata();
}
