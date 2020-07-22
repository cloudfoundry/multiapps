package org.cloudfoundry.multiapps.mta.parsers.v3;

import static org.cloudfoundry.multiapps.mta.handlers.v2.Schemas.MTAEXT;

import java.util.Map;

import org.cloudfoundry.multiapps.mta.model.ExtensionDescriptor;
import org.cloudfoundry.multiapps.mta.schema.MapElement;

public class ExtensionDescriptorParser extends org.cloudfoundry.multiapps.mta.parsers.v2.ExtensionDescriptorParser {

    public ExtensionDescriptorParser(Map<String, Object> source) {
        super(MTAEXT, source);
    }

    protected ExtensionDescriptorParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
    }

    @Override
    protected ExtensionDescriptor createEntity() {
        return ExtensionDescriptor.createV3();
    }

    @Override
    protected ExtensionModuleParser getModuleParser(Map<String, Object> source) {
        return new ExtensionModuleParser(source); // v3
    }

    @Override
    protected ExtensionResourceParser getResourceParser(Map<String, Object> source) {
        return new ExtensionResourceParser(source); // v3
    }

}
