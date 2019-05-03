package com.sap.cloud.lm.sl.mta.parsers.v3;

import static com.sap.cloud.lm.sl.mta.handlers.v3.Schemas.MTAEXT;

import java.util.Map;

import com.sap.cloud.lm.sl.mta.model.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ExtensionDescriptorParser extends com.sap.cloud.lm.sl.mta.parsers.v2.ExtensionDescriptorParser {

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
