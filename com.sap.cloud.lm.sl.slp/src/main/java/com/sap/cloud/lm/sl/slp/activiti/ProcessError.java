package com.sap.cloud.lm.sl.slp.activiti;

public class ProcessError {

    public enum ErrorType {
        PROCESS_EXECUTION_ERROR("1");

        private final String errorCode;

        ErrorType(String errorCode) {
            this.errorCode = errorCode;
        }

        private String getErrorCode() {
            return errorCode;
        }

    }

    private final ErrorType errorType;
    private final String message;

    public ProcessError(ErrorType errorType, String message) {
        super();
        this.errorType = errorType;
        this.message = message;
    }

    public String getCode() {
        return errorType.getErrorCode();
    }

    public String getMessage() {
        return message;
    }

}
