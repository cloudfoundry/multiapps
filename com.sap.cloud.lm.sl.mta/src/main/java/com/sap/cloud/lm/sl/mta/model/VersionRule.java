package com.sap.cloud.lm.sl.mta.model;

import static com.sap.cloud.lm.sl.mta.message.Messages.SAME_OR_HIGHER_VERSION_ALREADY_DEPLOYED;
import static com.sap.cloud.lm.sl.mta.message.Messages.HIGHER_VERSION_ALREADY_DEPLOYED;

public enum VersionRule {

    HIGHER(SAME_OR_HIGHER_VERSION_ALREADY_DEPLOYED, new VersionHandler() {
        @Override
        public boolean accept(Version candidateVersion, Version baseVersion) {
            return candidateVersion.compareTo(baseVersion) > 0;
        }
    }),

    SAME_HIGHER(HIGHER_VERSION_ALREADY_DEPLOYED, new VersionHandler() {
        @Override
        public boolean accept(Version candidateVersion, Version baseVersion) {
            return candidateVersion.compareTo(baseVersion) >= 0;
        }
    }),

    ALL("", new VersionHandler() {
        @Override
        public boolean accept(Version candidateVersion, Version baseVersion) {
            return true;
        }
    });

    public boolean accept(Version candidateVersion, Version baseVersion) {
        return versionHandler.accept(candidateVersion, baseVersion);
    }

    String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    private VersionRule(String errorMessage, VersionHandler versionHandler) {
        this.errorMessage = errorMessage;
        this.versionHandler = versionHandler;
    }

    private interface VersionHandler {
        boolean accept(Version candidateVersion, Version baseVersion);
    }

    private VersionHandler versionHandler;

}
