package com.sap.cloud.lm.sl.mta.model;

public interface PropertiesWithMetadataContainer extends PropertiesContainer {

    Metadata getPropertiesMetadata();

    void setPropertiesMetadata(Metadata metadata);
}
