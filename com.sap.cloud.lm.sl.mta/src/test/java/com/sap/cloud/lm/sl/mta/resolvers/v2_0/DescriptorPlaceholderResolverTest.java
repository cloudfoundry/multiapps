package com.sap.cloud.lm.sl.mta.resolvers.v2_0;

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
import com.sap.cloud.lm.sl.mta.handlers.v2_0.ConfigurationParser;
import com.sap.cloud.lm.sl.mta.handlers.v2_0.DescriptorParser;
import com.sap.cloud.lm.sl.mta.model.SystemParameters;
import com.sap.cloud.lm.sl.mta.model.v2_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2_0.Platform;
import com.sap.cloud.lm.sl.mta.model.v2_0.Target;
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
            // (00) Placeholder in requires dependency:
            {
                "mtad-01.yaml", "target.json", "platform-1.json", "system-parameters.json", "R:result-01.json",
            },
            // (01) Concatenation of placeholders:
            {
                "mtad-02.yaml", "target.json", "platform-1.json", "system-parameters.json", "R:result-02.json",
            },
            // (02) Placeholder in extension descriptor properties:
            {
                "mtad-03.yaml", "target.json", "platform-1.json", "system-parameters.json", "R:result-03.json",
            },
            // (03) Placeholder in module:
            {
                "mtad-04.yaml", "target.json", "platform-1.json", "system-parameters.json", "R:result-04.json",
            },
            // (04) Placeholder in resource:
            {
                "mtad-05.yaml", "target.json", "platform-1.json", "system-parameters.json", "R:result-05.json",
            },
            // (05) Placeholder in provided dependency:
            {
                "mtad-06.yaml", "target.json", "platform-1.json", "system-parameters.json", "R:result-06.json",
            },
            // (06) Unable to resolve placeholder in requires dependency:
            {
                "mtad-07.yaml", "target.json", "platform-1.json", "system-parameters.json", "E:Unable to resolve \"pricing#pricing-db#non-existing\"",
            },
            // (07) Unable to resolve placeholder in extension descriptor properties:
            {
                "mtad-08.yaml", "target.json", "platform-1.json", "system-parameters.json", "E:Unable to resolve \"non-existing\"",
            },
            // (08) Unable to resolve placeholder in module:
            {
                "mtad-09.yaml", "target.json", "platform-1.json", "system-parameters.json", "E:Unable to resolve \"pricing#non-existing\"",
            },
            // (09) Placeholder in untyped resource:
            {
                "mtad-10.yaml", "target.json", "platform-1.json", "system-parameters.json", "R:result-10.json",
            },
            // (10) Placeholder in module type:
            {
                "mtad-11.yaml", "target.json", "platform-2.json", "system-parameters.json", "R:result-11.json",
            },
            // (11) Circular reference in module type:
            {
                "mtad-11.yaml", "target.json", "platform-3.json", "system-parameters.json", "E:Circular reference detected in \"pricing#pricing-db#baz\"",
            },
            // (12) Placeholder in requires dependency (default-uri):
            {
                "mtad-13.yaml", "target.json", "platform-1.json", "system-parameters.json", "R:result-13.json",
            },
            // (13) Placeholders in a map in resource parameters:
            {
                "mtad-14.yaml", "target.json", "platform-1.json", "system-parameters.json", "R:result-14.json",
            },
            // (14) Placeholders in a list in a map in resource parameters:
            {
                "mtad-15.yaml", "target.json", "platform-1.json", "system-parameters.json", "R:result-15.json",
            },
            // (15) Circular reference in a module parameter:
            {
                "mtad-16.yaml", "target.json", "platform-1.json", "system-parameters.json", "E:Circular reference detected in \"foo#bar#test_1\"",
            },
            // (16) Circular reference in a module parameter:
            {
                "mtad-17.yaml", "target.json", "platform-1.json", "system-parameters.json", "E:Circular reference detected in \"foo#test_1\"",
            },
            // (17) Global mta parameters to be resolved in lower scopes
            {
                "mtad-18.yaml", "target.json", "platform-1.json", "system-parameters.json", "R:result-16.json",  
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

        DeploymentDescriptor deploymentDescriptor = (DeploymentDescriptor) MtaTestUtil.loadDeploymentDescriptor(
            deploymentDescriptorLocation, descriptorParser, getClass());

        ConfigurationParser configurationParser = new ConfigurationParser();

        Target target = (Target) MtaTestUtil.loadTargets(targetLocation, configurationParser, getClass()).get(0);
        Platform platform = (Platform) MtaTestUtil.loadPlatforms(platformLocation, configurationParser, getClass()).get(0);

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
