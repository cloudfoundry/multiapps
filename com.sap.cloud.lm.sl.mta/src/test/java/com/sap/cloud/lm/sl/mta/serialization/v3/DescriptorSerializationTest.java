package com.sap.cloud.lm.sl.mta.serialization.v3;

import java.util.Arrays;
import java.util.Map;

import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.Tester.Expectation;
import com.sap.cloud.lm.sl.mta.parsers.v3.DeploymentDescriptorParser;
import com.sap.cloud.lm.sl.mta.parsers.v3.ExtensionDescriptorParser;

public class DescriptorSerializationTest extends com.sap.cloud.lm.sl.mta.serialization.v2.DescriptorSerializationTest {

    protected String deploymentDescriptorLocation;
    protected String serializedDescriptorLocation;

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) Valid deployment and extension descriptors:
            {
                "mtad-00.yaml", new Expectation(Expectation.Type.JSON, "serialized-descriptor-00.json"),
                "extension-descriptor-00.mtaext", new Expectation(Expectation.Type.JSON, "serialized-extension-00.json")
            },
            // (1)
            {
                "mtad-01.yaml", new Expectation(Expectation.Type.JSON, "serialized-descriptor-01.json"),
                "extension-descriptor-01.mtaext", new Expectation(Expectation.Type.JSON, "serialized-extension-01.json")
            },
// @formatter:on
        });
    }

    public DescriptorSerializationTest(String deploymentDescriptorLocation, Expectation expectedSerializedDescriptor,
                                       String extensionDescriptorLocation, Expectation expectedSerializedExtension) {
        super(deploymentDescriptorLocation, expectedSerializedDescriptor, extensionDescriptorLocation, expectedSerializedExtension);
    }

    @Override
    protected DeploymentDescriptorParser getDescriptorParser(Map<String, Object> yamlMap) {
        return new DeploymentDescriptorParser(yamlMap);
    }

    @Override
    protected ExtensionDescriptorParser getExtensionDescriptorParser(Map<String, Object> yamlMap) {
        return new ExtensionDescriptorParser(yamlMap);
    }

}
