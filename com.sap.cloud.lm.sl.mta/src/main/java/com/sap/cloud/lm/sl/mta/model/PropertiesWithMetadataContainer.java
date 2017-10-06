package com.sap.cloud.lm.sl.mta.model;

public interface PropertiesWithMetadataContainer extends PropertiesContainer {

    void setPropertiesMetadata(Metadata metadata);

    Metadata getPropertiesMetadata();
}
