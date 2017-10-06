package com.sap.cloud.lm.sl.slp.listener;

import javax.inject.Inject;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import com.sap.cloud.lm.sl.slp.services.ProcessLoggerProviderFactory;
import com.sap.cloud.lm.sl.slp.util.AbstractProcessComponentUtil;

// @formatter:off
public abstract class AbstractSLProcessExecutionListener implements ExecutionListener {

    private static final long serialVersionUID = 1L;

    @Inject
    protected ProcessLoggerProviderFactory processLoggerProviderFactory;

    @Override
    public void notify(DelegateExecution context) throws Exception {
        try {
            notifyInternal(context);
        } finally {
            writeLogs(context);
        }
    }

    protected void writeLogs(DelegateExecution context) {
        AbstractProcessComponentUtil.finalizeLogs(context, getProcessLoggerProviderFactory());
    }
    
    protected abstract void notifyInternal(DelegateExecution context) throws Exception;

    protected ProcessLoggerProviderFactory getProcessLoggerProviderFactory() {
        if (processLoggerProviderFactory == null) {
            processLoggerProviderFactory =  ProcessLoggerProviderFactory.getInstance();
        }
        return processLoggerProviderFactory;
    }
    
}
// @formatter:on
