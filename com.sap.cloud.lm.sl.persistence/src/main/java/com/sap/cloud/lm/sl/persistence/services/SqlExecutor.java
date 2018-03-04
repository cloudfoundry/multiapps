package com.sap.cloud.lm.sl.persistence.services;

import java.sql.Connection;
import java.sql.SQLException;

import com.sap.cloud.lm.sl.persistence.util.JdbcUtil;

abstract class SqlExecutor {

    public <T> T execute(StatementExecutor<T> statementExecutor) throws SQLException {
        Connection connection = getConnection();
        try {
            T result = statementExecutor.execute(connection);
            JdbcUtil.commit(connection);
            return result;
        } catch (SQLException e) {
            JdbcUtil.rollback(connection);
            throw e;
        } finally {
            JdbcUtil.closeQuietly(connection);
        }
    }

    public <T> T executeInSingleTransaction(StatementExecutor<T> statementExecutor) throws SQLException {
        Connection connection = getConnection();
        try {
            connection.setAutoCommit(false);
            T result = statementExecutor.execute(connection);
            JdbcUtil.commit(connection);
            return result;
        } catch (SQLException e) {
            JdbcUtil.rollback(connection);
            throw e;
        } finally {
            connection.setAutoCommit(true);
            JdbcUtil.closeQuietly(connection);
        }
    }

    protected abstract Connection getConnection() throws SQLException;

    protected interface StatementExecutor<T> {

        T execute(Connection connection) throws SQLException;

    }

}
