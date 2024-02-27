package com.sap.cloud.lm.sl.common;

public class ParsingException extends ContentException {

    private static final long serialVersionUID = 1864427839197780675L;

    public ParsingException(String message, Object... arguments) {
        super(message, arguments);
    }

    public ParsingException(String message) {
        super(message);
    }

    public ParsingException(Throwable cause, String message, Object... arguments) {
        super(cause, message, arguments);
    }

    public ParsingException(Throwable cause, String message) {
        super(cause, message);
    }

    public ParsingException(Throwable cause) {
        super(cause);
    }

}
