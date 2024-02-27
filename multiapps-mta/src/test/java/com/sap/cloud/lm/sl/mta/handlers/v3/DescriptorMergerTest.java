package com.sap.cloud.lm.sl.mta.handlers.v3;

import java.util.Arrays;

import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.TestUtil.Expectation;

public class DescriptorMergerTest extends com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorMergerTest {

    public DescriptorMergerTest(String deploymentDescriptorLocation, String[] extensionDescriptorLocations, Expectation expectation) {
        super(deploymentDescriptorLocation, extensionDescriptorLocations, expectation);
    }

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) Valid deployment and extension descriptor:
            {
                "/mta/sample/v3/mtad-01.yaml", new String[] { "/mta/sample/v3/config-01.mtaext", },
                new Expectation(Expectation.Type.RESOURCE, "/mta/sample/v3/merged-04.yaml.json"),
            },
            // (1) Valid deployment and extension descriptors (multiple):
            {
                "/mta/sample/v3/mtad-01.yaml", new String[] { "/mta/sample/v3/config-01.mtaext", "/mta/sample/v3/config-05.mtaext", },
                new Expectation(Expectation.Type.RESOURCE, "/mta/sample/v3/merged-05.yaml.json"),
            },
// @formatter:on
        });
    }

    @Override
    protected DescriptorMerger createDescriptorMerger() {
        return new DescriptorMerger();
    }

    @Override
    protected DescriptorParser getDescriptorParser() {
        return new DescriptorParser();
    }

}
