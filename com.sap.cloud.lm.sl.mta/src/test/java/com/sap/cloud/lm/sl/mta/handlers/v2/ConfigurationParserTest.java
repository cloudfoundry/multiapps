package com.sap.cloud.lm.sl.mta.handlers.v2;

import java.util.Arrays;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.TestUtil.Expectation;

@RunWith(value = Parameterized.class)
public class ConfigurationParserTest extends com.sap.cloud.lm.sl.mta.handlers.v1.ConfigurationParserTest {

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) Valid JSONs:
            {
                "/mta/sample/v2/platforms-01.json", "/mta/sample/v2/targets-01.json",
                new Expectation[] {
                    new Expectation(Expectation.Type.RESOURCE, "platforms-01.json.json"),
                    new Expectation(Expectation.Type.RESOURCE, "targets-01.json.json"),
                },
            },
            // (1) Invalid JSONs (invalid key 'properties' in platform resource type and resource type objects):
            {
                "/mta/sample/v2/platforms-02.json", "/mta/sample/v2/targets-02.json",
                new Expectation[] {
                    new Expectation(Expectation.Type.RESOURCE, "platforms-02.json.json"),
                    new Expectation(Expectation.Type.RESOURCE, "targets-02.json.json"),
                },
            },
            // (2) Invalid JSONs (platform and platform type names are not unique):
            {
                "/mta/sample/v2/platforms-03.json", "/mta/sample/v2/targets-03.json",
                new Expectation[] {
                    new Expectation(Expectation.Type.EXCEPTION, "Value \"CLOUD-FOUNDRY\" for key \"name\" not unique for object \"MTA platform type\""),
                    new Expectation(Expectation.Type.EXCEPTION, "Value \"CF-QUAL\" for key \"name\" not unique for object \"MTA platform\""),
                },
            },
            // (3) Valid JSONs (containing only names):
            {
                "/mta/sample/v2/platforms-04.json", "/mta/sample/v2/targets-04.json",
                new Expectation[] {
                    new Expectation(Expectation.Type.RESOURCE, "platforms-04.json.json"),
                    new Expectation(Expectation.Type.RESOURCE, "targets-04.json.json"),
                },
            },
// @formatter:on
        });
    }

    public ConfigurationParserTest(String platformsLocation, String targetsLocation, Expectation[] expected) {
        super(platformsLocation, targetsLocation, expected);
    }

    @Override
    protected ConfigurationParser createConfigurationParser() {
        return new ConfigurationParser();
    }

}
