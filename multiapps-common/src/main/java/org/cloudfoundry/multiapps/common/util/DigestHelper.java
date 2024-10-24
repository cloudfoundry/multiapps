package org.cloudfoundry.multiapps.common.util;

import jakarta.xml.bind.DatatypeConverter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestHelper {

    private static final int BUFFER_SIZE = 4 * 1024;

    private DigestHelper() {
    }

    public static String computeFileChecksum(Path file, String algorithm) throws NoSuchAlgorithmException, IOException {
        return DatatypeConverter.printHexBinary(computeFileCheckSumBytes(file, algorithm));
    }

    private static byte[] computeFileCheckSumBytes(Path file, String algorithm) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance(algorithm);

        try (InputStream is = Files.newInputStream(file)) {
            int read = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((read = is.read(buffer)) > -1) {
                md.update(buffer, 0, read);
            }
        }
        return md.digest();
    }

    public static String computeDirectoryCheckSum(Path filePath, String algorithm) throws NoSuchAlgorithmException, IOException {
        return DatatypeConverter.printHexBinary(computeDirectoryCheckSumBytes(filePath, algorithm));
    }

    private static byte[] computeDirectoryCheckSumBytes(Path directoryPath, String algorithm) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        for (File fileInDir : directoryPath.toFile()
                                           .listFiles()) {
            Path filePathInDir = fileInDir.toPath();
            if (Files.isSymbolicLink(filePathInDir)) {
                continue;
            }
            if (filePathInDir.toFile()
                             .isDirectory()) {
                md.update(computeDirectoryCheckSumBytes(filePathInDir, algorithm));
            } else {
                md.update(computeFileCheckSumBytes(filePathInDir, algorithm));
            }
        }
        return md.digest();
    }
}
