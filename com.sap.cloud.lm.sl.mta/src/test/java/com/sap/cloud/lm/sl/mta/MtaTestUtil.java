package com.sap.cloud.lm.sl.mta;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.handlers.ConfigurationParser;
import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorParser;
import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.Platform;

public class MtaTestUtil {

    public static DeploymentDescriptor loadDeploymentDescriptor(String location, DescriptorParser parser, Class<?> clazz)
        throws ParsingException {
        InputStream deploymentDescriptorYaml = clazz.getResourceAsStream(location);
        return parser.parseDeploymentDescriptorYaml(deploymentDescriptorYaml);
    }

    public static List<ExtensionDescriptor> loadExtensionDescriptors(String[] locations, DescriptorParser parser, Class<?> clazz)
        throws ParsingException {
        return Arrays.stream(locations)
                     .map(extensionDescriptorLocation -> loadExtensionDescriptor(extensionDescriptorLocation, parser, clazz))
                     .collect(Collectors.toList());
    }

    public static ExtensionDescriptor loadExtensionDescriptor(String location, DescriptorParser descriptorParser, Class<?> clazz)
        throws ParsingException {
        InputStream extensionDescriptorYaml = clazz.getResourceAsStream(location);
        return descriptorParser.parseExtensionDescriptorYaml(extensionDescriptorYaml);
    }

    public static Platform loadPlatform(String location, Class<?> clazz) throws ParsingException {
        InputStream platformJson = clazz.getResourceAsStream(location);
        return new ConfigurationParser().parsePlatformJson(platformJson);
    }

}
