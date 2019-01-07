package com.sap.cloud.lm.sl.mta.model;

import java.util.Arrays;
import java.util.List;

public class SupportedVersions {

    public static final List<String> SUPPORTED_VERSIONS = Arrays.asList("1.0", "2.0", "2.1", "2.2", "3.0", "3.1", "3.2");

    public static boolean isSupported(Version schemaVersion) {
        return SUPPORTED_VERSIONS.stream()
            .anyMatch(schemaVersion::satisfies);
    }

}
