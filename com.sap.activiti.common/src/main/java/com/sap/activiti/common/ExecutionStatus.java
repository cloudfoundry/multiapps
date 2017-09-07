package com.sap.activiti.common;

public enum ExecutionStatus {
    NEW, // Task/Service is not started
    RUNNING, SUCCESS, FAILED, //
    LOGICAL_RETRY, // Logical composite step has failed but may be retried
    SKIPPED; // Task has been skipped by the operator
}
