package com.sap.cloud.lm.sl.mta.parsers.v2;

import static com.sap.cloud.lm.sl.mta.handlers.v2.Schemas.RESOURCE;

import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v2.Resource;
import com.sap.cloud.lm.sl.mta.model.v2.Resource.Builder;
import com.sap.cloud.lm.sl.mta.parsers.ModelParser;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ResourceParser extends ModelParser<Resource> {

    public static final String PARAMETERS = "parameters";
    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String DESCRIPTION = "description";
    public static final String GROUPS = "groups";
    public static final String PROPERTIES = "properties";
    protected static final String PROCESSED_OBJECT_NAME = "MTA resource";

    public ResourceParser(Map<String, Object> source) {
        this(RESOURCE, source);
    }

    protected ResourceParser(MapElement schema, Map<String, Object> source) {
        super(PROCESSED_OBJECT_NAME, schema, source);
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

    protected String getName() {
        return getStringElement(NAME);
    }

    protected String getType() {
        return getStringElement(TYPE);
    }

    protected String getDescription() {
        return getStringElement(DESCRIPTION);
    }

    protected List<String> getGroups() {
        return getListElement(GROUPS);
    }

    protected Map<String, Object> getProperties() {
        return getMapElement(PROPERTIES);
    }

    protected Map<String, Object> getParameters() {
        return getMapElement(PARAMETERS);
    }

}
