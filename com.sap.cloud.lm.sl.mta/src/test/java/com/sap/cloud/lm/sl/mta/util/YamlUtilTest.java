package com.sap.cloud.lm.sl.mta.util;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.Callable;
import com.sap.cloud.lm.sl.common.util.TestUtil;

@RunWith(Parameterized.class)
public class YamlUtilTest {

    protected String deploymentDescriptorLocation;
    protected static String expectedResult;
    protected static String expectedExceptionMsgFromParsingFromInputStream = "E:Error while parsing YAML stream";
    protected static String expectedExceptionMsgFromParsingFromString = "E:null; could not determine a constructor for the tag tag:yaml.org,2002:javax.script.ScriptEngineManager";

    protected static String expectedExceptionCauseFromParsingFromInputStream = "E:could not determine a constructor for the tag tag:yaml.org,2002:javax.script.ScriptEngineManager\n"
        + " in \"<reader>\", line 10, column 17:\n" + "         instances: !!javax.script.ScriptEngineManager [\n"
        + "                    ^";
    protected static String expectedExceptionCauseFromParsingFromString = "E:could not determine a constructor for the tag tag:yaml.org,2002:javax.script.ScriptEngineManager\n"
        + " in \"<string>\", line 10, column 17:\n" + "         instances: !!javax.script.ScriptEngineManager [\n"
        + "                    ^";

    protected static String[] expectedExceptionMsgs = new String[] { expectedExceptionMsgFromParsingFromInputStream,
        expectedExceptionMsgFromParsingFromString };
    protected static String[] expectedExceptionCauses = new String[] { expectedExceptionCauseFromParsingFromInputStream,
        expectedExceptionCauseFromParsingFromString };

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) Correct deployment descriptor
            {
                "mtad-01.yaml", "R:expected-map-01.json", new String[] {"", ""}, new String[] {"",""}
            },
            // (1) Deployment descriptor with security violation
            {
                "mtad-02.yaml", "", expectedExceptionMsgs, expectedExceptionCauses
            },
// @formatter:on
        });
    }

    @SuppressWarnings("static-access")
    public YamlUtilTest(String deploymentDescriptorLocation, String expectedResult, String[] expectedExceptionMsgs,
        String[] expectedExceptionCauses) throws Exception {
        this.deploymentDescriptorLocation = deploymentDescriptorLocation;
        this.expectedResult = expectedResult;
        this.expectedExceptionMsgs = expectedExceptionMsgs;
        this.expectedExceptionCauses = expectedExceptionCauses;
    }

    @Test
    public void testConvertYamlInputStreamToMap() {
        TestUtil.test(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                InputStream inputStream = null;
                try {
                    inputStream = getClass().getResourceAsStream(deploymentDescriptorLocation);
                    Map<String, Object> map;
                    map = YamlUtil.convertYamlToMap(inputStream);
                    return map;
                } finally {
                    IOUtils.closeQuietly(inputStream);
                }
            }
        }, expectedResult, expectedExceptionMsgs[0], expectedExceptionCauses[0], getClass());
    }

    @Test
    public void testConvertYamlStringToMap() {
        TestUtil.test(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                InputStream inputStream = null;
                try {
                    inputStream = getClass().getResourceAsStream(deploymentDescriptorLocation);
                    String deploymentDescriptor = IOUtils.toString(inputStream);
                    Map<String, Object> map;
                    map = YamlUtil.convertYamlToMap(deploymentDescriptor);
                    return map;
                } finally {
                    IOUtils.closeQuietly(inputStream);
                }
            }
        }, expectedResult, expectedExceptionMsgs[1], expectedExceptionCauses[1], getClass());
    }
}
