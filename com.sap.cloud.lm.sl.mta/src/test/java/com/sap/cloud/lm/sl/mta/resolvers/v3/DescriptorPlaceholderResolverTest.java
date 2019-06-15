package com.sap.cloud.lm.sl.mta.resolvers.v3;

import java.io.InputStream;
import java.util.Collections;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.sap.cloud.lm.sl.common.util.Tester;
import com.sap.cloud.lm.sl.common.util.Tester.Expectation;
import com.sap.cloud.lm.sl.mta.handlers.DescriptorParserFacade;
import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;

public class DescriptorPlaceholderResolverTest {

    protected static final String SYSTEM_PARAMETERS_LOCATION = "system-parameters.json";

    protected final Tester tester = Tester.forClass(getClass());
    protected DescriptorPlaceholderResolver resolver;

    public static Stream<Arguments> testResolve() {
        return Stream.of(
// @formatter:off            
            // (00) mtad-with-placeholders-in-hooks
            Arguments.of(
                "mtad-with-placeholders-in-hook.yaml", new Expectation(Expectation.Type.JSON, "result-from-placeholders-in-hooks.json")
            ),
            // (01)
            Arguments.of(
                "mtad-with-escaped-placeholders.yaml", new Expectation(Expectation.Type.JSON, "result-from-escaped-placeholders.json")
            )
// @formatter:on
        );
    }


    @ParameterizedTest
    @MethodSource
    public void testResolve(String descriptorLocation, Expectation expectation) {
        init(descriptorLocation);

        tester.test(() -> resolver.resolve(), expectation);
    }

    public void init(String descriptorLocation) {
        DeploymentDescriptor deploymentDescriptor = parseDeploymentDescriptor(descriptorLocation);
        this.resolver = createDescriptorPlaceholderResolver(deploymentDescriptor);
    }

    protected DeploymentDescriptor parseDeploymentDescriptor(String descriptorLocation) {
        DescriptorParserFacade parser = new DescriptorParserFacade();
        InputStream descriptor = getClass().getResourceAsStream(descriptorLocation);
        return parser.parseDeploymentDescriptor(descriptor);
    }

    protected DescriptorPlaceholderResolver createDescriptorPlaceholderResolver(DeploymentDescriptor deploymentDescriptor) {
        return new DescriptorPlaceholderResolver(deploymentDescriptor, new ResolverBuilder(), new ResolverBuilder(),
            Collections.emptyMap());
    }

}
