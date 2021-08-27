package org.cloudfoundry.multiapps.mta.parsers.v3;

import static org.cloudfoundry.multiapps.mta.handlers.v3.Schemas.EXT_RESOURCE;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cloudfoundry.multiapps.common.ParsingException;
import org.cloudfoundry.multiapps.mta.model.ExtensionRequiredDependency;
import org.cloudfoundry.multiapps.mta.model.ExtensionResource;
import org.cloudfoundry.multiapps.mta.parsers.ListParser;
import org.cloudfoundry.multiapps.mta.parsers.v2.ExtensionRequiredDependencyParser;
import org.cloudfoundry.multiapps.mta.schema.MapElement;

public class ExtensionResourceParser extends org.cloudfoundry.multiapps.mta.parsers.v2.ExtensionResourceParser {

    public static final String ACTIVE = "active";
    public static final String OPTIONAL = "optional";
    public static final String REQUIRES = "requires";
    protected final Set<String> usedRequiredDependencyNames = new HashSet<>();

    public ExtensionResourceParser(Map<String, Object> source) {
        super(EXT_RESOURCE, source);
    }

    protected ExtensionResourceParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
    }

    @Override
    public ExtensionResource parse() throws ParsingException {
        return super.parse().setActive(getActive())
                            .setOptional(getOptional())
                            .setRequiredDependencies(getExtensionRequiredDependencies());
    }

    @Override
    protected ExtensionResource createEntity() {
        return ExtensionResource.createV3();
    }

    protected Boolean getActive() {
        return getBooleanElement(ACTIVE);
    }

    protected Boolean getOptional() {
        return getBooleanElement(OPTIONAL);
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
