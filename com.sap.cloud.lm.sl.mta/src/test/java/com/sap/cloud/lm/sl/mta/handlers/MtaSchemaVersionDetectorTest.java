package com.sap.cloud.lm.sl.mta.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.Callable;
import com.sap.cloud.lm.sl.common.util.TestUtil;

@RunWith(Parameterized.class)
public class MtaSchemaVersionDetectorTest {

    private final TestInput input;
    private final String expected;

    private String deploymentDescriptorString;
    private List<String> extensionDescriptorStrings;

    public MtaSchemaVersionDetectorTest(TestInput input, String expected) {
        this.input = input;
        this.expected = expected;
    }

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) All descriptors are of version 1 (implicit):
            {
                new TestInput("mtad-06.yaml", Arrays.asList(new String[] { "config-05.mtaext", "config-06.mtaext", })), "1.0.0",
            },
            // (1) All descriptors are of version 1 (explicit):
            {
                new TestInput("mtad-07.yaml", Arrays.asList(new String[] { "config-05.mtaext", "config-06.mtaext", })), "1.0.0",
            },
            // (2) All descriptors are of version 2 (explicit):
            {
                new TestInput("mtad-08.yaml", Arrays.asList(new String[] { "config-07.mtaext", "config-08.mtaext", })), "2.0.0",
            },
            // (3) Mixed versions:
            {
                new TestInput("mtad-08.yaml", Arrays.asList(new String[] { "config-05.mtaext", "config-06.mtaext", })), "E:Versions \"2.0.0\" and \"1.0.0\" are incompatible",
            },
            // (4) A descriptor has an unknown version:
            {
                new TestInput("mtad-09.yaml", Arrays.asList(new String[] { "config-05.mtaext", "config-06.mtaext", })), "E:Versions \"9.0.0\" and \"1.0.0\" are incompatible",
            },
            // (5) A descriptor has an integer version:
            {
                new TestInput("mtad-10.yaml", Collections.<String> emptyList()), "3.2.0",
            },
            // (6) A descriptor has an list of versions:
            {
                new TestInput("mtad-11.yaml", Collections.<String> emptyList()), "E:Unable to parse version \"[2.0, 2.1]\"",
            },
            // (7) A descriptor has an double version:
            {
                new TestInput("mtad-12.yaml", Collections.<String> emptyList()), "2.1.0",
            },
// @formatter:on
        });
    }

    @Before
    public void loadDescriptors() throws Exception {
        deploymentDescriptorString = TestUtil.getResourceAsString(input.getDeploymentDescriptorLocation(), getClass());

        extensionDescriptorStrings = new ArrayList<String>();
        for (String location : input.getExtensionDescriptorLocations()) {
            String extensionDescriptorString = TestUtil.getResourceAsString(location, getClass());
            extensionDescriptorStrings.add(extensionDescriptorString);
        }
    }

    @Test
    public void testExecute() {
        TestUtil.test(new Callable<String>() {

            @Override
            public String call() throws Exception {
                MtaSchemaVersionDetector detector = new MtaSchemaVersionDetector();

                return detector.detect(deploymentDescriptorString, extensionDescriptorStrings)
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
