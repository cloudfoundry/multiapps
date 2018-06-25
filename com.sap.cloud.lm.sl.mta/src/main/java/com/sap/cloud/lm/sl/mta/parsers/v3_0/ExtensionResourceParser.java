package com.sap.cloud.lm.sl.mta.parsers.v3_0;

import static com.sap.cloud.lm.sl.mta.handlers.v3_0.Schemas.EXT_RESOURCE;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v3_0.ExtensionResource;
import com.sap.cloud.lm.sl.mta.model.v3_0.ExtensionResource.Builder;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ExtensionResourceParser extends com.sap.cloud.lm.sl.mta.parsers.v2_0.ExtensionResourceParser {

    public ExtensionResourceParser(Map<String, Object> source) {
        super(EXT_RESOURCE, source);
    }

    protected ExtensionResourceParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
    }

    @Override
    public ExtensionResource parse() throws ParsingException {
        Builder builder = new Builder();
        builder.setName(getName());
        builder.setParameters(getParameters());
        builder.setProperties(getProperties());
        return builder.build();
    }

}
