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

    public static Manifest getManifest(InputStream is) throws SLException {
        InputStream manifestStream = null;
        try {
            manifestStream = getInputStream(is, JarFile.MANIFEST_NAME);
            return new Manifest(manifestStream);
        } catch (IOException e) {
            throw new SLException(e, Messages.ERROR_RETRIEVING_MTA_ARCHIVE_MANIFEST);
        } finally {
            IOUtils.closeQuietly(manifestStream);
            IOUtils.closeQuietly(is);
        }
    }

    public static String getDescriptor(InputStream is, long maxMtaDescriptorSize) throws SLException {
        InputStream descriptorStream = null;
        try {
            descriptorStream = getInputStream(is, MTA_DEPLOYMENT_DESCRIPTOR_NAME);
            String descriptorString = IOUtils.toString(descriptorStream);
            long descriptorSize = descriptorString.getBytes(StandardCharsets.UTF_8).length;
            if (descriptorSize > maxMtaDescriptorSize) {
                throw new ContentException(Messages.ERROR_SIZE_OF_FILE_EXCEEDS_CONFIGURED_MAX_SIZE_LIMIT, descriptorSize,
                    MTA_DEPLOYMENT_DESCRIPTOR_NAME, maxMtaDescriptorSize);
            }
            return descriptorString;
        } catch (IOException e) {
            throw new SLException(e, Messages.ERROR_RETRIEVING_MTA_MODULE_CONTENT);
        } finally {
            IOUtils.closeQuietly(descriptorStream);
            IOUtils.closeQuietly(is);
        }
    }

    public static byte[] getFileContent(InputStream is, String fileName) throws SLException {
        InputStream moduleStream = null;
        try {
            moduleStream = getInputStream(is, fileName);
            return IOUtils.toByteArray(moduleStream);
        } catch (IOException e) {
            throw new SLException(e, Messages.ERROR_RETRIEVING_MTA_MODULE_CONTENT, fileName);
        } finally {
            IOUtils.closeQuietly(moduleStream);
            IOUtils.closeQuietly(is);
        }
    }

    public static InputStream getInputStream(InputStream is, String entryName) throws SLException {
        try {
            ZipInputStream zis = new ZipInputStream(is);
            for (ZipEntry e; (e = zis.getNextEntry()) != null;) {
                if (e.getName().equals(entryName)) {
                    return zis;
                }
            }
            throw new ContentException(Messages.CANNOT_FIND_ARCHIVE_ENTRY, entryName);
        } catch (IOException e) {
            throw new SLException(e, Messages.ERROR_RETRIEVING_ARCHIVE_ENTRY, entryName);
        }
    }

}
