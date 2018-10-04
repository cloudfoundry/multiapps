package com.sap.cloud.lm.sl.mta.handlers.v1;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.Callable;
import com.sap.cloud.lm.sl.common.util.Pair;
import com.sap.cloud.lm.sl.common.util.TestUtil;
import com.sap.cloud.lm.sl.mta.MtaTestUtil;
import com.sap.cloud.lm.sl.mta.handlers.v1.DescriptorMerger;
import com.sap.cloud.lm.sl.mta.handlers.v1.DescriptorParser;
import com.sap.cloud.lm.sl.mta.model.v1.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1.ExtensionDescriptor;

@RunWith(Parameterized.class)
public class DescriptorMergerTest {

    private final String deploymentDescriptorLocation;
    private final String[] extensionDescriptorLocations;
    private final String expected;

    private DeploymentDescriptor deploymentDescriptor;
    private List<ExtensionDescriptor> extensionDescriptors;

    private DescriptorMerger merger;

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) Valid deployment and extension descriptor:
            {
                "/mta/sample/v1/mtad-01.yaml", new String[] { "/mta/sample/v1/config-01.mtaext", }, "R:/mta/sample/v1/merged-04.yaml.json",
            },
            // (1) Valid deployment and extension descriptors (multiple):
            {
                "/mta/sample/v1/mtad-01.yaml", new String[] { "/mta/sample/v1/config-01.mtaext", "/mta/sample/v1/config-05.mtaext", }, "R:/mta/sample/v1/merged-05.yaml.json",
            },
// @formatter:on
        });
    }

    public DescriptorMergerTest(String deploymentDescriptorLocation, String[] extensionDescriptorLocations, String expected) {
        this.deploymentDescriptorLocation = deploymentDescriptorLocation;
        this.extensionDescriptorLocations = extensionDescriptorLocations;
        this.expected = expected;
    }

    @Before
    public void setUp() throws Exception {
        DescriptorParser descriptorParser = getDescriptorParser();
        if (deploymentDescriptorLocation != null) {
            deploymentDescriptor = MtaTestUtil.loadDeploymentDescriptor(deploymentDescriptorLocation, descriptorParser, getClass());
        }
        if (extensionDescriptorLocations != null) {
            extensionDescriptors = MtaTestUtil.loadExtensionDescriptors(extensionDescriptorLocations, descriptorParser, getClass());
        }

        merger = createDescriptorMerger();
    }

    protected DescriptorMerger createDescriptorMerger() {
        return new DescriptorMerger();
    }

    protected DescriptorParser getDescriptorParser() {
        return new DescriptorParser();
    }

    @Test
    public void testMerge() {
        TestUtil.test(new Callable<Pair<DeploymentDescriptor, List<String>>>() {
            @Override
            public Pair<DeploymentDescriptor, List<String>> call() throws Exception {
                return merger.merge(deploymentDescriptor, extensionDescriptors);
            }
        }, expected, getClass());
    }

}
