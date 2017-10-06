package com.sap.activiti.common.actions;

import com.sap.activiti.common.util.ISkipHelper;
import com.sap.activiti.common.util.impl.EngineServicesSkipHelper;

public class ActivitiSkipAction extends AbstractStepRelatedAction {
    private static final String LOG_MESSAGE = "User [%s] called skip for step [%s]. Reason: %s";

    public ActivitiSkipAction(String processInstanceId, String userId, String reasonMessage, String jobId) {
        super(processInstanceId, userId, reasonMessage, jobId);
    }

    @Override
    public void execute() {
		try {

			logActionInContext();
			prepareContext();
			executeJob();

		} catch (LogicalStepNameProviderException e) {
			throw new IllegalStateException(e);
		}
    }

    private void logActionInContext() {
        logTracibilityInformation(String.format(LOG_MESSAGE, getUserName(), getActivityId(), getReasonMessage()));
    }

    private void executeJob() {
        getDefaultProcessEngine().getManagementService().executeJob(getJobId());
    }

	private void prepareContext() throws LogicalStepNameProviderException {
		createSkipHelper().createSkipRequest(getLogicalStepNameProvider().getLogicalStepName());
	}

	private ISkipHelper createSkipHelper() {
		return new EngineServicesSkipHelper(getDefaultProcessEngine(), getProcessInstanceId());
	}

	@Override
    public ActionType getType() {
        return IActivitiAction.ActionType.SKIP;
    }
}
