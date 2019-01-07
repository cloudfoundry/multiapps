package com.sap.cloud.lm.sl.mta.handlers.v2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.JsonUtil;
import com.sap.cloud.lm.sl.mta.model.v2.Platform;
import com.sap.cloud.lm.sl.mta.parsers.v2.PlatformParser;
import com.sap.cloud.lm.sl.mta.schema.SchemaValidator;

public class ConfigurationParser {

    protected final SchemaValidator platformValidator;

    public ConfigurationParser() {
        this(new SchemaValidator(Schemas.PLATFORM));
    }

    protected ConfigurationParser(SchemaValidator platformValidator) {
        this.platformValidator = platformValidator;
    }

    public Platform parsePlatformJson2(String json) throws ParsingException {
        return parsePlatform(JsonUtil.convertJsonToMap(json));
    }

    public Platform parsePlatformJson2(InputStream json) throws ParsingException {
        return parsePlatform(JsonUtil.convertJsonToMap(json));
    }

    public Platform parsePlatformJson(InputStream json) throws ParsingException {
        // TODO: Java 9 - Remove the second variable (https://blogs.oracle.com/darcy/more-concise-try-with-resources-statements-in-jdk-9).
        try (InputStream closableJson = json) {
            return parsePlatform(JsonUtil.convertJsonToMap(closableJson));
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private Platform parsePlatform(Map<String, Object> source) {
        platformValidator.validate(source);
        return getPlatformParser(source).parse();
    }

    protected PlatformParser getPlatformParser(Map<String, Object> source) {
        return new PlatformParser(source);
    }
}
