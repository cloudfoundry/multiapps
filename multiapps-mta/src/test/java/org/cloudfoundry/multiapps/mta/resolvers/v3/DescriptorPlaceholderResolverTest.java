package org.cloudfoundry.multiapps.mta.resolvers.v3;

import java.io.InputStream;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

import org.cloudfoundry.multiapps.common.test.Tester;
import org.cloudfoundry.multiapps.common.test.Tester.Expectation;
import org.cloudfoundry.multiapps.mta.handlers.DescriptorParserFacade;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.resolvers.ResolverBuilder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DescriptorPlaceholderResolverTest {

    private static final Set<String> DYNAMIC_RESOLVABLE_PARAMETERS = Set.of("service-guid");
    protected static final String SYSTEM_PARAMETERS_LOCATION = "system-parameters.json";

    protected final Tester tester = Tester.forClass(getClass());
    protected DescriptorPlaceholderResolver resolver;

    static Stream<Arguments> testResolve() {
        return Stream.of(
                         // (00) mtad-with-placeholders-in-hooks
                         Arguments.of("mtad-with-placeholders-in-hook.yaml",
                                      new Expectation(Expectation.Type.JSON, "result-from-placeholders-in-hooks.json")),
                         // (01)
                         Arguments.of("mtad-with-escaped-placeholders.yaml",
                                      new Expectation(Expectation.Type.JSON, "result-from-escaped-placeholders.json")),
                         // (02) Dynamic parameter replaced with template
                         Arguments.of("mtad-with-dynamic-parameter-placeholder.yaml",
                                      new Expectation(Expectation.Type.JSON, "result-from-dynamic-parameter-placeholder.json")));
    }

    @ParameterizedTest
    @MethodSource
    void testResolve(String descriptorLocation, Expectation expectation) {
        init(descriptorLocation);

        tester.test(() -> resolver.resolve(), expectation);
    }

    private void init(String descriptorLocation) {
        DeploymentDescriptor deploymentDescriptor = parseDeploymentDescriptor(descriptorLocation);
        this.resolver = createDescriptorPlaceholderResolver(deploymentDescriptor);
    }

    protected DeploymentDescriptor parseDeploymentDescriptor(String descriptorLocation) {
        DescriptorParserFacade parser = new DescriptorParserFacade();
        InputStream descriptor = getClass().getResourceAsStream(descriptorLocation);
        return parser.parseDeploymentDescriptor(descriptor);
    }

    protected DescriptorPlaceholderResolver createDescriptorPlaceholderResolver(DeploymentDescriptor deploymentDescriptor) {
        return new DescriptorPlaceholderResolver(deploymentDescriptor,
                                                 new ResolverBuilder(),
                                                 new ResolverBuilder(),
                                                 Collections.emptyMap(),
                                                 DYNAMIC_RESOLVABLE_PARAMETERS);
    }

}
