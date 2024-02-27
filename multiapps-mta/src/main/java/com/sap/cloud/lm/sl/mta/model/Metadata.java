package com.sap.cloud.lm.sl.mta.model;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;

public class Metadata {

    public static final Metadata DEFAULT_METADATA = new Metadata(Collections.<String, Map<String, Object>> emptyMap());
    private static final boolean DEFAULT_OPTIONAL_VALUE = false;
    private static final boolean DEFAULT_OVERWRITABLE_VALUE = true;
    private static final boolean DEFAULT_SENSITIVE_VALUE = false;
    private static final String OVERWRITABLE = "overwritable";
    private static final String OPTIONAL = "optional";
    private static final String SENSITIVE = "sensitive";
    private Map<String, Map<String, Object>> metadata;

    public Metadata(Map<String, Map<String, Object>> metadata) {
        this.metadata = metadata;
    }

    public Map<String, Map<String, Object>> getMetadataMap() {
        return metadata;
    }

    @SuppressWarnings("unchecked")
    public <E> E getPropertyMetadata(String property, String metadataName) {
        Map<String, Object> propertyMetadata = metadata.get(property);
        if (propertyMetadata == null) {
            return null;
        }
        return (E) propertyMetadata.get(metadataName);
    }

    public boolean getOverwritableMetadata(String property) {
        return getPropertyMetadata(property, OVERWRITABLE, DEFAULT_OVERWRITABLE_VALUE);
    }

    public boolean getOptionalMetadata(String property) {
        return getPropertyMetadata(property, OPTIONAL, DEFAULT_OPTIONAL_VALUE);
    }

    public boolean getSensitiveMetadata(String property) {
        return getPropertyMetadata(property, SENSITIVE, DEFAULT_SENSITIVE_VALUE);
    }

    private <E> E getPropertyMetadata(String property, String metadataName, E defaultValue) {
        E overWritableMetadata = this.getPropertyMetadata(property, metadataName);
        return ObjectUtils.defaultIfNull(overWritableMetadata, defaultValue);
    }

}
