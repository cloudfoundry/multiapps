package virusscanner;

import java.io.File;

public interface VirusScanner {

    public void scanFile(File file) throws VirusScannerException;

}
