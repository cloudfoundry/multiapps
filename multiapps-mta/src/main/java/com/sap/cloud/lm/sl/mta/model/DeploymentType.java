package com.sap.cloud.lm.sl.mta.model;

public enum DeploymentType {

    DEPLOYMENT, REDEPLOYMENT, UPGRADE, DOWNGRADE;

    public static DeploymentType fromVersions(Version oldVersion, Version newVersion) {
        if (oldVersion == null) {
            return DEPLOYMENT;
        }
        int comparisonResult = oldVersion.compareTo(newVersion);
        if (comparisonResult > 0) {
            return DOWNGRADE;
        }
        if (comparisonResult < 0) {
            return UPGRADE;
        }
        return REDEPLOYMENT;
    }

}
