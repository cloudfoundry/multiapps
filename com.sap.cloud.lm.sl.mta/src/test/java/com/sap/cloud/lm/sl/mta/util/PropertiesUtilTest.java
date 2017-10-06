package com.sap.cloud.lm.sl.mta.util;

import java.util.Arrays;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.Callable;
import com.sap.cloud.lm.sl.common.util.JsonUtil;
import com.sap.cloud.lm.sl.common.util.TestUtil;

@RunWith(Parameterized.class)
public class PropertiesUtilTest {

    private Map<String, Object> deploymentDescriptorProperties;
    private Map<String, Object> extensionDescriptorProperties;
    private String expected;

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) Merge normal properties maps
            {
                "deployment-properties-01.json", "extension-properties-01.json", "R:merged-properties-01.json", 
            },
            // (1) No changes in the extension => merged properties should be the same
            {
                "deployment-properties-02.json", "extension-properties-02.json", "R:merged-properties-02.json", 
            },
            // (2) Merging of nested maps
            {
                "deployment-properties-03.json", "extension-properties-03.json", "R:merged-properties-03.json", 
            },
            // (3) Scalar parameter cannot be overwritten by a structured parameter
            {
                "deployment-properties-04.json", "extension-properties-04.json", "E:",
            },
            // (4) Structured parameter cannot be overwritten by a scalar parameter
            {
                "deployment-properties-05.json", "extension-properties-05.json", "E:",
            }
            
            
// @formatter:on
        });
    }

    public PropertiesUtilTest(String deploymentDescriptorPath, String extensionDescriptorPath, String expected) throws Exception {
        deploymentDescriptorProperties = JsonUtil.fromJson(TestUtil.getResourceAsString(deploymentDescriptorPath, getClass()), Map.class);
        extensionDescriptorProperties = JsonUtil.fromJson(TestUtil.getResourceAsString(extensionDescriptorPath, getClass()), Map.class);
        this.expected = expected;
    }

    @Test
    public void mergeTest() {
        TestUtil.test(new Callable<Map<String, Object>>() {

            @Override
            public Map<String, Object> call() throws Exception {
                return PropertiesUtil.mergeExtensionProperties(deploymentDescriptorProperties, extensionDescriptorProperties);
            }

        }, expected, getClass());
    }
}