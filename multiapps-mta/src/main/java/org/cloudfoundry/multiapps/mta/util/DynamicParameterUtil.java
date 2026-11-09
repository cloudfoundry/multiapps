package org.cloudfoundry.multiapps.mta.util;

public final class DynamicParameterUtil {

    public static final String PATTERN_FOR_DYNAMIC_PARAMETERS = "'{'ds/{0}/{1}'}'";
    public static final String REGEX_PATTERN_FOR_DYNAMIC_PARAMETERS = "^\\{ds[A-Za-z0-9_\\-\\.\\/]+\\}$";

    private static final String SERVICE_KEY_REFERENCE_PREFIX = "service-keys.";
    private static final String CREDENTIALS_SEPARATOR = ".credentials.";

    private DynamicParameterUtil() {
    }

    public static String getRelationshipName(String dynamicParameter) {
        return dynamicParameter.substring(dynamicParameter.indexOf("/") + 1, dynamicParameter.lastIndexOf("/"));
    }

    public static String getParameterName(String dynamicParameter) {
        return dynamicParameter.substring(dynamicParameter.lastIndexOf("/") + 1, dynamicParameter.lastIndexOf("}"));
    }

    public static boolean isServiceKeyCredentialReference(String parameterName) {
        return parameterName.startsWith(SERVICE_KEY_REFERENCE_PREFIX) && parameterName.contains(CREDENTIALS_SEPARATOR);
    }

    public static String getServiceKeyName(String parameterName) {
        return parameterName.substring(SERVICE_KEY_REFERENCE_PREFIX.length(), parameterName.indexOf(CREDENTIALS_SEPARATOR));
    }

    public static String getCredentialFieldPath(String parameterName) {
        return parameterName.substring(parameterName.indexOf(CREDENTIALS_SEPARATOR) + CREDENTIALS_SEPARATOR.length());
    }

}
