package com.sap.cloud.lm.sl.mta.model;

public enum VersionRule {

    SAME_HIGHER(new VersionRuleValidator() {
        @Override
        public boolean allows(DeploymentType deploymentType) {
            return deploymentType != DeploymentType.DOWNGRADE;
        }
    }),

    HIGHER(new VersionRuleValidator() {
        @Override
        public boolean allows(DeploymentType deploymentType) {
            return deploymentType != DeploymentType.DOWNGRADE && deploymentType != DeploymentType.REDEPLOYMENT;
        }
    }),

    ALL(new VersionRuleValidator() {
        @Override
        public boolean allows(DeploymentType deploymentType) {
            return true;
        }
    });

    private VersionRuleValidator versionRuleValidator;

    private VersionRule(VersionRuleValidator versionRuleValidator) {
        this.versionRuleValidator = versionRuleValidator;
    }

    public boolean allows(DeploymentType deploymentType) {
        return versionRuleValidator.allows(deploymentType);
    }

    private interface VersionRuleValidator {
        boolean allows(DeploymentType deploymentType);
    }

}
