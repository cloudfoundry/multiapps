package com.sap.cloud.lm.sl.mta.parsers.v2;

import static com.sap.cloud.lm.sl.mta.handlers.v2.Schemas.EXT_PROVIDED_DEPENDENCY;

import java.util.Map;

import com.sap.cloud.lm.sl.mta.model.v2.ExtensionProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionProvidedDependency.Builder;
import com.sap.cloud.lm.sl.mta.parsers.ModelParser;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ExtensionProvidedDependencyParser extends ModelParser<ExtensionProvidedDependency> {

    public static final String NAME = "name";
    public static final String PROPERTIES = "properties";
    protected static final String PROCESSED_OBJECT_NAME = "MTA extension provided dependency";

    public ExtensionProvidedDependencyParser(Map<String, Object> source) {
        this(EXT_PROVIDED_DEPENDENCY, source);
    }

    protected ExtensionProvidedDependencyParser(MapElement schema, Map<String, Object> source) {
        super(PROCESSED_OBJECT_NAME, schema, source);
    }

    protected String getName() {
        return getStringElement(NAME);
    }

    protected Map<String, Object> getProperties() {
        return getMapElement(PROPERTIES);
    }

    @Override
    public ExtensionProvidedDependency parse() {
        Builder builder = new Builder();
        builder.setName(getName());
        builder.setProperties(getProperties());
        return builder.build();
    }

}
