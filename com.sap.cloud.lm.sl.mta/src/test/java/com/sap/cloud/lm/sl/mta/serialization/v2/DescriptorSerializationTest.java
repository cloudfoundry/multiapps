package com.sap.cloud.lm.sl.mta.serialization.v2;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.Callable;
import com.sap.cloud.lm.sl.common.util.TestUtil;
import com.sap.cloud.lm.sl.common.util.YamlUtil;
import com.sap.cloud.lm.sl.common.util.TestUtil.Expectation;
import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.parsers.v2.DeploymentDescriptorParser;
import com.sap.cloud.lm.sl.mta.parsers.v2.ExtensionDescriptorParser;

@RunWith(Parameterized.class)
public class DescriptorSerializationTest {
    
    protected String deploymentDescriptorLocation;
    protected Expectation expectedSerializedDescriptor;
    protected String extensionDescriptorLocation;
    protected Expectation expectedSerializedExtension;

    private InputStream deploymentDescriptorYaml;
    private InputStream extensionDescriptorYaml;

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) Valid deployment and extension descriptors:
            {
                "mtad-00.yaml", new Expectation(Expectation.Type.RESOURCE, "serialized-descriptor-00.json"),
                "extension-descriptor-00.mtaext", new Expectation(Expectation.Type.RESOURCE, "serialized-extension-00.json"),
            }
            // @formatter:on
        });
    }

    public DescriptorSerializationTest(String deploymentDescriptorLocation, Expectation expectedSerializedDescriptor,
        String extensionDescriptorLocation, Expectation expectedSerializedExtension) {
        this.deploymentDescriptorLocation = deploymentDescriptorLocation;
        this.expectedSerializedDescriptor = expectedSerializedDescriptor;
        this.extensionDescriptorLocation = extensionDescriptorLocation;
        this.expectedSerializedExtension = expectedSerializedExtension;
    }

    @Before
    public void setUp() {
        deploymentDescriptorYaml = getClass().getResourceAsStream(deploymentDescriptorLocation);
        extensionDescriptorYaml = getClass().getResourceAsStream(extensionDescriptorLocation);
    }
    
    @Test
    public void testDescriptorSerialization() {
        TestUtil.test(new Callable<DeploymentDescriptor>() {

            @Override
            public DeploymentDescriptor call() {
                Map<String, Object> map = YamlUtil.convertYamlToMap(deploymentDescriptorYaml);
                String serializedMap = YamlUtil.convertToYaml(getDescriptorFromMap(map));
                return getDescriptorFromMap(YamlUtil.convertYamlToMap(serializedMap));
            }

        }, expectedSerializedDescriptor, getClass());
    }

    @Test
    public void testExtensionSerialization() {
        TestUtil.test(new Callable<ExtensionDescriptor>() {

            @Override
            public ExtensionDescriptor call() {
                Map<String, Object> map = YamlUtil.convertYamlToMap(extensionDescriptorYaml);
                String serializedMap = YamlUtil.convertToYaml(getExtensionDescriptorFromMap(map));
                return getExtensionDescriptorFromMap(YamlUtil.convertYamlToMap(serializedMap));
            }

        }, expectedSerializedExtension, getClass());
    }

    private DeploymentDescriptor getDescriptorFromMap(Map<String, Object> yamlMap) {
        return getDescriptorParser(yamlMap).parse();
    }
    
    protected DeploymentDescriptorParser getDescriptorParser(Map<String, Object> yamlMap) {
        return new DeploymentDescriptorParser(yamlMap);
    }
    
    private ExtensionDescriptor getExtensionDescriptorFromMap(Map<String, Object> yamlMap) {
        return getExtensionDescriptorParser(yamlMap).parse();
    }

    protected ExtensionDescriptorParser getExtensionDescriptorParser(Map<String, Object> yamlMap) {
        return new ExtensionDescriptorParser(yamlMap);
    }

}
