package org.cloudfoundry.multiapps.mta.resolvers;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import org.cloudfoundry.multiapps.mta.handlers.DescriptorParserFacade;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ParameterCheckerTest {
    static Stream<Arguments> testGetMatches() {
        return Stream.of(
            Arguments.of("mtad-match-parameter.yaml",
                         List.of(new CustomParameterContainer("foo", List.of("other-parameter"), "foo"))),
            Arguments.of("mtad-match-parameter-resource.yaml",
                         List.of(new CustomParameterContainer("baz", List.of("other-parameter"), "baz"))),
            Arguments.of("mtad-match-provides-requires.yaml",
                         List.of(new CustomParameterContainer("bar", List.of("other-required-dependency-parameter"), "ztana#bar"),
                                 new CustomParameterContainer("backend-live", List.of("other-provided-dependency-parameter"),
                                                              "ztana#backend-live"),
                                 new CustomParameterContainer("bar", List.of("other-resource-dependency-parameter"), "baz#baz"))),
            Arguments.of("mtad-match-hooks.yaml",
                         List.of(new CustomParameterContainer("test-hook1", List.of("other-hook-parameter"), "foo#test-hook1"))),
            Arguments.of("mtad-match-complex.yaml",
                         List.of(new CustomParameterContainer("ztana", List.of("other-parameter"), "ztana"),
                                 new CustomParameterContainer("backend-live", List.of("other-provided-dependency-parameter"),
                                                              "ztana#backend-live"),
                                 new CustomParameterContainer("bar", List.of("other-required-dependency-parameter"), "ztana#bar"),
                                 new CustomParameterContainer("test-hook1", List.of("other-hook-parameter"), "ztana#test-hook1"),
                                 new CustomParameterContainer("baz", List.of("other-resource-parameter"), "baz"),
                                 new CustomParameterContainer("bar", List.of("other-resource-dependency-parameter"), "baz#baz"))));
    }

    @ParameterizedTest
    @MethodSource
    void testGetMatches(String descriptorLocation, List<CustomParameterContainer> expectedResult) {
        ParameterChecker parameterChecker = new ParameterCheckerTestImpl();
        DeploymentDescriptor descriptor = parseDeploymentDescriptor(descriptorLocation);
        List<CustomParameterContainer> actualResult = parameterChecker.getCustomParameters(descriptor);
        assertContainsAllCustomParameters(actualResult, expectedResult);
    }

    private DeploymentDescriptor parseDeploymentDescriptor(String deploymentDescriptorLocation) {
        InputStream deploymentDescriptorYaml = getClass().getResourceAsStream(deploymentDescriptorLocation);
        return new DescriptorParserFacade().parseDeploymentDescriptor(deploymentDescriptorYaml);
    }

    public static void assertContainsAllCustomParameters(List<CustomParameterContainer> actual, List<CustomParameterContainer> expected) {
        for (CustomParameterContainer exp : expected) {
            boolean matchFound = containsParameter(actual, exp.getParameterOwner(), exp.getParameters(), exp.getPrefixedName());
            if (!matchFound) {
                throw new AssertionError("Expected custom parameter not found: " + exp);
            }
        }

        if (actual.size() != expected.size()) {
            throw new AssertionError("Expected size " + expected.size() + " but got " + actual.size());
        }
    }

    public static boolean containsParameter(List<CustomParameterContainer> parameters, String owner, List<String> customParameters,
                                            String prefixedName) {
        return parameters.stream()
                         .anyMatch(param ->
                                       Objects.equals(param.getParameterOwner(), owner) &&
                                           Objects.equals(param.getParameters(), customParameters) &&
                                           Objects.equals(param.getPrefixedName(), prefixedName)
                         );
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
