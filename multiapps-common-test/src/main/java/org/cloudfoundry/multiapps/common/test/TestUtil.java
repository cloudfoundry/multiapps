package org.cloudfoundry.multiapps.common.test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.cloudfoundry.multiapps.common.ParsingException;
import org.cloudfoundry.multiapps.common.util.JsonUtil;
import org.cloudfoundry.multiapps.common.util.YamlParser;

public class TestUtil {

    public static InputStream getResourceAsInputStream(String resource, Class<?> resourceClass) {
        return resourceClass.getResourceAsStream(resource);
    }

    public static String getResourceAsStringWithoutCarriageReturns(String resource, Class<?> resourceClass) {
        String resourceString = getResourceAsString(resource, resourceClass);
        return removeCarriageReturns(resourceString);
    }

    public static String getResourceAsString(String resource, Class<?> resourceClass) {
        try (InputStream resourceStream = getResourceAsInputStream(resource, resourceClass)) {
            return IOUtils.toString(resourceStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static String removeCarriageReturns(String string) {
        return string.replaceAll("\\r|\\\\r", "");
    }

    public static Map<String, Object> getMap(String file, Class<?> clazz) throws ParsingException {
        if (isYAMLFile(file)) {
            YamlParser yamlParser = new YamlParser();
            return yamlParser.convertYamlToMap(clazz.getResourceAsStream(file));
        }
        if (isJSONFile(file)) {
            return JsonUtil.convertJsonToMap(clazz.getResourceAsStream(file));
        }
        return null;
    }

    public static List<Object> getList(String file, Class<?> clazz) throws ParsingException {
        if (isYAMLFile(file)) {
            YamlParser yamlParser = new YamlParser();
            return yamlParser.convertYamlToList(clazz.getResourceAsStream(file));
        }
        if (isJSONFile(file)) {
            return JsonUtil.convertJsonToList(clazz.getResourceAsStream(file));
        }
        return null;
    }

    public static boolean isYAMLFile(String filename) {
        return filename != null && (filename.endsWith(".yaml") || filename.endsWith(".mtaext"));
    }

    public static boolean isJSONFile(String filename) {
        return filename != null && filename.endsWith(".json");
    }

}
