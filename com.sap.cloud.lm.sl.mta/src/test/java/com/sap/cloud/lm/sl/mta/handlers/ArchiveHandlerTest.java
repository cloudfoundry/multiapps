package com.sap.cloud.lm.sl.mta.handlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class ArchiveHandlerTest {

    private static final String SAMPLE_MTAR = "com.sap.mta.sample-1.2.1-beta.mtar";
    private static final String SAMPLE_FLAT_MTAR = "com.sap.mta.sample-1.2.1-beta-flat.mtar";
    private static final long MAX_MTA_DESCRIPTOR_SIZE = 1024 * 1024L;

    @Test
    public void testGetManifest() throws Exception {
        Manifest manifest = ArchiveHandler.getManifest(ArchiveHandlerTest.class.getResourceAsStream(SAMPLE_MTAR));
        Map<String, Attributes> entries = manifest.getEntries();
        assertEquals(4, entries.size());
        assertTrue(entries.containsKey("web/web-server.zip"));
        assertTrue(entries.containsKey("applogic/pricing.zip"));
        assertTrue(entries.containsKey("db/pricing-db.zip"));
        assertTrue(entries.containsKey("META-INF/mtad.yaml"));
    }

    @Test
    public void testGetDescriptor() throws Exception {
        String descriptor = ArchiveHandler.getDescriptor(ArchiveHandlerTest.class.getResourceAsStream(SAMPLE_MTAR),
            MAX_MTA_DESCRIPTOR_SIZE);
        assertTrue(descriptor.contains("com.sap.mta.sample"));
    }

    @Test
    public void testGetModuleContent() throws Exception {
        byte[] moduleContent = ArchiveHandler.getFileContent(ArchiveHandlerTest.class.getResourceAsStream(SAMPLE_MTAR),
            "web/web-server.zip");

        InputStream entryStream = getEntryStream(moduleContent, "readme.txt");
        String readmeContent = IOUtils.toString(entryStream);
        assertEquals("App router code will be packaged in this archive", readmeContent);
    }

    @Test
    public void testGetManifestFlat() throws Exception {
        Manifest manifest = ArchiveHandler.getManifest(ArchiveHandlerTest.class.getResourceAsStream(SAMPLE_FLAT_MTAR));
        Map<String, Attributes> entries = manifest.getEntries();
        assertEquals(4, entries.size());
        assertTrue(entries.containsKey("web/"));
        assertTrue(entries.containsKey("applogic/"));
        assertTrue(entries.containsKey("db/"));
        assertTrue(entries.containsKey("META-INF/mtad.yaml"));
    }

    @Test
    public void testGetDescriptorFlat() throws Exception {
        String descriptor = ArchiveHandler.getDescriptor(ArchiveHandlerTest.class.getResourceAsStream(SAMPLE_FLAT_MTAR),
            MAX_MTA_DESCRIPTOR_SIZE);
        assertTrue(descriptor.contains("com.sap.mta.sample"));
    }

    @Test
    public void testGetModuleContentFlat() throws Exception {
        InputStream is = null;
        try {
            is = ArchiveHandler.getInputStream(ArchiveHandlerTest.class.getResourceAsStream(SAMPLE_FLAT_MTAR), "web/");
            ZipInputStream zis = (ZipInputStream) is;
            for (ZipEntry e; (e = zis.getNextEntry()) != null;) {
                if (e.getName().equals("web/readme.txt")) {
                    String readmeContent = IOUtils.toString(zis);
                    assertEquals("App router code will be packaged in this archive", readmeContent);
                }
            }
        } finally {
            if (is != null) {
                IOUtils.closeQuietly(is);
            }
        }
    }

    private InputStream getEntryStream(byte[] content, String entryName) throws IOException {
        ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(content));
        for (ZipEntry e; (e = zis.getNextEntry()) != null;) {
            if (e.getName().equals(entryName)) {
                return zis;
            }
        }
        return null;
    }

}
