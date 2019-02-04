package com.sap.cloud.lm.sl.mta.resolvers.v3;

import java.util.Arrays;
import java.util.Collections;

import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.TestUtil.Expectation;
import com.sap.cloud.lm.sl.mta.model.v3.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;

public class DescriptorPlaceholderResolverTest extends com.sap.cloud.lm.sl.mta.resolvers.v2.DescriptorPlaceholderResolverTest {

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (00) Placeholder in parameters in provided dependencies:
            {
                "mtad-01.yaml", new Expectation(Expectation.Type.RESOURCE, "result-01.json"),
            },
// @formatter:on
        });
    }

    public DescriptorPlaceholderResolverTest(String deploymentDescriptorLocation, Expectation expectation) {
        super(deploymentDescriptorLocation, expectation);
    }

    @Override
    protected DescriptorPlaceholderResolver createDescriptorPlaceholderResolver(
        com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor deploymentDescriptor) {
        return new DescriptorPlaceholderResolver((DeploymentDescriptor) deploymentDescriptor, new ResolverBuilder(), new ResolverBuilder(),
            Collections.emptyMap());
    }

}
