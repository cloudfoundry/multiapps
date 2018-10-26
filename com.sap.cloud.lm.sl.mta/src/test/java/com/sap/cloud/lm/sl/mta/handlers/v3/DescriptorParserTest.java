package com.sap.cloud.lm.sl.mta.handlers.v3;

import java.util.Arrays;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.mta.handlers.v3.DescriptorParser;

@RunWith(Parameterized.class)
public class DescriptorParserTest extends com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorParserTest {

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0)
            {
                "mtad-00.yaml", "config-00.mtaext", new String[] { "R:parsed-mtad-00.json", "R:parsed-config-00.mtaext" },
            },
            // (1)
            {
                "mtad-01.yaml", "config-01.mtaext", new String[] { "R:parsed-mtad-01.json", "R:parsed-config-01.mtaext" },
            },
            // (2) Sensitive properties and parameters test:
            {
                "mtad-02.yaml", null, new String[] { "R:parsed-mtad-02.json", "S" },
            },
            // (3) Partial schema version support test (int):
            {
                "mtad-with-partial-schema-version-major.yaml", "config-with-partial-schema-version-major.mtaext", new String[] { "R:parsed-mtad-with-partial-schema-version.json", "R:parsed-config-with-partial-schema-version.json" },
            },
            // (4) Partial schema version support test (double):
            {
                "mtad-with-partial-schema-version-major.minor.yaml", "config-with-partial-schema-version-major.minor.mtaext", new String[] { "R:parsed-mtad-with-partial-schema-version.json", "R:parsed-config-with-partial-schema-version.json" },
            },
            // (5) Partial schema version support test (string):
            {
                "mtad-with-partial-schema-version-major-quoted.yaml", "config-with-partial-schema-version-major-quoted.mtaext", new String[] { "R:parsed-mtad-with-partial-schema-version.json", "R:parsed-config-with-partial-schema-version.json" },
            },
            // (6) Partial schema version support test (string):
            {
                "mtad-with-partial-schema-version-major.minor-quoted.yaml", "config-with-partial-schema-version-major.minor-quoted.mtaext", new String[] { "R:parsed-mtad-with-partial-schema-version.json", "R:parsed-config-with-partial-schema-version.json" },
            },
// @formatter:on
        });
    }

    public DescriptorParserTest(String deploymentDescriptorLocation, String extensionDescriptorLocation, String[] expected) {
        super(deploymentDescriptorLocation, extensionDescriptorLocation, expected);
    }

    @Override
    protected DescriptorParser createDescriptorParser() {
        return new DescriptorParser();
    }

}
