package com.sap.cloud.lm.sl.slp.activiti.action;

import java.util.List;

import com.sap.cloud.lm.sl.slp.activiti.ActivitiFacade;
import com.sap.lmsl.slp.SlpTaskState;

public class AbortActivitiAction extends ActivitiAction {

    public AbortActivitiAction(ActivitiFacade activitiFacade, String userId) {
        super(activitiFacade, userId);
    }

    @Override
    public void executeAction(String superProcessInstanceId) {
        List<String> executionIds = getActiveExecutionIds(superProcessInstanceId);
        for (String executionId : executionIds) {
            activitiFacade.deleteProcessInstance(userId, executionId, SlpTaskState.SLP_TASK_STATE_ABORTED.value());
        }
    }

}
