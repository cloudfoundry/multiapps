package com.sap.cloud.lm.sl.mta.util;

import java.util.Map;

import com.sap.cloud.lm.sl.mta.model.Metadata;

public class MetadataConverter implements YamlConverter<Metadata, Map<String, Map<String, Object>>> {

    @Override
    public Map<String, Map<String, Object>> convert(Metadata value) {
        return value.getMetadataMap();
    }
}
