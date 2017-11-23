package com.sap.cloud.lm.sl.persistence.services;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.sap.cloud.lm.sl.common.SLException;
import com.sap.cloud.lm.sl.persistence.message.Messages;
import com.sap.cloud.lm.sl.persistence.model.FileEntry;
import com.sap.cloud.lm.sl.persistence.processors.DefaultFileDownloadProcessor;
import com.sap.cloud.lm.sl.persistence.services.SqlExecutor.StatementExecutor;
import com.sap.cloud.lm.sl.persistence.util.JdbcUtil;

public class ProcessLogsService extends DatabaseFileService {
    private static final String TABLE_NAME = "process_log";
    private static final String DELETE_CONTENT_BY_NAMESPACE = "DELETE FROM {0} WHERE NAMESPACE=?";

    private static ProcessLogsService instance;

    public static ProcessLogsService getInstance() {
        if (instance == null) {
            instance = new ProcessLogsService();
        }
        return instance;
    }

    public ProcessLogsService() {
        super(TABLE_NAME);
    }

    public List<String> getLogNames(String space, String processId) throws FileStorageException {
        List<String> result = new ArrayList<String>();
        List<FileEntry> logFiles = listFiles(space, processId);
        for (FileEntry logFile : logFiles) {
            result.add(logFile.getName());
        }
        return result;
    }

    public String getLogContent(String space, String processId, String logName) throws FileStorageException {
        final StringBuilder builder = new StringBuilder();
        String logId = findFileId(space, processId, logName);

        FileContentProcessor streamProcessor = new FileContentProcessor() {
            @Override
            public void processFileContent(InputStream is) throws IOException {
                builder.append(IOUtils.toString(is));
            }
        };
        processFileContent(new DefaultFileDownloadProcessor(space, logId, streamProcessor));
        return builder.toString();
    }

    public String findFileId(String space, String processId, String fileName) throws FileStorageException {
        List<FileEntry> listFiles = listFiles(space, processId);
        for (FileEntry fileEntry : listFiles) {
            if (fileEntry.getName().equals(fileName)) {
                return fileEntry.getId();
            }
        }
        return null;
    }

    public int deleteAllByProcessIds(final List<String> processIds) throws SLException {
        SqlExecutor<Integer> executor = new FileServiceSqlExecutor<Integer>();
        try {
            return executor.execute(new StatementExecutor<Integer>() {
                @Override
                public Integer execute(Connection connection) throws SQLException {
                    int rowsRemoved = 0;
                    PreparedStatement statement = null;
                    try {
                        connection.setAutoCommit(false);
                        statement = connection.prepareStatement(getQuery(DELETE_CONTENT_BY_NAMESPACE, tableName));
                        for (String processId : processIds) {
                            statement.setString(1, processId);
                            statement.addBatch();
                        }
                        int[] rowsRemovedArray = statement.executeBatch();
                        rowsRemoved = Arrays.stream(rowsRemovedArray).sum();
                        JdbcUtil.commit(connection);
                    } catch (SQLException e) {
                        JdbcUtil.rollback(connection);
                        throw e;
                    } finally {
                        JdbcUtil.closeQuietly(statement);
                    }
                    return rowsRemoved;
                }
            });
        } catch (SQLException e) {
            throw new SLException(e, MessageFormat.format(Messages.ERROR_DELETING_MESSAGES_BY_PROCESS_ID, processIds));
        }
    }

}
