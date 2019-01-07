package com.sap.cloud.lm.sl.mta.handlers.v1;

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
import com.sap.cloud.lm.sl.mta.model.v1.Platform;

@RunWith(Parameterized.class)
public class ConfigurationParserTest {

    private final String platformLocation;
    private InputStream platformInputStream;

    private final Expectation expectation;

    private ConfigurationParser parser;

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) Valid JSON:
            { 
                "/mta/sample/v1/platform-01.json", new Expectation(Expectation.Type.RESOURCE, "platform-01.json.json"),
            },
            // (1) Invalid JSON (invalid key 'parameters' in module types):
            {
                "/mta/sample/v1/platform-02.json", new Expectation(Expectation.Type.EXCEPTION, "Invalid key \"module-types#0#parameters\""),
            },
            // (2) Invalid JSON (invalid key 'parameters' in platform):
            {
                "/mta/sample/v1/platform-03.json", new Expectation(Expectation.Type.EXCEPTION, "Invalid key \"parameters\""),
            },
            // (3) Valid JSON (containing only a name):
            {
                "/mta/sample/v1/platform-04.json", new Expectation(Expectation.Type.RESOURCE, "platform-04.json.json"),
            },
// @formatter:on
        });
    }

    public ConfigurationParserTest(String platformLocation, Expectation expectation) {
        this.platformLocation = platformLocation;
        this.expectation = expectation;
    }

    @Before
    public void setUp() throws Exception {
        if (platformLocation != null) {
            platformInputStream = getClass().getResourceAsStream(platformLocation);
        }
        parser = createConfigurationParser();
    }

    protected ConfigurationParser createConfigurationParser() {
        return new ConfigurationParser();
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
