package com.sap.activiti.common.actions;

public class ActivitiWriteMemoAction extends AbstractStepRelatedAction {
    private static final String LOG_MESSAGE = "User [%s] wrote memo on step [%s]: %s";

    public ActivitiWriteMemoAction(String processInstanceId, String userId, String reasonMessage, String jobId) {
        super(processInstanceId, userId, reasonMessage, jobId);
    }

    @Override
    public void execute() {
        logTracibilityInformation(String.format(LOG_MESSAGE, getUserName(), getActivityId(), getReasonMessage()));
    }

    @Override
    public ActionType getType() {
        return IActivitiAction.ActionType.WRITE_MEMO;
    }
}
