package org.cloudfoundry.multiapps.mta.model;

public interface PropertiesWithMetadataContainer extends PropertiesContainer {

    Object setPropertiesMetadata(Metadata metadata);

    Metadata getPropertiesMetadata();
}
