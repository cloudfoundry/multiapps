package com.sap.cloud.lm.sl.persistence.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.sql.DataSource;
import javax.xml.bind.DatatypeConverter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sap.cloud.lm.sl.common.util.DigestHelper;
import com.sap.cloud.lm.sl.common.util.TestDataSourceProvider;
import com.sap.cloud.lm.sl.persistence.model.FileEntry;
import com.sap.cloud.lm.sl.persistence.processors.DefaultFileDownloadProcessor;
import com.sap.cloud.lm.sl.persistence.processors.DefaultFileUploadProcessor;

public class DatabaseFileServiceTest {

    private static final String LIQUIBASE_CHANGELOG_LOCATION = "com/sap/cloud/lm/sl/persistence/db/changelog/db-changelog.xml";

    protected static final String DIGEST_METHOD = "MD5";

    private static final String WAR_RESOURCE_NAME = "com.sap.cloud.lm.sl.web-0.1.0-SNAPSHOT.war";
    private static final String WAR_STORAGE_NAME = "sl_sysapp.war";
    private static final int WAR_SIZE = 14877376;
    private static final String WAR_MD5_DIGEST = "01f5fe82cb9e70f3dca930856510ad90";
    private static final String SYSTEM_NAMESPACE = "system/deployables";
    private static final String MY_SPACE_ID = "myspace";

    private static final String IMG_STORAGE_NAME = "arborek.jpg";
    private static final String IMG_RESOURCE_NAME = "hell.jpg";
    private static final String PERSONAL_NAMESPACE = "dido";

    private static final BigInteger MAX_UPLOAD_SIZE = BigInteger.valueOf(WAR_SIZE);

    protected AbstractFileService fileService;

    private FileEntry storedFile;

    private DataSource testDataSource;

    @Before
    public void setUp() throws Exception {
        setupConnection();
        insertInitialData();
    }

    @After
    public void tearDown() throws Exception {
        sweepFiles();
        tearDownConnection();
    }

    protected void sweepFiles() throws FileStorageException, Exception {
        fileService.deleteAll(MY_SPACE_ID, SYSTEM_NAMESPACE);
        fileService.deleteAll(MY_SPACE_ID, PERSONAL_NAMESPACE);
    }

    protected void setupConnection() throws Exception {
        fileService = getFileService();
        testDataSource = TestDataSourceProvider.getDataSource(LIQUIBASE_CHANGELOG_LOCATION);
        fileService.init(testDataSource);
    }

    protected AbstractFileService getFileService() {
        return new DatabaseFileService("LM_SL_PERSISTENCE_FILE");
    }

    protected void tearDownConnection() throws Exception {
        // actually close the connection
        testDataSource.getConnection().close();
    }

    @SuppressWarnings("deprecation")
    protected void insertInitialData() throws Exception {
        // store a file
        InputStream resourceStream = getResource(WAR_RESOURCE_NAME);
        storedFile = fileService.addFile(MY_SPACE_ID, SYSTEM_NAMESPACE, WAR_STORAGE_NAME, resourceStream, MAX_UPLOAD_SIZE, false);
    }

