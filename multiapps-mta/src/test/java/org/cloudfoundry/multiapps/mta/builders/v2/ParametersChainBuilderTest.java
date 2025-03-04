package org.cloudfoundry.multiapps.mta.builders.v2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.cloudfoundry.multiapps.common.test.Tester;
import org.cloudfoundry.multiapps.common.test.Tester.Expectation;
import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorParser;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.Platform;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ParametersChainBuilderTest extends AbstractChainBuilderTest {

    protected final Tester tester = Tester.forClass(getClass());

    @Override
    protected DescriptorParser createDescriptorParser() {
        return new DescriptorParser();
    }

    @Override
    protected PropertiesChainBuilder createPropertiesChainBuilder(DeploymentDescriptor dd, Platform p) {
        return new ParametersChainBuilder(dd, p);
    }

    @ParameterizedTest
    @MethodSource("moduleChainSource")
    void testBuildModuleChain(String deploymentDescriptorLocation, String platformLocation, Expectation expectation) {
        executeTestBuildModuleChain(tester, deploymentDescriptorLocation, platformLocation, expectation);
    }

    static Stream<Arguments> moduleChainSource() {
        return Stream.of(Arguments.of("mtad-02.yaml", "platform-04.json", new Expectation(Expectation.Type.JSON, "module-chain-01.json")),
                         Arguments.of("mtad-02.yaml", "platform-02.json", new Expectation(Expectation.Type.JSON, "module-chain-02.json")),
                         Arguments.of("mtad-02.yaml", "platform-05.json", new Expectation(Expectation.Type.JSON, "module-chain-03.json")));
    }

    @ParameterizedTest
    @MethodSource("moduleChainWithoutDependenciesSource")
    void testBuildModuleChainWithoutDependencies(String deploymentDescriptorLocation, String platformLocation, Expectation expectation) {
        executeTestBuildModuleChainWithoutDependencies(tester, deploymentDescriptorLocation, platformLocation, expectation);
    }

    static Stream<Arguments> moduleChainWithoutDependenciesSource() {
        return Stream.of(Arguments.of("mtad-02.yaml", "platform-04.json",
                                      new Expectation(Expectation.Type.JSON, "module-chain-without-dependencies-01.json")),
                         Arguments.of("mtad-02.yaml", "platform-02.json",
                                      new Expectation(Expectation.Type.JSON, "module-chain-without-dependencies-02.json")),
                         Arguments.of("mtad-02.yaml", "platform-05.json",
                                      new Expectation(Expectation.Type.JSON, "module-chain-without-dependencies-03.json")));
    }

    @ParameterizedTest
    @MethodSource("resourceChainSource")
    void testBuildResourceChain(String deploymentDescriptorLocation, String platformLocation, Expectation expectation) {
        executeTestBuildResourceChain(tester, deploymentDescriptorLocation, platformLocation, expectation);
    }

    static Stream<Arguments> resourceChainSource() {
        return Stream.of(Arguments.of("mtad-02.yaml", "platform-04.json", new Expectation(Expectation.Type.JSON, "resource-chain-04.json")),
                         Arguments.of("mtad-02.yaml", "platform-02.json", new Expectation(Expectation.Type.JSON, "resource-chain-02.json")),
                         Arguments.of("mtad-02.yaml", "platform-05.json",
                                      new Expectation(Expectation.Type.JSON, "resource-chain-05.json")));
    }

    @ParameterizedTest
    @MethodSource("resourceTypeChainSource")
    void testBuildResourceTypeChain(String deploymentDescriptorLocation, String platformLocation, Expectation expectation) {
        DeploymentDescriptor deploymentDescriptor = parseDeploymentDescriptor(deploymentDescriptorLocation);
        Platform platform = parsePlatform(platformLocation);

        PropertiesChainBuilder builder = createPropertiesChainBuilder(deploymentDescriptor, platform);
        tester.test(() -> {
            List<List<Map<String, Object>>> resourceTypeChains = new ArrayList<>();
            for (String resourceName : getResourceNames(deploymentDescriptor)) {
                resourceTypeChains.add(builder.buildResourceTypeChain(resourceName));
            }
            return resourceTypeChains;
        }, expectation);
    }

    static Stream<Arguments> resourceTypeChainSource() {
        return Stream.of(Arguments.of("mtad-02.yaml", "platform-04.json",
                        new Expectation(Expectation.Type.JSON, "resource-type-chain-01.json")),
                Arguments.of("mtad-02.yaml", "platform-02.json",
                        new Expectation(Expectation.Type.JSON, "resource-type-chain-02.json")),
                Arguments.of("mtad-02.yaml", "platform-05.json",
                        new Expectation(Expectation.Type.JSON, "resource-type-chain-03.json")));
    }

}
