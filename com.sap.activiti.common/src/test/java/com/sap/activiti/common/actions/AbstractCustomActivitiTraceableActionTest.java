package com.sap.activiti.common.actions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.identity.User;
import org.apache.commons.io.Charsets;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;

import com.sap.activiti.common.ActivitiIdentityServiceRule;
import com.sap.activiti.common.ActivitiTestCfgRuleChain;
import com.sap.activiti.common.Constants;
import com.sap.activiti.common.MockProcessEngine;

public class AbstractCustomActivitiTraceableActionTest {

    public enum TestType {
        TEST1, TEST2
    };

    private static final String ANONYMOUS_USER_NAME = "ANONYMOUS";

    @Rule
    public RuleChain activitiChain = ActivitiTestCfgRuleChain.getChain()
        .around(new ActivitiIdentityServiceRule());

    @Test
    public void whenNullUserIsPassed_thenAnnonymousIsReturned() {
        AbstractTraceableAction action = createActionWithUser(null);

        assertEquals(ANONYMOUS_USER_NAME, action.getUserName());
    }

    @Test
    public void whenEmptyUserIsPassed_thenAnnonymousIsReturned() {
        AbstractTraceableAction action = createActionWithUser("");

        assertEquals(ANONYMOUS_USER_NAME, action.getUserName());
    }

    @Test
    public void whenBothUserNamesAreMissing_thenUserIdIsReturned() {
        IdentityService identityService = ActivitiTestCfgRuleChain.getActivitiRule()
            .getIdentityService();
        User newUser = identityService.newUser("i123456");
        identityService.saveUser(newUser);

        AbstractTraceableAction action = createActionWithUser("i123456");
        assertEquals("i123456", action.getUserName());
        identityService.deleteUser("i123456");
    }

    @Test
    public void whenUserInfoNotFound_thenUserIdIsReturned() {
        String notPersistedUser = "i0000000";
        AbstractTraceableAction action = createActionWithUser(notPersistedUser);

        assertEquals(notPersistedUser, action.getUserName());
    }

    @Test
    public void whenUserInfoFound_thenUserNameIsReturned() {
        AbstractTraceableAction action = createActionWithUser(ActivitiIdentityServiceRule.TEST_USER);
        assertTrue(action.getUserName()
            .contains(ActivitiIdentityServiceRule.TEST_FIRST_NAME)
            && action.getUserName()
                .contains(ActivitiIdentityServiceRule.TEST_LAST_NAME));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testAppendActionLog() {
        String timestamp = "test";
        String msg1 = "testMsg1";
        String msg2 = "testMsg2";

        ProcessEngine processEngine = MockProcessEngine.createMockInstance();
        AbstractTraceableAction action = spy(createActionWithUser(ActivitiIdentityServiceRule.TEST_USER));
        when(action.getDefaultProcessEngine()).thenReturn(processEngine);
        when(action.getCurrentTimestamp()).thenReturn(timestamp);

        when(action.getType()).thenReturn((Enum) TestType.TEST1);
        action.logTracibilityInformation(msg1);

        verify(processEngine.getRuntimeService()).setVariable("", Constants.ACTION_LOG, String.format("[%s] %s\n", timestamp, msg1)
            .getBytes(Charsets.UTF_8));
        verify(processEngine.getRuntimeService()).setVariable("", Constants.EXECUTED_ACTIONS_TYPE_LOG,
            String.format("%s\n", TestType.TEST1.name()
                .toUpperCase())
                .getBytes(Charsets.UTF_8));

        when(action.getType()).thenReturn((Enum) TestType.TEST2);
        when(action.getDefaultProcessEngine()
            .getRuntimeService()
            .getVariable("", Constants.ACTION_LOG)).thenReturn(String.format("[%s] %s\n", timestamp, msg1)
                .getBytes(Charsets.UTF_8));
        when(action.getDefaultProcessEngine()
            .getRuntimeService()
            .getVariable("", Constants.EXECUTED_ACTIONS_TYPE_LOG)).thenReturn(String
                .format("%s\n", TestType.TEST1.name()
                    .toUpperCase())
                .getBytes(Charsets.UTF_8));

        action.logTracibilityInformation(msg2);

        verify(processEngine.getRuntimeService()).setVariable("", Constants.ACTION_LOG,
            String.format("[%s] %s\n[%s] %s\n", timestamp, msg1, timestamp, msg2)
                .getBytes(Charsets.UTF_8));
        verify(processEngine.getRuntimeService()).setVariable("", Constants.EXECUTED_ACTIONS_TYPE_LOG,
            String.format("%s\n%s\n", TestType.TEST1.name()
                .toUpperCase(),
                TestType.TEST2.name()
                    .toUpperCase())
                .getBytes(Charsets.UTF_8));
    }

    private AbstractTraceableAction createActionWithUser(String userName) {
        return new AbstractTraceableAction("", userName, "") {

            @Override
            public void execute() {
            }

            @Override
            public TestType getType() {
                return TestType.TEST1;
            }
        };
    }
}
