package com.sap.cloud.lm.sl.mta.parsers.v1_0;

import static com.sap.cloud.lm.sl.mta.handlers.v1_0.Schemas.PTF_MODULE_TYPE;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v1_0.PlatformModuleType;
import com.sap.cloud.lm.sl.mta.model.v1_0.PlatformModuleType.PlatformModuleTypeBuilder;
import com.sap.cloud.lm.sl.mta.parsers.ModelParser;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class PlatformModuleTypeParser extends ModelParser<PlatformModuleType> {

    protected static final String PROCESSED_OBJECT_NAME = "MTA platform module type";

    public static final String NAME = "name";
    public static final String PROPERTIES = "properties";

    public PlatformModuleTypeParser(Map<String, Object> source) {
        this(PTF_MODULE_TYPE, source);
    }

    protected PlatformModuleTypeParser(MapElement schema, Map<String, Object> source) {
        super(PROCESSED_OBJECT_NAME, schema, source);
    }

    @Override
    public PlatformModuleType parse() throws ParsingException {
        PlatformModuleTypeBuilder builder = new PlatformModuleTypeBuilder();
        builder.setName(getName());
        builder.setProperties(getProperties());
        return builder.build();
    }

    protected String getName() throws ParsingException {
        return getStringElement(NAME);
    }

    protected Map<String, Object> getProperties() {
        return getMapElement(PROPERTIES);
    }

}
