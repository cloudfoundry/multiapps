package com.sap.cloud.lm.sl.slp.activiti.action;

import java.util.LinkedList;
import java.util.List;

import com.sap.cloud.lm.sl.slp.activiti.ActivitiFacade;

public abstract class ActivitiAction {
    protected ActivitiFacade activitiFacade;
    protected String userId;

    public ActivitiAction(ActivitiFacade activitiFacade, String userId) {
        this.activitiFacade = activitiFacade;
        this.userId = userId;
    }

    public abstract void executeAction(String superProcessInstanceId);

    protected List<String> getActiveExecutionIds(String superProcessInstanceId) {
        List<String> activeHistoricSubProcessIds = activitiFacade.getActiveHistoricSubProcessIds(superProcessInstanceId);
        LinkedList<String> subProcessIds = new LinkedList<>(activeHistoricSubProcessIds);
        subProcessIds.addFirst(superProcessInstanceId);
        return subProcessIds;
    }
}
