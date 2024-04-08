package org.cloudfoundry.multiapps.mta.resolvers;

import org.cloudfoundry.multiapps.common.test.Tester;
import org.cloudfoundry.multiapps.mta.handlers.DescriptorParserFacade;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.InputStream;
import java.util.stream.Stream;

public class LiveRoutesProvidedParametersResolverTest {

    public static final String USE_LIVE_ROUTES = "use-live-routes";
    protected final Tester tester = Tester.forClass(getClass());
    private LiveRoutesProvidedParametersResolver resolver;

    static Stream<Arguments> testResolve() {
        return Stream.of(
                Arguments.of("mtad-with-use-live-routes-in-provides-param.yaml",
                        new Tester.Expectation(Tester.Expectation.Type.JSON, "result-from-use-live-routes-in-provides-param.json")));
    }

    @ParameterizedTest
    @MethodSource
    void testResolve(String descriptorLocation, Tester.Expectation expectation) {
        init(descriptorLocation);

        tester.test(() -> resolver.resolve(), expectation);
    }

    private void init(String descriptorLocation) {
        DeploymentDescriptor deploymentDescriptor = parseDeploymentDescriptor(descriptorLocation);
        resolver = new LiveRoutesProvidedParametersResolver(deploymentDescriptor, USE_LIVE_ROUTES);
    }

    private DeploymentDescriptor parseDeploymentDescriptor(String descriptorLocation) {
        DescriptorParserFacade parser = new DescriptorParserFacade();
        InputStream descriptor = getClass().getResourceAsStream(descriptorLocation);
        return parser.parseDeploymentDescriptor(descriptor);
    }
}
