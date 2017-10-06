package com.sap.cloud.lm.sl.mta.model;

import java.util.Arrays;
import java.util.List;

public class SupportedVersions {

    public static final List<String> SUPPORTED_VERSIONS = Arrays.asList("1.0", "2.0", "2.1", "2.2", "3.0", "3.1");

    public static boolean isSupported(Version schemaVersion) {
        for (String rule : SUPPORTED_VERSIONS) {
            if (schemaVersion.satisfies(rule)) {
                return true;
            }
        }
        return false;
    }

}
