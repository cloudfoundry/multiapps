package com.sap.cloud.lm.sl.mta.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.common.SLException;
import com.sap.cloud.lm.sl.mta.message.Messages;

public class ArchiveHandler {

    public static final String MTA_DEPLOYMENT_DESCRIPTOR_NAME = "META-INF/mtad.yaml";

    public static Manifest getManifest(InputStream archiveStream, long maxManifestSize) throws SLException {
        try (InputStream manifestStream = getInputStream(archiveStream, JarFile.MANIFEST_NAME, maxManifestSize)) {
            return new Manifest(manifestStream);
        } catch (IOException e) {
            throw new SLException(e, Messages.ERROR_RETRIEVING_MTA_ARCHIVE_MANIFEST);
        }
    }

    public static String getDescriptor(InputStream archiveStream, long maxMtaDescriptorSize) throws SLException {
        try (InputStream descriptorStream = getInputStream(archiveStream, MTA_DEPLOYMENT_DESCRIPTOR_NAME, maxMtaDescriptorSize)) {
            return IOUtils.toString(descriptorStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new SLException(e, Messages.ERROR_RETRIEVING_MTA_MODULE_CONTENT);
        }
    }

    public static byte[] getFileContent(InputStream archiveStream, String fileName, long maxMtaFileSize) throws SLException {
        try (InputStream moduleStream = getInputStream(archiveStream, fileName, maxMtaFileSize)) {
            return IOUtils.toByteArray(moduleStream);
        } catch (IOException e) {
            throw new SLException(e, Messages.ERROR_RETRIEVING_MTA_MODULE_CONTENT, fileName);
        }
    }

    public static InputStream getInputStream(InputStream is, String entryName, long maxEntrySize) {
        try {
            ZipInputStream zis = new ZipInputStream(is);
            for (ZipEntry e; (e = zis.getNextEntry()) != null;) {
                if (e.getName()
                     .equals(entryName)) {
                    validateZipEntrySize(e, maxEntrySize);
                    return zis;
                }
            }
            throw new ContentException(Messages.CANNOT_FIND_ARCHIVE_ENTRY, entryName);
        } catch (IOException e) {
            throw new SLException(e, Messages.ERROR_RETRIEVING_ARCHIVE_ENTRY, entryName);
        }
    }

    private static void validateZipEntrySize(ZipEntry zipEntry, long maxEntrySize) {
        if (zipEntry.getSize() > maxEntrySize) {
            throw new ContentException(Messages.ERROR_SIZE_OF_FILE_EXCEEDS_CONFIGURED_MAX_SIZE_LIMIT,
                                       zipEntry.getSize(),
                                       zipEntry.getName(),
                                       maxEntrySize);
        }
    }
}
