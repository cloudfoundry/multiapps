package com.sap.cloud.lm.sl.mta.handlers.v2;

import java.util.Arrays;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.TestUtil.Expectation;

@RunWith(value = Parameterized.class)
public class DescriptorParserTest extends com.sap.cloud.lm.sl.mta.handlers.v1.DescriptorParserTest {

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) Valid deployment and extension descriptors:
            {
                "/mta/sample/v2/mtad-01.yaml", "/mta/sample/v2/config-01.mtaext",
                new Expectation[] {
                    new Expectation(Expectation.Type.RESOURCE, "mtad-01.yaml.json"),
                    new Expectation(Expectation.Type.RESOURCE, "config-01.mtaext.json"),
                },
            },
            // (1) Valid deployment and extension descriptors for version 1 of MTA specification:
            {
                "/mta/sample/v1/mtad-01.yaml", null,
                new Expectation[] {
                    new Expectation(Expectation.Type.EXCEPTION, "Invalid type for key \"modules#0#requires#0\", expected \"Map\" but got \"String\""),
                    new Expectation(Expectation.Type.SKIP, null),
                },
            },
            // (2) Multiple modules with the same name:
            {
                "mtad-01.yaml", "config-01.mtaext",
                new Expectation[] {
                    new Expectation(Expectation.Type.EXCEPTION, "Value \"foo\" for key \"name\" not unique for object \"MTA module\""),
                    new Expectation(Expectation.Type.EXCEPTION, "Value \"foo\" for key \"name\" not unique for object \"MTA extension module\""),
                },
            },
            // (3) Multiple provided dependencies with the same name:
            {
                "mtad-02.yaml", "config-02.mtaext",
                new Expectation[] {
                    new Expectation(Expectation.Type.EXCEPTION, "Value \"bar\" for key \"name\" not unique for object \"MTA provided dependency\""),
                    new Expectation(Expectation.Type.EXCEPTION, "Value \"bar\" for key \"name\" not unique for object \"MTA extension provided dependency\""),
                },
            },
            // (4) Multiple required dependencies with the same name:
            {
                "mtad-03.yaml", "config-03.mtaext",
                new Expectation[] {
                    new Expectation(Expectation.Type.EXCEPTION, "Value \"bar\" for key \"name\" not unique for object \"MTA required dependency\""),
                    new Expectation(Expectation.Type.EXCEPTION, "Value \"baz\" for key \"name\" not unique for object \"MTA extension required dependency\""),
                },
            },
            // (5) Multiple provided dependencies with the same name (in the same module):
            {
                "mtad-04.yaml", "config-04.mtaext",
                new Expectation[] {
                    new Expectation(Expectation.Type.EXCEPTION, "Value \"foo\" for key \"name\" not unique for object \"MTA provided dependency\""),
                    new Expectation(Expectation.Type.EXCEPTION, "Value \"bar\" for key \"name\" not unique for object \"MTA extension provided dependency\""),
                },
            },
            // (6) Explicit declaration that module provides itself as a dependency:
            {
                "/mta/sample/v2/mtad-05.yaml", null,
                new Expectation[] {
                    new Expectation(Expectation.Type.RESOURCE, "mtad-01.yaml.json"),
                    new Expectation(Expectation.Type.SKIP, null),
                },
            },
            // (7) Module and resource with the same name:
            {
                "mtad-19.yaml", null,
                new Expectation[] {
                    new Expectation(Expectation.Type.EXCEPTION, "Value \"foo\" for key \"name\" not unique for object \"MTA resource\""),
                    new Expectation(Expectation.Type.SKIP, null),
                },
            },
            // (8) Resource and module provided dependency with the same name:
            {
                "mtad-20.yaml", null,
                new Expectation[] {
                    new Expectation(Expectation.Type.EXCEPTION, "Value \"bar\" for key \"name\" not unique for object \"MTA resource\""),
                    new Expectation(Expectation.Type.SKIP, null),
                },
            },
            // (9) Provided dependency name same as another module:
            {
                "mtad-21.yaml", null,
                new Expectation[] {
                    new Expectation(Expectation.Type.EXCEPTION, "Value \"bar\" for key \"name\" not unique for object \"MTA provided dependency\""),
                    new Expectation(Expectation.Type.SKIP, null),
                },
            },
            // (10) Partial schema version support test (int):
            {
                "mtad-with-partial-schema-version-major.yaml", "config-with-partial-schema-version-major.mtaext",
                new Expectation[] {
                    new Expectation(Expectation.Type.RESOURCE, "parsed-mtad-with-partial-schema-version.json"),
                    new Expectation(Expectation.Type.RESOURCE, "parsed-config-with-partial-schema-version.json"),
                },
            },
            // (11) Partial schema version support test (double):
            {
                "mtad-with-partial-schema-version-major.minor.yaml", "config-with-partial-schema-version-major.minor.mtaext",
                new Expectation[] {
                    new Expectation(Expectation.Type.RESOURCE, "parsed-mtad-with-partial-schema-version.json"),
                    new Expectation(Expectation.Type.RESOURCE, "parsed-config-with-partial-schema-version.json"),
                },
            },
            // (12) Partial schema version support test (string):
            {
                "mtad-with-partial-schema-version-major-quoted.yaml", "config-with-partial-schema-version-major-quoted.mtaext",
                new Expectation[] {
                    new Expectation(Expectation.Type.RESOURCE, "parsed-mtad-with-partial-schema-version.json"),
                    new Expectation(Expectation.Type.RESOURCE, "parsed-config-with-partial-schema-version.json"),
                },
            },
            // (13) Partial schema version support test (string):
            {
                "mtad-with-partial-schema-version-major.minor-quoted.yaml", "config-with-partial-schema-version-major.minor-quoted.mtaext",
                new Expectation[] {
                    new Expectation(Expectation.Type.RESOURCE, "parsed-mtad-with-partial-schema-version.json"),
                    new Expectation(Expectation.Type.RESOURCE, "parsed-config-with-partial-schema-version.json"),
                },
            },
// @formatter:on
        });
    }

    public DescriptorParserTest(String deploymentDescriptorLocation, String extensionDescriptorLocation, Expectation[] expectation) {
        super(deploymentDescriptorLocation, extensionDescriptorLocation, expectation);
    }

    @Override
    protected DescriptorParser createDescriptorParser() {
        return new DescriptorParser();
    }

}
