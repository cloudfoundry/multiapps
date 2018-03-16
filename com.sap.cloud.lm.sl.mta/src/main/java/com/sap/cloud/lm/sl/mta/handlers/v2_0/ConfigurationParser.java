package com.sap.cloud.lm.sl.mta.handlers.v2_0;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.v2_0.Platform;
import com.sap.cloud.lm.sl.mta.model.v2_0.Target;
import com.sap.cloud.lm.sl.mta.parsers.v2_0.PlatformParser;
import com.sap.cloud.lm.sl.mta.parsers.v2_0.TargetParser;
import com.sap.cloud.lm.sl.mta.schema.SchemaValidator;

public class ConfigurationParser extends com.sap.cloud.lm.sl.mta.handlers.v1_0.ConfigurationParser {

    public ConfigurationParser() {
        super(new SchemaValidator(Schemas.PLATFORM_TYPES), new SchemaValidator(Schemas.PLATFORMS), new SchemaValidator(Schemas.PLATFORM));
    }

    protected ConfigurationParser(SchemaValidator platformTypesValidator, SchemaValidator platformsValidator,
        SchemaValidator platformValidator) {
        super(platformTypesValidator, platformsValidator, platformValidator);
    }

    public List<Platform> parsePlatformTypesJson2_0(String json) throws ParsingException {
        return ListUtil.cast(super.parsePlatformsJson(json));
    }

    public List<Platform> parsePlatformsJson2_0(InputStream json) throws ParsingException {
        return ListUtil.cast(super.parsePlatformsJson(json));
    }

    @Override
    protected PlatformParser getPlatformParser(Map<String, Object> source) {
        return new com.sap.cloud.lm.sl.mta.parsers.v2_0.PlatformParser(source);
    }

    public List<Target> parsePlatformsJson2_0(String json) throws ParsingException {
        return ListUtil.cast(super.parseTargetsJson(json));
    }

    public List<Target> parseTargetsJson2_0(InputStream json) throws ParsingException {
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
        return new com.sap.cloud.lm.sl.mta.parsers.v2_0.TargetParser(source);
    }

}
