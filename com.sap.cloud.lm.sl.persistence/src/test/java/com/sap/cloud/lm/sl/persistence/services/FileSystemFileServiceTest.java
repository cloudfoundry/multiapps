package com.sap.cloud.lm.sl.persistence.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.sap.cloud.lm.sl.common.util.DigestHelper;
import com.sap.cloud.lm.sl.persistence.model.FileEntry;
import com.sap.cloud.lm.sl.persistence.processors.DefaultFileUploadProcessor;

public class FileSystemFileServiceTest extends DatabaseFileServiceTest {

    private static final String TEST_FILE_LOCATION = "src/test/resources/com.sap.cloud.lm.sl.web-0.1.0-SNAPSHOT.war";
    private static final String SECOND_FILE_TEST_LOCATION = "src/test/resources/hell.jpg";

    private String spaceId;
    private String namespace;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Path temporaryStorageLocation;

    @Before
    public void setUp() throws Exception {
        temporaryStorageLocation = Files.createTempDirectory("testfileService");
        spaceId = UUID.randomUUID().toString();
        namespace = UUID.randomUUID().toString();
        setupConnection();
        insertInitialData();
    }

    @Test
    public void testAddFile() throws FileStorageException, IOException {
        Path testFilePath = Paths.get(TEST_FILE_LOCATION).toAbsolutePath();
        FileEntry addedFile = fileService.addFile(spaceId, namespace, testFilePath.toFile().getName(),
            new DefaultFileUploadProcessor(false), Files.newInputStream(testFilePath));
        validateFileExistance(addedFile);
    }

    @Test
    public void testAddFileWichAlreadyExists() throws FileStorageException, IOException {
        Path testFilePath = Paths.get(TEST_FILE_LOCATION).toAbsolutePath();
        fileService.addFile(spaceId, namespace, testFilePath.toFile().getName(), new DefaultFileUploadProcessor(false),
            testFilePath.toFile());
        FileEntry addedFile = fileService.addFile(spaceId, namespace, testFilePath.toFile().getName(),
            new DefaultFileUploadProcessor(false), testFilePath.toFile());
        validateFileExistance(addedFile);
    }

    @Test
    public void testAddExistingFile() throws FileStorageException, IOException {
        Path testFilePath = Paths.get(TEST_FILE_LOCATION).toAbsolutePath();
        FileEntry addedFile = fileService.addFile(spaceId, namespace, testFilePath.toFile().getName(),
            new DefaultFileUploadProcessor(false), testFilePath.toFile());

        validateFileExistance(addedFile);
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
        Path testFilePath = Paths.get(TEST_FILE_LOCATION).toAbsolutePath();
        FileEntry addedFile = fileService.addFile(spaceId, namespace, testFilePath.toFile().getName(),
            new DefaultFileUploadProcessor(false), testFilePath.toFile());

        FileEntry actualFile = fileService.getFile(spaceId, addedFile.getId());
        String expectedFileFileDigest = DigestHelper.computeFileChecksum(testFilePath, DIGEST_METHOD);
        String actualFileFileDigest = actualFile.getDigest();
        Assert.assertEquals(expectedFileFileDigest, actualFileFileDigest);
    }

    @Test
    public void testDeleteFile() throws FileStorageException {
        Path testFilePath = Paths.get(TEST_FILE_LOCATION).toAbsolutePath();
        FileEntry addedFile = fileService.addFile(spaceId, namespace, testFilePath.toFile().getName(),
            new DefaultFileUploadProcessor(false), testFilePath.toFile());

        int deletionResult = fileService.deleteFile(spaceId, addedFile.getId());
        Assert.assertEquals(1, deletionResult);
    }

    @Test
    public void testUploadTwoFiles() throws FileStorageException, IOException {
        Path testFilePath = Paths.get(TEST_FILE_LOCATION);
        FileEntry firstAddedFile = fileService.addFile(spaceId, namespace, testFilePath.toFile().getName(),
            new DefaultFileUploadProcessor(false), Files.newInputStream(testFilePath));

        Path secondTestFilePath = Paths.get(SECOND_FILE_TEST_LOCATION);
        FileEntry secondAddedFile = fileService.addFile(spaceId, namespace, secondTestFilePath.toFile().getName(),
            new DefaultFileUploadProcessor(false), Files.newInputStream(secondTestFilePath));

        List<FileEntry> fileEntries = fileService.listFiles(spaceId, namespace);
        Assert.assertEquals(2, fileEntries.size());
        validateFilesEquality(fileEntries, firstAddedFile, secondAddedFile);

    }

    @Override
    public void testFileContent() throws Exception {
        Path testFilePath = Paths.get(TEST_FILE_LOCATION);
        FileEntry addedFile = fileService.addFile(spaceId, namespace, testFilePath.toFile().getName(),
            new DefaultFileUploadProcessor(false), Files.newInputStream(testFilePath));

        String testFileDigest = DigestHelper.computeFileChecksum(testFilePath, DIGEST_METHOD).toLowerCase();
        validateFileContent(addedFile, testFileDigest);
    }

    @Test
    public void testFileContentNotExisting() throws Exception {
        String testFileDigest = DigestHelper.computeFileChecksum(Paths.get(TEST_FILE_LOCATION), DIGEST_METHOD).toLowerCase();
        FileEntry dummyFileEntry = new FileEntry();
        dummyFileEntry.setId("not-existing-file-id");
        dummyFileEntry.setSpace("not-existing-space-id");
        expectedException.expect(FileStorageException.class);
        expectedException.expectMessage("File with id not-existing-file-id and space not-existing-space-id does not exist.");
        validateFileContent(dummyFileEntry, testFileDigest);
    }

    @Override
    public void testUploadTwoIdenticalFiles() throws Exception {
        Path testFilePath = Paths.get(TEST_FILE_LOCATION);
        fileService.addFile(spaceId, namespace, testFilePath.toFile().getName(), new DefaultFileUploadProcessor(false),
            Files.newInputStream(testFilePath));

        fileService.addFile(spaceId, namespace, testFilePath.toFile().getName(), new DefaultFileUploadProcessor(false),
            Files.newInputStream(testFilePath));

        List<FileEntry> listFiles = fileService.listFiles(spaceId, namespace);
        Assert.assertEquals(2, listFiles.size());
    }

    @Override
    public void testNamespaceIsolation() throws Exception {
        // No need for testing as the isolation is covered in the other test case scenarios
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
            if (expected.getId().equals(fileEntry.getId())) {
                return fileEntry;
            }
        }
        return null;
    }

    private void validateFileExistance(FileEntry actualFile) {
        Assert.assertTrue(Files.exists(Paths.get(temporaryStorageLocation.toString(), spaceId, "files", actualFile.getId())));
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
    protected AbstractFileService getFileService() {
        return new FileSystemFileService(temporaryStorageLocation.toString());
    }
}
