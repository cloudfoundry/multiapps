package com.sap.activiti.common;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.junit.rules.ExternalResource;

public class ActivitiIdentityServiceRule extends ExternalResource {

    public static final String TEST_LAST_NAME = "testLastName";
    public static final String TEST_FIRST_NAME = "testFirstName";
    public static final String TEST_EMAIL = "test.user@sap.com";
    public static final String TEST_USER = "TEST_USER";
    public static User testUser;

    @Override
    protected void before() throws Throwable {
        IdentityService identityService = ActivitiTestCfgRuleChain.getActivitiRule().getIdentityService();
        prepareUserTable(identityService);
        identityService.setAuthenticatedUserId(TEST_USER);
    }

    @Override
    protected void after() {
        IdentityService identityService = ActivitiTestCfgRuleChain.getActivitiRule().getIdentityService();
        identityService.setAuthenticatedUserId(null);
        identityService.deleteUser(TEST_USER);
    }

    private void prepareUserTable(IdentityService identityService) {
        testUser = identityService.newUser(TEST_USER);
        testUser.setEmail(TEST_EMAIL);
        testUser.setFirstName(TEST_FIRST_NAME);
        testUser.setLastName(TEST_LAST_NAME);
        identityService.saveUser(testUser);
    }
}
