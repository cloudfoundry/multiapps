package org.cloudfoundry.multiapps.common;

public class NotFoundException extends SLException {

    private static final long serialVersionUID = 7986577364697817675L;

    public NotFoundException(String message, Object... arguments) {
        super(message, arguments);
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(Throwable cause, String message, Object... arguments) {
        super(cause, message, arguments);
    }

    public NotFoundException(Throwable cause, String message) {
        super(cause, message);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

}
