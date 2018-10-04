package com.sap.cloud.lm.sl.mta.parsers.v3;

import static com.sap.cloud.lm.sl.mta.handlers.v3.Schemas.REQUIRED_DEPENDENCY;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.Metadata;
import com.sap.cloud.lm.sl.mta.model.v3.RequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v3.RequiredDependency.Builder;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class RequiredDependencyParser extends com.sap.cloud.lm.sl.mta.parsers.v2.RequiredDependencyParser {

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
        builder.setPropertiesMetadata(getPropertiesMetadata());
        builder.setParametersMetadata(getParametersMetadata());
        return builder.build();
    }

    protected Metadata getPropertiesMetadata() {
        return getMetadata(PROPERTIES_METADATA, getProperties());
    }

    protected Metadata getParametersMetadata() {
        return getMetadata(PARAMETERS_METADATA, getParameters());
    }

}
