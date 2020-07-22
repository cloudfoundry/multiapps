package org.cloudfoundry.multiapps.mta.parsers.v2;

import static org.cloudfoundry.multiapps.mta.handlers.v2.Schemas.RESOURCE;

import java.util.List;
import java.util.Map;

import org.cloudfoundry.multiapps.common.ParsingException;
import org.cloudfoundry.multiapps.mta.model.Resource;
import org.cloudfoundry.multiapps.mta.parsers.ModelParser;
import org.cloudfoundry.multiapps.mta.schema.MapElement;

public class ResourceParser extends ModelParser<Resource> {

    protected static final String PROCESSED_OBJECT_NAME = "MTA resource";

    public static final String PARAMETERS = "parameters";
    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String DESCRIPTION = "description";
    public static final String GROUPS = "groups";
    public static final String PROPERTIES = "properties";

    public ResourceParser(Map<String, Object> source) {
        this(RESOURCE, source);
    }

    protected ResourceParser(MapElement schema, Map<String, Object> source) {
        super(PROCESSED_OBJECT_NAME, schema, source);
    }

    @Override
    public Resource parse() throws ParsingException {
        return createEntity().setDescription(getDescription())
                             .setName(getName())
                             .setType(getType())
                             .setProperties(getProperties())
                             .setParameters(getParameters());
    }

    protected Resource createEntity() {
        return Resource.createV2();
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
