package com.sap.cloud.lm.sl.mta.mergers.v2_0;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.mta.handlers.v1_0.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.mergers.v1_0.PlatformMerger;
import com.sap.cloud.lm.sl.mta.model.v1_0.Platform;

@RunWith(Parameterized.class)
public class PlatformMegerTest extends com.sap.cloud.lm.sl.mta.mergers.v1_0.PlatformMergerTest {

    @Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0)
            {
                "mtad-00.yaml", "platforms-00.json", "R:result-platform-00.json",
            },
            // (1)
            {
                "mtad-01.yaml", "platforms-01.json", "R:result-platform-01.json",
            },
            // (2)
            {
                "mtad-00.yaml", "platforms-02.json", "R:result-platform-02.json",
            },
// @formatter:on
        });
    }

    public PlatformMegerTest(String deploymentDescriptorLocation, String platformTypesLocation, String expectedJsonLocation) {
        super(deploymentDescriptorLocation, platformTypesLocation, expectedJsonLocation);
    }

    protected int getMajorVersion() {
        return 2;
    }

    @Override
    protected PlatformMerger getPlatformMerger(Platform platform, DescriptorHandler handler) {
        return new com.sap.cloud.lm.sl.mta.mergers.v2_0.PlatformMerger((com.sap.cloud.lm.sl.mta.model.v2_0.Platform) platform,
            (com.sap.cloud.lm.sl.mta.handlers.v2_0.DescriptorHandler) handler);
    }
}
