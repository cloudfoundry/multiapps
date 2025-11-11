package org.cloudfoundry.multiapps.mta.model;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VersionTest {

    public static final String UNABLE_TO_PARSE_VERSION = "Unable to parse version";

    // ---------------------------------------------------------------------------------------------
    //  PARSING AND NORMALIZATION TESTS
    // ---------------------------------------------------------------------------------------------
    @Nested
    @DisplayName("parseVersion() and normalization behavior")
    class ParseVersionTests {

        @Test
        @DisplayName("should parse complex versions with build metadata and timestamps")
        void testParseComplexVersions() {
            Version v1 = Version.parseVersion("0.0.1-20251105063220+e303076b5d51da7f0f3a10cd69de018ac0a3853d");
            Version v2 = Version.parseVersion("1.2.0-20251105043948+cb754700fb069b3367c869938ac6beb396c800e4");
            Version v3 = Version.parseVersion("1.0.0-SNAPSHOT-20240116224612+200df4832787863cc2f94d998c6cbcd711518933");

            assertNotNull(v1);
            assertNotNull(v2);
            assertNotNull(v3);

            assertVersionParts(v1, 0, 0, 1);
            assertVersionParts(v2, 1, 2, 0);
            assertVersionParts(v3, 1, 0, 0);
        }

        @ParameterizedTest(name = "Partial version \"{0}\" should normalize to \"{1}\"")
        @MethodSource
        void testNormalizePartialVersions(String input, String expected) {
            Version version = Version.parseVersion(input);
            assertEquals(expected, version.toString());
        }

        static Stream<Arguments> testNormalizePartialVersions() {
            return Stream.of(
                Arguments.of("1.0.0", "1.0.0"),
                Arguments.of("2.1", "2.1.0"),
                Arguments.of("2", "2.0.0"),
                Arguments.of("1.9.0-SNAPSHOT", "1.9.0-SNAPSHOT"),
                Arguments.of("1.9-SNAPSHOT", "1.9.0"),
                Arguments.of("1-SNAPSHOT", "1.0.0"),
                Arguments.of("1.2.0-beta+exp.sha.5114f85", "1.2.0-beta+exp.sha.5114f85"),
                Arguments.of("1.2-beta+exp.sha.5114f85", "1.2.0"),
                Arguments.of("1-beta+exp.sha.5114f85", "1.0.0"),
                Arguments.of("v1.2.3", "1.2.3")
            );
        }

        @ParameterizedTest(name = "Invalid version \"{0}\" should throw SemverException or ParsingException")
        @MethodSource
        void testInvalidVersions(String invalid, String expectedMessage) {
            Exception ex = assertThrows(Exception.class, () -> Version.parseVersion(invalid));
            assertTrue(ex.getMessage()
                         .contains(expectedMessage));
        }

        static Stream<Arguments> testInvalidVersions() {
            return Stream.of(
                Arguments.of("a.b.c", UNABLE_TO_PARSE_VERSION),
                Arguments.of("", UNABLE_TO_PARSE_VERSION),
                Arguments.of("null", UNABLE_TO_PARSE_VERSION),
                Arguments.of("not-a-version", UNABLE_TO_PARSE_VERSION)
            );
        }
    }

    // ---------------------------------------------------------------------------------------------
    //  COMPARISON AND EQUALITY TESTS
    // ---------------------------------------------------------------------------------------------
    @Nested
    @DisplayName("compareTo() and equality behavior")
    class CompareTests {

        @Test
        void testVersionComparison() {
            Version v1 = Version.parseVersion("1.2.3");
            Version v2 = Version.parseVersion("1.3.0");
            Version v3 = Version.parseVersion("1.2.3");

            assertTrue(v1.compareTo(v2) < 0);
            assertTrue(v2.compareTo(v1) > 0);
            assertEquals(0, v1.compareTo(v3));
        }

        @Test
        void testEqualsAndHashCode() {
            Version v1 = Version.parseVersion("2.0.0");
            Version v2 = Version.parseVersion("2.0.0");
            Version v3 = Version.parseVersion("2.0.1");

            assertEquals(v1, v2);
            assertNotEquals(v1, v3);
            assertEquals(v1.hashCode(), v2.hashCode());
            assertNotEquals(v1.hashCode(), v3.hashCode());
        }
    }

    // ---------------------------------------------------------------------------------------------
    //  TO STRING & EDGE CASE TESTS
    // ---------------------------------------------------------------------------------------------
    @Nested
    class ToStringAndEdgeCases {

        @Test
        void testToStringRepresentation() {
            String raw = "3.4.5-alpha+build";
            Version version = Version.parseVersion(raw);
            assertEquals(raw, version.toString());
        }

        @Test
        void testLargeVersionNumbers() {
            Version version = Version.parseVersion("9999.9999.9999");
            assertEquals(9999, version.getMajor());
        }

        @Test
        void testPreReleaseVersion() {
            Version version = Version.parseVersion("1.0.0-alpha");
            assertTrue(version.toString()
                              .contains("alpha"));
        }
    }

    // ---------------------------------------------------------------------------------------------
    //  SATISFIES() RANGE CHECK TESTS
    // ---------------------------------------------------------------------------------------------
    @Nested
    class SatisfiesBehavior {

        @Test
        void testSimpleComparisons() {
            Version v = Version.parseVersion("3.1.4");

            assertTrue(v.satisfies(">3.0.0"));
            assertTrue(v.satisfies(">=3.1.4"));
            assertFalse(v.satisfies("<3.0.0"));
            assertFalse(v.satisfies("<=3.1.3"));
        }

        @Test
        void testCompositeRanges() {
            Version v = Version.parseVersion("3.1.4");
            assertTrue(v.satisfies(">=3.1.4 <4.0.0"));
            assertTrue(v.satisfies(">=3.0.0 <=3.1.4"));
            assertFalse(v.satisfies(">=3.2.0 <4.0.0"));
        }

        @Test
        void testCaretAndTilde() {
            Version v = Version.parseVersion("1.3.5");
            assertTrue(v.satisfies("^1.3.0"));
            assertTrue(v.satisfies("~1.3.0"));
            assertFalse(v.satisfies("^2.0.0"));
        }

        @Test
        void testMalformedRequirementsReturnFalse() {
            Version v = Version.parseVersion("3.1.4");
            assertFalse(v.satisfies("not-a-requirement"));
            assertFalse(v.satisfies("pesho3.1.4"));
            assertFalse(v.satisfies("3.1.4.2.3.4.5"));
        }

        @Test
        void testNullRequirement() {
            Version v = Version.parseVersion("3.1.4");
            assertThrows(NullPointerException.class, () -> v.satisfies(null));
        }

        @Test
        void testSatisfiesWithComplexVersions() {
            Version v = Version.parseVersion("1.2.3-20251105063220+e303076b5d51da7f0f3a10cd69de018ac0a3853d");
            assertTrue(v.satisfies(">=1.2.0"));
            assertFalse(v.satisfies(">1.2.3"));
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