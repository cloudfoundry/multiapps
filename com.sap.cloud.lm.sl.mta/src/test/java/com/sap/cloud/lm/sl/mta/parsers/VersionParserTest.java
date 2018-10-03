package com.sap.cloud.lm.sl.mta.parsers;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class VersionParserTest {

    @Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (00) Supported version:
            {
                "1.0.0", "1.0.0", null,
            },
            // (01) Supported version:
            {
                "2.0.0", "2.0.0", null,
            },
            // (02) Supported version:
            {
                "2.1.0", "2.1.0", null,
            },
            // (03) Supported version:
            {
                "2", "2.2.0", null,
            },
            // (04) Supported version:
            {
                "2.1", "2.1.0", null,
            },
            // (05) Supported version:
            {
                "3.2", "3.2.0", null,
            },
            // (06) Supported version:
            {
                "3.1", "3.1.0", null,
            },
            // (07) Supported version:
            {
                "6", "6.0.0", null,
            },
            // (08) Supported version:
            {
                "6.1000.12", "6.1000.12", null,
            },
            // (09) Supported version with SNAPSHOT:
            {
                "1.9.0-SHAPSHOT", "1.9.0-SHAPSHOT", null,
            },
            // (10) Supported version with beta and with build version:
            {
                "1-beta+exp.sha.5114f85", "1.0.0-beta+exp.sha.5114f85", null,
            },
            // (11) Supported version with beta and with build version:
            {
                "1.0.0-beta+exp.sha.5114f85", "1.0.0-beta+exp.sha.5114f85", null,
            },
            // (12) Not support build version with beta:
            {
                "1.0.0-beta+", null, "The build cannot be empty.",
            },
            // (13) Invalid version:
            {
                "3.a", null, "Invalid version (no minor version): 3.a",
            },
            // (14) Invalid version:
            {
                "a.b.c", null, "Invalid version (no major version): a",
            },
            // (15) Invalid version:
            {
                "", null, "Invalid version (no major version): ",
            },
            // (16) Supported version with SNAPSHOT:
            {
                "1.9-SHAPHOT", "1.9.0-SHAPHOT", null,
            },
            // (17) Version range:
            {
                "[ 2.0, 2.1 ]", null, "Invalid version (no major version): [ 2.0, 2.1 ]",
            },
// @formatter:on
        });
    }

    private VersionParser parser;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private String schemaVersion;
    private String expectedVersion;
    private String expectedExceptionMessage;

    public VersionParserTest(String schemaVersion, String expectedVersion, String expectedExceptionMessage) {
        this.schemaVersion = schemaVersion;
        this.expectedVersion = expectedVersion;
        this.expectedExceptionMessage = expectedExceptionMessage;
    }

    @Before
    public void setUp() {
        parser = new VersionParser();
        if (expectedExceptionMessage != null) {
            expectedException.expectMessage(expectedExceptionMessage);
        }
    }

    @Test
    public void testParse() {
        String parsedVersion = parser.parse(schemaVersion);

        assertEquals(expectedVersion, parsedVersion);
    }
}
