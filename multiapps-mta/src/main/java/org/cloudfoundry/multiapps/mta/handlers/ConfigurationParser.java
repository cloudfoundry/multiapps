package org.cloudfoundry.multiapps.mta.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.cloudfoundry.multiapps.common.util.JsonUtil;
import org.cloudfoundry.multiapps.mta.handlers.v2.Schemas;
import org.cloudfoundry.multiapps.mta.model.Platform;
import org.cloudfoundry.multiapps.mta.parsers.PlatformParser;
import org.cloudfoundry.multiapps.mta.schema.SchemaValidator;

public class ConfigurationParser {

    protected final SchemaValidator platformValidator;

    public ConfigurationParser() {
        this(new SchemaValidator(Schemas.PLATFORM));
    }

    protected ConfigurationParser(SchemaValidator platformsValidator) {
        this.platformValidator = platformsValidator;
    }

    public Platform parsePlatformJson(String json) {
        return parsePlatform(JsonUtil.convertJsonToMap(json));
    }

    public Platform parsePlatformJson(InputStream json) {
        // TODO: Java 9 - Remove the second variable (https://blogs.oracle.com/darcy/more-concise-try-with-resources-statements-in-jdk-9).
        try (InputStream closableJson = json) {
            return parsePlatform(JsonUtil.convertJsonToMap(closableJson));
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private Platform parsePlatform(Map<String, Object> source) {
        platformValidator.validate(source);
        return new PlatformParser(source).parse();
    }

}
