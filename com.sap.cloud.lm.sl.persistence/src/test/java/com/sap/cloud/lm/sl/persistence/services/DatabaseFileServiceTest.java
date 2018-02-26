package com.sap.cloud.lm.sl.persistence.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.DatatypeConverter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sap.cloud.lm.sl.common.util.DigestHelper;
import com.sap.cloud.lm.sl.common.util.TestDataSourceProvider;
import com.sap.cloud.lm.sl.persistence.DataSourceWithDialect;
import com.sap.cloud.lm.sl.persistence.model.FileEntry;
import com.sap.cloud.lm.sl.persistence.processors.DefaultFileDownloadProcessor;
import com.sap.cloud.lm.sl.persistence.processors.DefaultFileUploadProcessor;
import com.sap.cloud.lm.sl.persistence.util.JdbcUtil;

public class DatabaseFileServiceTest {

    private static final String UPDATE_MODIFICATION_TIME = "UPDATE {0} SET MODIFIED=? WHERE FILE_ID=?";
    private static final String DEFAULT_TABLE_NAME = "LM_SL_PERSISTENCE_FILE";

    private static final String LIQUIBASE_CHANGELOG_LOCATION = "com/sap/cloud/lm/sl/persistence/db/changelog/db-changelog.xml";

    protected static final String DIGEST_METHOD = "MD5";

    private static final String PIC1_RESOURCE_NAME = "pexels-photo-401794.jpeg";
    private static final String PIC1_STORAGE_NAME = "pic1.jpeg";
    private static final int PIC1_SIZE = 2095730;
    private static final String PIC1_MD5_DIGEST = "b39a167875c3771c384c9aa5601fc2d6";
    private static final String SYSTEM_NAMESPACE = "system/deployables";
    protected static final String MY_SPACE_ID = "myspace";
    protected static final String MY_SPACE_2_ID = "myspace2";

    private static final String PIC2_RESOURCE_NAME = "pexels-photo-463467.jpeg";
    private static final String PIC2_STORAGE_NAME = "pic2.jpeg";
    private static final String PERSONAL_NAMESPACE = "dido";

    private static final BigInteger MAX_UPLOAD_SIZE = BigInteger.valueOf(PIC1_SIZE);

    protected AbstractFileService fileService;

    private FileEntry storedFile;

    protected DataSourceWithDialect testDataSource;

    @Before
    public void setUp() throws Exception {
        this.testDataSource = createDataSource();
        this.fileService = createFileService(testDataSource);
        insertInitialData();
    }

    protected DataSourceWithDialect createDataSource() throws Exception {
        return new DataSourceWithDialect(TestDataSourceProvider.getDataSource(LIQUIBASE_CHANGELOG_LOCATION));
    }

    protected AbstractFileService createFileService(DataSourceWithDialect dataSource) {
        return new DatabaseFileService(dataSource);
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

    protected void tearDownConnection() throws Exception {
        // actually close the connection
        testDataSource.getDataSource().getConnection().close();
    }

    protected void insertInitialData() throws Exception {
        storedFile = addFileEntry(MY_SPACE_ID);
    }

    @SuppressWarnings("deprecation")
    private FileEntry addFileEntry(String spaceId) throws FileStorageException {
        InputStream resourceStream = getResource(PIC1_RESOURCE_NAME);
        return fileService.addFile(spaceId, SYSTEM_NAMESPACE, PIC1_STORAGE_NAME, resourceStream, MAX_UPLOAD_SIZE, false);
    }

    private InputStream getResource(String name) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
    }

    private void verifyInitialEntry(FileEntry entry) {
        // verify image name
        assertEquals(PIC1_STORAGE_NAME, entry.getName());
        assertEquals(SYSTEM_NAMESPACE, entry.getNamespace());
        // the size of the uploaded file
        assertEquals(BigInteger.valueOf(PIC1_SIZE), entry.getSize());

        // verify the MD5 digest, compare with one taken with md5sum
        assertEquals(PIC1_MD5_DIGEST.toLowerCase(), entry.getDigest().toLowerCase());
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
        InputStream resourceStream = getResource(PIC2_RESOURCE_NAME);
        fileService.addFile(MY_SPACE_ID, SYSTEM_NAMESPACE, PIC2_STORAGE_NAME, new DefaultFileUploadProcessor(false), resourceStream);
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
        Path expectedFile = Paths.get("src/test/resources/", PIC1_RESOURCE_NAME);
        String expectedFileDigest = DigestHelper.computeFileChecksum(expectedFile, DIGEST_METHOD).toLowerCase();
        validateFileContent(storedFile, expectedFileDigest);
    }

