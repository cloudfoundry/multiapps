package org.cloudfoundry.multiapps.mta.parsers.v3;

import static org.cloudfoundry.multiapps.mta.handlers.v2.Schemas.EXT_REQUIRED_DEPENDENCY;

import java.util.Map;

import org.cloudfoundry.multiapps.mta.model.ExtensionRequiredDependency;
import org.cloudfoundry.multiapps.mta.schema.MapElement;

public class ExtensionRequiredDependencyParser extends org.cloudfoundry.multiapps.mta.parsers.v2.ExtensionRequiredDependencyParser {

    public ExtensionRequiredDependencyParser(Map<String, Object> source) {
        super(EXT_REQUIRED_DEPENDENCY, source);
    }

    protected ExtensionRequiredDependencyParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
    }

    @Override
    protected ExtensionRequiredDependency createEntity() {
        return ExtensionRequiredDependency.createV3();
    }

}
