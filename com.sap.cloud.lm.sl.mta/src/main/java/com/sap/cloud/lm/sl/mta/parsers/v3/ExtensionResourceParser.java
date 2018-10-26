package com.sap.cloud.lm.sl.mta.parsers.v3;

import static com.sap.cloud.lm.sl.mta.handlers.v3.Schemas.EXT_RESOURCE;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v3.ExtensionRequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v3.ExtensionResource;
import com.sap.cloud.lm.sl.mta.model.v3.ExtensionResource.Builder;
import com.sap.cloud.lm.sl.mta.parsers.ListParser;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ExtensionResourceParser extends com.sap.cloud.lm.sl.mta.parsers.v2.ExtensionResourceParser {

    public static final String ACTIVE = "active";
    public static final String REQUIRES = "requires";
    protected final Set<String> usedRequiredDependencyNames = new HashSet<String>();

    public ExtensionResourceParser(Map<String, Object> source) {
        super(EXT_RESOURCE, source);
    }

    protected ExtensionResourceParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
    }

    @Override
    public ExtensionResource parse() throws ParsingException {
        Builder builder = new Builder();
        builder.setName(getName());
        builder.setActive(getActive());
        builder.setParameters(getParameters());
        builder.setProperties(getProperties());
        builder.setRequiredDependencies(getExtensionRequiredDependencies());
        return builder.build();
    }
    
    protected Boolean getActive() {
        return getBooleanElement(ACTIVE);
    }

    protected List<ExtensionRequiredDependency> getExtensionRequiredDependencies() {
        return getListElement(REQUIRES, new ListParser<ExtensionRequiredDependency>() {
            @Override
            protected ExtensionRequiredDependency parseItem(Map<String, Object> map) {
                ExtensionRequiredDependencyParser parser = getRequiredDependencyParser(map);
                parser.setUsedValues(usedRequiredDependencyNames);
                return parser.parse();
            }
        });
    }

    protected ExtensionRequiredDependencyParser getRequiredDependencyParser(Map<String, Object> source) {
        return new ExtensionRequiredDependencyParser(source);
    }

}
