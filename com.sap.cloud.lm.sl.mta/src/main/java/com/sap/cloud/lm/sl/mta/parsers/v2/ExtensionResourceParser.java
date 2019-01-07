package com.sap.cloud.lm.sl.mta.parsers.v2;

import static com.sap.cloud.lm.sl.mta.handlers.v2.Schemas.EXT_RESOURCE;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionResource;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionResource.Builder;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ExtensionResourceParser extends com.sap.cloud.lm.sl.mta.parsers.v1.ExtensionResourceParser {

    public static final String PARAMETERS = "parameters";

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

    protected Map<String, Object> getParameters() {
        return getMapElement(PARAMETERS);
    }

}
