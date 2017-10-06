package com.sap.activiti.common.actions;

import com.sap.activiti.common.Constants;
import com.sap.activiti.common.ExecutionStatus;

public class ActivitiRetryAction extends AbstractStepRelatedAction {

	public static final String TRYCOUNT_CONTEXT_PREFIX = Constants.TRY_COUNTER + "_";

	private static final String LOG_MESSAGE = "User [%s] called retrigger on step [%s]. Reason: %s";
	private static final int RESET_RETRY_COUNT_VALUE = 0;

	public ActivitiRetryAction(String processInstanceId, String userId, String reasonMessage, String jobId) {
		super(processInstanceId, userId, reasonMessage, jobId);
	}

	@Override
	public void execute() {
		logTracibilityInformation(String.format(LOG_MESSAGE, getUserName(), getActivityId(), getReasonMessage()));
		resetTaskCounter();
		setStatusToFailed();
		executeJob();
	}

	private void executeJob() {
		getManagementService().executeJob(getJobId());
	}

	private void resetTaskCounter() {
		boolean taskHasTryCount = getDefaultProcessEngine().getRuntimeService().hasVariable(getProcessInstanceId(),
		        constructLogicalStepContextVariable(TRYCOUNT_CONTEXT_PREFIX));

		if (taskHasTryCount) {
			getDefaultProcessEngine().getRuntimeService().setVariable(getProcessInstanceId(),
			        constructLogicalStepContextVariable(TRYCOUNT_CONTEXT_PREFIX), RESET_RETRY_COUNT_VALUE);
		}
	}

	private void setStatusToFailed() {
		getDefaultProcessEngine().getRuntimeService().setVariable(getProcessInstanceId(),
		        constructLogicalStepContextVariable(Constants.STEP_NAME_PREFIX), ExecutionStatus.FAILED.name());
	}

	private String constructLogicalStepContextVariable(String prefix) {
		String statusVariable = null;
		try {
			statusVariable = prefix + getLogicalStepNameProvider().getLogicalStepName();
		} catch (LogicalStepNameProviderException e) {
			throw new IllegalStateException(e);
		}
		return statusVariable;
	}

	@Override
	public ActionType getType() {
		return IActivitiAction.ActionType.RETRY;
	}
}
