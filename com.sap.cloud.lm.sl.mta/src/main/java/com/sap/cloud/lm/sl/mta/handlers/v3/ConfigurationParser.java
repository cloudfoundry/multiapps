package com.sap.cloud.lm.sl.mta.handlers.v3;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.v3.Platform;
import com.sap.cloud.lm.sl.mta.model.v3.Target;
import com.sap.cloud.lm.sl.mta.parsers.v3.PlatformParser;
import com.sap.cloud.lm.sl.mta.parsers.v3.TargetParser;
import com.sap.cloud.lm.sl.mta.schema.SchemaValidator;

public class ConfigurationParser extends com.sap.cloud.lm.sl.mta.handlers.v2.ConfigurationParser {

    public ConfigurationParser() {
        super(new SchemaValidator(Schemas.PLATFORM_TYPES), new SchemaValidator(Schemas.PLATFORMS), new SchemaValidator(Schemas.PLATFORM));
    }

    protected ConfigurationParser(SchemaValidator platformTypesValidator, SchemaValidator platformsValidator,
        SchemaValidator platformValidator) {
        super(platformTypesValidator, platformsValidator, platformValidator);
    }

    public List<Platform> parsePlatformsJson3(String json) throws ParsingException {
        return ListUtil.cast(super.parsePlatformsJson(json));
    }

    public List<Platform> parsePlatformsJson3(InputStream json) throws ParsingException {
        return ListUtil.cast(super.parsePlatformsJson(json));
    }

    @Override
    protected PlatformParser getPlatformParser(Map<String, Object> source) {
        return new com.sap.cloud.lm.sl.mta.parsers.v3.PlatformParser(source);
    }

    public List<Target> parseTargetsJson3(String json) throws ParsingException {
        return ListUtil.cast(super.parseTargetsJson(json));
    }

    public List<Target> parseTargetsJson3(InputStream json) throws ParsingException {
        return ListUtil.cast(super.parseTargetsJson(json));
    }

    @Override
    public Target parseTargetJson(InputStream json) throws ParsingException {
        return (Target) super.parseTargetJson(json);
    }

    @Override
    public Target parseTargetJson(String json) throws ParsingException {
        return (Target) super.parseTargetJson(json);
    }

    @Override
    protected TargetParser getDeployTargetParser(Map<String, Object> source) {
        return new com.sap.cloud.lm.sl.mta.parsers.v3.TargetParser(source);
    }

}
