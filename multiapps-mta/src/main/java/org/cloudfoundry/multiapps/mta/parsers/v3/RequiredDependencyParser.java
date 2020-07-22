package org.cloudfoundry.multiapps.mta.parsers.v3;

import static org.cloudfoundry.multiapps.mta.handlers.v3.Schemas.REQUIRED_DEPENDENCY;

import java.util.Map;

import org.cloudfoundry.multiapps.common.ParsingException;
import org.cloudfoundry.multiapps.mta.model.Metadata;
import org.cloudfoundry.multiapps.mta.model.RequiredDependency;
import org.cloudfoundry.multiapps.mta.schema.MapElement;

public class RequiredDependencyParser extends org.cloudfoundry.multiapps.mta.parsers.v2.RequiredDependencyParser {

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
        return super.parse().setPropertiesMetadata(getPropertiesMetadata())
                            .setParametersMetadata(getParametersMetadata());
    }

    @Override
    public RequiredDependency createEntity() {
        return RequiredDependency.createV3();
    }

    protected Metadata getPropertiesMetadata() {
        return getMetadata(PROPERTIES_METADATA, getProperties());
    }

    protected Metadata getParametersMetadata() {
        return getMetadata(PARAMETERS_METADATA, getParameters());
    }

}
