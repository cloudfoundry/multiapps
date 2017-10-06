package com.sap.cloud.lm.sl.slp.activiti.action;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.activiti.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.JsonUtil;
import com.sap.cloud.lm.sl.common.util.TestUtil;
import com.sap.cloud.lm.sl.slp.activiti.ActivitiFacade;

@RunWith(Parameterized.class)
public class ResumeActivitiActionTest {

    private ActivitiFacade activitiFacade;
    private ActivitiAction activitiAction;
    private ActionInput input = new ActionInput();

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            {
                "resume-action-input-00.json"
            },
            {
                "resume-action-input-01.json"
            }
// @formatter:on
        });
    }

    public ResumeActivitiActionTest(String inputLocation) throws ParsingException, IOException {
        this.input = JsonUtil.fromJson(TestUtil.getResourceAsString(inputLocation, ResumeActivitiAction.class), ActionInput.class);
    }

    @Before
    public void prepareAction() {
        activitiFacade = Mockito.mock(ActivitiFacade.class);
        Mockito.when(activitiFacade.getActiveHistoricSubProcessIds(input.superProcessId)).thenReturn(input.activeSubprocessIds);
        for (String processId : input.activeSubprocessIds) {
            mockActivityType(processId);
        }
        mockActivityType(input.superProcessId);
        activitiAction = new ResumeActivitiAction(activitiFacade, input.userId);
    }

    private void mockActivityType(String processId) {
        String executionActivityId = input.subProcessActivityIds.get(processId);
        Execution processExecution = mockExecution(executionActivityId);
        Mockito.when(activitiFacade.getProcessExecution(processId)).thenReturn(processExecution);
        Mockito.when(activitiFacade.getActivityType(processId, processExecution.getActivityId())).thenReturn(executionActivityId);
    }

    private Execution mockExecution(String executionActivityId) {
        Execution processExecution = Mockito.mock(Execution.class);
        Mockito.when(processExecution.getActivityId()).thenReturn(executionActivityId);
        return processExecution;
    }

    @Test
    public void testExecute() {
        activitiAction.executeAction(input.superProcessId);

        validateExecution();
    }

    private void validateExecution() {
        String receiveTaskProcessId = getReceiveTaskProcessId();
        if (receiveTaskProcessId != null) {
            Mockito.verify(activitiFacade).signal(input.userId, receiveTaskProcessId);
        }
    }

    private String getReceiveTaskProcessId() {
        for (Entry<String, String> subProcessActivityids : input.subProcessActivityIds.entrySet()) {
            if (subProcessActivityids.getKey().equals("receiveTask")) {
                return subProcessActivityids.getKey();
            }
        }
        return null;
    }

    private static class ActionInput {
        List<String> activeSubprocessIds;
        String superProcessId;
        String userId;
        Map<String, String> subProcessActivityIds;
    }
}
