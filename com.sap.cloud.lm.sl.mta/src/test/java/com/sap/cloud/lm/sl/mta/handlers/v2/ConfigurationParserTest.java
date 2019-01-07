package com.sap.cloud.lm.sl.mta.handlers.v2;

import java.util.Arrays;

import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.TestUtil.Expectation;

public class ConfigurationParserTest extends com.sap.cloud.lm.sl.mta.handlers.v1.ConfigurationParserTest {

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) Valid JSON:
            {
                "/mta/sample/v2/platform-01.json", new Expectation(Expectation.Type.RESOURCE, "platform-01.json.json"),
            },
            // (1) Invalid JSON (invalid key 'properties' in resource types):
            {
                "/mta/sample/v2/platform-02.json", new Expectation(Expectation.Type.EXCEPTION, "Invalid key \"resource-types#0#properties\""),
            },
            // (3) Valid JSON (containing only a name):
            {
                "/mta/sample/v2/platform-03.json", new Expectation(Expectation.Type.RESOURCE, "platform-03.json.json"),
            },
// @formatter:on
        });
    }

    public ConfigurationParserTest(String platformsLocation, Expectation expectation) {
        super(platformsLocation, expectation);
    }

    @Override
    protected ConfigurationParser createConfigurationParser() {
        return new ConfigurationParser();
    }

}
