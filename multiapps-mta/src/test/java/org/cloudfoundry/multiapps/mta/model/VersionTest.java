package org.cloudfoundry.multiapps.mta.model;

import java.text.MessageFormat;

import org.cloudfoundry.multiapps.common.ParsingException;
import org.cloudfoundry.multiapps.mta.Messages;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VersionTest {


    @Nested
    @DisplayName("parseVersion() tests")
    class ParseVersionTests {

        @Test
        @DisplayName("should parse a standard semantic version successfully")
        void testParseStandardVersion() {
            Version version = Version.parseVersion("1.2.3");

            assertVersionParts(version, 1, 2, 3);
            assertEquals("1.2.3", version.toString());
        }

        @Test
        @DisplayName("should parse versions with build metadata and timestamps")
        void testParseComplexVersions() {
            Version v1 = Version.parseVersion("0.0.1-20251105063220+e303076b5d51da7f0f3a10cd69de018ac0a3853d");
            Version v2 = Version.parseVersion("1.2.0-20251105043948+cb754700fb069b3367c869938ac6beb396c800e4");
            Version v3 = Version.parseVersion("1.0.0-SNAPSHOT-20240116224612+200df4832787863cc2f94d998c6cbcd711518933");

            assertNotNull(v1);
            assertNotNull(v2);
            assertNotNull(v3);

            // Check major/minor/patch extracted properly
            assertVersionParts(v1, 0, 0, 1);
            assertVersionParts(v2, 1, 2, 0);
            assertVersionParts(v3, 1, 0, 0);
        }

        @Test
        @DisplayName("should coerce loose versions like '1', '1.2', or 'v1.2.3'")
        void testParseCoercibleVersions() {
            Version v1 = Version.parseVersion("1");
            assertVersionParts(v1, 1, 0, 0);

            Version v2 = Version.parseVersion("1.2");
            assertVersionParts(v2, 1, 2, 0);

            Version v3 = Version.parseVersion("v1.2.3");
            assertVersionParts(v3, 1, 2, 3);
        }

        @Test
        @DisplayName("should throw ParsingException for invalid version strings")
        void testInvalidVersionThrowsException() {
            String invalidVersion = "not-a-version";

            Exception exception = assertThrows(ParsingException.class,
                                                      () -> Version.parseVersion(invalidVersion));

            assertInstanceOf(ParsingException.class, exception);
            assertTrue(exception.getMessage()
                                .contains(MessageFormat.format(Messages.UNABLE_TO_PARSE_VERSION, invalidVersion)));
        }

        @Test
        @DisplayName("should throw ParsingException for empty or null input")
        void testEmptyOrNullThrowsException() {
            assertThrows(ParsingException.class, () -> Version.parseVersion(""));
            assertThrows(ParsingException.class, () -> Version.parseVersion(null));
        }
    }

    @Nested
    @DisplayName("compareTo() and equality tests")
    class CompareTests {

        @Test
        @DisplayName("should correctly compare different versions")
        void testVersionComparison() {
            Version v1 = Version.parseVersion("1.2.3");
            Version v2 = Version.parseVersion("1.3.0");
            Version v3 = Version.parseVersion("1.2.3");

            assertTrue(v1.compareTo(v2) < 0);
            assertTrue(v2.compareTo(v1) > 0);
            assertEquals(0, v1.compareTo(v3));
        }

        @Test
        @DisplayName("should maintain consistent equals/hashCode contracts")
        void testEqualsAndHashCode() {
            Version v1 = Version.parseVersion("2.0.0");
            Version v2 = Version.parseVersion("2.0.0");
            Version v3 = Version.parseVersion("2.0.1");

            assertEquals(v1.hashCode(), v2.hashCode());
            assertNotEquals(v1.hashCode(), v3.hashCode());
            assertEquals(0, v1.compareTo(v2));
        }
    }

    @Nested
    @DisplayName("toString() tests")
    class ToStringTests {

        @Test
        @DisplayName("should return string representation matching Semver")
        void testToStringRepresentation() {
            String raw = "3.4.5-alpha+build";
            Version version = Version.parseVersion(raw);

            assertEquals(raw, version.toString());
        }
    }

    @Nested
    @DisplayName("edge cases and robustness")
    class EdgeCases {

        @Test
        @DisplayName("should handle large version numbers gracefully")
        void testLargeVersionNumbers() {
            Version version = Version.parseVersion("9999.9999.9999");
            assertEquals(9999, version.getMajor());
        }

        @Test
        @DisplayName("should handle pre-release versions correctly")
        void testPreReleaseVersion() {
            Version version = Version.parseVersion("1.0.0-alpha");
            assertEquals(1, version.getMajor());
            assertTrue(version.toString().contains("alpha"));
        }
    }

    @Nested
    @DisplayName("satisfies() behavior")
    class SatisfiesBehavior {

        @Test
        @DisplayName("simple comparisons: greater/less/equal")
        void testSimpleComparisons() {
            Version v = Version.parseVersion("3.1.4");

            assertTrue(v.satisfies(">3.0.0"));
            assertTrue(v.satisfies(">=3.1.4"));
            assertFalse(v.satisfies("<3.0.0"));
            assertFalse(v.satisfies("<=3.1.3"));
        }

        @Test
        @DisplayName("composite ranges (space separated)")
        void testCompositeRanges() {
            Version v = Version.parseVersion("3.1.4");

            assertTrue(v.satisfies(">=3.1.4 <4.0.0"));
            assertTrue(v.satisfies(">=3.0.0 <=3.1.4"));
            assertFalse(v.satisfies(">=3.2.0 <4.0.0"));
        }

        @Test
        @DisplayName("caret and tilde ranges")
        void testCaretAndTilde() {
            Version v = Version.parseVersion("1.3.5");

            assertTrue(v.satisfies("^1.3.0"));
            assertTrue(v.satisfies("~1.3.0"));
            assertFalse(v.satisfies("^2.0.0"));
        }

        @Test
        @DisplayName("malformed or non-matching requirement strings are treated as non-matching (false)")
        void testMalformedRequirementsReturnFalse() {
            Version v = Version.parseVersion("3.1.4");

            assertFalse(v.satisfies("not-a-requirement"));
            assertFalse(v.satisfies("pesho3.1.4"));
            assertFalse(v.satisfies("3.1.4.2.3.4.5"));
        }

        @Test
        @DisplayName("null requirement propagates NullPointerException")
        void testNullRequirement() {
            Version v = Version.parseVersion("3.1.4");
            assertThrows(NullPointerException.class, () -> v.satisfies(null));
        }

        @Test
        @DisplayName("use versions with build metadata and timestamps for positive results")
        void testSatisfiesWithComplexVersions() {
            Version v = Version.parseVersion("1.2.3-20251105063220+e303076b5d51da7f0f3a10cd69de018ac0a3853d");

            assertTrue(v.satisfies(">=1.2.0"));
            assertFalse(v.satisfies(">1.2.3"));
            assertFalse(v.satisfies("=1.2.3"));

            // Additional checks: composite ranges and boundaries
            assertTrue(v.satisfies(">=1.2.0 <2.0.0"));
            assertFalse(v.satisfies(">1.2.3 <2.0.0"));
        }
    }

    private void assertVersionParts(Version version, int major, int minor, int patch) {
        assertEquals(major, version.getMajor());
        assertEquals(minor, version.getMinor());
        assertEquals(patch, version.getPatch());
    }
}
