package com.sap.cloud.lm.sl.persistence.changes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.lm.sl.persistence.services.SqlExecutor;
import com.sap.cloud.lm.sl.persistence.services.SqlExecutor.StatementExecutor;
import com.sap.cloud.lm.sl.persistence.util.JdbcUtil;

public class IndexSpaceOfLmSlPersistenceFilePostgreSQLChange implements AsyncChange {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexProcessIdsOfProgressMessagesPostgreSQLChange.class);
    private static final String CREATE_INDEX = "CREATE INDEX CONCURRENTLY IDX_LM_SL_PERSISTENCE_SPACE ON LM_SL_PERSISTENCE_FILE(SPACE)";

    @Override
    public void execute(DataSource dataSource) throws SQLException {
        SqlExecutor executor = new SqlExecutor(dataSource);
        executor.execute(new StatementExecutor<Void>() {

            @Override
            public Void execute(Connection connection) throws SQLException {
                PreparedStatement statement = null;
                try {
                    LOGGER.info("Processing index for column space of lm_sl_persistence_file");
                    statement = connection.prepareStatement(CREATE_INDEX);
                    statement.executeUpdate();
                    LOGGER.info("Space column indexed");
                } finally {
                    JdbcUtil.closeQuietly(statement);
                }
                return null;
            }

        });
    }
}
