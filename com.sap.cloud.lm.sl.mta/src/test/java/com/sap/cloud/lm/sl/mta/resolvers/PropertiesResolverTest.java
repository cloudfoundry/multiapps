package com.sap.cloud.lm.sl.mta.resolvers;

import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.TestUtil;
import com.sap.cloud.lm.sl.common.util.Tester;
import com.sap.cloud.lm.sl.common.util.Tester.Expectation;

public class PropertiesResolverTest {

    protected static final PropertiesResolver testResolver = new PropertiesResolver(null, null, null, "test-", false);

    private final Tester tester = Tester.forClass(getClass());

    @Parameters
    public static Stream<Arguments> testResolve() {
        return Stream.of(
        // @formatter:off
            Arguments.of("moduleProperties.yaml", "test-map/a-list/1", new Expectation("second-item")),
            Arguments.of("moduleProperties.yaml", "test-list/0/foo", new Expectation("@foo")),
            Arguments.of("moduleProperties.yaml", "test-list/0/foo/", new Expectation("@foo")),
            Arguments.of("moduleProperties.yaml", "test-list/1", new Expectation("{fizz=@fizz, buzz=@buzz}")),
            Arguments.of("moduleProperties.yaml", "test-list/1/buzz", new Expectation("@buzz")),
            Arguments.of("moduleProperties.yaml", "test-list/2", new Expectation("a string in list")),
            Arguments.of("moduleProperties.yaml", "hosts/0/", new Expectation("some host")),
            Arguments.of("moduleProperties.yaml", "hosts/1.0", new Expectation(Expectation.Type.EXCEPTION, "Unable to resolve \"test-#hosts/1.0\"")),
            Arguments.of("moduleProperties.yaml", "test-list/10/foo/", new Expectation(Expectation.Type.EXCEPTION, "Unable to resolve \"test-#test-list/10/foo/\"")),
            Arguments.of("moduleProperties.yaml", "test-list//foo/", new Expectation(Expectation.Type.EXCEPTION, "Unable to resolve \"test-#test-list//foo/\"")),
            Arguments.of("moduleProperties.yaml", "tricky-map/just a key", new Expectation("foo")),
            Arguments.of("moduleProperties.yaml", "tricky-map/0/1", new Expectation("baz")),
            Arguments.of("moduleProperties.yaml", "tricky-map/one/two/", new Expectation("why")),
            Arguments.of("moduleProperties.yaml", "tricky-map/3/4/5", new Expectation("stop")));
        // @formatter:on
    }

    @ParameterizedTest
    @MethodSource
    public void testResolve(String modulePropertiesLocation, String parameterExpression, Expectation expectation) {
        Map<String, Object> moduleProperties = TestUtil.getMap(modulePropertiesLocation, getClass());

        tester.test(() -> {
            return testResolver.resolveReferenceInDepth(parameterExpression, moduleProperties);
        }, expectation);
    }

}
