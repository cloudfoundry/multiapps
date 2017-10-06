package com.sap.cloud.lm.sl.mta.builders.v2_0;

import java.util.Arrays;

import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.mta.builders.v2_0.ParametersChainBuilder;
import com.sap.cloud.lm.sl.mta.builders.v2_0.PropertiesChainBuilder;
import com.sap.cloud.lm.sl.mta.handlers.v2_0.ConfigurationParser;
import com.sap.cloud.lm.sl.mta.handlers.v2_0.DescriptorParser;
import com.sap.cloud.lm.sl.mta.model.v2_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2_0.Target;
import com.sap.cloud.lm.sl.mta.model.v2_0.Platform;

public class ParametersChainBuilderTest extends com.sap.cloud.lm.sl.mta.builders.v1_0.PropertiesChainBuilderTest {

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) All module and resource types are present in target and platform:
            {
                "mtad-02.yaml", "platform-04.json", "target-04.json", new String[] { "R:module-chain-01.json", "R:module-chain-without-dependencies-01.json", "R:resource-chain-04.json", "R:resource-type-chain-01.json", },
            },
            // (1) No module and resource types in target and platform:
            {
                "mtad-02.yaml", "platform-02.json", "target-02.json", new String[] { "R:module-chain-02.json", "R:module-chain-without-dependencies-02.json", "R:resource-chain-02.json", "R:resource-type-chain-02.json", },
            },
            // (2) Some module and resource types are present in target and platform:
            {
                "mtad-02.yaml", "platform-05.json", "target-05.json", new String[] { "R:module-chain-03.json", "R:module-chain-without-dependencies-03.json", "R:resource-chain-05.json", "R:resource-type-chain-03.json", },
            },
// @formatter:on
        });
    }

    public ParametersChainBuilderTest(String deploymentDescriptorLocation, String platformLocation, String targetLocation,
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
    protected PropertiesChainBuilder createPropertiesChainBuilder(
        com.sap.cloud.lm.sl.mta.model.v1_0.DeploymentDescriptor deploymentDescriptor, com.sap.cloud.lm.sl.mta.model.v1_0.Platform platform,
        com.sap.cloud.lm.sl.mta.model.v1_0.Target target) {
        return new ParametersChainBuilder((DeploymentDescriptor) deploymentDescriptor, (Target) target, (Platform) platform);
    }

}
