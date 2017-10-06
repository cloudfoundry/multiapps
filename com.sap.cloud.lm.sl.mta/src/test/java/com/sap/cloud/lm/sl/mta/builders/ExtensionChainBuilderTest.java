package com.sap.cloud.lm.sl.mta.builders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.Callable;
import com.sap.cloud.lm.sl.common.util.TestUtil;
import com.sap.cloud.lm.sl.mta.MtaTestUtil;
import com.sap.cloud.lm.sl.mta.handlers.v1_0.DescriptorParser;
import com.sap.cloud.lm.sl.mta.model.v1_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionDescriptor;

@RunWith(Parameterized.class)
public class ExtensionChainBuilderTest {

    private final String deploymentDescriptorLocation;
    private final String[] extensionDescriptorLocations;
    private final String expected;
    private ExtensionChainBuilder<ExtensionDescriptor> builder;

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) Extension descriptors form a valid chain:
            {
                "/mta/sample/v1_0/mtad-01.yaml", new String[] { "/mta/sample/v1_0/config-01.mtaext", "/mta/sample/v1_0/config-05.mtaext", }, "R:descriptor-chain-01.json",
            },
            // (1) Extension descriptor has an unknown parent descriptor:
            {
                "/mta/sample/v1_0/mtad-01.yaml", new String[] { "/mta/sample/v1_0/config-06.mtaext", }, "E:Unknown parent descriptor \"com.sap.mta.samplex\" in extension descriptor \"com.sap.mta.sample.config-06\"",
            },
            // (2) Extension descriptors do not form a valid chain (both extend the deployment descriptor):
            {
                "/mta/sample/v1_0/mtad-01.yaml", new String[] { "/mta/sample/v1_0/config-01.mtaext", "/mta/sample/v1_0/config-08.mtaext", }, "E:Cannot build extension descriptor chain, last valid extension descriptor is \"com.sap.mta.sample.config-01\"",
            },
// @formatter:on
        });
    }

    public ExtensionChainBuilderTest(String deploymentDescriptorLocation, String[] extensionDescriptorLocations, String expected) {
        this.deploymentDescriptorLocation = deploymentDescriptorLocation;
        this.extensionDescriptorLocations = extensionDescriptorLocations;
        this.expected = expected;
    }

    @Before
    public void setUp() throws Exception {
        DescriptorParser parser = new DescriptorParser();

        DeploymentDescriptor deploymentDescriptor = MtaTestUtil.loadDeploymentDescriptor(deploymentDescriptorLocation, parser, getClass());
        List<ExtensionDescriptor> extensionDescriptors = MtaTestUtil.loadExtensionDescriptors(extensionDescriptorLocations, parser,
            getClass());

        builder = new ExtensionChainBuilder<>(deploymentDescriptor, extensionDescriptors);
    }

    @Test
    public void testBuild() {
        TestUtil.test(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                List<String> descriptorIds = new ArrayList<String>();
                for (ExtensionDescriptor descriptor : builder.build()) {
                    descriptorIds.add(descriptor.getId());
                }
                return descriptorIds;
            }
        }, expected, getClass());
    }

}
