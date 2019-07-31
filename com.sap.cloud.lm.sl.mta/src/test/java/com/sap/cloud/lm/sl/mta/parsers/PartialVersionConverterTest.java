package com.sap.cloud.lm.sl.mta.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.vdurmont.semver4j.SemverException;

public class PartialVersionConverterTest {

    private final PartialVersionConverter partialVersionConverter = new PartialVersionConverter();

    @ParameterizedTest
    @MethodSource
    public void testConvertWithInvalidVersions(String versionString, String expectedExceptionMessage) {
        SemverException exception = assertThrows(SemverException.class,
                                                 () -> partialVersionConverter.convertToFullVersionString(versionString));

        assertEquals(expectedExceptionMessage, exception.getMessage());
    }

    public static Stream<Arguments> testConvertWithInvalidVersions() {
        return Stream.of(
// @formatter:off
            Arguments.of("1.0.0-beta+", "The build cannot be empty."),
            Arguments.of("3.a", "Invalid version (no minor version): 3.a"),
            Arguments.of("a.b.c", "Invalid version (no major version): a.b.c"),
            Arguments.of( "", "Invalid version (no major version): "),
            Arguments.of("[ 2.0, 2.1 ]", "Invalid version (no major version): [ 2.0, 2.1 ]")
// @formatter:on
        );
    }

    @ParameterizedTest
    @MethodSource
    public void testConvertWithValidVersions(String versionString, String expectedResult) {
        String fullVersionString = partialVersionConverter.convertToFullVersionString(versionString);

        assertEquals(expectedResult, fullVersionString);
    }

    public static Stream<Arguments> testConvertWithValidVersions() {
        return Stream.of(
// @formatter:off
            // Full version:
            Arguments.of("1.0.0", "1.0.0"),
            // Partial version with minor version:
            Arguments.of("2.1", "2.1.0"),
            // Partial version with patch version:
            Arguments.of("2", "2.0.0"),
            // Full version with suffix tokens:
            Arguments.of("1.9.0-SHAPSHOT", "1.9.0-SHAPSHOT"),
            // Partial version with suffix tokens:
            Arguments.of("1.9-SHAPHOT", "1.9.0-SHAPHOT"),
            // Partial version with suffix tokens:
            Arguments.of("1-SHAPHOT", "1.0.0-SHAPHOT"),
            // Full version with suffix tokens and build information:
            Arguments.of("1.2.0-beta+exp.sha.5114f85", "1.2.0-beta+exp.sha.5114f85"),
            // Partial version with suffix tokens and build information:
            Arguments.of("1.2-beta+exp.sha.5114f85", "1.2.0-beta+exp.sha.5114f85"),
            // Partial version with suffix tokens and build information:
            Arguments.of("1-beta+exp.sha.5114f85", "1.0.0-beta+exp.sha.5114f85")
// @formatter:on
        );
    }

}
