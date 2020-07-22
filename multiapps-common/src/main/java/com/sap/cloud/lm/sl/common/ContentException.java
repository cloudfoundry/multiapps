package com.sap.cloud.lm.sl.common;

public class ContentException extends SLException {

    private static final long serialVersionUID = 4471159624548251863L;

    public ContentException(String message, Object... arguments) {
        super(message, arguments);
    }

    public ContentException(String message) {
        super(message);
    }

    public ContentException(Throwable cause, String message, Object... arguments) {
        super(cause, message, arguments);
    }

    public ContentException(Throwable cause, String message) {
        super(cause, message);
    }

    public ContentException(Throwable cause) {
        super(cause);
    }

}
