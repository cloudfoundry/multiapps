package org.cloudfoundry.multiapps.mta.builders.v2;

import java.util.stream.Stream;

import org.cloudfoundry.multiapps.common.test.Tester;
import org.cloudfoundry.multiapps.common.test.Tester.Expectation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PropertiesChainBuilderTest extends AbstractChainBuilderTest{

    protected final Tester tester = Tester.forClass(getClass());

    @ParameterizedTest
    @MethodSource("moduleChainSource")
    void testBuildModuleChain(String deploymentDescriptorLocation, String platformLocation, Expectation expectation) {
        executeTestBuildModuleChain(tester, deploymentDescriptorLocation, platformLocation, expectation);
    }

    static Stream<Arguments> moduleChainSource() {
        return Stream.of(Arguments.of("mtad-01.yaml", "platform-01.json", new Expectation(Expectation.Type.JSON, "module-chain-04.json")),
                Arguments.of("mtad-01.yaml", "platform-02.json", new Expectation(Expectation.Type.JSON, "module-chain-05.json")),
                Arguments.of("mtad-01.yaml", "platform-03.json", new Expectation(Expectation.Type.JSON, "module-chain-06.json")));
    }

    @ParameterizedTest
    @MethodSource("moduleChainWithoutDependenciesSource")
    void testBuildModuleChainWithoutDependencies(String deploymentDescriptorLocation, String platformLocation, Expectation expectation) {
        executeTestBuildModuleChainWithoutDependencies(tester, deploymentDescriptorLocation, platformLocation, expectation);
    }

    static Stream<Arguments> moduleChainWithoutDependenciesSource() {
        return Stream.of(Arguments.of("mtad-01.yaml", "platform-01.json",
                        new Expectation(Expectation.Type.JSON, "module-chain-without-dependencies-04.json")),
                Arguments.of("mtad-01.yaml", "platform-02.json",
                        new Expectation(Expectation.Type.JSON, "module-chain-without-dependencies-05.json")),
                Arguments.of("mtad-01.yaml", "platform-03.json",
                        new Expectation(Expectation.Type.JSON, "module-chain-without-dependencies-06.json")));
    }

    @ParameterizedTest
    @MethodSource("resourceChainSource")
    void testBuildResourceChain(String deploymentDescriptorLocation, String platformLocation, Expectation expectation) {
        executeTestBuildResourceChain(tester, deploymentDescriptorLocation, platformLocation, expectation);
    }

    static Stream<Arguments> resourceChainSource() {
        return Stream.of(Arguments.of("mtad-01.yaml", "platform-01.json", new Expectation(Expectation.Type.JSON, "resource-chain-01.json")),
                Arguments.of("mtad-01.yaml", "platform-02.json", new Expectation(Expectation.Type.JSON, "resource-chain-06.json")),
                Arguments.of("mtad-01.yaml", "platform-03.json",
                        new Expectation(Expectation.Type.JSON, "resource-chain-03.json")));
    }

}
