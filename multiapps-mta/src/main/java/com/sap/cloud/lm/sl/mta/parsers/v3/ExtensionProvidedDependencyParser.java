package com.sap.cloud.lm.sl.mta.parsers.v3;

import static com.sap.cloud.lm.sl.mta.handlers.v3.Schemas.EXT_PROVIDED_DEPENDENCY;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v3.ExtensionProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v3.ExtensionProvidedDependency.Builder;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ExtensionProvidedDependencyParser extends com.sap.cloud.lm.sl.mta.parsers.v2.ExtensionProvidedDependencyParser {

    public ExtensionProvidedDependencyParser(Map<String, Object> source) {
        super(EXT_PROVIDED_DEPENDENCY, source);
    }

    protected ExtensionProvidedDependencyParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
    }

    @Override
    public ExtensionProvidedDependency parse() throws ParsingException {
        Builder builder = new Builder();
        builder.setName(getName());
        builder.setProperties(getProperties());
        return builder.build();
    }

}
