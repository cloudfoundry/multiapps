package com.sap.activiti.common.actions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.activiti.engine.ActivitiException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;

import com.sap.activiti.common.ActivitiIdentityServiceRule;
import com.sap.activiti.common.ActivitiTestCfgRuleChain;
import com.sap.activiti.common.Constants;
import com.sap.activiti.common.EmptyActivitiStep;
import com.sap.activiti.common.ExecutionStatus;

public class ActivitiRetryActionTest {

    private static final String TASK_NAME = "EmptyActivitiStep";
    private static final String STATUS_VARIABLE = Constants.STEP_NAME_PREFIX + TASK_NAME;

    private ActivitiRetryAction service;

    @Rule
    public RuleChain activitiChain = ActivitiTestCfgRuleChain.getChain()
        .around(new ActivitiIdentityServiceRule());

    private ActivitiRetryActionTestPO pageObject;

    @Before
    public void before() throws Exception {
        pageObject = new ActivitiRetryActionTestPO();

        pageObject.init();
        service = pageObject.getService();
    }

    @Test(expected = ActivitiException.class)
    public void testExceptionIsThrownWhenJobFails() throws Exception {
        try {
            service.execute();
        } catch (ActivitiException e) {
            assertEquals(EmptyActivitiStep.TEST_EXCEPTION_MESSAGE, e.getCause()
                .getMessage());

            throw e;
        }
    }

    @Test
    public void testResetTryCountOnRetry() throws Exception {
        pageObject.initWithTryCount(TASK_NAME);
        assertEquals(ActivitiRetryActionTestPO.DEFAULT_TRYCOUNT,
            pageObject.getVariable(ActivitiRetryAction.TRYCOUNT_CONTEXT_PREFIX + TASK_NAME));
        pageObject.retryFailingJobSafely();
        assertEquals(0, pageObject.getVariable(ActivitiRetryAction.TRYCOUNT_CONTEXT_PREFIX + TASK_NAME));
    }

    @Test
    public void testResetTryCountForNonRetyTaskOnRetry() throws Exception {
        assertNull(pageObject.getVariable(ActivitiRetryAction.TRYCOUNT_CONTEXT_PREFIX + TASK_NAME));
        pageObject.retryFailingJobSafely();
        assertNull(pageObject.getVariable(ActivitiRetryAction.TRYCOUNT_CONTEXT_PREFIX + TASK_NAME));
    }

    @Test
    public void testTracibilityInfoIsSetInContextAfterFailedRetry() throws Exception {
        pageObject.retryFailingJobSafely();
        assertTrue(pageObject.hasTracibilityInfo());
    }

    @Test
    public void testActionTypeIsSetInContextAfterFailedRetry() throws Exception {
        pageObject.retryFailingJobSafely();
        assertTrue(pageObject.hasExecutedActionsTypeLog());
    }

    @Test
    public void testTracibilityInfoIsSetInContextAfterSuccessfulRetry() throws Exception {
        pageObject.removeFailureVariable();
        service.execute();

        assertTrue(pageObject.hasTracibilityInfo());
    }

    @Test
    public void testServiceSetsFailedStatusInContext() throws Exception {
        pageObject.retryFailingJobSafely();

        String status = (String) pageObject.getVariable(STATUS_VARIABLE);

        assertEquals(ExecutionStatus.FAILED.name(), status);
    }

    @Test
    public void testOkStatusReturnedWhenJobIsSuccessful() throws Exception {
        pageObject.removeFailureVariable();
        service.execute();
    }
}
