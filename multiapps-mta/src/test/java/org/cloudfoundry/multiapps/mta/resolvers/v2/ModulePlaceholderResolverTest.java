package org.cloudfoundry.multiapps.mta.resolvers.v2;

import org.cloudfoundry.multiapps.common.test.Tester;
import org.cloudfoundry.multiapps.mta.builders.v2.ParametersChainBuilder;
import org.cloudfoundry.multiapps.mta.handlers.DescriptorParserFacade;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.resolvers.ResolverBuilder;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Map;

public class ModulePlaceholderResolverTest {
    protected final Tester tester = Tester.forClass(getClass());
    protected ModulePlaceholderResolver resolver;

    @Test
    void testResolve() {
        String descriptorLocation = "mtad-with-single-elem-in-buildpacks.yaml";
        Tester.Expectation expectation = new Tester.Expectation(Tester.Expectation.Type.EXCEPTION,
                                                                "Invalid type provided for buildpacks : Expected a list of elements but another type was provided");
        init(descriptorLocation);

        tester.test(() -> resolver.resolve(), expectation);
    }

    private void init(String descriptorLocation) {
        DeploymentDescriptor deploymentDescriptor = parseDeploymentDescriptor(descriptorLocation);
        this.resolver = createModulePlaceholderResolver(deploymentDescriptor);
    }

    protected DeploymentDescriptor parseDeploymentDescriptor(String descriptorLocation) {
        DescriptorParserFacade parser = new DescriptorParserFacade();
        InputStream descriptor = getClass().getResourceAsStream(descriptorLocation);
        return parser.parseDeploymentDescriptor(descriptor);
    }

    protected ModulePlaceholderResolver createModulePlaceholderResolver(DeploymentDescriptor deploymentDescriptor) {
        return new ModulePlaceholderResolver(deploymentDescriptor.getModules()
                                                                 .get(0),
                                             "",
                                             new ParametersChainBuilder(deploymentDescriptor),
                                             new ResolverBuilder(),
                                             new ResolverBuilder(),
                                             Map.of("buildpack", "buildpacks"));
    }
}
