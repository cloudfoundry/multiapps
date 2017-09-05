package com.sap.cloud.lm.sl.persistence.services;

import java.sql.Connection;
import java.sql.SQLException;

import com.sap.cloud.lm.sl.persistence.util.JdbcUtil;

class SqlExecutor<T> {
    public T execute(StatementExecutor<T> statementExecutor) throws SQLException {
        T result = null;
        try {
            Connection connection = getConnection();
            try {
                result = statementExecutor.execute(connection);
            } finally {
                JdbcUtil.closeQuietly(connection);
            }
        } catch (SQLException e) {
            throw e;
        }
        return result;
    }

    protected Connection getConnection() throws SQLException {
        return null;
    }

    protected interface StatementExecutor<T> {
        T execute(Connection connection) throws SQLException;
    }
}
