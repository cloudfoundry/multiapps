package org.cloudfoundry.multiapps.mta.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupportedVersions {

    private static final Map<Integer, List<Integer>> SUPPORTED_VERSIONS;

    static {
        SUPPORTED_VERSIONS = new HashMap<>();
        SUPPORTED_VERSIONS.put(2, Arrays.asList(0, 1, 2));
        SUPPORTED_VERSIONS.put(3, Arrays.asList(0, 1, 2, 3));
    }

    public static boolean isSupported(Version schemaVersion) {
        int majorSchemaVersion = schemaVersion.getMajor();
        return SUPPORTED_VERSIONS.keySet()
                                 .stream()
                                 .anyMatch(majorVersion -> majorVersion.equals(majorSchemaVersion));
    }

    public static boolean isFullySupported(Version schemaVersion) {
        int majorSchemaVersion = schemaVersion.getMajor();
        int minorSchemaVersion = schemaVersion.getMinor();

        if (!SUPPORTED_VERSIONS.containsKey(majorSchemaVersion)) {
            return false;
        }

        return SUPPORTED_VERSIONS.get(majorSchemaVersion)
                                 .stream()
                                 .anyMatch(minorVersion -> minorVersion.equals(minorSchemaVersion));
    }

}
