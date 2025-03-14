package org.cloudfoundry.multiapps.mta.serialization.common;

import java.io.InputStream;
import java.util.Map;

import org.cloudfoundry.multiapps.common.test.Tester;
import org.cloudfoundry.multiapps.common.util.YamlParser;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.ExtensionDescriptor;
import org.cloudfoundry.multiapps.mta.parsers.v2.DeploymentDescriptorParser;
import org.cloudfoundry.multiapps.mta.parsers.v2.ExtensionDescriptorParser;

public abstract class AbstractDescriptorSerializationTest {

    public void executeTestDeploymentDescriptorSerialization(Tester tester, String deploymentDescriptorLocation,
                                                             Tester.Expectation expectation) {
        InputStream deploymentDescriptorYaml = getClass().getResourceAsStream(deploymentDescriptorLocation);
        tester.test(() -> {
            YamlParser yamlParser = new YamlParser();
            Map<String, Object> map = yamlParser.convertYamlToMap(deploymentDescriptorYaml);
            String serializedMap = yamlParser.convertToYaml(parseDeploymentDescriptor(map));
            return parseDeploymentDescriptor(yamlParser.convertYamlToMap(serializedMap));
        }, expectation);
    }

    public void executeTestExtensionDescriptorSerialization(Tester tester, String extensionDescriptorLocation,
                                                            Tester.Expectation expectation) {
        InputStream extensionDescriptorYaml = getClass().getResourceAsStream(extensionDescriptorLocation);
        tester.test(() -> {
            YamlParser yamlParser = new YamlParser();
            Map<String, Object> map = yamlParser.convertYamlToMap(extensionDescriptorYaml);
            String serializedMap = yamlParser.convertToYaml(parseExtensionDescriptor(map));
            return parseExtensionDescriptor(yamlParser.convertYamlToMap(serializedMap));
        }, expectation);
    }

    private DeploymentDescriptor parseDeploymentDescriptor(Map<String, Object> yamlMap) {
        return createDeploymentDescriptorParser(yamlMap).parse();
    }

    private ExtensionDescriptor parseExtensionDescriptor(Map<String, Object> yamlMap) {
        return createExtensionDescriptorParser(yamlMap).parse();
    }

    protected DeploymentDescriptorParser createDeploymentDescriptorParser(Map<String, Object> yamlMap) {
        return new DeploymentDescriptorParser(yamlMap);
    }

    protected ExtensionDescriptorParser createExtensionDescriptorParser(Map<String, Object> yamlMap) {
        return new ExtensionDescriptorParser(yamlMap);
    }

}
