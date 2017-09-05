package com.sap.cloud.lm.sl.slp.steps;

import javax.inject.Inject;
import javax.inject.Named;

import org.activiti.engine.delegate.DelegateExecution;
import org.apache.log4j.Logger;

import com.sap.activiti.common.ExecutionStatus;
import com.sap.activiti.common.impl.AbstractActivitiStep;
import com.sap.cloud.lm.sl.common.SLException;
import com.sap.cloud.lm.sl.persistence.services.AbstractFileService;
import com.sap.cloud.lm.sl.persistence.services.ProgressMessageService;
import com.sap.cloud.lm.sl.slp.services.ProcessLoggerProviderFactory;

public abstract class AbstractSLProcessStep extends AbstractActivitiStep implements StepIndexProvider {

    @Inject
    @Named("fileService")
    protected AbstractFileService fileService;

    @Inject
    protected ProcessLoggerProviderFactory processLoggerProviderFactory;

    @Inject
    protected ProgressMessageService progressMessageService;

    protected SLProcessStepHelper stepHelper;

    @Override
    protected void preExecuteStep(DelegateExecution context) throws SLException {
        getStepHelper().preExecuteStep(context, ExecutionStatus.NEW);
    }

    @Override
    public void execute(DelegateExecution context) throws Exception {
        super.execute(context);
        getStepHelper().failStepIfProcessIsAborted(context);
    }

    @Override
    protected void postExecuteStep(DelegateExecution context, ExecutionStatus status) {
        try {
            getStepHelper().postExecuteStep(context, status);
        } catch (SLException e) {
            getStepHelper().storeExceptionInProgressMessageService(context, e);
            logException(context, e);
            throw e;
        }
    }

    @Override
    public void logException(DelegateExecution context, Throwable t) {
        getStepHelper().logException(context, t);
    }

    protected void logInfo(DelegateExecution context, String message) {
        getStepHelper().logInfo(context, message);
    }

    protected void logDebug(DelegateExecution context, String message) {
        getStepHelper().logDebug(context, message);
    }

    protected Logger getLogger(DelegateExecution context) {
        return getStepHelper().getLogger(context);
    }

    @Override
    public Integer getDefaultStepIndex(DelegateExecution context) {
        return -1;
    }

    protected ProgressMessageService getProgressMessageService() {
        if (progressMessageService == null) {
            progressMessageService = ProgressMessageService.getInstance();
        }
        return progressMessageService;
    }

    protected ProcessLoggerProviderFactory getProcessLoggerProvider() {
        if (processLoggerProviderFactory == null) {
            processLoggerProviderFactory = ProcessLoggerProviderFactory.getInstance();
        }
        return processLoggerProviderFactory;
    }

    protected SLProcessStepHelper getStepHelper() {
        if (stepHelper == null) {
            stepHelper = new SLProcessStepHelper(getProgressMessageService(), getProcessLoggerProvider(), this);
        }
        return stepHelper;
    }
}
