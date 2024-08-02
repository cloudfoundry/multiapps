package org.cloudfoundry.multiapps.mta.resolvers;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

import org.cloudfoundry.multiapps.common.test.TestUtil;
import org.cloudfoundry.multiapps.common.test.Tester;
import org.cloudfoundry.multiapps.common.test.Tester.Expectation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PropertiesResolverTest {

    protected static final Map<String, Object> replacementValues = TestUtil.getMap("moduleProperties.yaml", PropertiesResolverTest.class);

    private final Tester tester = Tester.forClass(getClass());

    static Stream<Arguments> testResolve() {
        return Stream.of(Arguments.of("list-in-map", "${test-map/a-list/1}", new Expectation("second-item")),
                         Arguments.of("map-in-list", "${test-list/0/foo}", new Expectation("@foo")),
                         Arguments.of("map-in-list-extra-slash", "${test-list/0/foo/}", new Expectation("@foo")),
                         Arguments.of("complex-value", "${test-list/1}", new Expectation("{buzz=@buzz, fizz=@fizz}")),
                         Arguments.of("simple-value", "${test-list/2}", new Expectation("a string in list")),
                         Arguments.of("first-in-list", "${hosts/0/}", new Expectation("some host")),
                         Arguments.of("bad-index", "${hosts/1.0}",
                                      new Expectation(Expectation.Type.EXCEPTION, "Unable to resolve \"test-#hosts/1.0\"")),
                         Arguments.of("too-high-index", "${test-list/10/foo/}",
                                      new Expectation(Expectation.Type.EXCEPTION, "Unable to resolve \"test-#test-list/10/foo/\"")),
                         Arguments.of("missing-index", "${test-list//foo/}",
                                      new Expectation(Expectation.Type.EXCEPTION, "Unable to resolve \"test-#test-list//foo/\"")),
                         Arguments.of("corner-case-whitespace", "${tricky-map/just a key}", new Expectation("foo")),
                         Arguments.of("corner-case-map-and-list", "${tricky-map/0/1}", new Expectation("baz")),
                         Arguments.of("corner-case-slash-1", "${tricky-map/one/two/}", new Expectation("why")),
                         Arguments.of("corner-case-slash-2", "${tricky-map/3/4/5}", new Expectation("stop")),
                         Arguments.of("9-qux", "${NO_CIRCULAR_REF}", new Expectation("qux-qux-qux-qux-qux-qux-qux-qux-qux")),
                         Arguments.of("9-qux-direct", "${foo}-${bar}-${baz}", new Expectation("qux-qux-qux-qux-qux-qux-qux-qux-qux")),
                         Arguments.of("2-ha", "${NO_CIRCULAR_REF_IN_MAP/a}", new Expectation("ha-ha")),
                         Arguments.of("circular-ref", "${CIRCULAR_REF}",
                                      new Expectation(Expectation.Type.EXCEPTION, "Circular reference detected in \"test-#circular-ref\"")),
                         Arguments.of("circular-ref-map", "${CIRCULAR_REF_MAP_IN_MAP/key/key}", new Expectation(Expectation.Type.EXCEPTION,
                                                                                                                "Circular reference detected in \"test-#circular-ref-map\"")));
    }

    @ParameterizedTest
    @MethodSource
    void testResolve(String parameterName, String parameterValue, Expectation expectation) {
        PropertiesResolver testResolver = new PropertiesResolver(null, irrelevant -> replacementValues, ReferencePattern.PLACEHOLDER,
                                                                 "test-", false, Collections.emptySet());
        tester.test(() -> testResolver.visit(parameterName, parameterValue), expectation);
    }

}
