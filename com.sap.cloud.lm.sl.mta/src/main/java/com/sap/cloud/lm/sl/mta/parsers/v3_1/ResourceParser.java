package com.sap.cloud.lm.sl.mta.parsers.v3_1;

import static com.sap.cloud.lm.sl.mta.handlers.v3_1.Schemas.RESOURCE;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.Metadata;
import com.sap.cloud.lm.sl.mta.model.v3_1.RequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v3_1.Resource;
import com.sap.cloud.lm.sl.mta.model.v3_1.Resource.ResourceBuilder;
import com.sap.cloud.lm.sl.mta.parsers.ListParser;
import com.sap.cloud.lm.sl.mta.schema.MapElement;
import com.sap.cloud.lm.sl.mta.util.MetadataUpdater;

public class ResourceParser extends com.sap.cloud.lm.sl.mta.parsers.v3_0.ResourceParser {

    public static final String PROPERTIES_METADATA = "properties-metadata";
    public static final String PARAMETERS_METADATA = "parameters-metadata";
    public static final String REQUIRES = "requires";
    public static final String OPTIONAL = "optional";

    protected final Set<String> usedRequiredDependencyNames = new HashSet<String>();

    public ResourceParser(Map<String, Object> source) {
        super(RESOURCE, source);
    }

    protected ResourceParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
    }

    @Override
    public Resource parse() throws ParsingException {
        ResourceBuilder builder = new ResourceBuilder();
        builder.setName(getName());
        builder.setDescription(getDescription());
        builder.setType(getType());
        builder.setProperties(getProperties());
        builder.setParameters(getParameters());
        builder.setParametersMetadata(getParametersMetadata());
        builder.setPropertiesMetadata(getPropertiesMetadata());
        builder.setRequiredDependencies(getRequiredDependencies());
        builder.setOptional(getOptional());
        return builder.build();
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
            protected RequiredDependency parseItem(Map<String, Object> map) throws ParsingException {
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
