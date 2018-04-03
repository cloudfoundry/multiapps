package com.sap.cloud.lm.sl.persistence.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.sap.cloud.lm.sl.common.util.DigestHelper;
import com.sap.cloud.lm.sl.persistence.DataSourceWithDialect;
import com.sap.cloud.lm.sl.persistence.message.Messages;
import com.sap.cloud.lm.sl.persistence.model.FileEntry;
import com.sap.cloud.lm.sl.persistence.processors.DefaultFileUploadProcessor;

public class FileSystemFileServiceTest extends DatabaseFileServiceTest {

    private static final String TEST_FILE_LOCATION = "src/test/resources/pexels-photo-401794.jpeg";
    private static final String SECOND_FILE_TEST_LOCATION = "src/test/resources/pexels-photo-463467.jpeg";

    private String spaceId;
    private String namespace;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Path temporaryStorageLocation;

    @Before
    public void setUp() throws Exception {
        this.testDataSource = createDataSource();
        this.temporaryStorageLocation = Files.createTempDirectory("testfileService");
        fileService = createFileService(testDataSource);
        spaceId = UUID.randomUUID()
            .toString();
        namespace = UUID.randomUUID()
            .toString();
        insertInitialData();
    }

    @Test
    public void testAddFile() throws FileStorageException, IOException {
        Path testFilePath = Paths.get(TEST_FILE_LOCATION)
            .toAbsolutePath();
        FileEntry addedFile = fileService.addFile(spaceId, namespace, testFilePath.toFile()
            .getName(), new DefaultFileUploadProcessor(false), Files.newInputStream(testFilePath));
        assertFileExists(true, addedFile);
    }

    @Test
    public void testAddFileWichAlreadyExists() throws FileStorageException, IOException {
        Path testFilePath = Paths.get(TEST_FILE_LOCATION)
            .toAbsolutePath();
        fileService.addFile(spaceId, namespace, testFilePath.toFile()
            .getName(), new DefaultFileUploadProcessor(false), testFilePath.toFile());
        FileEntry addedFile = fileService.addFile(spaceId, namespace, testFilePath.toFile()
            .getName(), new DefaultFileUploadProcessor(false), testFilePath.toFile());
        assertFileExists(true, addedFile);
    }

    @Test
    public void testAddExistingFile() throws FileStorageException, IOException {
        Path testFilePath = Paths.get(TEST_FILE_LOCATION)
            .toAbsolutePath();
        FileEntry addedFile = fileService.addFile(spaceId, namespace, testFilePath.toFile()
            .getName(), new DefaultFileUploadProcessor(false), testFilePath.toFile());

        assertFileExists(true, addedFile);
    }

