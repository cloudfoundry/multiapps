package com.sap.cloud.lm.sl.mta.parsers.v3_1;

import static com.sap.cloud.lm.sl.mta.handlers.v3_1.Schemas.EXT_PROVIDED_DEPENDENCY;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v3_1.ExtensionProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v3_1.ExtensionProvidedDependency.ExtensionProvidedDependencyBuilder;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ExtensionProvidedDependencyParser extends com.sap.cloud.lm.sl.mta.parsers.v3_0.ExtensionProvidedDependencyParser {

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
