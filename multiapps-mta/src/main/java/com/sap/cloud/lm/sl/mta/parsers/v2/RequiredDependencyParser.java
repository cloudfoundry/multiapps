package com.sap.cloud.lm.sl.mta.parsers.v2;

import static com.sap.cloud.lm.sl.mta.handlers.v2.Schemas.REQUIRED_DEPENDENCY;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.RequiredDependency;
import com.sap.cloud.lm.sl.mta.parsers.ModelParser;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class RequiredDependencyParser extends ModelParser<RequiredDependency> {

    protected static final String PROCESSED_OBJECT_NAME = "MTA required dependency";

    public static final String NAME = "name";
    public static final String PROPERTIES = "properties";
    public static final String PARAMETERS = "parameters";
    public static final String GROUP = "group";
    public static final String LIST = "list";

    public RequiredDependencyParser(Map<String, Object> source) {
        this(REQUIRED_DEPENDENCY, source);
    }

    protected RequiredDependencyParser(MapElement schema, Map<String, Object> source) {
        super(PROCESSED_OBJECT_NAME, schema, source);
    }

    @Override
    public RequiredDependency parse() throws ParsingException {
        return createEntity().setGroup(getGroup())
                             .setName(setName())
                             .setList(getList())
                             .setProperties(getProperties())
                             .setParameters(getParameters());
    }

    protected RequiredDependency createEntity() {
        return RequiredDependency.createV2();
    }

    protected String setName() {
        return getStringElement(NAME);
    }

    protected String getGroup() {
        return getStringElement(GROUP);
    }

    protected String getList() {
        return getStringElement(LIST);
    }

    protected Map<String, Object> getProperties() {
        return getMapElement(PROPERTIES);
    }

    protected Map<String, Object> getParameters() {
        return getMapElement(PARAMETERS);
    }

}
