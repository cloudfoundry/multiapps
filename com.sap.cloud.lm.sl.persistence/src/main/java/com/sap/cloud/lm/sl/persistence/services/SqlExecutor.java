package com.sap.cloud.lm.sl.persistence.services;

import java.sql.Connection;
import java.sql.SQLException;

import com.sap.cloud.lm.sl.persistence.util.JdbcUtil;

abstract class SqlExecutor {

    public <T> T execute(StatementExecutor<T> statementExecutor) throws SQLException {
        Connection connection = getConnection();
        try {
            return statementExecutor.execute(connection);
        } finally {
            JdbcUtil.closeQuietly(connection);
        }
    }

    protected abstract Connection getConnection() throws SQLException;

    protected interface StatementExecutor<T> {

        T execute(Connection connection) throws SQLException;

    }

}
