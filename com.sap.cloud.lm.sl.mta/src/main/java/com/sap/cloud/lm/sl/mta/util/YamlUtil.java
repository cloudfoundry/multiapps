package com.sap.cloud.lm.sl.mta.util;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.CommonUtil;
import com.sap.cloud.lm.sl.mta.message.Messages;
import com.sap.cloud.lm.sl.mta.tags.YamlTaggedObjectsConstructor;

public class YamlUtil {

    private static final int EXCEPTION_MESSAGE_YAML_LENGTH_LIMIT = 255;

    @SuppressWarnings("unchecked")
    public static Map<String, Object> convertYamlToMap(String yaml) throws ParsingException {
        try {
            return (Map<String, Object>) new Yaml(new YamlTaggedObjectsConstructor()).load(yaml);
        } catch (Exception e) {
            throw new ParsingException(e, constructParsingExceptionMessage(e, yaml));
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> convertYamlToMap(InputStream yaml) throws ParsingException {
        try {
            return (Map<String, Object>) new Yaml(new YamlTaggedObjectsConstructor()).load(yaml);
        } catch (Exception e) {
            throw new ParsingException(e, Messages.ERROR_PARSING_YAML_STREAM, e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Object> convertYamlToList(String yaml) throws ParsingException {
        try {
            return (List<Object>) new Yaml(new YamlTaggedObjectsConstructor()).load(yaml);
        } catch (Exception e) {
            throw new ParsingException(e, constructParsingExceptionMessage(e, yaml));
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Object> convertYamlToList(InputStream yaml) throws ParsingException {
        try {
            return (List<Object>) new Yaml(new YamlTaggedObjectsConstructor()).load(yaml);
        } catch (Exception e) {
            throw new ParsingException(e, Messages.ERROR_PARSING_YAML_STREAM, e.getMessage());
        }
    }

    public static String convertToYaml(Object object) {
        Yaml yaml = new Yaml(new SafeConstructor(), new YamlRepresenter());
        return yaml.dumpAsMap(object);
    }

    private static String constructParsingExceptionMessage(Exception e, String yaml) {
        String trimmedYaml = CommonUtil.abbreviate(yaml, EXCEPTION_MESSAGE_YAML_LENGTH_LIMIT);
        return String.format(Messages.ERROR_PARSING_YAML_STRING, trimmedYaml, e.getMessage());
    }

}
