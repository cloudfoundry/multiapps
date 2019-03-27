package com.sap.cloud.lm.sl.mta.parsers;

import static com.sap.cloud.lm.sl.mta.handlers.v2.Schemas.RESOURCE_TYPE;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.ResourceType;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ResourceTypeParser extends ModelParser<ResourceType> {

    protected static final String PROCESSED_OBJECT_NAME = "MTA resource type";

    public static final String NAME = "name";
    public static final String PARAMETERS = "parameters";

    public ResourceTypeParser(Map<String, Object> source) {
        this(RESOURCE_TYPE, source);
    }

    protected ResourceTypeParser(MapElement schema, Map<String, Object> source) {
        super(PROCESSED_OBJECT_NAME, schema, source);
    }

    @Override
    public ResourceType parse() throws ParsingException {
        return new ResourceType().setName(getName())
            .setParameters(getParameters());
    }

    protected String getName() {
        return getStringElement(NAME);
    }

    protected Map<String, Object> getParameters() {
        return getMapElement(PARAMETERS);
    }

}
