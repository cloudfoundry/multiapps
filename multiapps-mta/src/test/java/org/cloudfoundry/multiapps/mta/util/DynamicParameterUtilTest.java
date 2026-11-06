package org.cloudfoundry.multiapps.mta.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DynamicParameterUtilTest {

    static Stream<Arguments> getParameters() {
        return Stream.of(Arguments.of("{ds/my-service/service-guid}", "my-service", "service-guid"),
                         Arguments.of("{ds/service-with/special_symbol/service-guid}", "service-with/special_symbol", "service-guid"));
    }

    @ParameterizedTest
    @MethodSource(value = "getParameters")
    void testParseOfParameterName(String dynamicParameter, String relationshipName, String parameterName) {
        assertEquals(parameterName, DynamicParameterUtil.getParameterName(dynamicParameter));
    }

    @ParameterizedTest
    @MethodSource(value = "getParameters")
    void testParseOfRelationshipName(String dynamicParameter, String relationshipName, String parameterName) {
        assertEquals(relationshipName, DynamicParameterUtil.getRelationshipName(dynamicParameter));
    }

    @ParameterizedTest
    @MethodSource(value = "getParameters")
    void testMatchDynamicParameter(String dynamicParameter, String relationshipName, String parameterName) {
        assertTrue(dynamicParameter.matches(DynamicParameterUtil.REGEX_PATTERN_FOR_DYNAMIC_PARAMETERS));
    }

    static Stream<Arguments> getServiceKeyCredentialParameters() {
        // dynamicParameter, relationshipName, serviceKeyName, credentialFieldPath
        return Stream.of(
            Arguments.of("{ds/ztis-config/service-keys.my-key.credentials.issuer}", "ztis-config", "my-key", "issuer"),
            Arguments.of("{ds/ztis-config/service-keys.my-key.credentials.endpoints.url}", "ztis-config", "my-key", "endpoints.url"));
    }

    @ParameterizedTest
    @MethodSource(value = "getServiceKeyCredentialParameters")
    void testIsServiceKeyCredentialReference(String dynamicParameter, String relationshipName, String serviceKeyName,
                                             String credentialFieldPath) {
        String parameterName = DynamicParameterUtil.getParameterName(dynamicParameter);
        assertTrue(DynamicParameterUtil.isServiceKeyCredentialReference(parameterName));
    }

    @ParameterizedTest
    @MethodSource(value = "getParameters")
    void testServiceGuidIsNotServiceKeyCredentialReference(String dynamicParameter, String relationshipName, String parameterName) {
        assertFalse(DynamicParameterUtil.isServiceKeyCredentialReference(parameterName));
    }

    @ParameterizedTest
    @MethodSource(value = "getServiceKeyCredentialParameters")
    void testParseOfServiceKeyName(String dynamicParameter, String relationshipName, String serviceKeyName,
                                   String credentialFieldPath) {
        String parameterName = DynamicParameterUtil.getParameterName(dynamicParameter);
        assertEquals(serviceKeyName, DynamicParameterUtil.getServiceKeyName(parameterName));
    }

    @ParameterizedTest
    @MethodSource(value = "getServiceKeyCredentialParameters")
    void testParseOfCredentialFieldPath(String dynamicParameter, String relationshipName, String serviceKeyName,
                                        String credentialFieldPath) {
        String parameterName = DynamicParameterUtil.getParameterName(dynamicParameter);
        assertEquals(credentialFieldPath, DynamicParameterUtil.getCredentialFieldPath(parameterName));
    }

}
