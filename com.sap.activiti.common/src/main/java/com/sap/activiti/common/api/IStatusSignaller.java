package com.sap.activiti.common.api;

import org.activiti.engine.delegate.DelegateExecution;

import com.sap.activiti.common.ExecutionStatus;

public interface IStatusSignaller {

    /**
     * Sets the execution status in the the provided context.
     * 
     * @param context
     * @param status
     */
    void signal(DelegateExecution context, ExecutionStatus status);
}
