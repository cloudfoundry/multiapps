package com.sap.cloud.lm.sl.mta.parsers.v3_1;

import static com.sap.cloud.lm.sl.mta.handlers.v3_1.Schemas.REQUIRED_DEPENDENCY;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.Metadata;
import com.sap.cloud.lm.sl.mta.model.v3_1.RequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v3_1.RequiredDependency.Builder;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class RequiredDependencyParser extends com.sap.cloud.lm.sl.mta.parsers.v3_0.RequiredDependencyParser {

    public static final String PROPERTIES_METADATA = "properties-metadata";
    public static final String PARAMETERS_METADATA = "parameters-metadata";

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
        builder.setParametersMetadata(getParametersMetadata());
        builder.setPropertiesMetadata(getPropertiesMetadata());
        return builder.build();
    }

    protected Metadata getPropertiesMetadata() {
        return getMetadata(PROPERTIES_METADATA, getProperties());
    }

    protected Metadata getParametersMetadata() {
        return getMetadata(PARAMETERS_METADATA, getParameters());
    }
}
