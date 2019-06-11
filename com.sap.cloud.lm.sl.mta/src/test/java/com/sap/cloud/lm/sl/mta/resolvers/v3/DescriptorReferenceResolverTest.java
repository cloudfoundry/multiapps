package com.sap.cloud.lm.sl.mta.resolvers.v3;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.sap.cloud.lm.sl.common.util.Tester;
import com.sap.cloud.lm.sl.common.util.Tester.Expectation;
import com.sap.cloud.lm.sl.mta.MtaTestUtil;
import com.sap.cloud.lm.sl.mta.handlers.v3.DescriptorParser;
import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;

public class DescriptorReferenceResolverTest {

    private final Tester tester = Tester.forClass(getClass());

    private DescriptorReferenceResolver resolver;

    public static Stream<Arguments> testResolve() {
        return Stream.of(
// @formatter:off
            // (0) Resolve references in resources:
            Arguments.of(
                "merged-01.yaml", new Expectation(Expectation.Type.JSON, "resolved-01.yaml.json")
            ),
            // (1) Resolve references in resources - cyclic dependencies & corner cases:
            Arguments.of(
                "merged-02.yaml", new Expectation(Expectation.Type.JSON, "resolved-02.yaml.json")
            ),
            // (2) Test error reporting on failure to resolve value:
            Arguments.of(
                "merged-03.yaml", new Expectation(Expectation.Type.EXCEPTION, "Unable to resolve \"baz##non-existing\"")
            ),
            // (3) Resolve references in hooks:
            Arguments.of(
                "merged-04.yaml", new Expectation(Expectation.Type.JSON, "resolved-03.yaml.json")
            )
// @formatter:on
        );
    }

    @ParameterizedTest
    @MethodSource
    public void testResolve(String mergedDescriptorLocation, Expectation expectation) {
        init(mergedDescriptorLocation);

        tester.test(() -> resolver.resolve(), expectation);
    }

    public void init(String mergedDescriptorLocation) {
        DeploymentDescriptor mergedDescriptor = MtaTestUtil.loadDeploymentDescriptor(mergedDescriptorLocation, new DescriptorParser(),
            getClass());
        resolver = new DescriptorReferenceResolver(mergedDescriptor, new ResolverBuilder(), new ResolverBuilder(), new ResolverBuilder());
    }

}
