package com.sap.cloud.lm.sl.mta.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.mta.model.Metadata;
import com.sap.cloud.lm.sl.mta.tags.TaggedObject;

public class MetadataUpdater {

    private Map<String, Object> properties;

    public MetadataUpdater(Map<String, Object> properties) {
        this.properties = properties;
    }

    public Metadata getUpdatedMetadata(Map<String, Map<String, Object>> metadataMap) {
        if (properties == null) {
            return new Metadata(ObjectUtils.defaultIfNull(metadataMap, Collections.<String, Map<String, Object>> emptyMap()));
        }
        if (metadataMap == null) {
            metadataMap = new LinkedHashMap<>();
        }
        updateMetadata(metadataMap);
        if (metadataMap.isEmpty()) {
            return new Metadata(Collections.<String, Map<String, Object>> emptyMap());
        }
        return new Metadata(metadataMap);
    }

    private void updateMetadata(Map<String, Map<String, Object>> metadataMap) {
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            if (entry.getValue() instanceof TaggedObject) {
                TaggedObject taggedObject = (TaggedObject) entry.getValue();
                addPropertyMetadata(metadataMap, entry.getKey(), taggedObject.getName(), taggedObject.getMetadataValue());
                entry.setValue(taggedObject.getValue());
            }
        }
    }

    private void addPropertyMetadata(Map<String, Map<String, Object>> metadataMap, String property, String metadataName,
                                     Object metadataValue) {
        Map<String, Object> metadataForProperty = metadataMap.get(property);
        if (metadataForProperty == null) {
            metadataForProperty = new HashMap<>();
            metadataMap.put(property, metadataForProperty);
        }
        metadataForProperty.put(metadataName, metadataValue);
    }
}
