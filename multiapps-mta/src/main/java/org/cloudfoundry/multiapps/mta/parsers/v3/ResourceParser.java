package org.cloudfoundry.multiapps.mta.parsers.v3;

import static org.cloudfoundry.multiapps.mta.handlers.v3.Schemas.RESOURCE;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cloudfoundry.multiapps.common.ParsingException;
import org.cloudfoundry.multiapps.mta.model.Metadata;
import org.cloudfoundry.multiapps.mta.model.RequiredDependency;
import org.cloudfoundry.multiapps.mta.model.Resource;
import org.cloudfoundry.multiapps.mta.parsers.ListParser;
import org.cloudfoundry.multiapps.mta.schema.MapElement;

public class ResourceParser extends org.cloudfoundry.multiapps.mta.parsers.v2.ResourceParser {

    public static final String ACTIVE = "active";
    public static final String PROPERTIES_METADATA = "properties-metadata";
    public static final String PARAMETERS_METADATA = "parameters-metadata";
    public static final String REQUIRES = "requires";
    public static final String OPTIONAL = "optional";

    protected final Set<String> usedRequiredDependencyNames = new HashSet<>();

    public ResourceParser(Map<String, Object> source) {
        super(RESOURCE, source);
    }

    protected ResourceParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
    }

    @Override
    public Resource parse() throws ParsingException {
        return super.parse().setActive(getActive())
                            .setOptional(getOptional())
                            .setParametersMetadata(getParametersMetadata())
                            .setPropertiesMetadata(getPropertiesMetadata())
                            .setRequiredDependencies(getRequiredDependencies());
    }

    @Override
    public Resource createEntity() {
        return Resource.createV3();
    }

    protected Boolean getActive() {
        return getBooleanElement(ACTIVE);
    }

    protected Metadata getPropertiesMetadata() {
        return getMetadata(PROPERTIES_METADATA, getProperties());
    }

    protected Metadata getParametersMetadata() {
        return getMetadata(PARAMETERS_METADATA, getParameters());
    }

    protected List<RequiredDependency> getRequiredDependencies() {
        return getListElement(REQUIRES, new ListParser<RequiredDependency>() {
            @Override
            protected RequiredDependency parseItem(Map<String, Object> map) {
                RequiredDependencyParser parser = getRequiredDependencyParser(map);
                parser.setUsedValues(usedRequiredDependencyNames);
                return parser.parse();
            }
        });
    }

    protected Boolean getOptional() {
        return getBooleanElement(OPTIONAL);
    }

    protected RequiredDependencyParser getRequiredDependencyParser(Map<String, Object> source) {
        return new RequiredDependencyParser(source);
    }

}
