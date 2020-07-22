package org.cloudfoundry.multiapps.mta.parsers.v3;

import static org.cloudfoundry.multiapps.mta.handlers.v2.Schemas.EXT_PROVIDED_DEPENDENCY;

import java.util.Map;

import org.cloudfoundry.multiapps.mta.model.ExtensionProvidedDependency;
import org.cloudfoundry.multiapps.mta.schema.MapElement;

public class ExtensionProvidedDependencyParser extends org.cloudfoundry.multiapps.mta.parsers.v2.ExtensionProvidedDependencyParser {

    public ExtensionProvidedDependencyParser(Map<String, Object> source) {
        super(EXT_PROVIDED_DEPENDENCY, source);
    }

    protected ExtensionProvidedDependencyParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
    }

    @Override
    protected ExtensionProvidedDependency createEntity() {
        return ExtensionProvidedDependency.createV3();
    }

}
