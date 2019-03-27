package com.sap.cloud.lm.sl.mta.handlers;

import java.io.InputStream;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.Callable;
import com.sap.cloud.lm.sl.common.util.TestUtil;
import com.sap.cloud.lm.sl.common.util.TestUtil.Expectation;
import com.sap.cloud.lm.sl.mta.handlers.ConfigurationParser;
import com.sap.cloud.lm.sl.mta.model.Platform;

@RunWith(Parameterized.class)
public class ConfigurationParserTest {

    private final String platformLocation;
    private InputStream platformInputStream;

    private final Expectation expectation;

    private ConfigurationParser parser = new ConfigurationParser();

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) Valid JSON:
            {
                "/mta/sample/platform-01.json", new Expectation(Expectation.Type.RESOURCE, "platform-01.json.json"),
            },
            // (1) Invalid JSON (invalid key 'properties' in resource types):
            {
                "/mta/sample/platform-02.json", new Expectation(Expectation.Type.RESOURCE, "platform-02.json.json"),
            },
            // (3) Valid JSON (containing only a name):
            {
                "/mta/sample/platform-03.json", new Expectation(Expectation.Type.RESOURCE, "platform-03.json.json"),
            },
// @formatter:on
        });
    }

    public ConfigurationParserTest(String platformsLocation, Expectation expectation) {
        this.platformLocation = platformsLocation;
        this.expectation = expectation;
    }

    @Before
    public void setUp() throws Exception {
        if (platformLocation != null) {
            platformInputStream = getClass().getResourceAsStream(platformLocation);
        }
    }

    @Test
    public void testParsePlatformsJson() throws Exception {
        TestUtil.test(new Callable<Platform>() {
            @Override
            public Platform call() throws Exception {
                return parser.parsePlatformJson(platformInputStream);
            }
        }, expectation, getClass());
    }

}
