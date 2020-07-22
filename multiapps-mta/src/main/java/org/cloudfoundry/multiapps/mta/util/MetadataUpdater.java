package org.cloudfoundry.multiapps.mta.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.cloudfoundry.multiapps.common.tags.TaggedObject;
import org.cloudfoundry.multiapps.mta.model.Metadata;

public class MetadataUpdater {

    private Map<String, Object> properties;

    public MetadataUpdater(Map<String, Object> properties) {
        this.properties = properties;
    }

    public Metadata getUpdatedMetadata(Map<String, Map<String, Object>> metadataMap) {

        if (metadataMap == null) {
            metadataMap = new LinkedHashMap<>();
        }
        if (properties != null) {
            updateMetadata(metadataMap);
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
        metadataMap.computeIfAbsent(property, k -> new HashMap<>())
                   .put(metadataName, metadataValue);
    }
}
