package com.sap.cloud.lm.sl.mta.parsers.v3_1;

import static com.sap.cloud.lm.sl.mta.handlers.v3_1.Schemas.PROVIDED_DEPENDENCY;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.Metadata;
import com.sap.cloud.lm.sl.mta.model.v3_1.ProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v3_1.ProvidedDependency.ProvidedDependencyBuilder;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ProvidedDependencyParser extends com.sap.cloud.lm.sl.mta.parsers.v3_0.ProvidedDependencyParser {

    public static final String PROPERTIES_METADATA = "properties-metadata";
    public static final String PARAMETERS = "parameters";
    public static final String PARAMETERS_METADATA = "parameters-metadata";

    public ProvidedDependencyParser(Map<String, Object> source) {
        super(PROVIDED_DEPENDENCY, source);
    }

    protected ProvidedDependencyParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
    }

    @Override
    public ProvidedDependency parse() throws ParsingException {
        ProvidedDependencyBuilder builder = new ProvidedDependencyBuilder();
        builder.setName(getName());
        builder.setPublic(isPublic());
        builder.setProperties(getProperties());
        builder.setPropertiesMetadata(getPropertiesMetadata());
        builder.setParameters(getParameters());
        builder.setParametersMetadata(getParametersMetadata());
        return builder.build();
    }

    protected Metadata getParametersMetadata() {
        return getMetadata(PARAMETERS_METADATA, getParameters());
    }

    protected Metadata getPropertiesMetadata() {
        return getMetadata(PROPERTIES_METADATA, getProperties());
    }

    protected Map<String, Object> getParameters() {
        return getMapElement(PARAMETERS);
    }
}
