package com.sap.cloud.lm.sl.mta.parsers.v2_0;

import static com.sap.cloud.lm.sl.mta.handlers.v2_0.Schemas.EXT_PROVIDED_DEPENDENCY;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionProvidedDependency.ExtensionProvidedDependencyBuilder;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ExtensionProvidedDependencyParser extends com.sap.cloud.lm.sl.mta.parsers.v1_0.ExtensionProvidedDependencyParser {

    public ExtensionProvidedDependencyParser(Map<String, Object> source) {
        super(EXT_PROVIDED_DEPENDENCY, source);
    }

    protected ExtensionProvidedDependencyParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
    }

    @Override
    public ExtensionProvidedDependency parse() throws ParsingException {
        ExtensionProvidedDependencyBuilder builder = new ExtensionProvidedDependencyBuilder();
        builder.setName(getName());
        builder.setProperties(getProperties());
        return builder.build();
    }

}
