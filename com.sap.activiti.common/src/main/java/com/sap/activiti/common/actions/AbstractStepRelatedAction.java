package com.sap.activiti.common.actions;

import org.activiti.engine.runtime.Job;

abstract class AbstractStepRelatedAction extends AbstractTraceableAction {
    private String jobId;

    public AbstractStepRelatedAction(String processInstanceId, String userId, String reasonMessage, String jobId) {
        super(processInstanceId, userId, reasonMessage);
        this.jobId = jobId;
    }

    protected LogicalStepNameProvider getLogicalStepNameProvider() {
        Job job = getManagementService().createJobQuery().jobId(getJobId()).singleResult();
        try {
            return new LogicalStepNameProvider(getDefaultProcessEngine(), job);
        } catch (LogicalStepNameProviderException e) {
            throw new IllegalStateException(e);
        }
    }

    protected String getActivityId() {
        return getLogicalStepNameProvider().getActivityId();
    }

    protected String getJobId() {
        return this.jobId;
    }
}
