package com.sap.cloud.lm.sl.mta.handlers.v3;

import java.io.InputStream;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v3.Platform;
import com.sap.cloud.lm.sl.mta.parsers.v3.PlatformParser;
import com.sap.cloud.lm.sl.mta.schema.SchemaValidator;

public class ConfigurationParser extends com.sap.cloud.lm.sl.mta.handlers.v2.ConfigurationParser {

    public ConfigurationParser() {
        super(new SchemaValidator(Schemas.PLATFORM));
    }

    protected ConfigurationParser(SchemaValidator platformTypesValidator) {
        super(platformTypesValidator);
    }

    public Platform parsePlatformJson3(String json) throws ParsingException {
        return (Platform) super.parsePlatformJson2(json);
    }

    public Platform parsePlatformJson3(InputStream json) throws ParsingException {
        return (Platform) super.parsePlatformJson2(json);
    }

    @Override
    protected PlatformParser getPlatformParser(Map<String, Object> source) {
        return new com.sap.cloud.lm.sl.mta.parsers.v3.PlatformParser(source);
    }

}
