package com.sap.cloud.lm.sl.mta.parsers.v2_0;

import static com.sap.cloud.lm.sl.mta.handlers.v2_0.Schemas.PTF_RESOURCE_TYPE;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v2_0.PlatformResourceType;
import com.sap.cloud.lm.sl.mta.model.v2_0.PlatformResourceType.PlatformResourceTypeBuilder;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class PlatformResourceTypeParser extends com.sap.cloud.lm.sl.mta.parsers.v1_0.PlatformResourceTypeParser {

    public static final String PARAMETERS = "parameters";

    public PlatformResourceTypeParser(Map<String, Object> source) {
        super(PTF_RESOURCE_TYPE, source);
    }

    protected PlatformResourceTypeParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
    }

    @Override
    public PlatformResourceType parse() throws ParsingException {
        PlatformResourceTypeBuilder builder = new PlatformResourceTypeBuilder();
        builder.setName(getName());
        builder.setParameters(getParameters());
        return builder.build();
    }

    protected Map<String, Object> getParameters() {
        return getMapElement(PARAMETERS);
    }

}
