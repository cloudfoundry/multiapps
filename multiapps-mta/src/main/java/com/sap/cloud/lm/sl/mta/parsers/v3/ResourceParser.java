package com.sap.cloud.lm.sl.mta.parsers.v3;

import static com.sap.cloud.lm.sl.mta.handlers.v3.Schemas.RESOURCE;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.Metadata;
import com.sap.cloud.lm.sl.mta.model.v3.RequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v3.Resource;
import com.sap.cloud.lm.sl.mta.model.v3.Resource.Builder;
import com.sap.cloud.lm.sl.mta.parsers.ListParser;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ResourceParser extends com.sap.cloud.lm.sl.mta.parsers.v2.ResourceParser {

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
        Builder builder = new Builder();
        builder.setName(getName());
        builder.setDescription(getDescription());
        builder.setType(getType());
        builder.setActive(getActive());
        builder.setProperties(getProperties());
        builder.setParameters(getParameters());
        builder.setPropertiesMetadata(getPropertiesMetadata());
        builder.setParametersMetadata(getParametersMetadata());
        builder.setRequiredDependencies3(getRequiredDependencies());
        builder.setOptional(getOptional());
        return builder.build();
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
