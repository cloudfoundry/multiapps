package org.cloudfoundry.multiapps.mta.resolvers;

import java.io.InputStream;
import java.util.Set;
import java.util.stream.Stream;

import org.cloudfoundry.multiapps.mta.handlers.DescriptorParserFacade;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReferencesFinderTest {

    static Stream<Arguments> testGetReferences() {
        return Stream.of(
            Arguments.of("mtad-reference-parameter.yaml",
                         Set.of("a", "b", "bar", "c", "d", "e", "non-existing", "f", "c/d", "bar/non-existing", "e/f")),
            Arguments.of("mtad-reference-property.yaml",
                         Set.of("b", "bar", "c", "d", "e", "non-existing", "f", "c/d", "bar/non-existing", "e/f")),
            Arguments.of("mtad-reference-provides-requires.yaml",
                         Set.of("ref-url-2", "non-existing", "non-existing-resource", "ref-url")),
            Arguments.of("mtad-reference-hooks.yaml",
                         Set.of("default-time")),
            Arguments.of("mtad-reference-complex.yaml",
                         Set.of("a", "ref-url-2", "b", "c", "d", "e", "non-existing-resource", "ref-url", "f", "task-ref")));
    }

    @ParameterizedTest
    @MethodSource
    void testGetReferences(String descriptorLocation, Set<String> expectedResult) {
        ReferencesFinder finder = new ReferencesFinder();
        DeploymentDescriptor descriptor = parseDeploymentDescriptor(descriptorLocation);
        Set<String> actualReferences = finder.findReferences(descriptor);
        assertEquals(expectedResult, actualReferences);
    }

    private DeploymentDescriptor parseDeploymentDescriptor(String deploymentDescriptorLocation) {
        InputStream deploymentDescriptorYaml = getClass().getResourceAsStream(deploymentDescriptorLocation);
        return new DescriptorParserFacade().parseDeploymentDescriptor(deploymentDescriptorYaml);
    }

}
