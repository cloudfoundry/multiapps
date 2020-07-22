package org.cloudfoundry.multiapps.mta.parsers.v2;

import static org.cloudfoundry.multiapps.mta.handlers.v2.Schemas.EXT_MODULE;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cloudfoundry.multiapps.common.ParsingException;
import org.cloudfoundry.multiapps.mta.model.ExtensionModule;
import org.cloudfoundry.multiapps.mta.model.ExtensionProvidedDependency;
import org.cloudfoundry.multiapps.mta.model.ExtensionRequiredDependency;
import org.cloudfoundry.multiapps.mta.parsers.ListParser;
import org.cloudfoundry.multiapps.mta.parsers.ModelParser;
import org.cloudfoundry.multiapps.mta.schema.MapElement;

public class ExtensionModuleParser extends ModelParser<ExtensionModule> {

    protected static final String PROCESSED_OBJECT_NAME = "MTA extension module";

    public static final String NAME = "name";
    public static final String PROPERTIES = "properties";
    public static final String PARAMETERS = "parameters";
    public static final String PROVIDES = "provides";
    public static final String REQUIRES = "requires";

    protected Set<String> usedProvidedDependencyNames = Collections.emptySet();
    protected Set<String> usedRequiredDependencyNames = new HashSet<>();

    public ExtensionModuleParser(Map<String, Object> source) {
        this(EXT_MODULE, source);
    }

    protected ExtensionModuleParser(MapElement schema, Map<String, Object> source) {
        super(PROCESSED_OBJECT_NAME, schema, source);
    }

    @Override
    public ExtensionModule parse() throws ParsingException {
        return createEntity().setName(getName())
                             .setProperties(getProperties())
                             .setParameters(getParameters())
                             .setRequiredDependencies(getExtensionRequiredDependencies())
                             .setProvidedDependencies(getExtensionProvidedDependencies());
    }

    protected ExtensionModule createEntity() {
        return ExtensionModule.createV2();
    }

    protected String getName() throws ParsingException {
        return getStringElement(NAME);
    }

    protected Map<String, Object> getProperties() {
        return getMapElement(PROPERTIES);
    }

    protected Map<String, Object> getParameters() {
        return getMapElement(PARAMETERS);
    }

    protected List<ExtensionRequiredDependency> getExtensionRequiredDependencies() {
        return getListElement(REQUIRES, new ListParser<ExtensionRequiredDependency>() {
            @Override
            protected ExtensionRequiredDependency parseItem(Map<String, Object> map) {
                return getRequiredDependencyParser(map).setUsedValues(usedRequiredDependencyNames)
                                                       .parse();
            }
        });
    }

    public ExtensionModuleParser setUsedProvidedDependencyNames(Set<String> usedProvidedDependencyNames) {
        this.usedProvidedDependencyNames = usedProvidedDependencyNames;
        return this;
    }

    protected List<ExtensionProvidedDependency> getExtensionProvidedDependencies() {
        return getListElement(PROVIDES, new ListParser<ExtensionProvidedDependency>() {
            @Override
            protected ExtensionProvidedDependency parseItem(Map<String, Object> map) {
                return getProvidedDependencyParser(map).setUsedValues(usedProvidedDependencyNames)
                                                       .parse();
            }
        });
    }

    protected ExtensionRequiredDependencyParser getRequiredDependencyParser(Map<String, Object> source) {
        return new org.cloudfoundry.multiapps.mta.parsers.v2.ExtensionRequiredDependencyParser(source);
    }

    protected ExtensionProvidedDependencyParser getProvidedDependencyParser(Map<String, Object> source) {
        return new org.cloudfoundry.multiapps.mta.parsers.v2.ExtensionProvidedDependencyParser(source);
    }

}
