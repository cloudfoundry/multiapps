package com.sap.cloud.lm.sl.mta.handlers.v2;

import java.util.Arrays;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorParser;

@RunWith(value = Parameterized.class)
public class DescriptorParserTest extends com.sap.cloud.lm.sl.mta.handlers.v1.DescriptorParserTest {

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) Valid deployment and extension descriptors:
            {
                "/mta/sample/v2/mtad-01.yaml", "/mta/sample/v2/config-01.mtaext", new String[] { "R:mtad-01.yaml.json", "R:config-01.mtaext.json", },
            },
            // (1) Valid deployment and extension descriptors for version 1 of MTA specification:
            {
                "/mta/sample/v1/mtad-01.yaml", null, new String[] { "E:Invalid type for key \"modules#0#requires#0\", expected \"Map\" but got \"String\"", "S", },
            },
            // (2) Multiple modules with the same name:
            {
                "mtad-01.yaml", "config-01.mtaext", new String[] { "E:Value \"foo\" for key \"name\" not unique for object \"MTA module\"", "E:Value \"foo\" for key \"name\" not unique for object \"MTA extension module\"", },
            },
            // (3) Multiple provided dependencies with the same name:
            {
                "mtad-02.yaml", "config-02.mtaext", new String[] { "E:Value \"bar\" for key \"name\" not unique for object \"MTA provided dependency\"", "E:Value \"bar\" for key \"name\" not unique for object \"MTA extension provided dependency\"", },
            },
            // (4) Multiple required dependencies with the same name:
            {
                "mtad-03.yaml", "config-03.mtaext", new String[] { "E:Value \"bar\" for key \"name\" not unique for object \"MTA required dependency\"", "E:Value \"baz\" for key \"name\" not unique for object \"MTA extension required dependency\"", },
            },
            // (5) Multiple provided dependencies with the same name (in the same module):
            {
                "mtad-04.yaml", "config-04.mtaext", new String[] { "E:Value \"foo\" for key \"name\" not unique for object \"MTA provided dependency\"", "E:Value \"bar\" for key \"name\" not unique for object \"MTA extension provided dependency\"", },
            },
            // (6) Explicit declaration that module provides itself as a dependency:
            {
                "/mta/sample/v2/mtad-05.yaml", null, new String[] { "R:mtad-01.yaml.json", "S", },
            },
            // (7) Module and resource with the same name:
            {
                "mtad-19.yaml", null, new String[] { "E:Value \"foo\" for key \"name\" not unique for object \"MTA resource\"", "S", },
            },
            // (8) Resource and module provided dependency with the same name:
            {
                "mtad-20.yaml", null, new String[] { "E:Value \"bar\" for key \"name\" not unique for object \"MTA resource\"", "S", },
            },
            // (9) Provided dependency name same as another module:
            {
                "mtad-21.yaml", null, new String[] { "E:Value \"bar\" for key \"name\" not unique for object \"MTA provided dependency\"", "S", },
            },
            // (10) Partial schema version support test (int):
            {
                "mtad-with-partial-schema-version-major.yaml", "config-with-partial-schema-version-major.mtaext", new String[] { "R:parsed-mtad-with-partial-schema-version.json", "R:parsed-config-with-partial-schema-version.json" },
            },
            // (11) Partial schema version support test (double):
            {
                "mtad-with-partial-schema-version-major.minor.yaml", "config-with-partial-schema-version-major.minor.mtaext", new String[] { "R:parsed-mtad-with-partial-schema-version.json", "R:parsed-config-with-partial-schema-version.json" },
            },
            // (12) Partial schema version support test (string):
            {
                "mtad-with-partial-schema-version-major-quoted.yaml", "config-with-partial-schema-version-major-quoted.mtaext", new String[] { "R:parsed-mtad-with-partial-schema-version.json", "R:parsed-config-with-partial-schema-version.json" },
            },
            // (13) Partial schema version support test (string):
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
