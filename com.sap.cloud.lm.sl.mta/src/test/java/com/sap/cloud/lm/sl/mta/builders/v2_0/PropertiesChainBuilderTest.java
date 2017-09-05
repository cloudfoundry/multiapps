package com.sap.cloud.lm.sl.mta.builders.v2_0;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.mta.builders.v2_0.PropertiesChainBuilder;
import com.sap.cloud.lm.sl.mta.handlers.v2_0.ConfigurationParser;
import com.sap.cloud.lm.sl.mta.handlers.v2_0.DescriptorParser;
import com.sap.cloud.lm.sl.mta.model.v2_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2_0.Target;
import com.sap.cloud.lm.sl.mta.model.v2_0.Platform;

@RunWith(Parameterized.class)
public class PropertiesChainBuilderTest extends com.sap.cloud.lm.sl.mta.builders.v1_0.PropertiesChainBuilderTest {

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(
            new Object[][] {
// @formatter:off
            // (0) All module and resource types are present in target and platform:
            {
                "mtad-01.yaml", "platform-01.json", "target-01.json", new String[] { "R:module-chain-04.json", "R:module-chain-without-dependencies-04.json", "R:resource-chain-01.json", },
            },
            // (1) No module and resource types in target and platform:
            {
                "mtad-01.yaml", "platform-02.json", "target-02.json", new String[] { "R:module-chain-05.json", "R:module-chain-without-dependencies-05.json", "R:resource-chain-06.json", },
            },
            // (2) Some module and resource types are present in target and platform:
            {
                "mtad-01.yaml", "platform-03.json", "target-03.json", new String[] { "R:module-chain-06.json", "R:module-chain-without-dependencies-06.json", "R:resource-chain-03.json", },
            },
// @formatter:on
        });
    }

    public PropertiesChainBuilderTest(String deploymentDescriptorLocation, String platformLocation, String targetLocation,
        String[] expected) {
        super(deploymentDescriptorLocation, platformLocation, targetLocation, expected);
    }

    @Override
    protected ConfigurationParser getConfigurationParser() {
        return new ConfigurationParser();
    }

    @Override
    protected DescriptorParser getDescriptorParser() {
        return new DescriptorParser();
    }

    @Override
    protected PropertiesChainBuilder createPropertiesChainBuilder(com.sap.cloud.lm.sl.mta.model.v1_0.DeploymentDescriptor deploymentDescriptor,
        com.sap.cloud.lm.sl.mta.model.v1_0.Platform platform, com.sap.cloud.lm.sl.mta.model.v1_0.Target target) {
        return new PropertiesChainBuilder((DeploymentDescriptor) deploymentDescriptor, (Target) target,
            (Platform) platform);
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void testBuildResourceTypeChain() {
        for (String resourceName : resourceNames) {
            builder.buildResourceTypeChain(resourceName);
        }
    }

}
