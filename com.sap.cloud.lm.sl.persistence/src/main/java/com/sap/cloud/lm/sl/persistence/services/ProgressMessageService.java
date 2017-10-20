package com.sap.cloud.lm.sl.persistence.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.sap.cloud.lm.sl.common.SLException;
import com.sap.cloud.lm.sl.persistence.dialects.DatabaseDialect;
import com.sap.cloud.lm.sl.persistence.dialects.DefaultDatabaseDialect;
import com.sap.cloud.lm.sl.persistence.message.Messages;
import com.sap.cloud.lm.sl.persistence.model.ProgressMessage;
import com.sap.cloud.lm.sl.persistence.model.ProgressMessage.ProgressMessageType;
import com.sap.cloud.lm.sl.persistence.services.SqlExecutor.StatementExecutor;
import com.sap.cloud.lm.sl.persistence.util.JdbcUtil;

public class ProgressMessageService {

    private static final String COLUMN_NAME_ID = "ID";
    private static final String COLUMN_NAME_PROCESS_ID = "PROCESS_ID";
    private static final String COLUMN_NAME_TASK_ID = "TASK_ID";
    private static final String COLUMN_NAME_TYPE = "TYPE";
    private static final String COLUMN_NAME_TEXT = "TEXT";
    private static final String COLUMN_NAME_TIMESTAMP = "TIMESTAMP";

    private static final String DEFAULT_DATASOURCE_JNDI_NAME = "java:comp/env/jdbc/DefaultDB";

    private static final String SELECT_MESSAGE_BY_PROCESS_ID = "SELECT ID, PROCESS_ID, TASK_ID, TYPE, TEXT, TIMESTAMP FROM {0} WHERE PROCESS_ID=? ORDER BY ID";
    private static final String SELECT_MESSAGE_BY_PROCESS_AND_TASK_ID = "SELECT ID, PROCESS_ID, TASK_ID, TYPE, TEXT, TIMESTAMP FROM {0} WHERE PROCESS_ID=? AND TASK_ID=? ORDER BY ID";
    private static final String SELECT_MESSAGE_BY_PROCESS_AND_TASK_ID_PREFIX = "SELECT TASK_ID FROM {0} WHERE PROCESS_ID=? AND TASK_ID LIKE ? ORDER BY ID DESC";
    private static final String SELECT_MESSAGE_SPECIFIC_MESSAGE = "SELECT ID, PROCESS_ID, TASK_ID, TYPE, TEXT, TIMESTAMP FROM {0} WHERE PROCESS_ID=? AND TASK_ID=? AND TYPE=? ORDER BY ID";
    private static final String SELECT_MESSAGE_BY_PROCESS_ID_AND_TYPE = "SELECT ID, PROCESS_ID, TASK_ID, TYPE, TEXT, TIMESTAMP FROM {0} WHERE PROCESS_ID=? AND TYPE=? ORDER BY ID";
    private static final String SELECT_ALL_MESSAGES = "SELECT ID, PROCESS_ID, TASK_ID, TYPE, TEXT, TIMESTAMP FROM {0} ORDER BY ID";
    private static final String INSERT_MESSAGE = "INSERT INTO {0} (ID, PROCESS_ID, TASK_ID, TYPE, TEXT, TIMESTAMP) VALUES({1}, ?, ?, ?, ?, ?)";
    private static final String DELETE_MESSAGE_BY_PROCESS_AND_TASK_ID = "DELETE FROM {0} WHERE PROCESS_ID=? AND TASK_ID=?";
    private static final String DELETE_MESSAGE_BY_PROCESS_ID = "DELETE FROM {0} WHERE PROCESS_ID=?";
    private static final String UPDATE_MESSAGE_BY_ID = "UPDATE {0} SET TEXT=?, TIMESTAMP=? WHERE ID=?";

    private static final String ID_SEQ_NAME = "ID_SEQ";

    private static final String DEFAULT_TABLE_NAME = "PROGRESS_MESSAGE";

    private static ProgressMessageService instance;

    private final String tableName;
    private DataSource dataSource;
    private DatabaseDialect databaseDialect;

    public static ProgressMessageService getInstance() {
        if (instance == null) {
            instance = new ProgressMessageService(DEFAULT_TABLE_NAME);
        }
        return instance;
    }

