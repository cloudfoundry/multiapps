package com.sap.cloud.lm.sl.mta.handlers;

import java.io.IOException;
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
import com.sap.cloud.lm.sl.mta.model.v1_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionDescriptor;

@RunWith(Parameterized.class)
public class SchemaVersionDetectorTest {

    private final TestInput input;
    private final String expected;

    private DeploymentDescriptor deploymentDescriptor;
    private List<ExtensionDescriptor> extensionDescriptors = new ArrayList<>();;

    public SchemaVersionDetectorTest(TestInput input, String expected) {
        this.input = input;
        this.expected = expected;
    }

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) All descriptors are of version 1 (explicit):
            {
                new TestInput("mtad-07.yaml", Arrays.asList(new String[] { "config-05.mtaext", "config-06.mtaext", })), "1.0.0",
            },
            // (1) All descriptors are of version 2 (explicit):
            {
                new TestInput("mtad-08.yaml", Arrays.asList(new String[] { "config-07.mtaext", "config-08.mtaext", })), "2.1.0",
            },
            // (2) Mixed versions:
            {
                new TestInput("mtad-08.yaml", Arrays.asList(new String[] { "config-05.mtaext", "config-06.mtaext", })), "E:Extension descriptors must have the same major schema version as the deployment descriptor, but the following do not: com.sap.mta.test.config-05,com.sap.mta.test.config-06",
            },
// @formatter:on
        });
    }

    @Before
    public void loadDescriptors() throws IOException {
        DescriptorParserFacade descriptorParserFacade = new DescriptorParserFacade();
        String deploymentDescriptorString = TestUtil.getResourceAsString(input.getDeploymentDescriptorLocation(), getClass());
        deploymentDescriptor = descriptorParserFacade.parseDeploymentDescriptor(deploymentDescriptorString);

        for (String location : input.getExtensionDescriptorLocations()) {
            String extensionDescriptorString = TestUtil.getResourceAsString(location, getClass());
            extensionDescriptors.add(descriptorParserFacade.parseExtensionDescriptor(extensionDescriptorString));
        }

    }

    @Test
    public void testExecute() {
        TestUtil.test(new Callable<String>() {

            @Override
            public String call() throws Exception {
                SchemaVersionDetector detector = new SchemaVersionDetector();

                return detector.detect(deploymentDescriptor, extensionDescriptors)
                    .toString();
            }

        }, expected, getClass(), false);
    }

    private static class TestInput {

        private String deploymentDescriptorLocation;
        private List<String> extensionDescriptorLocations;

        public TestInput(String deploymentDescriptorLocation, List<String> extensionDescriptorLocations) {
            this.deploymentDescriptorLocation = deploymentDescriptorLocation;
            this.extensionDescriptorLocations = extensionDescriptorLocations;
        }

        public List<String> getExtensionDescriptorLocations() {
            return extensionDescriptorLocations;
        }

        public String getDeploymentDescriptorLocation() {
            return deploymentDescriptorLocation;
        }

    }

}
