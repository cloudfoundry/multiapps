package org.cloudfoundry.multiapps.mta.util;

public final class DynamicParameterUtil {

    public static final String PATTERN_FOR_DYNAMIC_PARAMETERS = "'{'ds/{0}/{1}'}'";
    public static final String REGEX_PATTERN_FOR_DYNAMIC_PARAMETERS = "^\\{ds[A-Za-z0-9_\\-\\.\\/]+\\}$";

    private DynamicParameterUtil() {
    }

    public static String getRelationshipName(String dynamicParameter) {
        return dynamicParameter.substring(dynamicParameter.indexOf("/") + 1, dynamicParameter.lastIndexOf("/"));
    }

    public static String getParameterName(String dynamicParameter) {
        return dynamicParameter.substring(dynamicParameter.lastIndexOf("/") + 1, dynamicParameter.lastIndexOf("}"));
    }

}
