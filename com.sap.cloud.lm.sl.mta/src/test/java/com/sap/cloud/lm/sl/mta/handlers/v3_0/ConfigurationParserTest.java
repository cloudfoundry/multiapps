package com.sap.cloud.lm.sl.mta.handlers.v3_0;

import java.util.Arrays;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ConfigurationParserTest extends com.sap.cloud.lm.sl.mta.handlers.v2_0.ConfigurationParserTest {

    public ConfigurationParserTest(String platformTypesLocation, String platformsLocation, String[] expected) {
        super(platformTypesLocation, platformsLocation, expected);
    }

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            {
                "platforms-00.json", "targets-00.json", new String[] { "R:parsed-platforms-00.json", "R:parsed-targets-00.json", },
            }
// @formatter:on
        });
    }

    @Override
    protected ConfigurationParser createConfigurationParser() {
        return new ConfigurationParser();
    }

}
