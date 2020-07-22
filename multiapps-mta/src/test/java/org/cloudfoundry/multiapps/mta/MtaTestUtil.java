package org.cloudfoundry.multiapps.mta;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.cloudfoundry.multiapps.common.ParsingException;
import org.cloudfoundry.multiapps.common.util.YamlParser;
import org.cloudfoundry.multiapps.mta.handlers.ConfigurationParser;
import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorParser;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.ExtensionDescriptor;
import org.cloudfoundry.multiapps.mta.model.Platform;

public class MtaTestUtil {

    public static DeploymentDescriptor loadDeploymentDescriptor(String location, DescriptorParser parser, Class<?> clazz)
        throws ParsingException {
        InputStream deploymentDescriptorYaml = clazz.getResourceAsStream(location);
        Map<String, Object> deploymentDescriptor = new YamlParser().convertYamlToMap(deploymentDescriptorYaml);
        return parser.parseDeploymentDescriptor(deploymentDescriptor);
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
        Map<String, Object> extensionDescriptor = new YamlParser().convertYamlToMap(extensionDescriptorYaml);
        return descriptorParser.parseExtensionDescriptor(extensionDescriptor);
    }

    public static Platform loadPlatform(String location, Class<?> clazz) throws ParsingException {
        InputStream platformJson = clazz.getResourceAsStream(location);
        return new ConfigurationParser().parsePlatformJson(platformJson);
    }

}
