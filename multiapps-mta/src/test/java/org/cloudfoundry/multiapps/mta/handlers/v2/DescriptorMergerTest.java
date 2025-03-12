package org.cloudfoundry.multiapps.mta.handlers.v2;

import java.util.stream.Stream;

import org.cloudfoundry.multiapps.common.test.Tester;
import org.cloudfoundry.multiapps.common.test.Tester.Expectation;
import org.cloudfoundry.multiapps.mta.handlers.common.AbstractDescriptorMergerTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class DescriptorMergerTest extends AbstractDescriptorMergerTest {

    protected final Tester tester = Tester.forClass(getClass());

    @Override
    protected DescriptorMerger createDescriptorMerger() {
        return new DescriptorMerger();
    }

    @Override
    protected DescriptorParser createDescriptorParser() {
        return new DescriptorParser();
    }

    static Stream<Arguments> mergeSource() {
        return Stream.of(
                         // Valid deployment and extension descriptor:
                         Arguments.of("/mta/sample/v2/mtad-01.yaml", new String[] { "/mta/sample/v2/config-01.mtaext", },
                                      new Expectation(Expectation.Type.JSON, "/mta/sample/v2/merged-04.yaml.json")),
                         // Valid deployment and extension descriptors (multiple):
                         Arguments.of("/mta/sample/v2/mtad-01.yaml",
                                      new String[] { "/mta/sample/v2/config-01.mtaext", "/mta/sample/v2/config-05.mtaext", },
                                      new Expectation(Expectation.Type.JSON, "/mta/sample/v2/merged-05.yaml.json")));
    }

    @ParameterizedTest
    @MethodSource("mergeSource")
    void testMerge(String deploymentDescriptorLocation, String[] extensionDescriptorLocations, Expectation expectation) {
        executeTestMerge(tester, deploymentDescriptorLocation, extensionDescriptorLocations, expectation);
    }
}
