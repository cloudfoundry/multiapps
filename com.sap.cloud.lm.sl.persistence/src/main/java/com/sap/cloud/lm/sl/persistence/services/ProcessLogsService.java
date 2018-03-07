package com.sap.cloud.lm.sl.persistence.services;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;

import com.sap.cloud.lm.sl.common.SLException;
import com.sap.cloud.lm.sl.common.util.CommonUtil;
import com.sap.cloud.lm.sl.persistence.dialects.DatabaseDialect;
import com.sap.cloud.lm.sl.persistence.dialects.DefaultDatabaseDialect;
import com.sap.cloud.lm.sl.persistence.message.Messages;
import com.sap.cloud.lm.sl.persistence.model.FileEntry;
import com.sap.cloud.lm.sl.persistence.processors.DefaultFileDownloadProcessor;
import com.sap.cloud.lm.sl.persistence.services.SqlExecutor.StatementExecutor;
import com.sap.cloud.lm.sl.persistence.util.JdbcUtil;

public class ProcessLogsService extends DatabaseFileService {

    private static final String TABLE_NAME = "process_log";
    private static final String DELETE_CONTENT_BY_NAMESPACE = "DELETE FROM %s WHERE NAMESPACE=?";

    public ProcessLogsService(DataSource dataSource) {
        this(dataSource, new DefaultDatabaseDialect());
    }

    public ProcessLogsService(DataSource dataSource, DatabaseDialect databaseDialect) {
        super(TABLE_NAME, dataSource, databaseDialect);
    }

    public List<String> getLogNames(String space, String processId) throws FileStorageException {
        List<String> result = new ArrayList<>();
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
            if (fileEntry.getName()
                .equals(fileName)) {
                return fileEntry.getId();
            }
        }
        return null;
    }

    public int deleteAllByProcessIds(final List<String> processIds) throws SLException {
        try {
            return getSqlExecutor().executeInSingleTransaction(new StatementExecutor<Integer>() {
                @Override
                public Integer execute(Connection connection) throws SQLException {
                    PreparedStatement statement = null;
                    try {
                        statement = connection.prepareStatement(getQuery(DELETE_CONTENT_BY_NAMESPACE));
                        for (String processId : processIds) {
                            statement.setString(1, processId);
                            statement.addBatch();
                        }
                        int[] rowsRemovedArray = statement.executeBatch();
                        return CommonUtil.sumOfInts(rowsRemovedArray);
                    } finally {
                        JdbcUtil.closeQuietly(statement);
                    }
                }
            });
        } catch (SQLException e) {
            throw new SLException(e, Messages.ERROR_DELETING_MESSAGES_WITH_PROCESS_ID, processIds);
        }
    }

}
