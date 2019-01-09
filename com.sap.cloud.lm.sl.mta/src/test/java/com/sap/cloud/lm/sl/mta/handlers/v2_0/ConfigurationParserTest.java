package com.sap.cloud.lm.sl.mta.handlers.v2_0;

import java.util.Arrays;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class ConfigurationParserTest extends com.sap.cloud.lm.sl.mta.handlers.v1_0.ConfigurationParserTest {

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) Valid JSONs:
            {
                "/mta/sample/v2_0/platforms-01.json", "/mta/sample/v2_0/targets-01.json", new String[] { "R:platforms-01.json.json", "R:targets-01.json.json" },
            },
            // (1) Invalid JSONs (platform and platform type names are not unique):
            {
                "/mta/sample/v2_0/platforms-03.json", "/mta/sample/v2_0/targets-03.json", new String[] { "E:Value \"CLOUD-FOUNDRY\" for key \"name\" not unique for object \"MTA platform type\"", "E:Value \"CF-QUAL\" for key \"name\" not unique for object \"MTA platform\"" },
            },
            // (2) Valid JSONs (containing only names):
            {
                "/mta/sample/v2_0/platforms-04.json", "/mta/sample/v2_0/targets-04.json", new String[] { "R:platforms-04.json.json", "R:targets-04.json.json" },
            },
// @formatter:on
        });
    }

    public ConfigurationParserTest(String platformsLocation, String targetsLocation, String[] expected) {
        super(platformsLocation, targetsLocation, expected);
    }

    @Override
    protected ConfigurationParser createConfigurationParser() {
        return new ConfigurationParser();
    }

}
