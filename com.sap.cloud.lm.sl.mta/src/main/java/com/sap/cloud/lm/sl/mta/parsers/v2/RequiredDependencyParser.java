package com.sap.cloud.lm.sl.mta.parsers.v2;

import static com.sap.cloud.lm.sl.mta.handlers.v2.Schemas.REQUIRED_DEPENDENCY;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v2.RequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v2.RequiredDependency.Builder;
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
        Builder builder = new Builder();
        builder.setName(getName());
        builder.setGroup(getGroup());
        builder.setList(getList());
        builder.setProperties(getProperties());
        builder.setParameters(getParameters());
        return builder.build();
    }

    protected String getName() {
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
