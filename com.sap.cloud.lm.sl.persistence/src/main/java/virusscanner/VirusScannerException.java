package virusscanner;

import com.sap.cloud.lm.sl.persistence.services.FileStorageException;

public class VirusScannerException extends FileStorageException {

    private static final long serialVersionUID = -6324626174372599770L;

    public VirusScannerException(Throwable cause) {
        super(cause);
    }

    public VirusScannerException(String message) {
        super(message);
    }

    public VirusScannerException(String message, Throwable cause) {
        super(message, cause);
    }

}
