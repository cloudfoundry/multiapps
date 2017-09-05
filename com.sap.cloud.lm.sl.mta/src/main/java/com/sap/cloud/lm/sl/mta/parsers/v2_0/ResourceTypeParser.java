package com.sap.cloud.lm.sl.mta.parsers.v2_0;

import static com.sap.cloud.lm.sl.mta.handlers.v2_0.Schemas.RESOURCE_TYPE;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v2_0.ResourceType;
import com.sap.cloud.lm.sl.mta.model.v2_0.ResourceType.ResourceTypeBuilder;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ResourceTypeParser extends com.sap.cloud.lm.sl.mta.parsers.v1_0.ResourceTypeParser {

    public static final String PARAMETERS = "parameters";

    public ResourceTypeParser(Map<String, Object> source) {
        super(RESOURCE_TYPE, source);
    }

    protected ResourceTypeParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
    }

    @Override
    public ResourceType parse() throws ParsingException {
        ResourceTypeBuilder builder = new ResourceTypeBuilder();
        builder.setName(getName());
        builder.setResourceManager(getResourceManager());
        builder.setParameters(getParameters());
        return builder.build();
    }

    protected Map<String, Object> getParameters() {
        return getMapElement(PARAMETERS);
    }

}
