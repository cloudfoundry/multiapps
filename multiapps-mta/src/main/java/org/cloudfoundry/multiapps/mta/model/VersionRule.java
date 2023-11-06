package org.cloudfoundry.multiapps.mta.model;

import java.util.function.Predicate;

public enum VersionRule {

    SAME_HIGHER(VersionRule::isNotDowngrade),

    HIGHER(VersionRule::isUpgrade),

    ALL(VersionRule::isAny);

    private final Predicate<DeploymentType> versionRuleValidator;

    VersionRule(Predicate<DeploymentType> versionRuleValidator) {
        this.versionRuleValidator = versionRuleValidator;
    }

    public boolean allows(DeploymentType deploymentType) {
        return versionRuleValidator.test(deploymentType);
    }

    private static boolean isNotDowngrade(DeploymentType deploymentType) {
        return deploymentType != DeploymentType.DOWNGRADE;
    }

    private static boolean isUpgrade(DeploymentType deploymentType) {
        return isNotDowngrade(deploymentType) && deploymentType != DeploymentType.REDEPLOYMENT;
    }

    private static boolean isAny(DeploymentType deploymentType) {
        return true;
    }
}
