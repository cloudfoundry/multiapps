package com.sap.cloud.lm.sl.mta.parsers.v3;

import static com.sap.cloud.lm.sl.mta.handlers.v3.Schemas.RESOURCE_TYPE;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v3.ResourceType;
import com.sap.cloud.lm.sl.mta.model.v3.ResourceType.Builder;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ResourceTypeParser extends com.sap.cloud.lm.sl.mta.parsers.v2.ResourceTypeParser {

    public ResourceTypeParser(Map<String, Object> source) {
        super(RESOURCE_TYPE, source);
    }

    protected ResourceTypeParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
    }

    @Override
    public ResourceType parse() throws ParsingException {
        Builder builder = new Builder();
        builder.setName(getName());
        builder.setResourceManager(getResourceManager());
        builder.setParameters(getParameters());
        return builder.build();
    }

}
