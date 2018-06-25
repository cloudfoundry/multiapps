package com.sap.cloud.lm.sl.mta.parsers.v3_0;

import static com.sap.cloud.lm.sl.mta.handlers.v3_0.Schemas.RESOURCE;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v3_0.Resource;
import com.sap.cloud.lm.sl.mta.model.v3_0.Resource.Builder;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ResourceParser extends com.sap.cloud.lm.sl.mta.parsers.v2_0.ResourceParser {

    public ResourceParser(Map<String, Object> source) {
        super(RESOURCE, source);
    }

    protected ResourceParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
    }

    @Override
    public Resource parse() throws ParsingException {
        Builder builder = new Builder();
        builder.setName(getName());
        builder.setDescription(getDescription());
        builder.setType(getType());
        builder.setProperties(getProperties());
        builder.setParameters(getParameters());
        return builder.build();
    }

}
