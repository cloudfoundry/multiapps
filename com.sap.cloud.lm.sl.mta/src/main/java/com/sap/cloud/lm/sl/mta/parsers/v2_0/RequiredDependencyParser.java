package com.sap.cloud.lm.sl.mta.parsers.v2_0;

import static com.sap.cloud.lm.sl.mta.handlers.v2_0.Schemas.REQUIRED_DEPENDENCY;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v2_0.RequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v2_0.RequiredDependency.RequiredDependencyBuilder;
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
        RequiredDependencyBuilder builder = new RequiredDependencyBuilder();
        builder.setName(getName());
        builder.setGroup(getGroup());
        builder.setList(getList());
        builder.setProperties(getProperties());
        builder.setParameters(getParameters());
        return builder.build();
    }

    protected String getName() throws ParsingException {
        return getStringElement(NAME);
    }

    protected String getGroup() throws ParsingException {
        return getStringElement(GROUP);
    }

    protected String getList() throws ParsingException {
        return getStringElement(LIST);
    }

    protected Map<String, Object> getProperties() {
        return getMapElement(PROPERTIES);
    }

    protected Map<String, Object> getParameters() {
        return getMapElement(PARAMETERS);
    }

}
