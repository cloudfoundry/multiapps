package com.sap.cloud.lm.sl.mta.parsers.v2_0;

import static com.sap.cloud.lm.sl.mta.handlers.v2_0.Schemas.EXT_REQUIRED_DEPENDENCY;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionRequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionRequiredDependency.ExtensionRequiredDependencyBuilder;
import com.sap.cloud.lm.sl.mta.parsers.ModelParser;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ExtensionRequiredDependencyParser extends ModelParser<ExtensionRequiredDependency> {

    protected static final String PROCESSED_OBJECT_NAME = "MTA extension required dependency";

    public static final String NAME = "name";
    public static final String PROPERTIES = "properties";
    public static final String PARAMETERS = "parameters";

    public ExtensionRequiredDependencyParser(Map<String, Object> source) {
        this(EXT_REQUIRED_DEPENDENCY, source);
    }

    protected ExtensionRequiredDependencyParser(MapElement schema, Map<String, Object> source) {
        super(PROCESSED_OBJECT_NAME, schema, source);
    }

    @Override
    public ExtensionRequiredDependency parse() throws ParsingException {
        ExtensionRequiredDependencyBuilder builder = new ExtensionRequiredDependencyBuilder();
        builder.setName(getName());
        builder.setProperties(getProperties());
        builder.setParameters(getParameters());
        return builder.build();
    }

    protected String getName() throws ParsingException {
        return getStringElement(NAME);
    }

    protected Map<String, Object> getProperties() {
        return getMapElement(PROPERTIES);
    }

    protected Map<String, Object> getParameters() {
        return getMapElement(PARAMETERS);
    }

}
