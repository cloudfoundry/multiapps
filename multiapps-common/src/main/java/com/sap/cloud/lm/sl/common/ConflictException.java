package com.sap.cloud.lm.sl.common;

public class ConflictException extends SLException {

    private static final long serialVersionUID = -5242703502357086032L;

    public ConflictException(String message, Object... arguments) {
        super(message, arguments);
    }

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(Throwable cause, String message, Object... arguments) {
        super(cause, message, arguments);
    }

    public ConflictException(Throwable cause, String message) {
        super(cause, message);
    }

    public ConflictException(Throwable cause) {
        super(cause);
    }

}
