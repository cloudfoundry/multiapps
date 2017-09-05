package com.sap.cloud.lm.sl.common.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

public class TestDataSourceProvider {

    public static DataSource getDataSource(String liquibaseChangelogLocation) throws Exception {
        // create a hsql in memory connection
        Connection connection = createH2InMemory();

        // Create the schema for unit testing
        Database liquibaseDb = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        Liquibase lq = new Liquibase(liquibaseChangelogLocation, new ClassLoaderResourceAccessor(), liquibaseDb);
        lq.update("");

        // Initialize the fileService to use our in-memory connection through a pool emulation (so
        // that close releases rather than close)
        return new SingleConnectionDataSource(connection, true);
    }

    private static Connection createH2InMemory() throws SQLException {
        Connection connection = null;
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
        return connection;
    }
}
