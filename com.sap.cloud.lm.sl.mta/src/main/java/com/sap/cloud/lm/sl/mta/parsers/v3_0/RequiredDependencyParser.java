package com.sap.cloud.lm.sl.mta.parsers.v3_0;

import static com.sap.cloud.lm.sl.mta.handlers.v3_0.Schemas.REQUIRED_DEPENDENCY;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v3_0.RequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v3_0.RequiredDependency.Builder;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class RequiredDependencyParser extends com.sap.cloud.lm.sl.mta.parsers.v2_0.RequiredDependencyParser {

    public RequiredDependencyParser(Map<String, Object> source) {
        super(REQUIRED_DEPENDENCY, source);
    }

    protected RequiredDependencyParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
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

}
