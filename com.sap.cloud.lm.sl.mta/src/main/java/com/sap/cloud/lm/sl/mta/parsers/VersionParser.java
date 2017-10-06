package com.sap.cloud.lm.sl.mta.parsers;

import java.util.Arrays;

import com.sap.cloud.lm.sl.common.util.CommonUtil;
import com.sap.cloud.lm.sl.mta.model.SupportedVersions;
import com.sap.cloud.lm.sl.mta.model.VersionComponent;
import com.sap.cloud.lm.sl.mta.model.VersionComponent.VersionComponentBuilder;
import com.vdurmont.semver4j.Semver;
import com.vdurmont.semver4j.Semver.SemverType;

public class VersionParser {

    private static final String VERSION_STRING_TEMPLATE = "%s.%s.%s%s%s";
    private static final String VERSION_SUFFIX_STRING_TEMPLATE = "-%s";
    private static final String VERSION_BUILD_STRING_TEMPLATE = "+%s";
    private static final String DEFAULT_VERSION_SUFFIX = "";

    public String parse(String version) {
        Semver providedVersion = new Semver(version, SemverType.LOOSE);
        Integer majorVersion = providedVersion.getMajor();
        Integer minorVersion = providedVersion.getMinor();
        Integer patchVersion = providedVersion.getPatch();

        VersionComponent versionComponent = new VersionComponentBuilder(SupportedVersions.SUPPORTED_VERSIONS).build();
        VersionComponent minorVersionComponent = versionComponent.getSubVersions(majorVersion);
        if (minorVersion == null) {
            minorVersion = minorVersionComponent.getLatest();
        }
        if (patchVersion == null) {
            patchVersion = 0;
        }
        return buildVersionString(majorVersion, minorVersion, patchVersion, providedVersion.getSuffixTokens(), providedVersion.getBuild());
    }

    private String buildVersionString(int major, int minor, int patch, String[] suffixTokens, String buildVersion) {
        String formattedSuffixTokens = formattedSuffixTokens(suffixTokens);
        String formattedBuildVersion = formattedVersionBuildString(buildVersion);
        return String.format(VERSION_STRING_TEMPLATE, major, minor, patch, formattedSuffixTokens, formattedBuildVersion);
    }

    private String formattedVersionBuildString(String buildVersion) {
        if (buildVersion == null) {
            return DEFAULT_VERSION_SUFFIX;
        }
        return String.format(VERSION_BUILD_STRING_TEMPLATE, buildVersion);
    }

    private String formattedSuffixTokens(String... suffixTokens) {
        if (suffixTokens.length == 0) {
            return DEFAULT_VERSION_SUFFIX;
        }
        return String.format(VERSION_SUFFIX_STRING_TEMPLATE, CommonUtil.joinStrings(Arrays.asList(suffixTokens), "", "."));
    }
}
