package org.cloudfoundry.multiapps.mta.resolvers;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.cloudfoundry.multiapps.mta.handlers.DescriptorParserFacade;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static java.util.List.of;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class ReferencesFinderTest {

    static Stream<Arguments> testGetReferences() {
        return Stream.of(
            arguments("mtad-reference-parameter.yaml",
                      of(
                          container(null, of(ref("${a}", "a", null))),
                          container("foo", of(ref("${b}", "b", null))),
                          container("baz", of(
                              ref("~{c/d}", "d", "c"),
                              ref("~{e/f}", "f", "e"),
                              ref("~{bar/non-existing}", "non-existing", "bar")
                          ))
                      )
            ),
            arguments("mtad-reference-property.yaml",
                      of(
                          container("foo", of(ref("${b}", "b", null))),
                          container("baz", of(
                              ref("~{bar/non-existing}", "non-existing", "bar"),
                              ref("~{e/f}", "f", "e"),
                              ref("~{c/d}", "d", "c")
                          ))
                      )
            ),
            arguments("mtad-reference-provides-requires.yaml",
                      of(
                          container("ztanaa#backend-live", of(ref("${ref-url}", "ref-url", null))),
                          container("ztanaa#bar", of(ref("${non-existing}", "non-existing", null))),
                          container("ztanaa#backend-idle", of(ref("${ref-url-2}", "ref-url-2", null))),
                          container("baz#baz", of(ref("${non-existing-resource}", "non-existing-resource", null)))
                      )
            ),
            arguments("mtad-reference-hooks.yaml",
                      of(
                          container("foo#test-hook1", of(ref("${default-time}", "default-time", null)))
                      )
            ),
            arguments("mtad-reference-complex.yaml",
                      of(
                          container(null, of(ref("${a}", "a", null))),
                          container("foo", of(ref("${b}", "b", null), ref("${c}", "c", null))),
                          container("foo#backend-live", of(ref("${ref-url}", "ref-url", null))),
                          container("foo#backend-idle", of(ref("${ref-url-2}", "ref-url-2", null))),
                          container("foo#bar", of(ref("${d}", "d", null))),
                          container("foo#test-hook1", of(ref("${task-ref}", "task-ref", null))),
                          container("baz", of(ref("~{e}", "e", null), ref("~{f}", "f", null))),
                          container("baz#baz", of(ref("${non-existing-resource}", "non-existing-resource", null)))
                      )
            )
        );
    }

    @ParameterizedTest
    @MethodSource
    void testGetReferences(String descriptorLocation, List<ReferenceContainer> expectedResult) {
        ReferencesFinder finder = new ReferencesFinder();
        DeploymentDescriptor descriptor = parseDeploymentDescriptor(descriptorLocation);
        List<ReferenceContainer> actualReferences = finder.getAllReferences(descriptor);
        assertContainsAllReferences(actualReferences, expectedResult);
    }

    private DeploymentDescriptor parseDeploymentDescriptor(String deploymentDescriptorLocation) {
        InputStream deploymentDescriptorYaml = getClass().getResourceAsStream(deploymentDescriptorLocation);
        return new DescriptorParserFacade().parseDeploymentDescriptor(deploymentDescriptorYaml);
    }

    private static ReferenceContainer container(String containerName, List<Reference> references) {
        return new ReferenceContainer(containerName, references);
    }

    private static Reference ref(String pattern, String key, String dep) {
        return new Reference(pattern, key, dep);
    }

    public static void assertContainsAllReferences(List<ReferenceContainer> actual, List<ReferenceContainer> expected) {
        if (actual.size() != expected.size()) {
            throw new AssertionError("Expected size " + expected.size() + " but got " + actual.size());
        }

        for (ReferenceContainer expectedContainer : expected) {
            ReferenceContainer actualContainer = findMatchingActualContainer(actual, expectedContainer.getReferenceOwner());
            assertAllReferencesMatch(expectedContainer, actualContainer);
        }
    }

    private static ReferenceContainer findMatchingActualContainer(List<ReferenceContainer> actual, String expectedOwner) {
        return actual.stream()
                     .filter(container -> Objects.equals(expectedOwner, container.getReferenceOwner()))
                     .findFirst()
                     .orElseThrow(() -> new AssertionError("Expected owner not found: " + expectedOwner));
    }

    private static void assertAllReferencesMatch(ReferenceContainer expectedContainer, ReferenceContainer actualContainer) {
        String owner = expectedContainer.getReferenceOwner();

        expectedContainer.getParameters()
                         .forEach(expectedReference ->
                                      assertReferenceExists(actualContainer, expectedReference, owner)
                         );
    }

    private static void assertReferenceExists(ReferenceContainer actualContainer, Reference expectedReference, String owner) {
        boolean found = actualContainer.getParameters()
                                       .stream()
                                       .anyMatch(actualReference -> containsReference(
                                           actualContainer.getParameters(),
                                           expectedReference.getMatchedPattern(),
                                           expectedReference.getKey(),
                                           expectedReference.getDependencyName()
                                       ));

        if (!found) {
            throw new AssertionError("Expected reference not found for owner " + owner + ": " + expectedReference);
        }
    }

    public static boolean containsReference(List<Reference> set, String pattern, String key, String dependencyName) {
        return set.stream()
                  .anyMatch(ref ->
                                Objects.equals(ref.getMatchedPattern(), pattern) &&
                                    Objects.equals(ref.getKey(), key) &&
                                    Objects.equals(ref.getDependencyName(), dependencyName)
                  );
    }

}
