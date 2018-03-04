package com.sap.cloud.lm.sl.persistence.services;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.lm.sl.persistence.message.Messages;
import com.sap.cloud.lm.sl.persistence.model.FileEntry;
import com.sap.cloud.lm.sl.persistence.processors.FileDownloadProcessor;
import com.sap.cloud.lm.sl.persistence.services.SqlExecutor.StatementExecutor;
import com.sap.cloud.lm.sl.persistence.util.JdbcUtil;

/*
 * Provides functionality for uploading a file to the server and reading uploaded file contents
 *
 */
public class DatabaseFileService extends AbstractFileService {

    private static final String INSERT_FILE_CONTENT = "UPDATE %s SET CONTENT=? WHERE FILE_ID=?";
    private static final String SELECT_FILE_CONTENT_BY_ID = "SELECT FILE_ID, SPACE, CONTENT FROM %s WHERE FILE_ID=? AND SPACE=?";
    private static final String DELETE_FILES_WITHOUT_CONTENT = "DELETE FROM %s WHERE CONTENT IS NULL";

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseFileService.class);

    private static DatabaseFileService instance;

    public static DatabaseFileService getInstance() {
        if (instance == null) {
            instance = new DatabaseFileService(DEFAULT_TABLE_NAME);
        }
        return instance;
    }

    public DatabaseFileService(String tableName) {
        super(tableName);
    }

    @Override
    protected boolean storeFile(final FileEntry fileEntry, final InputStream inputStream) throws FileStorageException {
        try {
            return sqlExecutor.executeInSingleTransaction(new StatementExecutor<Boolean>() {
                @Override
                public Boolean execute(Connection connection) throws SQLException {
                    boolean attributesStoredSuccessfully = storeFileAttributes(connection, fileEntry);
                    if (!attributesStoredSuccessfully) {
                        return false;
                    }
                    return storeFileContent(connection, fileEntry, inputStream);
                }

            });
        } catch (SQLException e) {
            throw new FileStorageException(e.getMessage(), e);
        }
    }

    private boolean storeFileContent(Connection connection, FileEntry fileEntry, InputStream inputStream) throws SQLException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(getQuery(INSERT_FILE_CONTENT, tableName));
            getDatabaseDialect().setBlobAsBinaryStream(statement, 1, inputStream);
            statement.setString(2, fileEntry.getId());
            return statement.executeUpdate() > 0;
        } finally {
            JdbcUtil.closeQuietly(statement);
        }
    }

    @Override
    public void processFileContent(final FileDownloadProcessor fileDownloadProcessor) throws FileStorageException {
        deleteEntriesWithoutContent();
        try {
            sqlExecutor.executeInSingleTransaction(new StatementExecutor<Void>() {
                @Override
                public Void execute(Connection connection) throws SQLException {
                    PreparedStatement statement = null;
                    ResultSet resultSet = null;
                    try {
                        statement = connection.prepareStatement(getQuery(SELECT_FILE_CONTENT_BY_ID, tableName));
                        statement.setString(1, fileDownloadProcessor.getFileEntry()
                            .getId());
                        statement.setString(2, fileDownloadProcessor.getFileEntry()
                            .getSpace());
                        resultSet = statement.executeQuery();
                        if (resultSet.next()) {
                            processFileContent(resultSet, fileDownloadProcessor);
                        } else {
                            throw new SQLException(MessageFormat.format(Messages.FILE_NOT_FOUND, fileDownloadProcessor.getFileEntry()
                                .getId()));
                        }
                    } finally {
                        JdbcUtil.closeQuietly(resultSet);
                        JdbcUtil.closeQuietly(statement);
                    }
                    return null;
                }

            });
        } catch (SQLException e) {
            throw new FileStorageException(e.getMessage(), e);
        }
    }

    private void processFileContent(ResultSet resultSet, final FileDownloadProcessor fileDownloadProcessor) throws SQLException {
        InputStream fileStream = getDatabaseDialect().getBinaryStreamFromBlob(resultSet, FileServiceColumnNames.CONTENT);
        try {
            fileDownloadProcessor.processContent(fileStream);
        } catch (Exception e) {
            throw new SQLException(e.getMessage(), e);
        } finally {
            if (fileStream != null) {
                try {
                    fileStream.close();
                } catch (IOException e) {
                    LOGGER.error(Messages.UPLOAD_STREAM_FAILED_TO_CLOSE, e);
                }
            }
        }
    }

    /*
     * The implementation of this method is empty because the content of the file is being deleted by the abstract file service using a
     * DELETE query for the whole file record
     */
    @Override
    protected void deleteFileContent(String space, String id) throws FileStorageException {
    }

    @Override
    public List<FileEntry> listFiles(String space, String namespace) throws FileStorageException {
        deleteEntriesWithoutContent();
        return super.listFiles(space, namespace);
    }

    @Override
    public FileEntry getFile(String space, String id) throws FileStorageException {
        deleteEntriesWithoutContent();
        return super.getFile(space, id);
    }

    private int deleteEntriesWithoutContent() throws FileStorageException {
        try {
            return sqlExecutor.execute(new StatementExecutor<Integer>() {
                @Override
                public Integer execute(Connection connection) throws SQLException {
                    PreparedStatement statement = null;
                    try {
                        statement = connection.prepareStatement(getQuery(DELETE_FILES_WITHOUT_CONTENT, tableName));
                        int rowsDeleted = statement.executeUpdate();
                        return rowsDeleted;
                    } finally {
                        JdbcUtil.closeQuietly(statement);
                    }
                }

            });
        } catch (SQLException e) {
            throw new FileStorageException(e.getMessage(), e);
        }
    }

}
