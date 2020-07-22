package org.cloudfoundry.multiapps.mta.parsers.v2;

import static org.cloudfoundry.multiapps.mta.handlers.v2.Schemas.PROVIDED_DEPENDENCY;

import java.util.List;
import java.util.Map;

import org.cloudfoundry.multiapps.common.ParsingException;
import org.cloudfoundry.multiapps.mta.model.ProvidedDependency;
import org.cloudfoundry.multiapps.mta.parsers.ModelParser;
import org.cloudfoundry.multiapps.mta.schema.MapElement;

public class ProvidedDependencyParser extends ModelParser<ProvidedDependency> {

    protected static final String PROCESSED_OBJECT_NAME = "MTA provided dependency";

    public static final String NAME = "name";
    public static final String PROPERTIES = "properties";
    public static final String PARAMETERS = "parameters";
    public static final String GROUPS = "groups";
    public static final String PUBLIC = "public";

    public ProvidedDependencyParser(Map<String, Object> source) {
        this(PROVIDED_DEPENDENCY, source);
    }

    protected ProvidedDependencyParser(MapElement schema, Map<String, Object> source) {
        super(PROCESSED_OBJECT_NAME, schema, source);
    }

    @Override
    public ProvidedDependency parse() throws ParsingException {
        return createEntity().setName(getName())
                             .setPublic(isPublic())
                             .setProperties(getProperties())
                             .setParameters(getParameters());
    }

    protected ProvidedDependency createEntity() {
        return ProvidedDependency.createV2();
    }

    protected String getName() {
        return getStringElement(NAME);
    }

    protected List<String> getGroups() {
        return getListElement(GROUPS);
    }

    protected Map<String, Object> getProperties() {
        return getMapElement(PROPERTIES);
    }

    protected Map<String, Object> getParameters() {
        return getMapElement(PARAMETERS);
    }

    protected Boolean isPublic() {
        return getBooleanElement(PUBLIC);
    }

}
