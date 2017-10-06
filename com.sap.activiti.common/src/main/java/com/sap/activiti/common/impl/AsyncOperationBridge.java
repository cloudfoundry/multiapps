package com.sap.activiti.common.impl;

import org.activiti.engine.delegate.DelegateExecution;

import com.sap.activiti.common.Constants;
import com.sap.activiti.common.api.IAsyncOperationBridge;

public class AsyncOperationBridge implements IAsyncOperationBridge {

    private String tryCounterContextVarName;
    private String operationIdContextVarName;

    public AsyncOperationBridge(String logicalOperationName) {

        this.operationIdContextVarName = logicalOperationName + Constants.UUID_SUFFIX;
        this.tryCounterContextVarName = Constants.TRY_COUNTER + '_' + logicalOperationName;
    }

    @Override
    public void setOperationId(DelegateExecution context, String requestId) {
        context.setVariable(operationIdContextVarName, requestId);
        resetCounter(context);
    }

    @Override
    public String getOperationId(DelegateExecution context) {
        return (String) context.getVariable(operationIdContextVarName);
    }

    @Override
    public void incrementCounter(DelegateExecution context) {
        int tryCounter = getCounter(context) + 1;
        context.setVariable(tryCounterContextVarName, tryCounter);
    }

    @Override
    public int getCounter(DelegateExecution context) {
        return (Integer) context.getVariable(tryCounterContextVarName);
    }

    @Override
    public void resetCounter(DelegateExecution context) {
        context.setVariable(tryCounterContextVarName, 0);
    }

    String getTryCounterContextVarName() {
        return tryCounterContextVarName;
    }

    String getOperationIdContextVarName() {
        return operationIdContextVarName;
    }
}