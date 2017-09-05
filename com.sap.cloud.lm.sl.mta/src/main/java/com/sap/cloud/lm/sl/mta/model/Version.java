package com.sap.cloud.lm.sl.mta.model;

import java.text.MessageFormat;

import com.sap.cloud.lm.sl.mta.message.Messages;
import com.sap.cloud.lm.sl.mta.parsers.VersionParser;
import com.vdurmont.semver4j.Semver;
import com.vdurmont.semver4j.Semver.SemverType;
import com.vdurmont.semver4j.SemverException;

public class Version implements Comparable<Version> {

    private final Semver version;

    private Version(Semver version) {
        this.version = version;
    }

    public int getMajor() {
        return version.getMajor();
    }

    public int getMinor() {
        return version.getMinor();
    }

    public int getPatch() {
        return version.getPatch();
    }

    public String getBuild() {
        return version.getBuild();
    }

    public String[] getSuffixTokens() {
        return version.getSuffixTokens();
    }

    public static Version parseVersion(String versionString) {
        try {
            String parsedVersion = new VersionParser().parse(versionString);
            return new Version(new Semver(parsedVersion, SemverType.NPM));
        } catch (SemverException e) {
            throw new IllegalArgumentException(MessageFormat.format(Messages.UNABLE_TO_PARSE_VERSION, versionString), e);
        }
    }

    @Override
    public int hashCode() {
        return version.hashCode();
    }

    @Override
    public String toString() {
        return version.getValue();
    }

    @Override
    public int compareTo(Version o) {
        return version.compareTo(o.version);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Version other = (Version) obj;
        return this.version.equals(other.version);
    }

    public boolean satisfies(String requirement) {
        try {
            return version.satisfies(requirement);
        } catch (SemverException e) {
            throw new IllegalArgumentException(MessageFormat.format(Messages.UNABLE_TO_PARSE_VERSION_REQUIREMENT, requirement));
        }
    }
}
