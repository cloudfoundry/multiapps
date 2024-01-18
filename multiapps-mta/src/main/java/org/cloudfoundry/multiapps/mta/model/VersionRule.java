package org.cloudfoundry.multiapps.mta.model;

public enum VersionRule {

    SAME_HIGHER(VersionRule::isNotDowngrade),

    HIGHER(VersionRule::isUpgrade),

    ALL(VersionRule::isAny);

    private interface VersionRuleValidator {
        boolean allows(DeploymentType deploymentType);
    }

    private VersionRuleValidator versionRuleValidator;

    VersionRule(VersionRuleValidator versionRuleValidator) {
        this.versionRuleValidator = versionRuleValidator;
    }

    public boolean allows(DeploymentType deploymentType) {
        return versionRuleValidator.allows(deploymentType);
    }

    public static VersionRule value(String caseInsensitiveValue) {
        return VersionRule.valueOf(caseInsensitiveValue.toUpperCase());
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