    @Test
    public void testDeleteAllFiles() throws FileStorageException {
        try {
            fileService.deleteAll(spaceId, namespace);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof UnsupportedOperationException);
        }
    }

    @Test
    public void testGetFile() throws FileStorageException, NoSuchAlgorithmException, IOException {
        Path testFilePath = Paths.get(TEST_FILE_LOCATION)
            .toAbsolutePath();
        FileEntry addedFile = fileService.addFile(spaceId, namespace, testFilePath.toFile()
            .getName(), new DefaultFileUploadProcessor(false), testFilePath.toFile());

        FileEntry actualFile = fileService.getFile(spaceId, addedFile.getId());
        String expectedFileFileDigest = DigestHelper.computeFileChecksum(testFilePath, DIGEST_METHOD);
        String actualFileFileDigest = actualFile.getDigest();
        Assert.assertEquals(expectedFileFileDigest, actualFileFileDigest);
    }

    @Test
    public void testDeleteFile() throws FileStorageException {
        Path testFilePath = Paths.get(TEST_FILE_LOCATION)
            .toAbsolutePath();
        FileEntry addedFile = fileService.addFile(spaceId, namespace, testFilePath.toFile()
            .getName(), new DefaultFileUploadProcessor(false), testFilePath.toFile());

        int deletionResult = fileService.deleteFile(spaceId, addedFile.getId());
        Assert.assertEquals(1, deletionResult);
    }

    @Test
    public void testUploadTwoFiles() throws FileStorageException, IOException {
        Path testFilePath = Paths.get(TEST_FILE_LOCATION);
        FileEntry firstAddedFile = fileService.addFile(spaceId, namespace, testFilePath.toFile()
            .getName(), new DefaultFileUploadProcessor(false), Files.newInputStream(testFilePath));

        Path secondTestFilePath = Paths.get(SECOND_FILE_TEST_LOCATION);
        FileEntry secondAddedFile = fileService.addFile(spaceId, namespace, secondTestFilePath.toFile()
            .getName(), new DefaultFileUploadProcessor(false), Files.newInputStream(secondTestFilePath));

        List<FileEntry> fileEntries = fileService.listFiles(spaceId, namespace);
        Assert.assertEquals(2, fileEntries.size());
        validateFilesEquality(fileEntries, firstAddedFile, secondAddedFile);

    }

    @Override
    public void testFileContent() throws Exception {
        Path testFilePath = Paths.get(TEST_FILE_LOCATION);
        FileEntry addedFile = fileService.addFile(spaceId, namespace, testFilePath.toFile()
            .getName(), new DefaultFileUploadProcessor(false), Files.newInputStream(testFilePath));

        String testFileDigest = DigestHelper.computeFileChecksum(testFilePath, DIGEST_METHOD)
            .toLowerCase();
        validateFileContent(addedFile, testFileDigest);
    }

    @Test
    public void testFileContentNotExisting() throws Exception {
        String fileId = "not-existing-file-id";
        String fileSpace = "not-existing-space-id";
        String fileDigest = DigestHelper.computeFileChecksum(Paths.get(TEST_FILE_LOCATION), DIGEST_METHOD)
            .toLowerCase();
        FileEntry dummyFileEntry = new FileEntry();
        dummyFileEntry.setId(fileId);
        dummyFileEntry.setSpace(fileSpace);
        expectedException.expect(FileStorageException.class);
        expectedException.expectMessage(MessageFormat.format(Messages.FILE_WITH_ID_AND_SPACE_DOES_NOT_EXIST, fileId, fileSpace));
        validateFileContent(dummyFileEntry, fileDigest);
    }

    @Override
    public void testUploadTwoIdenticalFiles() throws Exception {
        Path testFilePath = Paths.get(TEST_FILE_LOCATION);
        fileService.addFile(spaceId, namespace, testFilePath.toFile()
            .getName(), new DefaultFileUploadProcessor(false), Files.newInputStream(testFilePath));

        fileService.addFile(spaceId, namespace, testFilePath.toFile()
            .getName(), new DefaultFileUploadProcessor(false), Files.newInputStream(testFilePath));

        List<FileEntry> listFiles = fileService.listFiles(spaceId, namespace);
        Assert.assertEquals(2, listFiles.size());
    }

    @Override
    public void testNamespaceIsolation() throws Exception {
        // No need for testing as the isolation is covered in the other test case scenarios
    }

    @Override
    public void testDeleteAllByFileIds() throws Exception {
        Path testFilePath = Paths.get(TEST_FILE_LOCATION)
            .toAbsolutePath();
        Path secondTestFilePath = Paths.get(SECOND_FILE_TEST_LOCATION)
            .toAbsolutePath();

        FileEntry fileEntry1 = fileService.addFile(MY_SPACE_ID, namespace, testFilePath.toFile()
            .getName(), new DefaultFileUploadProcessor(false), Files.newInputStream(testFilePath));
        FileEntry fileEntry2 = fileService.addFile(MY_SPACE_ID, namespace, secondTestFilePath.toFile()
            .getName(), new DefaultFileUploadProcessor(false), Files.newInputStream(secondTestFilePath));

        FileEntry fileEntry3 = fileService.addFile(MY_SPACE_2_ID, namespace, testFilePath.toFile()
            .getName(), new DefaultFileUploadProcessor(false), Files.newInputStream(testFilePath));
        FileEntry fileEntry4 = fileService.addFile(MY_SPACE_2_ID, namespace, secondTestFilePath.toFile()
            .getName(), new DefaultFileUploadProcessor(false), Files.newInputStream(secondTestFilePath));

        Map<String, List<String>> fileIdsToSpace = new HashMap<>();
        fileIdsToSpace.put(MY_SPACE_ID, Arrays.asList(fileEntry1.getId()));
        fileIdsToSpace.put(MY_SPACE_2_ID, Arrays.asList(fileEntry3.getId()));
        int deletedFiles = fileService.deleteAllByFileIds(fileIdsToSpace);

        assertEquals(2, deletedFiles);
        assertFileExists(false, fileEntry1);
        assertFileExists(true, fileEntry2);
        assertFileExists(false, fileEntry3);
        assertFileExists(true, fileEntry4);
    }

    @Test
    public void testListByModificationTime() throws Exception {
        Date now = new Date();
        long modificationTime = TimeUnit.DAYS.toMillis(5);

        Path testFilePath = Paths.get(TEST_FILE_LOCATION)
            .toAbsolutePath();
        Path secondTestFilePath = Paths.get(SECOND_FILE_TEST_LOCATION)
            .toAbsolutePath();
        FileEntry fileEntry1 = fileService.addFile(MY_SPACE_ID, namespace, testFilePath.toFile()
            .getName(), new DefaultFileUploadProcessor(false), Files.newInputStream(testFilePath));
        fileService.addFile(MY_SPACE_ID, namespace, secondTestFilePath.toFile()
            .getName(), new DefaultFileUploadProcessor(false), Files.newInputStream(secondTestFilePath));

        Files.setLastModifiedTime(getFileLocation(fileEntry1), FileTime.fromMillis(now.getTime() - (2 * modificationTime)));

        List<FileEntry> allEntries = fileService.listFiles(namespace);
        assertEquals(2, allEntries.size());

        List<FileEntry> oldEntries = fileService.listByModificationTime(new Date(now.getTime() - modificationTime));

        allEntries = fileService.listFiles(namespace);
        assertEquals(2, allEntries.size());

        assertEquals(1, oldEntries.size());
        assertTrue(oldEntries.get(0)
            .getId()
            .equals(fileEntry1.getId()));
    }

    private void validateFilesEquality(List<FileEntry> actualFileEntries, FileEntry... expected) {
        for (FileEntry expectedFileEntry : expected) {
            FileEntry actualFileEntry = find(expectedFileEntry, actualFileEntries);
            Assert.assertNotNull(actualFileEntry);
            validateFilesEquality(expectedFileEntry, actualFileEntry);
        }
    }

    private void validateFilesEquality(FileEntry expected, FileEntry actualFileEntry) {
        Assert.assertEquals(expected.getId(), actualFileEntry.getId());
        Assert.assertEquals(expected.getName(), actualFileEntry.getName());
        Assert.assertEquals(expected.getNamespace(), actualFileEntry.getNamespace());
        Assert.assertEquals(expected.getSpace(), actualFileEntry.getSpace());
        Assert.assertEquals(expected.getDigest(), actualFileEntry.getDigest());
        Assert.assertEquals(expected.getDigestAlgorithm(), actualFileEntry.getDigestAlgorithm());
        Assert.assertEquals(expected.getSize(), actualFileEntry.getSize());
    }

    private FileEntry find(FileEntry expected, List<FileEntry> actualFileEntries) {
        for (FileEntry fileEntry : actualFileEntries) {
            if (expected.getId()
                .equals(fileEntry.getId())) {
                return fileEntry;
            }
        }
        return null;
    }

    private void assertFileExists(boolean exceptedFileExist, FileEntry actualFile) {
        Assert.assertEquals(exceptedFileExist, Files.exists(getFileLocation(actualFile)));
    }

    private Path getFileLocation(FileEntry actualFile) {
        return Paths.get(temporaryStorageLocation.toString(), actualFile.getSpace(), "files", actualFile.getId());
    }

    @After
    public void tearDown() throws Exception {
        try {
            tearDownConnection();
            FileUtils.deleteDirectory(temporaryStorageLocation.toFile());
        } catch (IOException e) {
        }
    }

    @Override
    protected AbstractFileService createFileService(DataSourceWithDialect dataSource) {
        return new FileSystemFileService(testDataSource, temporaryStorageLocation.toString());
    }
}
