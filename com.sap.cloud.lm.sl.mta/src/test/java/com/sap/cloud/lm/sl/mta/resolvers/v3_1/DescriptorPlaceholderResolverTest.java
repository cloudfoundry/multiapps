package com.sap.cloud.lm.sl.mta.resolvers.v3_1;

import java.util.Arrays;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.mta.handlers.v3_1.ConfigurationParser;
import com.sap.cloud.lm.sl.mta.handlers.v3_1.DescriptorParser;
import com.sap.cloud.lm.sl.mta.model.SystemParameters;
import com.sap.cloud.lm.sl.mta.model.v3_1.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;

@RunWith(value = Parameterized.class)
public class DescriptorPlaceholderResolverTest extends com.sap.cloud.lm.sl.mta.resolvers.v2_0.DescriptorPlaceholderResolverTest {

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (00) Placeholder in parameters in provided dependencies:
            {
                "mtad-01.yaml", "target.json", "platform-1.json", "system-parameters.json", "R:result-01.json",
            },
// @formatter:on
        });
    }

    public DescriptorPlaceholderResolverTest(String deploymentDescriptorLocation, String targetLocation, String platformLocation,
        String systemParametersLocation, String expected) {
        super(deploymentDescriptorLocation, targetLocation, platformLocation, systemParametersLocation, expected);
    }

    @Override
    protected DescriptorPlaceholderResolver getDescriptorPlaceholderResolver(
        com.sap.cloud.lm.sl.mta.model.v2_0.DeploymentDescriptor deploymentDescriptor, com.sap.cloud.lm.sl.mta.model.v2_0.Target target,
        com.sap.cloud.lm.sl.mta.model.v2_0.Platform platform, SystemParameters systemParameters) {
        return new DescriptorPlaceholderResolver((DeploymentDescriptor) deploymentDescriptor, platform, target, systemParameters,
            new ResolverBuilder(), new ResolverBuilder());
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
