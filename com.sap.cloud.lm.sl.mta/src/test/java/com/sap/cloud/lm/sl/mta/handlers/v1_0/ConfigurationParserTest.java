package com.sap.cloud.lm.sl.mta.handlers.v1_0;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.Callable;
import com.sap.cloud.lm.sl.common.util.TestUtil;
import com.sap.cloud.lm.sl.mta.model.v1_0.Platform;
import com.sap.cloud.lm.sl.mta.model.v1_0.Target;

@RunWith(Parameterized.class)
public class ConfigurationParserTest {

    private final String platformsLocation;
    private InputStream platformsInputStream;
    private final String targetsLocation;
    private InputStream targetsInputStream;

    private final String[] expected;

    private ConfigurationParser parser;

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) Valid JSONs:
            { 
                "/mta/sample/v1_0/platforms-01.json", "/mta/sample/v1_0/targets-01.json", new String[] { "R:platforms-01.json.json", "R:targets-01.json.json" },
            },
            // (1) Invalid JSONs (invalid key 'parameters' in platform module type and module type objects):
            {
                "/mta/sample/v1_0/platforms-02.json", "/mta/sample/v1_0/targets-02.json", new String[] { "E:Invalid key \"0#module-types#0#parameters\"", "E:Invalid key \"1#module-types#0#parameters\"" },
            },
            // (2) Invalid JSONs (invalid key 'parameters' in target and platform objects):
            {
                "/mta/sample/v1_0/platforms-03.json", "/mta/sample/v1_0/targets-03.json", new String[] { "E:Invalid key \"1#parameters\"", "E:Invalid key \"0#parameters\"" },
            },
            // (3) Invalid JSONs (target and platform names are not unique):
            {
                "/mta/sample/v1_0/platforms-04.json", "/mta/sample/v1_0/targets-04.json", new String[] { "E:Value \"CLOUD-FOUNDRY\" for key \"name\" not unique for object \"MTA platform type\"", "E:Value \"CF-QUAL\" for key \"name\" not unique for object \"MTA platform\"" },
            },
            // (4) Valid JSONs (containing only names):
            {
                "/mta/sample/v1_0/platforms-05.json", "/mta/sample/v1_0/targets-05.json", new String[] { "R:platforms-05.json.json", "R:targets-05.json.json" },
            },
// @formatter:on
        });
    }

    public ConfigurationParserTest(String platformsLocation, String targetsLocation, String[] expected) {
        this.targetsLocation = targetsLocation;
        this.platformsLocation = platformsLocation;
        this.expected = expected;
    }

    @Before
    public void setUp() throws Exception {
        if (targetsLocation != null) {
            targetsInputStream = getClass().getResourceAsStream(targetsLocation);
        }
        if (platformsLocation != null) {
            platformsInputStream = getClass().getResourceAsStream(platformsLocation);
        }
        parser = createConfigurationParser();
    }

    protected ConfigurationParser createConfigurationParser() {
        return new ConfigurationParser();
    }

    @Test
    public void testParsePlatformsJson() throws Exception {
        TestUtil.test(new Callable<List<Platform>>() {
            @Override
            public List<Platform> call() throws Exception {
                return parser.parsePlatformsJson(platformsInputStream);
            }
        }, expected[0], getClass());
    }

    @Test
    public void testParseTargetsJson() throws Exception {
        TestUtil.test(new Callable<List<Target>>() {
            @Override
            public List<Target> call() throws Exception {
                return parser.parseTargetsJson(targetsInputStream);
            }
        }, expected[1], getClass());
    }

}
