package com.sap.cloud.lm.sl.mta.resolvers.v3;

import java.util.Arrays;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.TestUtil.Expectation;
import com.sap.cloud.lm.sl.mta.handlers.v3.ConfigurationParser;
import com.sap.cloud.lm.sl.mta.handlers.v3.DescriptorParser;
import com.sap.cloud.lm.sl.mta.model.SystemParameters;
import com.sap.cloud.lm.sl.mta.model.v3.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;

@RunWith(value = Parameterized.class)
public class DescriptorPlaceholderResolverTest extends com.sap.cloud.lm.sl.mta.resolvers.v2.DescriptorPlaceholderResolverTest {

    public DescriptorPlaceholderResolverTest(String deploymentDescriptorLocation, String platformLocation, Expectation expectation) {
        super(deploymentDescriptorLocation, platformLocation, expectation);
    }

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (00) Placeholder in parameters in provided dependencies:
            {
                "mtad-01.yaml", "platform-1.json", new Expectation(Expectation.Type.RESOURCE, "result-01.json"),
            },
// @formatter:on
        });
    }

    @Override
    protected DescriptorPlaceholderResolver
              getDescriptorPlaceholderResolver(com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor deploymentDescriptor,
                                               com.sap.cloud.lm.sl.mta.model.v2.Platform platform, SystemParameters systemParameters) {
        return new DescriptorPlaceholderResolver((DeploymentDescriptor) deploymentDescriptor,
                                                 platform,
                                                 systemParameters,
                                                 new ResolverBuilder(),
                                                 new ResolverBuilder());
    }

    @Override
    protected ConfigurationParser getConfigurationParser() {
        return new ConfigurationParser();
    }

    @Override
    protected DescriptorParser getDescriptorParser() {
        return new DescriptorParser();
    }

}