    protected void validateFileContent(FileEntry storedFile, final String expectedFileChecksum) throws FileStorageException {
        fileService
            .processFileContent(new DefaultFileDownloadProcessor(storedFile.getSpace(), storedFile.getId(), new FileContentProcessor() {
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

        InputStream resourceStream = getResource(PIC2_RESOURCE_NAME);
        fileService.addFile(MY_SPACE_ID, PERSONAL_NAMESPACE, PIC2_STORAGE_NAME, resourceStream, MAX_UPLOAD_SIZE, false);

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

    @Test
    public void testDeleteAllByFileIds() throws Exception {
        FileEntry fileEntry1 = addFileEntry(MY_SPACE_ID);
        FileEntry fileEntry2 = addFileEntry(MY_SPACE_ID);
        FileEntry fileEntry3 = addFileEntry(MY_SPACE_2_ID);
        FileEntry fileEntry4 = addFileEntry(MY_SPACE_2_ID);

        Map<String, List<String>> fileIdsToSpace = new HashMap<>();
        fileIdsToSpace.put(MY_SPACE_ID, Arrays.asList(fileEntry1.getId()));
        fileIdsToSpace.put(MY_SPACE_2_ID, Arrays.asList(fileEntry3.getId()));
        int deletedFiles = fileService.deleteAllByFileIds(fileIdsToSpace);

        assertEquals(2, deletedFiles);

        FileEntry missingEntry = fileService.getFile(MY_SPACE_ID, fileEntry1.getId());
        assertNull(missingEntry);

        FileEntry existingEntry = fileService.getFile(MY_SPACE_ID, fileEntry2.getId());
        assertNotNull(existingEntry);

        FileEntry missingEntry2 = fileService.getFile(MY_SPACE_ID, fileEntry3.getId());
        assertNull(missingEntry2);

        FileEntry existingEntry2 = fileService.getFile(MY_SPACE_ID, fileEntry4.getId());
        assertNull(existingEntry2);
    }

    @Test
    public void testListByModificationTime() throws Exception {
        Date now = new Date();
        long modificationTime = TimeUnit.DAYS.toMillis(5);

        FileEntry fileEntry1 = addFileEntry(MY_SPACE_ID);
        FileEntry fileEntry2 = addFileEntry(MY_SPACE_ID);
        FileEntry fileEntry3 = addFileEntry(MY_SPACE_2_ID);
        FileEntry fileEntry4 = addFileEntry(MY_SPACE_2_ID);

        Date pastModificationDate = new Date(now.getTime() - (2 * modificationTime));
        setMofidicationDate(fileEntry2, pastModificationDate);
        setMofidicationDate(fileEntry4, pastModificationDate);

        Map<String, List<String>> fileIdsToSpace = new HashMap<>();
        fileIdsToSpace.put(MY_SPACE_ID, Arrays.asList(fileEntry1.getId()));
        fileIdsToSpace.put(MY_SPACE_2_ID, Arrays.asList(fileEntry3.getId()));

        Date expirationModificationDate = new Date(now.getTime() - modificationTime);
        List<FileEntry> oldEntries = fileService.listByModificationTime(expirationModificationDate);
        assertEquals(2, oldEntries.size());

        assertTrue(oldEntries.get(0).getId().equals(fileEntry2.getId()));
        assertTrue(oldEntries.get(1).getId().equals(fileEntry4.getId()));
    }

    private void setMofidicationDate(FileEntry fileEntry, Date modificationDate) throws SQLException {
        PreparedStatement statement = null;
        try {
            statement = testDataSource.getDataSource().getConnection().prepareStatement(
                MessageFormat.format(UPDATE_MODIFICATION_TIME, DEFAULT_TABLE_NAME));
            statement.setTimestamp(1, new java.sql.Timestamp(modificationDate.getTime()));
            statement.setString(2, fileEntry.getId());
            statement.executeUpdate();
        } finally {
            JdbcUtil.closeQuietly(statement);
        }
    }

}
