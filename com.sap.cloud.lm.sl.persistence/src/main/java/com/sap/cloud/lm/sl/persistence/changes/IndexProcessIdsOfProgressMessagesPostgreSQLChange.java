package com.sap.cloud.lm.sl.persistence.changes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.lm.sl.persistence.message.Messages;
import com.sap.cloud.lm.sl.persistence.services.SqlExecutor;
import com.sap.cloud.lm.sl.persistence.services.SqlExecutor.StatementExecutor;
import com.sap.cloud.lm.sl.persistence.util.JdbcUtil;

public class IndexProcessIdsOfProgressMessagesPostgreSQLChange implements AsyncChange {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexProcessIdsOfProgressMessagesPostgreSQLChange.class);
    private static final String CREATE_INDEX = "CREATE INDEX CONCURRENTLY IDX_PROGRESS_MESSAGE_PROCESS_ID ON PROGRESS_MESSAGE(PROCESS_ID)";

    @Override
    public void execute(DataSource dataSource) throws SQLException {
        SqlExecutor executor = new SqlExecutor(dataSource);
        executor.execute(new StatementExecutor<Void>() {

            @Override
            public Void execute(Connection connection) throws SQLException {
                PreparedStatement statement = null;
                try {
                    LOGGER.info(Messages.INDEXING_PROCESS_IDS_OF_PROGRESS_MESSAGES);
                    statement = connection.prepareStatement(CREATE_INDEX);
                    statement.executeUpdate();
                    LOGGER.info(Messages.PROCESS_IDS_INDEXED);
                } finally {
                    JdbcUtil.closeQuietly(statement);
                }
                return null;
            }

        });
    }

}
