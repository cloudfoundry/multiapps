package com.sap.cloud.lm.sl.mta.serialization.v1_0;

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
import com.sap.cloud.lm.sl.mta.model.v1_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.parsers.v1_0.DeploymentDescriptorParser;
import com.sap.cloud.lm.sl.mta.parsers.v1_0.ExtensionDescriptorParser;
import com.sap.cloud.lm.sl.mta.util.YamlUtil;

@RunWith(value = Parameterized.class)
public class DescriptorSerializationTest {

    protected String deploymentDescriptorLocation;
    protected String serializedDescriptorLocation;
    protected String extensionDescriptorLocation;
    protected String serializedExtensionLocation;

    private InputStream deploymentDescriptorYaml;
    private InputStream extensionDescriptorYaml;

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) Valid deployment and extension descriptors:
            {
                "mtad-00.yaml", "R:serialized-descriptor-00.json", "extension-descriptor-00.mtaext", "R:serialized-extension-00.json"
            }
            // @formatter:on
        });
    }

    public DescriptorSerializationTest(String deploymentDescriptorLocation, String serializedDescriptorLocation,
        String extensionDescriptorLocation, String serializedExtensionLocation) {
        this.deploymentDescriptorLocation = deploymentDescriptorLocation;
        this.serializedDescriptorLocation = serializedDescriptorLocation;
        this.extensionDescriptorLocation = extensionDescriptorLocation;
        this.serializedExtensionLocation = serializedExtensionLocation;
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

        }, serializedDescriptorLocation, getClass());
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

        }, serializedExtensionLocation, getClass());
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
