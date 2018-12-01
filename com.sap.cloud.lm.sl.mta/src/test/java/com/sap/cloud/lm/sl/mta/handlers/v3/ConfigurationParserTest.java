package com.sap.cloud.lm.sl.mta.handlers.v3;

import java.util.Arrays;

import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.TestUtil.Expectation;

public class ConfigurationParserTest extends com.sap.cloud.lm.sl.mta.handlers.v2.ConfigurationParserTest {

    public ConfigurationParserTest(String platformsLocation, Expectation expectation) {
        super(platformsLocation, expectation);
    }

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            {
                "platform-00.json", new Expectation(Expectation.Type.RESOURCE, "parsed-platform-00.json"),
            }
// @formatter:on
        });
    }

    @Override
    protected ConfigurationParser createConfigurationParser() {
        return new ConfigurationParser();
    }

}
