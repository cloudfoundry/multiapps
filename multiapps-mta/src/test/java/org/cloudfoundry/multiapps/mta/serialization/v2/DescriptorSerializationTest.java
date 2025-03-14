package org.cloudfoundry.multiapps.mta.serialization.v2;

import java.util.Map;
import java.util.stream.Stream;

import org.cloudfoundry.multiapps.common.test.Tester;
import org.cloudfoundry.multiapps.common.test.Tester.Expectation;
import org.cloudfoundry.multiapps.mta.parsers.v2.DeploymentDescriptorParser;
import org.cloudfoundry.multiapps.mta.parsers.v2.ExtensionDescriptorParser;
import org.cloudfoundry.multiapps.mta.serialization.common.AbstractDescriptorSerializationTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class DescriptorSerializationTest extends AbstractDescriptorSerializationTest {

    protected final Tester tester = Tester.forClass(getClass());

    @ParameterizedTest
    @MethodSource("deploymentDescriptorSerializationSource")
    void testDeploymentDescriptorSerialization(String deploymentDescriptorLocation, Expectation expectation) {
        executeTestDeploymentDescriptorSerialization(tester, deploymentDescriptorLocation, expectation);
    }

    static Stream<Arguments> deploymentDescriptorSerializationSource() {
        return Stream.of(Arguments.of("mtad-00.yaml", new Expectation(Expectation.Type.JSON, "serialized-descriptor-00.json")));
    }

    @ParameterizedTest
    @MethodSource("testExtensionDescriptorSerializationSource")
    void testExtensionDescriptorSerialization(String extensionDescriptorLocation, Expectation expectation) {
        executeTestExtensionDescriptorSerialization(tester, extensionDescriptorLocation, expectation);
    }

    static Stream<Arguments> testExtensionDescriptorSerializationSource() {
        return Stream.of(Arguments.of("extension-descriptor-00.mtaext",
                                      new Expectation(Expectation.Type.JSON, "serialized-extension-00.json")));
    }

    @Override
    protected DeploymentDescriptorParser createDeploymentDescriptorParser(Map<String, Object> yamlMap) {
        return new DeploymentDescriptorParser(yamlMap);
    }

    @Override
    protected ExtensionDescriptorParser createExtensionDescriptorParser(Map<String, Object> yamlMap) {
        return new ExtensionDescriptorParser(yamlMap);
    }

}
