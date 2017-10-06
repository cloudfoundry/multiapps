package com.sap.cloud.lm.sl.slp.activiti.action;

import java.util.List;

import org.activiti.engine.runtime.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.lm.sl.slp.activiti.ActivitiFacade;

public class ResumeActivitiAction extends ActivitiAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResumeActivitiAction.class);

    public ResumeActivitiAction(ActivitiFacade activitiFacade, String userId) {
        super(activitiFacade, userId);
    }

    @Override
    public void executeAction(String superProcessInstanceId) {
        List<String> activeProcessIds = getActiveExecutionIds(superProcessInstanceId);
        String processInReceiveTask = findProcessInReceiveTask(activeProcessIds);
        if (processInReceiveTask == null) {
            LOGGER.warn("There is no process at a receiveTask activity");
            return;
        }
        activitiFacade.signal(userId, processInReceiveTask);
    }

    private String findProcessInReceiveTask(List<String> activeProcessIds) {
        for (String processId : activeProcessIds) {
            Execution processExecution = activitiFacade.getProcessExecution(processId);
            String activitiType = getActivitiType(processId, processExecution.getActivityId());
            if (activitiType.equals("receiveTask")) {
                return processId;
            }
        }
        return null;
    }

    private String getActivitiType(String processInstanceId, String activityId) {
        return activitiFacade.getActivityType(processInstanceId, activityId);
    }

}
