package com.sap.activiti.common.actions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;

import com.sap.activiti.common.ActivitiIdentityServiceRule;
import com.sap.activiti.common.ActivitiTestCfgRuleChain;

public class ActivitiAbortActionTest {
    private static final String NON_EXISTING_PROCESS_ID = "nn";

    private String procInstId;
    private ActivitiAbortActionTestPO pageObject;
    private ActivitiAbortAction abortService;

    @Rule
    public RuleChain activitiChain = ActivitiTestCfgRuleChain.getChain()
        .around(new ActivitiIdentityServiceRule());

    @Before
    public void setUp() throws Exception {
        this.pageObject = new ActivitiAbortActionTestPO();
        this.procInstId = pageObject.startTestProcess();
        abortService = pageObject.createService(procInstId);
    }

    @Test
    public void testIfProcessIsRunningAfterTheSetUp() {
        assertTrue("The process must be running before the test starts!", pageObject.isProcessRunning(procInstId));
    }

    @Test
    public void whenProcessIsAborted_thenUserIsSetInContext() {
        abortService.execute();

        assertTrue("The context should contain the name of the aborter.", pageObject.isUserSetInContext(procInstId));
    }

    @Test
    public void whenProcessIsAborted_thenReasonIsSetInContext() {
        abortService.execute();

        assertTrue("The context should contain the reason for abortion", pageObject.isAbortReasonSetInContext(procInstId));
    }

    @Test
    public void whenProcessIsAborted_thenTypeIsSetInContext() {
        abortService.execute();

        assertTrue("The context should contain the abort type",
            pageObject.isExecutedActionTypeSetInContext(IActivitiAction.ActionType.ABORT, procInstId));
    }

    @Test(expected = ActivitiObjectNotFoundException.class)
    public void whenNoProcessWithThatIdIsRunning_thenNotFoundExceptionIsThrown() {
        new ActivitiAbortAction(NON_EXISTING_PROCESS_ID, ActivitiIdentityServiceRule.TEST_USER, "").execute();
    }

    @Test
    public void whenProcessIsAborted_thenItIsRemovedFromTheRuntime() {
        abortService.execute();

        assertFalse("The process should not exist in runtime after abortion.", pageObject.isProcessRunning(procInstId));
    }
}
