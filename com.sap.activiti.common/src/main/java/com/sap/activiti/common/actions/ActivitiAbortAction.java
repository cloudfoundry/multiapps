package com.sap.activiti.common.actions;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.runtime.ProcessInstance;

public class ActivitiAbortAction extends AbstractTraceableAction {
    private static final String LOG_MESSAGE = "User [%s] called abort for process [%s]. Reason: %s";

    public ActivitiAbortAction(String processInstanceId, String userId, String reasonMessage) {
        super(processInstanceId, userId, reasonMessage);
    }

    @Override
    public void execute() {
        String processInstanceId = getProcessInstanceId();
        ensureProcessExists(processInstanceId);

        logTracibilityInformation(String.format(LOG_MESSAGE, getUserName(), processInstanceId, getReasonMessage()));

        deleteProcess(processInstanceId);
    }

    private void ensureProcessExists(String processInstanceId) {
        ProcessInstance processInstance = getDefaultProcessEngine().getRuntimeService().createProcessInstanceQuery().processInstanceId(
            processInstanceId).singleResult();
        if (processInstance == null) {
            throw new ActivitiObjectNotFoundException("Couldn't find process instance " + processInstanceId, ProcessInstance.class);
        }
    }

    private void deleteProcess(String processInstanceId) {
        getDefaultProcessEngine().getRuntimeService().deleteProcessInstance(processInstanceId, super.getReasonMessage());
    }

    @Override
    public ActionType getType() {
        return IActivitiAction.ActionType.ABORT;
    }
}
