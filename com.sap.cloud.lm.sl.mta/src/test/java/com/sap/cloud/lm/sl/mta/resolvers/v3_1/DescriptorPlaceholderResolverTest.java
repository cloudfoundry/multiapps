package com.sap.cloud.lm.sl.mta.resolvers.v3_1;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.Callable;
import com.sap.cloud.lm.sl.common.util.JsonUtil;
import com.sap.cloud.lm.sl.common.util.TestUtil;
import com.sap.cloud.lm.sl.mta.MtaTestUtil;
import com.sap.cloud.lm.sl.mta.handlers.v3_1.ConfigurationParser;
import com.sap.cloud.lm.sl.mta.handlers.v3_1.DescriptorParser;
import com.sap.cloud.lm.sl.mta.model.SystemParameters;
import com.sap.cloud.lm.sl.mta.model.v2_0.Platform;
import com.sap.cloud.lm.sl.mta.model.v2_0.Target;
import com.sap.cloud.lm.sl.mta.model.v3_1.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;

@RunWith(value = Parameterized.class)
public class DescriptorPlaceholderResolverTest {

    private final String deploymentDescriptorLocation;
    private final String platformLocation;
    private final String targetLocation;
    private final String systemParametersLocation;
    private final String expected;

    private DescriptorPlaceholderResolver resolver;

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
        this.deploymentDescriptorLocation = deploymentDescriptorLocation;
        this.platformLocation = platformLocation;
        this.targetLocation = targetLocation;
        this.systemParametersLocation = systemParametersLocation;
        this.expected = expected;
    }

    @Before
    public void initialize() throws Exception {
        DescriptorParser descriptorParser = new DescriptorParser();

        DeploymentDescriptor deploymentDescriptor = (DeploymentDescriptor) MtaTestUtil
            .loadDeploymentDescriptor(deploymentDescriptorLocation, descriptorParser, getClass());

        ConfigurationParser configurationParser = new ConfigurationParser();

        Target target = (Target) MtaTestUtil.loadTargets(targetLocation, configurationParser, getClass())
            .get(0);
        Platform platform = (Platform) MtaTestUtil.loadPlatforms(platformLocation, configurationParser, getClass())
            .get(0);

        SystemParameters systemParameters = JsonUtil.fromJson(TestUtil.getResourceAsString(systemParametersLocation, getClass()),
            SystemParameters.class);

        resolver = new DescriptorPlaceholderResolver(deploymentDescriptor, platform, target, systemParameters, new ResolverBuilder(),
            new ResolverBuilder());
    }

    @Test
    public void testResolve() {
        TestUtil.test(new Callable<DeploymentDescriptor>() {
            @Override
            public DeploymentDescriptor call() throws Exception {
                return resolver.resolve();
            }
        }, expected, getClass());
    }

}
