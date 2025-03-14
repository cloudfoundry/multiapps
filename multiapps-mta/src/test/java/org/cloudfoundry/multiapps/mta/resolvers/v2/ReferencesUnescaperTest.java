package org.cloudfoundry.multiapps.mta.resolvers.v2;

import java.util.stream.Stream;

import org.cloudfoundry.multiapps.common.test.Tester;
import org.cloudfoundry.multiapps.common.test.Tester.Expectation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ReferencesUnescaperTest extends org.cloudfoundry.multiapps.mta.resolvers.common.AbstractReferencesUnescaperTest {

    private Tester tester = Tester.forClass(getClass());

    @ParameterizedTest
    @MethodSource("unescapingResource")
    void testUnescaping(String descriptorResource, Expectation expectation) {
        executeTestUnescaping(tester, descriptorResource, expectation);
    }

    static Stream<Arguments> unescapingResource() {
        return Stream.of(Arguments.of("mtad-with-escaped-placeholders.yaml",
                                      new Expectation(Expectation.Type.JSON, "result-from-unescaping-placeholders.json")),
                         Arguments.of("mtad-with-escaped-references.yaml",
                                      new Expectation(Expectation.Type.JSON, "result-from-unescaping-references.json")));
    }

}
