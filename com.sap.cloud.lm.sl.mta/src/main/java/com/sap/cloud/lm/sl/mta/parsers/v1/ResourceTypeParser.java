package com.sap.cloud.lm.sl.mta.parsers.v1;

import static com.sap.cloud.lm.sl.mta.handlers.v1.Schemas.RESOURCE_TYPE;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v1.PlatformResourceType;
import com.sap.cloud.lm.sl.mta.model.v1.PlatformResourceType.Builder;
import com.sap.cloud.lm.sl.mta.parsers.ModelParser;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ResourceTypeParser extends ModelParser<PlatformResourceType> {

    protected static final String PROCESSED_OBJECT_NAME = "MTA resource type";

    public static final String PROPERTIES = "properties";
    public static final String NAME = "name";
    public static final String RESOURCE_MANAGER = "resource-manager";

    public ResourceTypeParser(Map<String, Object> source) {
        this(RESOURCE_TYPE, source);
    }

    protected ResourceTypeParser(MapElement schema, Map<String, Object> source) {
        super(PROCESSED_OBJECT_NAME, schema, source);
    }

    @Override
    public PlatformResourceType parse() throws ParsingException {
        Builder builder = new Builder();
        builder.setName(getName());
        builder.setResourceManager(getResourceManager());
        builder.setProperties(getProperties());
        return builder.build();
    }

    protected String getName() throws ParsingException {
        return getStringElement(NAME);
    }

    protected String getResourceManager() throws ParsingException {
        return getStringElement(RESOURCE_MANAGER);
    }

    protected Map<String, Object> getProperties() {
        return getMapElement(PROPERTIES);
    }

}
