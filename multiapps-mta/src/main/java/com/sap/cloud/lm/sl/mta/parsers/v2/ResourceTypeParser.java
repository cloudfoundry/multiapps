package com.sap.cloud.lm.sl.mta.parsers.v2;

import static com.sap.cloud.lm.sl.mta.handlers.v2.Schemas.RESOURCE_TYPE;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v2.ResourceType;
import com.sap.cloud.lm.sl.mta.model.v2.ResourceType.Builder;
import com.sap.cloud.lm.sl.mta.parsers.ModelParser;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ResourceTypeParser extends ModelParser<ResourceType> {

    public static final String NAME = "name";
    public static final String PROPERTIES = "properties";
    public static final String PARAMETERS = "parameters";
    public static final String RESOURCE_MANAGER = "resource-manager";
    protected static final String PROCESSED_OBJECT_NAME = "MTA resource type";

    public ResourceTypeParser(Map<String, Object> source) {
        this(RESOURCE_TYPE, source);
    }

    protected ResourceTypeParser(MapElement schema, Map<String, Object> source) {
        super(PROCESSED_OBJECT_NAME, schema, source);
    }

    @Override
    public ResourceType parse() throws ParsingException {
        Builder builder = new Builder();
        builder.setName(getName());
        builder.setResourceManager(getResourceManager());
        builder.setParameters(getParameters());
        return builder.build();
    }

    protected String getName() {
        return getStringElement(NAME);
    }

    protected String getResourceManager() {
        return getStringElement(RESOURCE_MANAGER);
    }

    protected Map<String, Object> getParameters() {
        return getMapElement(PARAMETERS);
    }

}
