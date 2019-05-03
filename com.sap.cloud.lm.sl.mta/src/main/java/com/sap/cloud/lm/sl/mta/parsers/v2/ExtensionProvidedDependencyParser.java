package com.sap.cloud.lm.sl.mta.parsers.v2;

import static com.sap.cloud.lm.sl.mta.handlers.v2.Schemas.EXT_PROVIDED_DEPENDENCY;

import java.util.Map;

import com.sap.cloud.lm.sl.mta.model.ExtensionProvidedDependency;
import com.sap.cloud.lm.sl.mta.parsers.ModelParser;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ExtensionProvidedDependencyParser extends ModelParser<ExtensionProvidedDependency> {

    protected static final String PROCESSED_OBJECT_NAME = "MTA extension provided dependency";

    public static final String NAME = "name";
    public static final String PROPERTIES = "properties";
    public static final String PARAMETERS = "parameters";

    public ExtensionProvidedDependencyParser(Map<String, Object> source) {
        this(EXT_PROVIDED_DEPENDENCY, source);
    }

    protected ExtensionProvidedDependencyParser(MapElement schema, Map<String, Object> source) {
        super(PROCESSED_OBJECT_NAME, schema, source);
    }

    @Override
    public ExtensionProvidedDependency parse() {
        return createEntity().setName(getName())
            .setProperties(getProperties())
            .setParameters(getParameters());
    }

    protected ExtensionProvidedDependency createEntity() {
        return ExtensionProvidedDependency.createV2();
    }

    protected String getName() {
        return getStringElement(NAME);
    }

    protected Map<String, Object> getProperties() {
        return getMapElement(PROPERTIES);
    }

    protected Map<String, Object> getParameters() {
        return getMapElement(PARAMETERS);
    }

}