    public ProgressMessageService(String tableName) {
        this.tableName = tableName;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setDatabaseDialect(DatabaseDialect databaseDialect) {
        this.databaseDialect = databaseDialect;
    }

    public void init(DataSource dataSource) {
        init(dataSource, new DefaultDatabaseDialect());
    }

    public void init(DataSource dataSource, DatabaseDialect databaseDialect) {
        this.dataSource = dataSource;
        this.databaseDialect = databaseDialect;
    }

    public boolean add(final ProgressMessage msg) throws SLException {
        SqlExecutor<Boolean> executor = new ProgressMessageSqlExecutor<Boolean>();
        try {
            return executor.execute(new StatementExecutor<Boolean>() {
                @Override
                public Boolean execute(Connection connection) throws SQLException {
                    PreparedStatement statement = null;
                    int rowsInserted = 0;
                    try {
                        statement = connection.prepareStatement(getQuery(INSERT_MESSAGE, tableName));
                        statement.setString(1, msg.getProcessId());
                        statement.setString(2, msg.getTaskId());
                        statement.setString(3, msg.getType().name());
                        statement.setString(4, msg.getText());
                        statement.setTimestamp(5, new Timestamp(msg.getTimestamp().getTime()));
                        rowsInserted = statement.executeUpdate();
                        JdbcUtil.commit(connection);
                        return (rowsInserted > 0);
                    } catch (SQLException e) {
                        JdbcUtil.rollback(connection);
                        throw e;
                    } finally {
                        JdbcUtil.closeQuietly(statement);
                    }
                }
            });
        } catch (SQLException e) {
            throw new SLException(e, MessageFormat.format(Messages.ERROR_SAVING_MESSAGE, msg.getProcessId(), msg.getTaskId()));
        }
    }

    public boolean update(final long existingId, final ProgressMessage newMsg) throws SLException {
        SqlExecutor<Boolean> executor = new ProgressMessageSqlExecutor<Boolean>();
        try {
            return executor.execute(new StatementExecutor<Boolean>() {
                @Override
                public Boolean execute(Connection connection) throws SQLException {
                    PreparedStatement statement = null;
                    int rowsUpdated = 0;
                    try {
                        statement = connection.prepareStatement(getQuery(UPDATE_MESSAGE_BY_ID, tableName));
                        statement.setString(1, newMsg.getText());
                        statement.setTimestamp(2, new Timestamp(newMsg.getTimestamp().getTime()));
                        statement.setLong(3, existingId);
                        rowsUpdated = statement.executeUpdate();
                        JdbcUtil.commit(connection);
                        return (rowsUpdated == 1);
                    } catch (SQLException e) {
                        JdbcUtil.rollback(connection);
                        throw e;
                    } finally {
                        JdbcUtil.closeQuietly(statement);
                    }
                }
            });
        } catch (SQLException e) {
            throw new SLException(e, MessageFormat.format(Messages.ERROR_UPDATING_MESSAGE, newMsg.getId()));
        }
    }

    public int removeByProcessId(final String processId) throws SLException {
        SqlExecutor<Integer> executor = new ProgressMessageSqlExecutor<Integer>();
        try {
            return executor.execute(new StatementExecutor<Integer>() {
                @Override
                public Integer execute(Connection connection) throws SQLException {
                    int rowsRemoved = 0;
                    PreparedStatement statement = null;
                    try {
                        statement = connection.prepareStatement(getQuery(DELETE_MESSAGE_BY_PROCESS_ID, tableName));
                        statement.setString(1, processId);
                        rowsRemoved = statement.executeUpdate();
                        JdbcUtil.commit(connection);
                        return rowsRemoved;
                    } catch (SQLException e) {
                        JdbcUtil.rollback(connection);
                        throw e;
                    } finally {
                        JdbcUtil.closeQuietly(statement);
                    }
                }
            });
        } catch (SQLException e) {
            throw new SLException(e, MessageFormat.format(Messages.ERROR_DELETING_MESSAGES_BY_PROCESS_ID, processId));
        }
    }

    public int removeByProcessIdAndTaskId(final String processId, final String taskId) throws SLException {
        SqlExecutor<Integer> executor = new ProgressMessageSqlExecutor<Integer>();
        try {
            return executor.execute(new StatementExecutor<Integer>() {
                @Override
                public Integer execute(Connection connection) throws SQLException {
                    int rowsRemoved = 0;
                    PreparedStatement statement = null;
                    try {
                        statement = connection.prepareStatement(getQuery(DELETE_MESSAGE_BY_PROCESS_AND_TASK_ID, tableName));
                        statement.setString(1, processId);
                        statement.setString(2, taskId);
                        rowsRemoved = statement.executeUpdate();
                        JdbcUtil.commit(connection);
                        return rowsRemoved;
                    } catch (SQLException e) {
                        JdbcUtil.rollback(connection);
                        throw e;
                    } finally {
                        JdbcUtil.closeQuietly(statement);
                    }
                }
            });
        } catch (SQLException e) {
            throw new SLException(e, MessageFormat.format(Messages.ERROR_DELETING_MESSAGES_BY_PROCESS_ID_AND_TASK_ID, processId, taskId));
        }
    }

    public List<ProgressMessage> findAll() throws SLException {
        SqlExecutor<List<ProgressMessage>> executor = new ProgressMessageSqlExecutor<List<ProgressMessage>>();
        try {
            return executor.execute(new StatementExecutor<List<ProgressMessage>>() {
                @Override
                public List<ProgressMessage> execute(Connection connection) throws SQLException {
                    List<ProgressMessage> messages = new ArrayList<ProgressMessage>();
                    PreparedStatement statement = null;
                    ResultSet resultSet = null;
                    try {
                        statement = connection.prepareStatement(getQuery(SELECT_ALL_MESSAGES, tableName));
                        resultSet = statement.executeQuery();
                        while (resultSet.next()) {
                            messages.add(getMessage(resultSet));
                        }
                    } finally {
                        JdbcUtil.closeQuietly(resultSet);
                        JdbcUtil.closeQuietly(statement);
                    }
                    return messages;
                }
            });
        } catch (SQLException e) {
            throw new SLException(e, Messages.ERROR_GETTING_ALL_MESSAGES);
        }
    }

    public List<ProgressMessage> findByProcessId(final String processId) throws SLException {
        SqlExecutor<List<ProgressMessage>> executor = new ProgressMessageSqlExecutor<List<ProgressMessage>>();
        try {
            return executor.execute(new StatementExecutor<List<ProgressMessage>>() {
                @Override
                public List<ProgressMessage> execute(Connection connection) throws SQLException {
                    List<ProgressMessage> messages = new ArrayList<ProgressMessage>();
                    PreparedStatement statement = null;
                    ResultSet resultSet = null;
                    try {
                        statement = connection.prepareStatement(getQuery(SELECT_MESSAGE_BY_PROCESS_ID, tableName));
                        statement.setString(1, processId);
                        resultSet = statement.executeQuery();
                        while (resultSet.next()) {
                            messages.add(getMessage(resultSet));
                        }
                    } finally {
                        JdbcUtil.closeQuietly(resultSet);
                        JdbcUtil.closeQuietly(statement);
                    }
                    return messages;
                }
            });
        } catch (SQLException e) {
            throw new SLException(e, MessageFormat.format(Messages.ERROR_GETTING_MESSAGE_BY_PROCESS_ID, processId));
        }
    }

    public List<ProgressMessage> findByProcessIdAndTaskId(final String processId, final String taskId) throws SLException {
        SqlExecutor<List<ProgressMessage>> executor = new ProgressMessageSqlExecutor<List<ProgressMessage>>();
        try {
            return executor.execute(new StatementExecutor<List<ProgressMessage>>() {
                @Override
                public List<ProgressMessage> execute(Connection connection) throws SQLException {
                    List<ProgressMessage> messages = new ArrayList<ProgressMessage>();
                    PreparedStatement statement = null;
                    ResultSet resultSet = null;
                    try {
                        statement = connection.prepareStatement(getQuery(SELECT_MESSAGE_BY_PROCESS_AND_TASK_ID, tableName));
                        statement.setString(1, processId);
                        statement.setString(2, taskId);
                        resultSet = statement.executeQuery();
                        while (resultSet.next()) {
                            messages.add(getMessage(resultSet));
                        }
                    } finally {
                        JdbcUtil.closeQuietly(resultSet);
                        JdbcUtil.closeQuietly(statement);
                    }
                    return messages;
                }
            });
        } catch (SQLException e) {
            throw new SLException(e, MessageFormat.format(Messages.ERROR_GETTING_MESSAGE_PROCESS_ID_TASK_ID, processId, taskId));
        }
    }

    public String findLastTaskId(final String processId, final String taskId) throws SLException {
        SqlExecutor<String> executor = new ProgressMessageSqlExecutor<String>();
        try {
            return executor.execute(new StatementExecutor<String>() {
                @Override
                public String execute(Connection connection) throws SQLException {
                    PreparedStatement statement = null;
                    ResultSet resultSet = null;
                    try {
                        statement = connection.prepareStatement(getQuery(SELECT_MESSAGE_BY_PROCESS_AND_TASK_ID_PREFIX, tableName));
                        statement.setString(1, processId);
                        statement.setString(2, taskId + "%");
                        resultSet = statement.executeQuery();
                        if (resultSet.next()) {
                            return resultSet.getString(1);
                        }
                    } finally {
                        JdbcUtil.closeQuietly(resultSet);
                        JdbcUtil.closeQuietly(statement);
                    }
                    return null;
                }
            });
        } catch (SQLException e) {
            throw new SLException(e, MessageFormat.format(Messages.ERROR_GETTING_MESSAGE_PROCESS_ID_STARTING_WITH, processId, taskId));
        }
    }

    public List<ProgressMessage> findByProcessIdTaskIdAndType(final String processId, final String taskId, final ProgressMessageType type)
        throws SLException {
        SqlExecutor<List<ProgressMessage>> executor = new ProgressMessageSqlExecutor<List<ProgressMessage>>();
        try {
            return executor.execute(new StatementExecutor<List<ProgressMessage>>() {
                @Override
                public List<ProgressMessage> execute(Connection connection) throws SQLException {
                    List<ProgressMessage> messages = new ArrayList<ProgressMessage>();
                    PreparedStatement statement = null;
                    ResultSet resultSet = null;
                    try {
                        statement = connection.prepareStatement(getQuery(SELECT_MESSAGE_SPECIFIC_MESSAGE, tableName));
                        statement.setString(1, processId);
                        statement.setString(2, taskId);
                        statement.setString(3, type.name());
                        resultSet = statement.executeQuery();
                        while (resultSet.next()) {
                            messages.add(getMessage(resultSet));
                        }
                    } finally {
                        JdbcUtil.closeQuietly(resultSet);
                        JdbcUtil.closeQuietly(statement);
                    }
                    return messages;
                }
            });
        } catch (SQLException e) {
            throw new SLException(e,
                MessageFormat.format(Messages.ERROR_GETTING_MESSAGE_PROCESS_ID_TASK_ID_TYPE, processId, taskId, type.name()));
        }
    }

    public List<ProgressMessage> findByProcessIdAndType(final String processInstanceId, final ProgressMessageType type) throws SLException {
        SqlExecutor<List<ProgressMessage>> executor = new ProgressMessageSqlExecutor<List<ProgressMessage>>();
        try {
            return executor.execute(new StatementExecutor<List<ProgressMessage>>() {
                @Override
                public List<ProgressMessage> execute(Connection connection) throws SQLException {
                    List<ProgressMessage> messages = new ArrayList<ProgressMessage>();
                    PreparedStatement statement = null;
                    ResultSet resultSet = null;
                    try {
                        statement = connection.prepareStatement(getQuery(SELECT_MESSAGE_BY_PROCESS_ID_AND_TYPE, tableName));
                        statement.setString(1, processInstanceId);
                        statement.setString(2, type.name());
                        resultSet = statement.executeQuery();
                        while (resultSet.next()) {
                            messages.add(getMessage(resultSet));
                        }
                    } finally {
                        JdbcUtil.closeQuietly(resultSet);
                        JdbcUtil.closeQuietly(statement);
                    }
                    return messages;
                }
            });
        } catch (SQLException e) {
            throw new SLException(e, MessageFormat.format(Messages.ERROR_GETTING_MESSAGE_PROCESS_ID_TYPE, processInstanceId, type.name()));
        }
    }

    private ProgressMessage getMessage(ResultSet resultSet) throws SQLException {
        ProgressMessage message = new ProgressMessage();
        message.setId(resultSet.getLong(COLUMN_NAME_ID));
        message.setProcessId(resultSet.getString(COLUMN_NAME_PROCESS_ID));
        message.setTaskId(resultSet.getString(COLUMN_NAME_TASK_ID));
        message.setText(resultSet.getString(COLUMN_NAME_TEXT));
        message.setType(ProgressMessageType.valueOf(resultSet.getString(COLUMN_NAME_TYPE)));
        Timestamp dbTimestamp = resultSet.getTimestamp(COLUMN_NAME_TIMESTAMP);
        Date timestamp = (dbTimestamp == null) ? new Date() : new Date(dbTimestamp.getTime());
        message.setTimestamp(timestamp);
        return message;
    }

    private DataSource getDataSource() {
        if (dataSource == null) {
            dataSource = lookupDataSource();
        }
        return dataSource;
    }

    private DatabaseDialect getDatabaseDialect() {
        if (databaseDialect == null) {
            databaseDialect = new DefaultDatabaseDialect();
        }
        return databaseDialect;
    }

    private static DataSource lookupDataSource() {
        try {
            return (DataSource) new InitialContext().lookup(DEFAULT_DATASOURCE_JNDI_NAME);
        } catch (NamingException e) {
            throw new IllegalStateException(e);
        }
    }

    private String getQuery(String statementTemplate, String tableName) {
        return MessageFormat.format(statementTemplate, tableName, getDatabaseDialect().getSequenceNextValueSyntax(ID_SEQ_NAME));
    }

    private class ProgressMessageSqlExecutor<T> extends SqlExecutor<T> {
        @Override
        protected Connection getConnection() throws SQLException {
            return getDataSource().getConnection();
        }
    }

}
