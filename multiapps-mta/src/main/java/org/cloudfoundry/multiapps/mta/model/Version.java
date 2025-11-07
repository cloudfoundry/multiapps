package org.cloudfoundry.multiapps.mta.model;

import java.text.MessageFormat;

import org.cloudfoundry.multiapps.common.ParsingException;
import org.cloudfoundry.multiapps.mta.Messages;

import org.semver4j.Semver;
import org.semver4j.SemverException;

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

    public static Version parseVersion(String versionString) {
        var version = Semver.coerce(versionString);
        if (version == null) {
            throw new ParsingException(MessageFormat.format(Messages.UNABLE_TO_PARSE_VERSION, versionString));
        }
        return new Version(version);
    }

    @Override
    public int hashCode() {
        return version.hashCode();
    }

    @Override
    public String toString() {
        return version.toString();
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
            // The second parameter is preventing the default behavior of Semver to treat the suffix as a pre-release tag.
            return version.satisfies(requirement, true);
        } catch (SemverException e) {
            throw new IllegalArgumentException(MessageFormat.format(Messages.UNABLE_TO_PARSE_VERSION_REQUIREMENT, requirement));
        }
    }
}
