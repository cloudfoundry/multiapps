package com.sap.cloud.lm.sl.mta.parsers.v3;

import static com.sap.cloud.lm.sl.mta.handlers.v3.Schemas.EXT_REQUIRED_DEPENDENCY;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v3.ExtensionRequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v3.ExtensionRequiredDependency.Builder;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ExtensionRequiredDependencyParser extends com.sap.cloud.lm.sl.mta.parsers.v2.ExtensionRequiredDependencyParser {

    public ExtensionRequiredDependencyParser(Map<String, Object> source) {
        super(EXT_REQUIRED_DEPENDENCY, source);
    }

    protected ExtensionRequiredDependencyParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
    }

    @Override
    public ExtensionRequiredDependency parse() throws ParsingException {
        Builder builder = new Builder();
        builder.setName(getName());
        builder.setProperties(getProperties());
        builder.setParameters(getParameters());
        return builder.build();
    }

}
