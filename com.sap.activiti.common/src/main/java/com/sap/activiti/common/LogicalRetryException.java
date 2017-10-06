package com.sap.activiti.common;

public class LogicalRetryException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LogicalRetryException(String message) {
        super(message);
    }
}
