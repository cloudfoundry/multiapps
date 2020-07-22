package org.cloudfoundry.multiapps.mta.util;

import java.util.Map;

import org.cloudfoundry.multiapps.common.util.yaml.YamlConverter;
import org.cloudfoundry.multiapps.mta.model.Metadata;

public class MetadataConverter implements YamlConverter<Metadata, Map<String, Map<String, Object>>> {

    @Override
    public Map<String, Map<String, Object>> convert(Metadata value) {
        return value.getMetadataMap();
    }
}
