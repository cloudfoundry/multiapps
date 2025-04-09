package org.cloudfoundry.multiapps.mta.resolvers;

import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.cloudfoundry.multiapps.mta.handlers.DescriptorParserFacade;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParameterCheckerTest {
    static Stream<Arguments> testGetMatches() {
        return Stream.of(
            Arguments.of("mtad-match-parameter.yaml",
                         List.of("other-parameter")),
            Arguments.of("mtad-match-parameter-resource.yaml",
                         List.of("other-parameter")),
            Arguments.of("mtad-match-provides-requires.yaml",
                         List.of("other-provided-dependency-parameter", "other-required-dependency-parameter",
                                 "other-resource-dependency-parameter")),
            Arguments.of("mtad-match-hooks.yaml",
                         List.of("other-hook-parameter")),
            Arguments.of("mtad-match-complex.yaml",
                         List.of("other-parameter", "other-provided-dependency-parameter", "other-required-dependency-parameter",
                                 "other-hook-parameter",
                                 "other-resource-parameter",
                                 "other-resource-dependency-parameter")));
    }

    @ParameterizedTest
    @MethodSource
    void testGetMatches(String descriptorLocation, List<String> expectedResult) {
        ParameterChecker parameterChecker = new ParameterCheckerTestImpl();
        DeploymentDescriptor descriptor = parseDeploymentDescriptor(descriptorLocation);
        List<String> actualResult = parameterChecker.getCustomParameters(descriptor);
        assertEquals(expectedResult, actualResult);
    }

    private DeploymentDescriptor parseDeploymentDescriptor(String deploymentDescriptorLocation) {
        InputStream deploymentDescriptorYaml = getClass().getResourceAsStream(deploymentDescriptorLocation);
        return new DescriptorParserFacade().parseDeploymentDescriptor(deploymentDescriptorYaml);
    }

    private class ParameterCheckerTestImpl extends ParameterChecker {

        @Override
        protected Set<String> getModuleParametersToMatch() {
            return Set.of("module-parameter");
        }

        @Override
        protected Set<String> getModuleHookParametersToMatch() {
            return Set.of("hook-parameter");
        }

        @Override
        protected Set<String> getResourceParametersToMatch() {
            return Set.of("resource-parameter");
        }

        @Override
        protected Set<String> getGlobalParametersToMatch() {
            return Set.of("global-parameter");
        }

        @Override
        protected Set<String> getDependencyParametersToMatch() {
            return Set.of("dependency-parameter");
        }
    }
}
