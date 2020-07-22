package com.sap.cloud.lm.sl.mta.model;

public interface PropertiesWithMetadataContainer extends PropertiesContainer {

    Object setPropertiesMetadata(Metadata metadata);

    Metadata getPropertiesMetadata();
}
