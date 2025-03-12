package org.cloudfoundry.multiapps.mta.handlers.common;

import java.io.InputStream;
import java.util.Map;

import org.cloudfoundry.multiapps.common.test.Tester;
import org.cloudfoundry.multiapps.common.util.YamlParser;
import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorParser;

public abstract class AbstractDescriptorParserTest {

    protected final DescriptorParser parser = createDescriptorParser();

    protected abstract DescriptorParser createDescriptorParser();

    public void executeTestParseDeploymentDescriptorYaml(Tester tester, String deploymentDescriptorLocation,
                                                         Tester.Expectation expectation) {
        InputStream deploymentDescriptorYaml = getClass().getResourceAsStream(deploymentDescriptorLocation);
        tester.test(() -> {
            Map<String, Object> deploymentDescriptorMap = new YamlParser().convertYamlToMap(deploymentDescriptorYaml);
            return parser.parseDeploymentDescriptor(deploymentDescriptorMap);
        }, expectation);
    }

    public void executeTestParseExtensionDescriptorYaml(Tester tester, String extensionDescriptorLocation, Tester.Expectation expectation) {
        InputStream extensionDescriptorYaml = getClass().getResourceAsStream(extensionDescriptorLocation);
        tester.test(() -> {
            Map<String, Object> extensionDescriptorMap = new YamlParser().convertYamlToMap(extensionDescriptorYaml);
            return parser.parseExtensionDescriptor(extensionDescriptorMap);
        }, expectation);
    }

}
