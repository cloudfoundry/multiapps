package com.sap.cloud.lm.sl.common;

import java.text.MessageFormat;

/**
 * Exception thrown when an LM-related error occurs.
 */
public class SLException extends RuntimeException {

    private static final long serialVersionUID = 8047984378256199958L;

    public SLException(String message, Object... arguments) {
        this(MessageFormat.format(message, arguments));
    }

    public SLException(String message) {
        super(message);
    }

    public SLException(Throwable cause, String message, Object... arguments) {
        super(MessageFormat.format(message, arguments), cause);
    }

    public SLException(Throwable cause, String message) {
        super(message, cause);
    }

    public SLException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

}
