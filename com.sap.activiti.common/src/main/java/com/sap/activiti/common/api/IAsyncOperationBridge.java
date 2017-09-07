package com.sap.activiti.common.api;

import org.activiti.engine.delegate.DelegateExecution;

public interface IAsyncOperationBridge {

    /**
     * Sets the currently executing operation's identifier. It could be addon name, request id, etc.
     * 
     * @param context
     * @param operationId
     */
    void setOperationId(DelegateExecution context, String operationId);

    /**
     * @param context
     * @return The currently executing operation's identifier. It could be addon name, request id, etc.
     */
    String getOperationId(DelegateExecution context);

    /**
     * Increments the counter value, that is used for monitoring a long-running asynchronous execution.
     * 
     * @param context
     */
    void incrementCounter(DelegateExecution context);

    /**
     * @param context
     * @return The counter value, that is used for monitoring a long-running asynchronous execution.
     */
    int getCounter(DelegateExecution context);

    /**
     * Resets the counter value, that is used for monitoring a long-running asynchronous execution.
     * 
     * @param context
     */
    void resetCounter(DelegateExecution context);

}