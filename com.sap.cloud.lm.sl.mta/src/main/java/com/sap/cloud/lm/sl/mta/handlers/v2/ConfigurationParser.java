package com.sap.cloud.lm.sl.mta.handlers.v2;

import java.io.InputStream;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v2.Platform;
import com.sap.cloud.lm.sl.mta.parsers.v2.PlatformParser;
import com.sap.cloud.lm.sl.mta.schema.SchemaValidator;

public class ConfigurationParser extends com.sap.cloud.lm.sl.mta.handlers.v1.ConfigurationParser {

    public ConfigurationParser() {
        super(new SchemaValidator(Schemas.PLATFORM));
    }

    protected ConfigurationParser(SchemaValidator platformsValidator) {
        super(platformsValidator);
    }

    public Platform parsePlatformJson2(String json) throws ParsingException {
        return (Platform) super.parsePlatformJson(json);
    }

    public Platform parsePlatformJson2(InputStream json) throws ParsingException {
        return (Platform) super.parsePlatformJson(json);
    }

    @Override
    protected PlatformParser getPlatformParser(Map<String, Object> source) {
        return new com.sap.cloud.lm.sl.mta.parsers.v2.PlatformParser(source);
    }

}
