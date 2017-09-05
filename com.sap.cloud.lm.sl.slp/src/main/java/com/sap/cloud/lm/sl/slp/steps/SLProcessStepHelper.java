package com.sap.cloud.lm.sl.slp.steps;

import static java.text.MessageFormat.format;

import java.sql.Timestamp;
import java.text.MessageFormat;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.JobQuery;
import org.apache.log4j.Logger;

import com.sap.activiti.common.ExecutionStatus;
import com.sap.activiti.common.LogicalRetryException;
import com.sap.cloud.lm.sl.common.SLException;
import com.sap.cloud.lm.sl.persistence.model.ProgressMessage;
import com.sap.cloud.lm.sl.persistence.model.ProgressMessage.ProgressMessageType;
import com.sap.cloud.lm.sl.persistence.services.ProgressMessageService;
import com.sap.cloud.lm.sl.slp.Constants;
import com.sap.cloud.lm.sl.slp.message.Messages;
import com.sap.cloud.lm.sl.slp.services.ProcessLoggerProviderFactory;
import com.sap.cloud.lm.sl.slp.util.AbstractProcessComponentUtil;

public class SLProcessStepHelper {

    private static final String CORRELATION_ID = "correlationId";

    private ProgressMessageService progressMessageService;
    protected ProcessLoggerProviderFactory processLoggerProviderFactory;
    private String indexedStepName;
    private StepIndexProvider stepIndexProvider;

    public SLProcessStepHelper(ProgressMessageService progressMessageService, ProcessLoggerProviderFactory processLoggerProviderFactory,
        StepIndexProvider stepIndexProvider) {
        this.progressMessageService = progressMessageService;
        this.processLoggerProviderFactory = processLoggerProviderFactory;
        this.stepIndexProvider = stepIndexProvider;
    }

    ProgressMessageService getProgressMessageService() {
        return progressMessageService;
    }

    String getIndexedStepName() {
        return indexedStepName;
    }

    void preExecuteStep(DelegateExecution context, ExecutionStatus initialStatus) throws SLException {
        boolean isInError = isInError(context);
        int stepIndex = computeStepIndex(context, initialStatus, isInError);

        indexedStepName = context.getCurrentActivityId() + stepIndex;
        context.setVariable(Constants.INDEXED_STEP_NAME, indexedStepName);

        if (isInError) {
            progressMessageService.removeByProcessIdAndTaskId(getCorrelationId(context), indexedStepName);
            if (context.hasVariable(Constants.RETRY_STEP_NAME)) {
                String taskId = (String) context.getVariable(Constants.RETRY_STEP_NAME) + stepIndex;
                progressMessageService.removeByProcessIdAndTaskId(getCorrelationId(context), taskId);
                context.removeVariable(Constants.RETRY_STEP_NAME);
            }
        }
        logTaskStartup(context, indexedStepName);
    }

    private void logTaskStartup(DelegateExecution context, String indexedStepName) {
        String message = format(Messages.EXECUTING_ACTIVITI_TASK, context.getId(), context.getCurrentActivityId());
        progressMessageService.add(new ProgressMessage(getCorrelationId(context), indexedStepName, ProgressMessageType.TASK_STARTUP,
            message, new Timestamp(System.currentTimeMillis())));
    }

    private int computeStepIndex(DelegateExecution context, ExecutionStatus initialStatus, boolean isInError) {
        int stepIndex = getLastStepIndex(context);

        if (!isInError && !initialStatus.equals(ExecutionStatus.LOGICAL_RETRY) && !initialStatus.equals(ExecutionStatus.RUNNING)) {
            return ++stepIndex;
        }

        return stepIndex;
    }

    void failStepIfProcessIsAborted(DelegateExecution context) throws SLException {
        Boolean processAborted = (Boolean) context.getVariable(Constants.PROCESS_ABORTED);
        if (processAborted != null && processAborted) {
            throw new SLException(Messages.PROCESS_WAS_ABORTED);
        }
    }

    protected void postExecuteStep(DelegateExecution context, ExecutionStatus status) {
        // Log step completion:
        logDebug(context, MessageFormat.format(Messages.STEP_FINISHED, context.getCurrentActivityName()));

        AbstractProcessComponentUtil.finalizeLogs(context, processLoggerProviderFactory);
    }

    void logException(DelegateExecution context, Throwable t) {
        getLogger(context).error(Messages.EXCEPTION_CAUGHT, t);

        if (!(t instanceof SLException) && !(t instanceof LogicalRetryException)) {
            storeExceptionInProgressMessageService(context, t);
        }
    }

    public void storeExceptionInProgressMessageService(DelegateExecution context, Throwable t) {
        try {
            ProgressMessage msg = new ProgressMessage(getCorrelationId(context), indexedStepName, ProgressMessageType.ERROR,
                MessageFormat.format(Messages.UNEXPECTED_ERROR, t.getMessage()), new Timestamp(System.currentTimeMillis()));
            progressMessageService.add(msg);
        } catch (SLException e) {
            getLogger(context).error(Messages.SAVING_ERROR_MESSAGE_FAILED, e);
        }
    }

    void logInfo(DelegateExecution context, String message) {
        getLogger(context).info(message);
    }

    protected void logDebug(DelegateExecution context, String message) {
        getLogger(context).debug(message);
    }

    Logger getLogger(DelegateExecution context) {
        return processLoggerProviderFactory.getDefaultLoggerProvider().getLogger(getCorrelationId(context), this.getClass().getName());
    }

    private String getCorrelationId(DelegateExecution context) {
        return (String) context.getVariable(CORRELATION_ID);
    }

    private int getLastStepIndex(DelegateExecution context) throws SLException {
        String activityId = context.getCurrentActivityId();
        String lastTaskId = progressMessageService.findLastTaskId(getCorrelationId(context), activityId);
        if (lastTaskId == null) {
            return stepIndexProvider.getDefaultStepIndex(context);
        }
        return Integer.parseInt(lastTaskId.substring(activityId.length()));
    }

    private boolean isInError(DelegateExecution context) {
        Job job = getJob(context);
        if (job == null) {
            return false;
        }
        String exceptionMessage = job.getExceptionMessage();
        return exceptionMessage != null && !exceptionMessage.isEmpty();
    }

    Job getJob(DelegateExecution context) {
        JobQuery jobQuery = context.getEngineServices().getManagementService().createJobQuery();
        if (jobQuery == null) {
            return null;
        }
        return jobQuery.processInstanceId(context.getProcessInstanceId()).singleResult();
    }
}