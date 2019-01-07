package com.sap.cloud.lm.sl.mta.parsers.v1;

import static com.sap.cloud.lm.sl.mta.handlers.v1.Schemas.PROVIDED_DEPENDENCY;

import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v1.ProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v1.ProvidedDependency.Builder;
import com.sap.cloud.lm.sl.mta.parsers.ModelParser;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ProvidedDependencyParser extends ModelParser<ProvidedDependency> {

    protected static final String PROCESSED_OBJECT_NAME = "MTA provided dependency";

    public static final String NAME = "name";
    public static final String PROPERTIES = "properties";
    public static final String GROUPS = "groups";

    public ProvidedDependencyParser(Map<String, Object> source) {
        this(PROVIDED_DEPENDENCY, source);
    }

    protected ProvidedDependencyParser(MapElement schema, Map<String, Object> source) {
        super(PROCESSED_OBJECT_NAME, schema, source);
    }

    @Override
    public ProvidedDependency parse() throws ParsingException {
        Builder builder = new Builder();
        builder.setName(getName());
        builder.setGroups(getGroups());
        builder.setProperties(getProperties());
        return builder.build();
    }

    protected String getName() {
        return getStringElement(NAME);
    }

    protected List<String> getGroups() {
        return getListElement(GROUPS);
    }

    protected Map<String, Object> getProperties() {
        return getMapElement(PROPERTIES);
    }

}
