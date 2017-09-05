package com.sap.cloud.lm.sl.persistence.services;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

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

    private static final String UPDATE_FILE_CONTENT_BY_ID = "UPDATE {0} SET CONTENT = ? WHERE FILE_ID = ?";
    private static final String SELECT_FILE_CONTENT_BY_ID = "SELECT FILE_ID, SPACE, CONTENT FROM {0} WHERE FILE_ID=? AND SPACE=?";

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
    protected boolean uploadFileContent(final InputStream inputStream, final FileEntry fileEntry) throws FileStorageException {
        SqlExecutor<Boolean> executor = new FileServiceSqlExecutor<>();
        try {
            return executor.execute(new StatementExecutor<Boolean>() {
                @Override
                public Boolean execute(Connection connection) throws SQLException {
                    PreparedStatement stmt = null;
                    int rowsInserted = 0;
                    try {
                        connection.setAutoCommit(false);
                        stmt = connection.prepareStatement(getQuery(UPDATE_FILE_CONTENT_BY_ID, tableName));
                        getDatabaseDialect().setBlobAsBinaryStream(stmt, 1, inputStream);
                        stmt.setString(2, fileEntry.getId());
                        rowsInserted = stmt.executeUpdate();
                        JdbcUtil.commit(connection);
                        return rowsInserted > 0;
                    } catch (SQLException e) {
                        JdbcUtil.rollback(connection);
                        throw e;
                    } finally {
                        JdbcUtil.closeQuietly(stmt);
                        connection.setAutoCommit(true);
                    }
                }

            });
        } catch (SQLException e) {
            throw new FileStorageException(e.getMessage(), e);
        }

    }

    @Override
    public void processFileContent(final FileDownloadProcessor fileDownloadProcessor) throws FileStorageException {
        SqlExecutor<Void> executor = new FileServiceSqlExecutor<Void>();
        try {
            executor.execute(new StatementExecutor<Void>() {
                @Override
                public Void execute(Connection connection) throws SQLException {
                    PreparedStatement stmt = null;
                    ResultSet resultSet = null;
                    try {
                        connection.setAutoCommit(false);
                        stmt = connection.prepareStatement(getQuery(SELECT_FILE_CONTENT_BY_ID, tableName));
                        stmt.setString(1, fileDownloadProcessor.getFileEntry().getId());
                        stmt.setString(2, fileDownloadProcessor.getFileEntry().getSpace());
                        resultSet = stmt.executeQuery();
                        JdbcUtil.commit(connection);
                        if (resultSet.next()) {
                            processFileContent(resultSet, fileDownloadProcessor);
                        } else {
                            throw new SQLException(
                                MessageFormat.format(Messages.FILE_NOT_FOUND, fileDownloadProcessor.getFileEntry().getId()));
                        }
                    } finally {
                        connection.setAutoCommit(true);
                        JdbcUtil.closeQuietly(resultSet);
                        JdbcUtil.closeQuietly(stmt);
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

}
