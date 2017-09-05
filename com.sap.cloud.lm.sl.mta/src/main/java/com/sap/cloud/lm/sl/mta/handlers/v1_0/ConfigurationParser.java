package com.sap.cloud.lm.sl.mta.handlers.v1_0;

import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.JsonUtil;
import com.sap.cloud.lm.sl.mta.model.v1_0.Target;
import com.sap.cloud.lm.sl.mta.model.v1_0.Platform;
import com.sap.cloud.lm.sl.mta.parsers.ListParser;
import com.sap.cloud.lm.sl.mta.parsers.v1_0.TargetParser;
import com.sap.cloud.lm.sl.mta.parsers.v1_0.PlatformParser;
import com.sap.cloud.lm.sl.mta.schema.SchemaValidator;

public class ConfigurationParser {

    protected final SchemaValidator platformsValidator;
    protected final SchemaValidator platformTypesValidator;
    protected final SchemaValidator platformValidator;

    protected final Set<String> usedPlatformNames = new HashSet<String>();
    protected final Set<String> usedPlatformTypeNames = new HashSet<String>();

    public ConfigurationParser() {
        this(new SchemaValidator(Schemas.PLATFORM_TYPES), new SchemaValidator(Schemas.PLATFORMS), new SchemaValidator(Schemas.PLATFORM));
    }

    protected ConfigurationParser(SchemaValidator platformTypesValidator, SchemaValidator platformsValidator,
        SchemaValidator platformValidator) {
        this.platformTypesValidator = platformTypesValidator;
        this.platformsValidator = platformsValidator;
        this.platformValidator = platformValidator;
    }

    public List<Platform> parsePlatformsJson(String json) throws ParsingException {
        return getPlatforms(JsonUtil.convertJsonToList(json));
    }

    protected List<Platform> getPlatforms(List<Object> list) throws ParsingException {
        platformTypesValidator.validate(list);
        return new ListParser<Platform>() {
            @Override
            protected Platform parseItem(Map<String, Object> map) throws ParsingException {
                return getPlatformParser(map).setUsedValues(usedPlatformTypeNames).parse();
            }
        }.setSource(list).parse();
    }

    protected PlatformParser getPlatformParser(Map<String, Object> source) {
        return new PlatformParser(source);
    }

    public List<Platform> parsePlatformsJson(InputStream json) throws ParsingException {
        try {
            return getPlatforms(JsonUtil.convertJsonToList(json));
        } finally {
            IOUtils.closeQuietly(json);
        }
    }

    public List<Target> parseTargetsJson(String json) throws ParsingException {
        return getDeployTargets(JsonUtil.convertJsonToList(json));
    }

    public Target parseTargetJson(String json) throws ParsingException {
        Map<String, Object> map = JsonUtil.convertJsonToMap(json);
        platformValidator.validate(map);
        return getDeployTargetParser(map).parse();
    }

    public Target parseTargetJson(InputStream json) throws ParsingException {
        Map<String, Object> map = JsonUtil.convertJsonToMap(json);
        platformValidator.validate(map);
        return getDeployTargetParser(map).parse();
    }

    protected List<Target> getDeployTargets(List<Object> list) throws ParsingException {
        platformsValidator.validate(list);
        return new ListParser<Target>() {
            @Override
            protected Target parseItem(Map<String, Object> map) throws ParsingException {
                return getDeployTargetParser(map).setUsedValues(usedPlatformNames).parse();
            }
        }.setSource(list).parse();
    }

    protected TargetParser getDeployTargetParser(Map<String, Object> source) {
        return new TargetParser(source);
    }

    public List<Target> parseTargetsJson(InputStream json) throws ParsingException {
        try {
            return getDeployTargets(JsonUtil.convertJsonToList(json));
        } finally {
            IOUtils.closeQuietly(json);
        }
    }

}
