package com.sap.cloud.lm.sl.mta.mergers;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.Callable;
import com.sap.cloud.lm.sl.common.util.TestUtil;
import com.sap.cloud.lm.sl.common.util.TestUtil.Expectation;
import com.sap.cloud.lm.sl.mta.handlers.ConfigurationParser;
import com.sap.cloud.lm.sl.mta.handlers.HandlerFactory;
import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorParser;
import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.Platform;

@RunWith(Parameterized.class)
public class PlatformMergerTest {

    @Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0)
            {
                "mtad-00.yaml", "platform-00.json", new Expectation(Expectation.Type.RESOURCE, "result-platform-00.json"),
            },
            // (1)
            {
                "mtad-01.yaml", "platform-01.json", new Expectation(Expectation.Type.RESOURCE, "result-platform-01.json"),
            },
            // (2)
            {
                "mtad-00.yaml", "platform-02.json", new Expectation(Expectation.Type.RESOURCE, "result-platform-02.json"),
            },
// @formatter:on
        });
    }

    private String deploymentDescriptorLocation;
    private String platformLocation;
    private Expectation expectation;
    private DeploymentDescriptor descriptor;
    private Platform platform;

    public PlatformMergerTest(String deploymentDescriptorLocation, String platformTypesLocation, Expectation expectation) {
        this.deploymentDescriptorLocation = deploymentDescriptorLocation;
        this.platformLocation = platformTypesLocation;
        this.expectation = expectation;
    }

    protected int getMajorVersion() {
        return 2;
    }

    protected PlatformMerger getPlatformMerger(Platform platform, DescriptorHandler handler) {
        return new PlatformMerger(platform, handler);
    }

    @Before
    public void prepare() throws Exception {
        loadDeploymentDescriptor();
        loadPlatform();
    }

    private void loadDeploymentDescriptor() throws Exception {
        DescriptorParser parser = getHandlerFactory().getDescriptorParser();
        InputStream deploymentDescriptorYaml = getClass().getResourceAsStream(deploymentDescriptorLocation);
        this.descriptor = parser.parseDeploymentDescriptorYaml(deploymentDescriptorYaml);
    }

    private void loadPlatform() throws Exception {
        InputStream platformJson = getClass().getResourceAsStream(platformLocation);
        this.platform = new ConfigurationParser().parsePlatformJson(platformJson);
    }

    @Test
    public void testMerge() {
        DescriptorHandler handler = getHandlerFactory().getDescriptorHandler();
        PlatformMerger merger = getPlatformMerger(platform, handler);
        TestUtil.test(new Callable<DeploymentDescriptor>() {

            @Override
            public DeploymentDescriptor call() throws Exception {
                merger.mergeInto(descriptor);
                return descriptor;
            }

        }, expectation, getClass());
    }

    protected HandlerFactory getHandlerFactory() {
        return new HandlerFactory(getMajorVersion());
    }

}
