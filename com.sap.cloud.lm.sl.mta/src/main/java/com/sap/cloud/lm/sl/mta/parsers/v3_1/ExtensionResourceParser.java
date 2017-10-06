
package com.sap.cloud.lm.sl.mta.parsers.v3_1;

import static com.sap.cloud.lm.sl.mta.handlers.v3_1.Schemas.EXT_RESOURCE;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v3_1.ExtensionRequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v3_1.ExtensionResource;
import com.sap.cloud.lm.sl.mta.model.v3_1.ExtensionResource.ExtensionResourceBuilder;
import com.sap.cloud.lm.sl.mta.parsers.ListParser;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ExtensionResourceParser extends com.sap.cloud.lm.sl.mta.parsers.v3_0.ExtensionResourceParser {

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
        ExtensionResourceBuilder builder = new ExtensionResourceBuilder();
        builder.setName(getName());
        builder.setParameters(getParameters());
        builder.setProperties(getProperties());
        builder.setRequiredDependencies(geExtensionRequiredDependencies());
        return builder.build();
    }

    protected List<ExtensionRequiredDependency> geExtensionRequiredDependencies() {
        return getListElement(REQUIRES, new ListParser<ExtensionRequiredDependency>() {
            @Override
            protected ExtensionRequiredDependency parseItem(Map<String, Object> map) throws ParsingException {
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
