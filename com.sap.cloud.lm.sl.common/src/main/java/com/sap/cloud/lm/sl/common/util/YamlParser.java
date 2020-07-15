package com.sap.cloud.lm.sl.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import com.sap.cloud.lm.sl.common.Messages;
import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.tags.YamlTaggedObjectsConstructor;
import com.sap.cloud.lm.sl.common.util.yaml.YamlRepresenter;
import org.yaml.snakeyaml.representer.Representer;

public class YamlParser {

    private static final int EXCEPTION_MESSAGE_YAML_LENGTH_LIMIT = 255;
    private final LoaderOptions loaderOptions;

    public YamlParser() {
        this(new LoaderOptions());
    }

    public YamlParser(LoaderOptions loaderOptions) {
        this.loaderOptions = loaderOptions;
    }

    public Map<String, Object> convertYamlToMap(InputStream yamlStream) throws ParsingException {
        String yaml = toString(yamlStream);
        return convertYamlToMap(yaml);
    }

    public Map<String, Object> convertYamlToMap(String yaml) throws ParsingException {
        try {
            return getYamlWithTaggedObjectsConstructor().load(yaml);
        } catch (Exception e) {
            throw new ParsingException(e, constructParsingExceptionMessage(e, yaml));
        }
    }

    public List<Object> convertYamlToList(InputStream yamlStream) throws ParsingException {
        String yaml = toString(yamlStream);
        return convertYamlToList(yaml);
    }

    public List<Object> convertYamlToList(String yaml) throws ParsingException {
        try {
            return getYamlWithTaggedObjectsConstructor().load(yaml);
        } catch (Exception e) {
            throw new ParsingException(e, constructParsingExceptionMessage(e, yaml));
        }
    }

    public String convertToYaml(Object object) {
        return getYamlWithSafeConstructor().dumpAsMap(object);
    }

    private String constructParsingExceptionMessage(Exception e, String yaml) {
        String trimmedYaml = StringUtils.abbreviate(yaml, EXCEPTION_MESSAGE_YAML_LENGTH_LIMIT);
        return String.format(Messages.ERROR_PARSING_YAML_STRING, trimmedYaml, e.getMessage());
    }

    private String toString(InputStream yamlStream) {
        try {
            return IOUtils.toString(yamlStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ParsingException(e, Messages.ERROR_PARSING_YAML_STREAM, e.getMessage());
        }
    }

    private Yaml getYamlWithTaggedObjectsConstructor() {
        Representer representer = new Representer();
        return new Yaml(new YamlTaggedObjectsConstructor(), representer, createDumperOptions(representer), loaderOptions);
    }

    private Yaml getYamlWithSafeConstructor() {
        YamlRepresenter yamlRepresenter = new YamlRepresenter();
        return new Yaml(new SafeConstructor(), yamlRepresenter, createDumperOptions(yamlRepresenter), loaderOptions);
    }

    private DumperOptions createDumperOptions(Representer representer) {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(representer.getDefaultFlowStyle());
        dumperOptions.setDefaultScalarStyle(representer.getDefaultScalarStyle());
        dumperOptions.setAllowReadOnlyProperties(representer.getPropertyUtils().isAllowReadOnlyProperties());
        dumperOptions.setTimeZone(representer.getTimeZone());
        return dumperOptions;
    }

}
