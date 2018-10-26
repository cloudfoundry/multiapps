package com.sap.cloud.lm.sl.mta.parsers.v1;

import static com.sap.cloud.lm.sl.mta.handlers.v1.Schemas.EXT_RESOURCE;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v1.ExtensionResource;
import com.sap.cloud.lm.sl.mta.model.v1.ExtensionResource.Builder;
import com.sap.cloud.lm.sl.mta.parsers.ModelParser;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ExtensionResourceParser extends ModelParser<ExtensionResource> {

    protected static final String PROCESSED_OBJECT_NAME = "MTA extension resource";

    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String PROPERTIES = "properties";

    public ExtensionResourceParser(Map<String, Object> source) {
        this(EXT_RESOURCE, source);
    }

    protected ExtensionResourceParser(MapElement schema, Map<String, Object> source) {
        super(PROCESSED_OBJECT_NAME, schema, source);
    }

    @Override
    public ExtensionResource parse() throws ParsingException {
        Builder builder = new Builder();
        builder.setName(getName());
        builder.setProperties(getProperties());
        return builder.build();
    }

    protected String getName() {
        return getStringElement(NAME);
    }

    protected Map<String, Object> getProperties() {
        return getMapElement(PROPERTIES);
    }

}
