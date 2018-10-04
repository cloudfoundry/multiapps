package com.sap.cloud.lm.sl.mta;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.JsonUtil;
import com.sap.cloud.lm.sl.mta.handlers.v1.ConfigurationParser;
import com.sap.cloud.lm.sl.mta.handlers.v1.DescriptorParser;
import com.sap.cloud.lm.sl.mta.model.v1.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1.Platform;
import com.sap.cloud.lm.sl.mta.model.v1.Target;
import com.sap.cloud.lm.sl.mta.util.YamlUtil;

public class MtaTestUtil {

    public static Map<String, Object> getMap(String file, Class<?> clazz) throws ParsingException {
        Map<String, Object> map = null;
        if (file == null) {
            map = null;
        } else if (file.endsWith(".yaml") || file.endsWith(".mtaext")) {
            map = YamlUtil.convertYamlToMap(clazz.getResourceAsStream(file));
        } else if (file.endsWith(".json")) {
            map = JsonUtil.convertJsonToMap(clazz.getResourceAsStream(file));
        }
        return map;
    }

    public static List<Object> getList(String file, Class<?> clazz) throws ParsingException {
        List<Object> list = null;
        if (file == null) {
            list = null;
        } else if (file.endsWith(".yaml") || file.endsWith(".mtaext")) {
            list = YamlUtil.convertYamlToList(clazz.getResourceAsStream(file));
        } else if (file.endsWith(".json")) {
            list = JsonUtil.convertJsonToList(clazz.getResourceAsStream(file));
        }
        return list;
    }

    public static DeploymentDescriptor loadDeploymentDescriptor(String location, DescriptorParser parser, Class<?> clazz)
        throws ParsingException {
        InputStream deploymentDescriptorYaml = clazz.getResourceAsStream(location);
        return parser.parseDeploymentDescriptorYaml(deploymentDescriptorYaml);
    }

    public static List<ExtensionDescriptor> loadExtensionDescriptors(String[] locations, DescriptorParser parser, Class<?> clazz)
        throws ParsingException {
        List<ExtensionDescriptor> extensionDescriptors = new ArrayList<ExtensionDescriptor>();
        for (String extensionDescriptorLocation : locations) {
            extensionDescriptors.add(loadExtensionDescriptor(extensionDescriptorLocation, parser, clazz));
        }
        return extensionDescriptors;
    }

    public static ExtensionDescriptor loadExtensionDescriptor(String location, DescriptorParser descriptorParser, Class<?> clazz)
        throws ParsingException {
        InputStream extensionDescriptorYaml = clazz.getResourceAsStream(location);
        return descriptorParser.parseExtensionDescriptorYaml(extensionDescriptorYaml);
    }

    public static List<Platform> loadPlatforms(String location, ConfigurationParser parser, Class<?> clazz) throws ParsingException {
        InputStream platformTypesJson = clazz.getResourceAsStream(location);
        return parser.parsePlatformsJson(platformTypesJson);
    }

    public static List<Target> loadTargets(String location, ConfigurationParser parser, Class<?> clazz) throws ParsingException {
        InputStream platformsJson = clazz.getResourceAsStream(location);
        return parser.parseTargetsJson(platformsJson);
    }

}
