package com.sap.cloud.lm.sl.persistence.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcUtil {
    public static void closeQuietly(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (Exception ignored) {
            // ignore
        }
    }

    public static void closeQuietly(PreparedStatement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (Exception ignored) {
            // ignore
        }
    }

    public static void closeQuietly(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception ignored) {
            // ignore
        }
    }

    public static void commit(Connection connection) throws SQLException {
        if (!connection.getAutoCommit()) {
            connection.commit();
        }
    }

    public static void rollback(Connection connection) throws SQLException {
        if (!connection.getAutoCommit()) {
            connection.rollback();
        }
    }

}
