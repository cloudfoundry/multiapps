package com.sap.cloud.lm.sl.mta.resolvers.v2;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.Callable;
import com.sap.cloud.lm.sl.common.util.JsonUtil;
import com.sap.cloud.lm.sl.common.util.TestUtil;
import com.sap.cloud.lm.sl.common.util.TestUtil.Expectation;
import com.sap.cloud.lm.sl.mta.MtaTestUtil;
import com.sap.cloud.lm.sl.mta.handlers.v2.ConfigurationParser;
import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorParser;
import com.sap.cloud.lm.sl.mta.model.SystemParameters;
import com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2.Platform;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;

@RunWith(Parameterized.class)
public class DescriptorPlaceholderResolverTest {

    protected static final String SYSTEM_PARAMETERS_LOCATION = "system-parameters.json";

    protected final String deploymentDescriptorLocation;
    protected final String platformLocation;
    protected final Expectation expectation;

    protected DescriptorPlaceholderResolver resolver;

    public DescriptorPlaceholderResolverTest(String deploymentDescriptorLocation, String platformLocation, Expectation expectation) {
        this.deploymentDescriptorLocation = deploymentDescriptorLocation;
        this.platformLocation = platformLocation;
        this.expectation = expectation;
    }

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (00) Placeholder in requires dependency:
            {
                "mtad-01.yaml", "platform-1.json", new Expectation(Expectation.Type.RESOURCE, "result-01.json"),
            },
            // (01) Concatenation of placeholders:
            {
                "mtad-02.yaml", "platform-1.json",  new Expectation(Expectation.Type.RESOURCE, "result-02.json"),
            },
            // (02) Placeholder in extension descriptor properties:
            {
                "mtad-03.yaml",  "platform-1.json", new Expectation(Expectation.Type.RESOURCE, "result-03.json"),
            },
            // (03) Placeholder in module:
            {
                "mtad-04.yaml",  "platform-1.json", new Expectation(Expectation.Type.RESOURCE, "result-04.json"),
            },
            // (04) Placeholder in resource:
            {
                "mtad-05.yaml",  "platform-1.json",  new Expectation(Expectation.Type.RESOURCE, "result-05.json"),
            },
            // (05) Placeholder in provided dependency:
            {
                "mtad-06.yaml",  "platform-1.json",  new Expectation(Expectation.Type.RESOURCE, "result-06.json"),
            },
            // (06) Unable to resolve placeholder in requires dependency:
            {
                "mtad-07.yaml",  "platform-1.json",  new Expectation(Expectation.Type.EXCEPTION, "Unable to resolve \"pricing#pricing-db#non-existing\""),
            },
            // (07) Unable to resolve placeholder in extension descriptor properties:
            {
                "mtad-08.yaml",  "platform-1.json",  new Expectation(Expectation.Type.EXCEPTION, "Unable to resolve \"non-existing\""),
            },
            // (08) Unable to resolve placeholder in module:
            {
                "mtad-09.yaml",  "platform-1.json",  new Expectation(Expectation.Type.EXCEPTION, "Unable to resolve \"pricing#non-existing\""),
            },
            // (09) Placeholder in untyped resource:
            {
                "mtad-10.yaml",  "platform-1.json",  new Expectation(Expectation.Type.RESOURCE, "result-10.json"),
            },
            // (10) Placeholder in module type:
            {
                "mtad-11.yaml",  "platform-2.json", new Expectation(Expectation.Type.RESOURCE, "result-11.json"),
            },
            // (11) Circular reference in module type:
            {
                "mtad-11.yaml",  "platform-3.json", new Expectation(Expectation.Type.EXCEPTION, "Circular reference detected in \"pricing#pricing-db#baz\""),
            },
            // (12) Placeholder in requires dependency (default-uri):
            {
                "mtad-13.yaml",  "platform-1.json", new Expectation(Expectation.Type.RESOURCE, "result-13.json"),
            },
            // (13) Placeholders in a map in resource parameters:
            {
                "mtad-14.yaml",  "platform-1.json",  new Expectation(Expectation.Type.RESOURCE, "result-14.json"),
            },
            // (14) Placeholders in a list in a map in resource parameters:
            {
                "mtad-15.yaml",  "platform-1.json",  new Expectation(Expectation.Type.RESOURCE, "result-15.json"),
            },
            // (15) Circular reference in a module parameter:
            {
                "mtad-16.yaml",  "platform-1.json",  new Expectation(Expectation.Type.EXCEPTION, "Circular reference detected in \"foo#bar#test_1\""),
            },
            // (16) Circular reference in a module parameter:
            {
                "mtad-17.yaml",  "platform-1.json",  new Expectation(Expectation.Type.EXCEPTION, "Circular reference detected in \"foo#test_1\""),
            },
            // (17) Global mta parameters to be resolved in lower scopes
            {
                "mtad-18.yaml",  "platform-1.json", new Expectation(Expectation.Type.RESOURCE, "result-16.json"),
            },
            // (18) The same placeholder occurs multiple times in the same parameter:
            {
                "mtad-with-repeating-placeholder.yaml", "platform-1.json", new Expectation(Expectation.Type.RESOURCE, "result-from-repeating-placeholder.json"),
            },
// @formatter:on
        });
    }

    @Before
    public void initialize() throws Exception {
        DescriptorParser descriptorParser = getDescriptorParser();

        DeploymentDescriptor deploymentDescriptor = getDeploymentDescriptor(descriptorParser);

        ConfigurationParser configurationParser = getConfigurationParser();

        Platform platform = getPlatform(configurationParser);

        SystemParameters systemParameters = JsonUtil.fromJson(TestUtil.getResourceAsString(SYSTEM_PARAMETERS_LOCATION, getClass()),
                                                              SystemParameters.class);

        resolver = getDescriptorPlaceholderResolver(deploymentDescriptor, platform, systemParameters);
    }

    protected DescriptorPlaceholderResolver getDescriptorPlaceholderResolver(DeploymentDescriptor deploymentDescriptor, Platform platform,
                                                                             SystemParameters systemParameters) {
        return new DescriptorPlaceholderResolver(deploymentDescriptor,
                                                 platform,
                                                 systemParameters,
                                                 new ResolverBuilder(),
                                                 new ResolverBuilder());
    }

    protected Platform getPlatform(ConfigurationParser configurationParser) {
        return (Platform) MtaTestUtil.loadPlatform(platformLocation, configurationParser, getClass());
    }

    protected ConfigurationParser getConfigurationParser() {
        return new ConfigurationParser();
    }

    protected DeploymentDescriptor getDeploymentDescriptor(DescriptorParser descriptorParser) {
        return (DeploymentDescriptor) MtaTestUtil.loadDeploymentDescriptor(deploymentDescriptorLocation, descriptorParser, getClass());
    }

    protected DescriptorParser getDescriptorParser() {
        return new DescriptorParser();
    }

    @Test
    public void testResolve() {
        TestUtil.test(new Callable<DeploymentDescriptor>() {
            @Override
            public DeploymentDescriptor call() throws Exception {
                return resolver.resolve();
            }
        }, expectation, getClass());
    }

}
