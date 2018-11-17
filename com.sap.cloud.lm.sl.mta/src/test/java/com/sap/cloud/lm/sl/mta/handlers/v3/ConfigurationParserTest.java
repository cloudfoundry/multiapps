package com.sap.cloud.lm.sl.mta.handlers.v3;

import java.util.Arrays;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.TestUtil.Expectation;

@RunWith(Parameterized.class)
public class ConfigurationParserTest extends com.sap.cloud.lm.sl.mta.handlers.v2.ConfigurationParserTest {

    public ConfigurationParserTest(String platformTypesLocation, String platformsLocation, Expectation[] expectations) {
        super(platformTypesLocation, platformsLocation, expectations);
    }

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            {
                "platforms-00.json", "targets-00.json",
                new Expectation[] {
                    new Expectation(Expectation.Type.RESOURCE, "parsed-platforms-00.json"),
                    new Expectation(Expectation.Type.RESOURCE, "parsed-targets-00.json"),
                },
            }
// @formatter:on
        });
    }

    @Override
    protected ConfigurationParser createConfigurationParser() {
        return new ConfigurationParser();
    }

}
