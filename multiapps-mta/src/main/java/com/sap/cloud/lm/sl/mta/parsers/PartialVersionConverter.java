package com.sap.cloud.lm.sl.mta.parsers;

import com.vdurmont.semver4j.Semver;
import com.vdurmont.semver4j.Semver.SemverType;

public class PartialVersionConverter {

    private static final String VERSION_STRING_TEMPLATE = "%s.%s.%s%s%s";
    private static final String VERSION_SUFFIX_STRING_TEMPLATE = "-%s";
    private static final String VERSION_BUILD_STRING_TEMPLATE = "+%s";
    private static final String DEFAULT_VERSION_SUFFIX = "";

    public String convertToFullVersionString(String partialVersionString) {
        Semver partialVersion = new Semver(partialVersionString, SemverType.LOOSE);
        Integer majorVersion = partialVersion.getMajor();
        Integer minorVersion = partialVersion.getMinor();
        Integer patchVersion = partialVersion.getPatch();

        if (minorVersion == null) {
            minorVersion = 0;
        }
        if (patchVersion == null) {
            patchVersion = 0;
        }
        return buildVersionString(majorVersion, minorVersion, patchVersion, partialVersion.getSuffixTokens(), partialVersion.getBuild());
    }

    private String buildVersionString(int major, int minor, int patch, String[] suffixTokens, String buildVersion) {
        String formattedSuffixTokens = formatSuffixTokens(suffixTokens);
        String formattedBuildVersion = formatBuildVersion(buildVersion);
        return String.format(VERSION_STRING_TEMPLATE, major, minor, patch, formattedSuffixTokens, formattedBuildVersion);
    }

    private String formatSuffixTokens(String[] suffixTokens) {
        if (suffixTokens.length == 0) {
            return DEFAULT_VERSION_SUFFIX;
        }
        return String.format(VERSION_SUFFIX_STRING_TEMPLATE, String.join(".", suffixTokens));
    }

    private String formatBuildVersion(String buildVersion) {
        if (buildVersion == null) {
            return DEFAULT_VERSION_SUFFIX;
        }
        return String.format(VERSION_BUILD_STRING_TEMPLATE, buildVersion);
    }

}
