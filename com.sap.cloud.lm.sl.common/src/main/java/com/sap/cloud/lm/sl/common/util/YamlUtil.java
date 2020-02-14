package com.sap.cloud.lm.sl.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import com.sap.cloud.lm.sl.common.Messages;
import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.tags.YamlTaggedObjectsConstructor;
import com.sap.cloud.lm.sl.common.util.yaml.YamlRepresenter;

public class YamlUtil {

    private static final int EXCEPTION_MESSAGE_YAML_LENGTH_LIMIT = 255;

    private YamlUtil() {
    }

    public static Map<String, Object> convertYamlToMap(InputStream yamlStream) throws ParsingException {
        String yaml = toString(yamlStream);
        return convertYamlToMap(yaml);
    }

    public static Map<String, Object> convertYamlToMap(String yaml) throws ParsingException {
        try {
            return new Yaml(new YamlTaggedObjectsConstructor()).load(yaml);
        } catch (Exception e) {
            throw new ParsingException(e, constructParsingExceptionMessage(e, yaml));
        }
    }

    public static List<Object> convertYamlToList(InputStream yamlStream) throws ParsingException {
        String yaml = toString(yamlStream);
        return convertYamlToList(yaml);
    }

    public static List<Object> convertYamlToList(String yaml) throws ParsingException {
        try {
            return new Yaml(new YamlTaggedObjectsConstructor()).load(yaml);
        } catch (Exception e) {
            throw new ParsingException(e, constructParsingExceptionMessage(e, yaml));
        }
    }

    public static String convertToYaml(Object object) {
        Yaml yaml = new Yaml(new SafeConstructor(), new YamlRepresenter());
        return yaml.dumpAsMap(object);
    }

    private static String constructParsingExceptionMessage(Exception e, String yaml) {
        String trimmedYaml = StringUtils.abbreviate(yaml, EXCEPTION_MESSAGE_YAML_LENGTH_LIMIT);
        return String.format(Messages.ERROR_PARSING_YAML_STRING, trimmedYaml, e.getMessage());
    }

    private static String toString(InputStream yamlStream) {
        try {
            return IOUtils.toString(yamlStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ParsingException(e, Messages.ERROR_PARSING_YAML_STREAM, e.getMessage());
        }
    }

}
