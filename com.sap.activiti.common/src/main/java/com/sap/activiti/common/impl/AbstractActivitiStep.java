package com.sap.activiti.common.impl;

import static com.sap.activiti.common.Constants.LOGICAL_STEP_RETRY_MSG_SUFFIX;
import static com.sap.activiti.common.Constants.STEP_NAME_PREFIX;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import com.sap.activiti.common.ExecutionStatus;
import com.sap.activiti.common.Logger;
import com.sap.activiti.common.api.ILogicalStep;
import com.sap.activiti.common.api.IStatusSignaller;
import com.sap.activiti.common.util.ISkipHelper;
import com.sap.activiti.common.util.impl.DelegateExecutionSkipHelper;

public abstract class AbstractActivitiStep implements JavaDelegate, ILogicalStep {

    private static final Logger LOGGER = Logger.getInstance(AbstractActivitiStep.class);

	/**
	 * <b>Don't use it!</b> Use the {@link #skipHelper skipHelper( )} method
	 * instead. But if you really must, use the {@link ISkipHelper.SkipRequest}
	 * enum.
	 */
	@Deprecated
	public enum SkipOption {

		SKIP

	};

    private IStatusSignaller signaller;

	protected abstract ExecutionStatus executeStep(DelegateExecution context) throws Exception;

    @Override
    public void execute(DelegateExecution context) throws Exception {
        LOGGER.debug(context, "Starting execution");
        getStatusSignaller();

		if (createSkipHelper(context).hasSkipRequest(getLogicalStepName())) {
            getSignaller().signal(context, ExecutionStatus.SKIPPED);
            LOGGER.debug(context, "Skipping step as instructed with a context variable");

			// Remove the skip request after it is processed.
            dropSkipRequestWhenDone(context);
            return;
        }

        ExecutionStatus status = null;
        HttpLogger log = HttpLogger.getInstance();
        
        try {
            log.enableLogCollection();
            preExecuteStep(context);
            status = executeStep(context);
            log.persistLog(getLogicalStepName(), context);
            clearRetryMessage(status, context);
            LOGGER.debug(context, "Execution finished");
        } catch (Throwable t) {
            status = ExecutionStatus.FAILED;
            t = getWithProperMessage(t);
            logException(context, t);
            throw t instanceof Exception ? (Exception) t : new Exception(t);
        } finally {
            log.disableLogCollection();
            postExecuteStep(context, status);
            getSignaller().signal(context, status);
        }
    }

    protected Throwable getWithProperMessage(Throwable t) {
        if (t.getMessage() == null || t.getMessage().isEmpty()) {
            return new Exception("An unknown error occurred", t);
        }
        return t;
    }

    protected void logException(DelegateExecution context, Throwable t) {
        LOGGER.error(context, "Execution failed", t);
    }

    protected void preExecuteStep(DelegateExecution context) throws Exception {

    }

    protected void postExecuteStep(DelegateExecution context, ExecutionStatus status) {

    }

	protected void dropSkipRequestWhenDone(DelegateExecution context) {
		createSkipHelper(context).removeSkipRequest(getLogicalStepName());
    }

    public IStatusSignaller getSignaller() {
        return this.signaller;
    }
    
    protected ISkipHelper createSkipHelper(DelegateExecution context) {
    	return new DelegateExecutionSkipHelper(context);
    }

    private void getStatusSignaller() {
        if (getSignaller() == null) {
            this.signaller = new StatusSignaller(getLogicalStepName());
        }
    }
    
    private void clearRetryMessage(ExecutionStatus status, DelegateExecution context) {
        if (!ExecutionStatus.LOGICAL_RETRY.equals(status)) {
            context.removeVariable(getLogicalStepName() + LOGICAL_STEP_RETRY_MSG_SUFFIX);
        }
    }

    @Override
    public String getLogicalStepName() {
        return this.getClass().getSimpleName();
    }

    protected String getStatusVariable() {
        return STEP_NAME_PREFIX + getLogicalStepName();
    }
}
