package com.sap.cloud.lm.sl.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.sap.cloud.lm.sl.common.ParsingException;

public class TestUtil {

    public static InputStream getResourceAsInputStream(String name, Class<?> resourceClass) {
        return resourceClass.getResourceAsStream(name);
    }

    public static String getResourceAsString(String name, Class<?> resourceClass) {
        try {
            String resource = IOUtils.toString(getResourceAsInputStream(name, resourceClass), StandardCharsets.UTF_8);
            return resource.replace("\r", "");
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static Map<String, Object> getMap(String file, Class<?> clazz) throws ParsingException {
        if (isYAMLFile(file)) {
            return YamlUtil.convertYamlToMap(clazz.getResourceAsStream(file));
        }
        if (isJSONFile(file)) {
            return JsonUtil.convertJsonToMap(clazz.getResourceAsStream(file));
        }
        return null;
    }

    public static List<Object> getList(String file, Class<?> clazz) throws ParsingException {
        if (isYAMLFile(file)) {
            return YamlUtil.convertYamlToList(clazz.getResourceAsStream(file));
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
