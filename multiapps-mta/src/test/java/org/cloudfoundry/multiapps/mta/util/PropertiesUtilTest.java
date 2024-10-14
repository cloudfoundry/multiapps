package org.cloudfoundry.multiapps.mta.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.common.test.TestUtil;
import org.cloudfoundry.multiapps.common.test.Tester;
import org.cloudfoundry.multiapps.common.test.Tester.Expectation;
import org.cloudfoundry.multiapps.common.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PropertiesUtilTest {

    private final Tester tester = Tester.forClass(getClass());
    private final String TEST_PARAMETER_KEY = "test-key";
    private final String TEST_MISSING_PARAMETER_KEY = "missing-test-key";
    private final String TEST_PARAMETER_VALUE = "test-value";

    static Stream<Arguments> mergeTest() {
        return Stream.of(
                         // (0) Merge normal properties maps
                         Arguments.of("deployment-properties-01.json", "extension-properties-01.json",
                                      new Expectation(Expectation.Type.JSON, "merged-properties-01.json")),
                         // (1) No changes in the extension => merged properties should be the same
                         Arguments.of("deployment-properties-02.json", "extension-properties-02.json",
                                      new Expectation(Expectation.Type.JSON, "merged-properties-02.json")),
                         // (2) Merging of nested maps
                         Arguments.of("deployment-properties-03.json", "extension-properties-03.json",
                                      new Expectation(Expectation.Type.JSON, "merged-properties-03.json")),
                         // (3) Scalar parameter cannot be overwritten by a structured parameter
                         Arguments.of("deployment-properties-04.json", "extension-properties-04.json",
                                      new Expectation(Expectation.Type.EXCEPTION, "")),
                         // (4) Structured parameter cannot be overwritten by a scalar parameter
                         Arguments.of("deployment-properties-05.json", "extension-properties-05.json",
                                      new Expectation(Expectation.Type.EXCEPTION, "")));
    }

    @ParameterizedTest
    @MethodSource
    void mergeTest(String deploymentDescriptorPath, String extensionDescriptorPath, Expectation expectation) {
        Map<String, Object> deploymentDescriptorProperties = JsonUtil.convertJsonToMap(TestUtil.getResourceAsString(deploymentDescriptorPath,
                                                                                                                    getClass()));
        Map<String, Object> extensionDescriptorProperties = JsonUtil.convertJsonToMap(TestUtil.getResourceAsString(extensionDescriptorPath,
                                                                                                                   getClass()));

        tester.test(() -> PropertiesUtil.mergeExtensionProperties(deploymentDescriptorProperties, extensionDescriptorProperties),
                    expectation);
    }

    static Stream<Arguments> testGetPluralOrSingular() {
        return Stream.of(Arguments.of("host-1", null, new Expectation(Expectation.Type.STRING,
                                                                      Collections.singletonList("host-1")
                                                                                 .toString())),
                         Arguments.of(null, Arrays.asList("host-1", "host-2"),
                                      new Expectation(Expectation.Type.STRING,
                                                      Arrays.asList("host-1", "host-2")
                                                            .toString())),
                         Arguments.of("host-1", Arrays.asList("host-2", "host-3"),
                                      new Expectation(Expectation.Type.STRING,
                                                      Arrays.asList("host-2", "host-3")
                                                            .toString())));
    }

    @ParameterizedTest
    @MethodSource
    void testGetPluralOrSingular(String singular, List<String> plural, Expectation expectation) {
        String singularKey = "host";
        String pluralKey = "hosts";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(singularKey, singular);
        parameters.put(pluralKey, plural);
        List<Map<String, Object>> parametersList = Collections.singletonList(parameters);

        tester.test(() -> PropertiesUtil.getPluralOrSingular(parametersList, pluralKey, singularKey), expectation);

    }

    @Test
    void testGetRequiredParameterWithProvidedParameter() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(TEST_PARAMETER_KEY, TEST_PARAMETER_VALUE);
        String result = PropertiesUtil.getRequiredParameter(parameters, TEST_PARAMETER_KEY);

        assertEquals(TEST_PARAMETER_VALUE ,result);
    }

    @Test
    void testGetRequiredParameterWithoutProvidedParameter() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(TEST_PARAMETER_KEY, TEST_PARAMETER_VALUE);

        assertThrows(ContentException.class,() -> PropertiesUtil.getRequiredParameter(parameters, TEST_MISSING_PARAMETER_KEY));
    }

    @Test
    void testGetRequiredParameterWithoutProvidedKey() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(TEST_PARAMETER_KEY, TEST_PARAMETER_VALUE);

        assertThrows(ContentException.class,() -> PropertiesUtil.getRequiredParameter(parameters, null));
    }

    @Test
    void testGetOptionalParameterWithProvidedParameter() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(TEST_PARAMETER_KEY, TEST_PARAMETER_VALUE);
        String result = PropertiesUtil.getOptionalParameter(parameters, TEST_PARAMETER_KEY);

        assertEquals(TEST_PARAMETER_VALUE ,result);
    }

    @Test
    void testGetOptionalParameterWithoutProvidedParameter() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(TEST_PARAMETER_KEY, TEST_PARAMETER_VALUE);

        String result = PropertiesUtil.getOptionalParameter(parameters, TEST_MISSING_PARAMETER_KEY);

        assertNull(result);
    }

    @Test
    void testGetOptionalParameterWithoutProvidedKey() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(TEST_PARAMETER_KEY, TEST_PARAMETER_VALUE);

        String result = PropertiesUtil.getOptionalParameter(parameters, null);

        assertNull(result);
    }

    static Stream<Arguments> testParsingGetPluralOrSingular() {
        return Stream.of(
                         // formatter:off
                         Arguments.of("buildpack", "random-buildpack", "buildpacks", "some-buildpacks",
                                      new Expectation(Expectation.Type.EXCEPTION,
                                                      "Invalid type provided for \"buildpacks\": Expected a list of elements but another type was provided")),

                         Arguments.of("host", "random-host", "hosts", "some-hosts",
                                      new Expectation(Expectation.Type.EXCEPTION,
                                                      "Invalid type provided for \"hosts\": Expected a list of elements but another type was provided")),

                         Arguments.of("idle-host", "random-idle-host", "idle-hosts", "some-idle-hosts",
                                      new Expectation(Expectation.Type.EXCEPTION,
                                                      "Invalid type provided for \"idle-hosts\": Expected a list of elements but another type was provided")),

                         Arguments.of("domain", "random-domain", "domains", "some-domains",
                                      new Expectation(Expectation.Type.EXCEPTION,
                                                      "Invalid type provided for \"domains\": Expected a list of elements but another type was provided")),

                         Arguments.of("idle-domain", "random-idle-domain", "idle-domains", "some-idle-domains",
                                      new Expectation(Expectation.Type.EXCEPTION,
                                                      "Invalid type provided for \"idle-domains\": Expected a list of elements but another type was provided")));
        // formatter:on
    }

    @ParameterizedTest
    @MethodSource("testParsingGetPluralOrSingular")
    void testParsingGetPluralOrSingular(String singularKey, String singularValue, String pluralKey, String pluralValue,
                                        Expectation expectation) {
        List<Map<String, Object>> listOfParameters = getParameterList(singularKey, singularValue, pluralKey, pluralValue);
        tester.test(() -> PropertiesUtil.getPluralOrSingular(listOfParameters, pluralKey, singularKey), expectation);
    }

    private List<Map<String, Object>> getParameterList(String singularKey, String singularValue, String pluralKey, String pluralValue) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(singularKey, singularValue);
        parameters.put(pluralKey, pluralValue);
        return Collections.singletonList(parameters);
    }
}