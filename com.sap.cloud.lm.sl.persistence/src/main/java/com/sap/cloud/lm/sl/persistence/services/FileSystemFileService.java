package com.sap.cloud.lm.sl.persistence.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.lm.sl.common.SLException;
import com.sap.cloud.lm.sl.common.util.CommonUtil;
import com.sap.cloud.lm.sl.persistence.message.Messages;
import com.sap.cloud.lm.sl.persistence.model.FileEntry;
import com.sap.cloud.lm.sl.persistence.processors.FileDownloadProcessor;

public class FileSystemFileService extends AbstractFileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseFileService.class);

    private static final String DEFAULT_FILES_STORAGE_PATH = "files";
    private static FileSystemFileService instance;

    private String storagePath;

    public static FileSystemFileService getInstance() {
        if (instance == null) {
            instance = new FileSystemFileService(DEFAULT_FILES_STORAGE_PATH);
        }
        return instance;
    }

    public FileSystemFileService(String storagePath) {
        super(DEFAULT_TABLE_NAME);
        this.storagePath = storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    @Override
    protected boolean uploadFileContent(InputStream inputStream, FileEntry fileEntry) throws FileStorageException {
        try {
            Path filesDirectory = getFilesDirectory(fileEntry.getSpace());
            Path newFilePath = Paths.get(filesDirectory.toString(), fileEntry.getId());
            Files.copy(inputStream, newFilePath, StandardCopyOption.REPLACE_EXISTING);
            return Files.exists(newFilePath);
        } catch (IOException e) {
            throw new FileStorageException(e.getMessage(), e);
        }
    }

    @Override
    protected void deleteFileContent(String space, String id) throws FileStorageException {
        try {
            Path filesDirectory = getFilesDirectory(space);
            Path filePath = Paths.get(filesDirectory.toString(), id);
            LOGGER.debug(MessageFormat.format(Messages.DELETING_FILE_WITH_PATH, filePath.toString()));
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new FileStorageException(MessageFormat.format(Messages.ERROR_DELETING_FILE_WITH_ID, id), e);
        }
    }

    @Override
    public void processFileContent(FileDownloadProcessor fileDownloadProcessor) throws FileStorageException {
        InputStream fileContentStream = null;
        try {
            String fileId = fileDownloadProcessor.getFileEntry().getId();
            String space = fileDownloadProcessor.getFileEntry().getSpace();
            Path filesDirectory = getFilesDirectory(space);
            Path filePathLocation = Paths.get(filesDirectory.toString(), fileId);
            if (!Files.exists(filePathLocation)) {
                deleteFileAttributes(space, fileId);
                throw new FileStorageException(MessageFormat.format(Messages.FILE_WITH_ID_DOES_NOT_EXIST, fileId, space));
            }
            fileContentStream = Files.newInputStream(filePathLocation);
            fileDownloadProcessor.processContent(fileContentStream);
        } catch (Exception e) {
            throw new FileStorageException(e);
        } finally {
            if (fileContentStream != null) {
                IOUtils.closeQuietly(fileContentStream);
            }
        }
    }

    @Override
    public int deleteAll(String space, String namespace) throws FileStorageException {
        throw new UnsupportedOperationException();
    }

    private Path getFilesDirectory(String space) throws IOException {
        Path filesPerSpaceDirectory = getFilesPerSpaceDirectory(space);
        if (!Files.exists(filesPerSpaceDirectory)) {
            Files.createDirectories(filesPerSpaceDirectory);
        }
        return filesPerSpaceDirectory;
    }

    @Override
    public int deleteAllByFileIds(Map<String, List<String>> spaceToFileIds) throws SLException {
        int deletedFiles = 0;
        for (String space : spaceToFileIds.keySet()) {
            Path filesPerSpaceDirectory = getFilesPerSpaceDirectory(space);
            if (!Files.exists(filesPerSpaceDirectory) || CommonUtil.isNullOrEmpty(spaceToFileIds.get(space))) {
                continue;
            }
            for (String fileId : spaceToFileIds.get(space)) {
                Path filePath = Paths.get(filesPerSpaceDirectory.toString(), fileId);
                try {
                    if (Files.deleteIfExists(filePath)) {
                        LOGGER.info(MessageFormat.format(Messages.DELETING_FILE_WITH_PATH, filePath.toString()));
                        deletedFiles++;
                    }
                } catch (IOException e) {
                    throw new SLException(MessageFormat.format(Messages.ERROR_DELETING_FILE_WITH_ID, fileId), e);
                }
            }
        }
        return deletedFiles;
    }

    private Path getFilesPerSpaceDirectory(String space) {
        Path filesPerSpaceDirectory = Paths.get(storagePath, space, DEFAULT_FILES_STORAGE_PATH);
        return filesPerSpaceDirectory;
    }
}
