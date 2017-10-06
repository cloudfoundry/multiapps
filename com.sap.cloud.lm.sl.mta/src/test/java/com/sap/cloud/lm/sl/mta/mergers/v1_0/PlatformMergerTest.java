package com.sap.cloud.lm.sl.mta.mergers.v1_0;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.Callable;
import com.sap.cloud.lm.sl.common.util.TestUtil;
import com.sap.cloud.lm.sl.mta.handlers.HandlerFactory;
import com.sap.cloud.lm.sl.mta.handlers.v1_0.ConfigurationParser;
import com.sap.cloud.lm.sl.mta.handlers.v1_0.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.handlers.v1_0.DescriptorParser;
import com.sap.cloud.lm.sl.mta.model.v1_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1_0.Platform;

@RunWith(Parameterized.class)
public class PlatformMergerTest {

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

    private String deploymentDescriptorLocation;
    private String platformsLocation;
    private String expectedJsonLocation;
    private DeploymentDescriptor descriptor;
    private List<Platform> platforms;

    public PlatformMergerTest(String deploymentDescriptorLocation, String platformsLocation, String expectedJsonLocation) {
        this.deploymentDescriptorLocation = deploymentDescriptorLocation;
        this.platformsLocation = platformsLocation;
        this.expectedJsonLocation = expectedJsonLocation;
    }

    @Before
    public void prepare() throws Exception {
        loadDeploymentDescriptor();
        loadPlatform();
    }

    private void loadPlatform() throws Exception {
        DescriptorParser parser = getHandlerFactory().getDescriptorParser();
        InputStream deploymentDescriptorYaml = getClass().getResourceAsStream(deploymentDescriptorLocation);
        this.descriptor = parser.parseDeploymentDescriptorYaml(deploymentDescriptorYaml);
    }

    private void loadDeploymentDescriptor() throws Exception {
        ConfigurationParser parser = getHandlerFactory().getConfigurationParser();
        InputStream platformsTypeJson = getClass().getResourceAsStream(platformsLocation);
        this.platforms = parser.parsePlatformsJson(platformsTypeJson);
    }

    @Test
    public void testMerge() {
        DescriptorHandler handler = getHandlerFactory().getDescriptorHandler();
        for (Platform platform : platforms) {
            final PlatformMerger merger = getPlatformMerger(platform, handler);
            TestUtil.test(new Callable<DeploymentDescriptor>() {

                @Override
                public DeploymentDescriptor call() throws Exception {
                    merger.mergeInto(descriptor);
                    return descriptor;
                }

            }, expectedJsonLocation, getClass());
        }
    }

    protected HandlerFactory getHandlerFactory() {
        return new HandlerFactory(getMajorVersion());
    }

    protected int getMajorVersion() {
        return 1;
    }

    protected PlatformMerger getPlatformMerger(Platform platform, DescriptorHandler handler) {
        return new PlatformMerger(platform, handler);
    }

}
