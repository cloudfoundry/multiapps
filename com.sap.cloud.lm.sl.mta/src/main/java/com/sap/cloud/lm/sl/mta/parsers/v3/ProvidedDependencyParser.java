package com.sap.cloud.lm.sl.mta.parsers.v3;

import static com.sap.cloud.lm.sl.mta.handlers.v3.Schemas.PROVIDED_DEPENDENCY;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.Metadata;
import com.sap.cloud.lm.sl.mta.model.ProvidedDependency;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ProvidedDependencyParser extends com.sap.cloud.lm.sl.mta.parsers.v2.ProvidedDependencyParser {

    public static final String PROPERTIES_METADATA = "properties-metadata";
    public static final String PARAMETERS_METADATA = "parameters-metadata";

    public ProvidedDependencyParser(Map<String, Object> source) {
        super(PROVIDED_DEPENDENCY, source);
    }

    protected ProvidedDependencyParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
    }

    @Override
    public ProvidedDependency parse() throws ParsingException {
        return super.parse().setPropertiesMetadata(getPropertiesMetadata())
                            .setParametersMetadata(getParametersMetadata());
    }

    @Override
    public ProvidedDependency createEntity() {
        return ProvidedDependency.createV3();
    }

    protected Metadata getParametersMetadata() {
        return getMetadata(PARAMETERS_METADATA, getParameters());
    }

    protected Metadata getPropertiesMetadata() {
        return getMetadata(PROPERTIES_METADATA, getProperties());
    }

}