    private InputStream getResource(String name) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
    }

    private void verifyInitialEntry(FileEntry entry) {
        // verify image name
        assertEquals(WAR_STORAGE_NAME, entry.getName());
        assertEquals(SYSTEM_NAMESPACE, entry.getNamespace());
        // the size of the uploaded file
        assertEquals(BigInteger.valueOf(WAR_SIZE), entry.getSize());

        // verify the MD5 digest, compare with one taken with md5sum
        assertEquals(WAR_MD5_DIGEST.toLowerCase(), entry.getDigest().toLowerCase());
        assertEquals(DIGEST_METHOD, entry.getDigestAlgorithm());
    }

    @Test
    public void testUploadTwoIdenticalFiles() throws Exception {
        List<FileEntry> listFiles = fileService.listFiles(MY_SPACE_ID, SYSTEM_NAMESPACE);
        assertEquals(1, listFiles.size());
        FileEntry entry = listFiles.get(0);

        verifyInitialEntry(entry);
    }

    @Test
    public void testUploadTwoFiles() throws Exception {
        InputStream resourceStream = getResource(IMG_RESOURCE_NAME);
        fileService.addFile(MY_SPACE_ID, SYSTEM_NAMESPACE, IMG_STORAGE_NAME, new DefaultFileUploadProcessor(false), resourceStream);
        List<FileEntry> listFiles = fileService.listFiles(MY_SPACE_ID, SYSTEM_NAMESPACE);
        assertEquals(2, listFiles.size());
    }

    @Test
    public void testGetFile() throws FileStorageException, NoSuchAlgorithmException, IOException {
        FileEntry fileEntry = fileService.getFile(MY_SPACE_ID, storedFile.getId());
        verifyInitialEntry(fileEntry);
    }

    @Test
    public void testFileContent() throws Exception {
        Path expectedFile = Paths.get("src/test/resources/", WAR_RESOURCE_NAME);
        String expectedFileDigest = DigestHelper.computeFileChecksum(expectedFile, DIGEST_METHOD).toLowerCase();
        validateFileContent(storedFile, expectedFileDigest);
    }

    protected void validateFileContent(FileEntry storedFile, final String expectedFileChecksum) throws FileStorageException {
        fileService.processFileContent(
            new DefaultFileDownloadProcessor(storedFile.getSpace(), storedFile.getId(), new FileContentProcessor() {
                @Override
                public void processFileContent(InputStream contentStream) throws Exception {
                    // make a digest out of the content and compare it to the original
                    final byte[] digest = calculateFileDigest(contentStream);
                    assertEquals(expectedFileChecksum, DatatypeConverter.printHexBinary(digest).toLowerCase());
                }

            }));
    }

    protected byte[] calculateFileDigest(InputStream contentStream) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance(DIGEST_METHOD);
        int read = 0;
        byte[] buffer = new byte[4 * 1024];
        while ((read = contentStream.read(buffer)) > -1) {
            md.update(buffer, 0, read);
        }
        return md.digest();
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testNamespaceIsolation() throws Exception {
        List<FileEntry> personalFiles = fileService.listFiles(MY_SPACE_ID, PERSONAL_NAMESPACE);
        assertEquals(0, personalFiles.size());

        List<FileEntry> systemFiles = fileService.listFiles(MY_SPACE_ID, SYSTEM_NAMESPACE);
        assertEquals(1, systemFiles.size());

        InputStream resourceStream = getResource(IMG_RESOURCE_NAME);
        fileService.addFile(MY_SPACE_ID, PERSONAL_NAMESPACE, IMG_STORAGE_NAME, resourceStream, MAX_UPLOAD_SIZE, false);

        personalFiles = fileService.listFiles(MY_SPACE_ID, PERSONAL_NAMESPACE);
        assertEquals(1, personalFiles.size());

        systemFiles = fileService.listFiles(MY_SPACE_ID, SYSTEM_NAMESPACE);
        assertEquals(1, systemFiles.size());

        fileService.deleteFile(MY_SPACE_ID, systemFiles.get(0).getId());

        personalFiles = fileService.listFiles(MY_SPACE_ID, PERSONAL_NAMESPACE);
        assertEquals(1, personalFiles.size());

        systemFiles = fileService.listFiles(MY_SPACE_ID, SYSTEM_NAMESPACE);
        assertEquals(0, systemFiles.size());
    }

    @Test
    public void testDeleteFile() throws FileStorageException {
        fileService.deleteFile(MY_SPACE_ID, storedFile.getId());

        FileEntry missingEntry = fileService.getFile(MY_SPACE_ID, storedFile.getId());
        assertNull(missingEntry);

        List<FileEntry> namespaceFiles = fileService.listFiles(MY_SPACE_ID, SYSTEM_NAMESPACE);
        assertEquals(0, namespaceFiles.size());
    }

}
