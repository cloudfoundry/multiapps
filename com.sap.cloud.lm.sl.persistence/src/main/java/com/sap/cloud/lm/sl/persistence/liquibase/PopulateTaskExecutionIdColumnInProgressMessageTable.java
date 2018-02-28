package com.sap.cloud.lm.sl.persistence.liquibase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sap.cloud.lm.sl.persistence.message.Messages;

public class PopulateTaskExecutionIdColumnInProgressMessageTable
    extends AbstractDataTransformationChange<List<String>, Map<String, PopulateTaskExecutionIdColumnInProgressMessageTable.SplitTaskId>> {

    private static final String TABLE_NAME = "PROGRESS_MESSAGE";

    private static final String TASK_ID_COLUMN = "TASK_ID";
    private static final String SELECT_PROGRESS_MESSAGES = "SELECT DISTINCT TASK_ID FROM %s";
    private static final String UPDATE_PROGRESS_MESSAGES = "UPDATE %s SET TASK_ID=?, TASK_EXECUTION_ID=? WHERE TASK_ID=?";

    private static final String TASK_ID_GROUP = "taskId";
    private static final String TASK_INDEX_GROUP = "taskIndex";
    private static final String TASK_ID_WITH_APPENDED_INDEX_REGEX = String.format("(?<%s>[a-z_A-Z]+)(?<%s>\\d+)", TASK_ID_GROUP,
        TASK_INDEX_GROUP);

    private static final Pattern TASK_ID_WITH_APPENDED_INDEX_PATTERN = Pattern.compile(TASK_ID_WITH_APPENDED_INDEX_REGEX);

    static final String DEFAULT_TASK_EXECUTION_ID = "";

    private String tableName;

    public PopulateTaskExecutionIdColumnInProgressMessageTable() {
        this(TABLE_NAME);
    }

    public PopulateTaskExecutionIdColumnInProgressMessageTable(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String getConfirmationMessage() {
        return MessageFormat.format(Messages.POPULATED_TASK_EXECUTION_ID_COLUMN, tableName);
    }

    @Override
    public Map<String, SplitTaskId> transformData(List<String> retrievedData) {
        Map<String, SplitTaskId> transformedData = new HashMap<>();
        for (String retrievedTaskId : retrievedData) {
            SplitTaskId splitTaskId = splitTaskId(retrievedTaskId);
            transformedData.put(retrievedTaskId, splitTaskId);
        }
        return transformedData;
    }

    private SplitTaskId splitTaskId(String retrievedTaskId) {
        Matcher matcher = TASK_ID_WITH_APPENDED_INDEX_PATTERN.matcher(retrievedTaskId);
        if (!matcher.matches()) {
            return new SplitTaskId(retrievedTaskId, DEFAULT_TASK_EXECUTION_ID);
        }
        String taskId = matcher.group(TASK_ID_GROUP);
        String taskIndex = matcher.group(TASK_INDEX_GROUP);
        return new SplitTaskId(taskId, taskIndex);
    }

    @Override
    public List<String> extractData(ResultSet resultSet) throws SQLException {
        List<String> retrievedData = new LinkedList<>();
        while (resultSet.next()) {
            retrievedData.add(resultSet.getString(TASK_ID_COLUMN));
        }
        return retrievedData;
    }

    @Override
    public String getSelectStatement() {
        return String.format(SELECT_PROGRESS_MESSAGES, tableName);
    }

    @Override
    public String getUpdateStatement() {
        return String.format(UPDATE_PROGRESS_MESSAGES, tableName);
    }

    @Override
    public void setUpdateStatementParameters(PreparedStatement preparedStatement, Map<String, SplitTaskId> transformedData)
        throws SQLException {
        for (String retrievedTaskId : transformedData.keySet()) {
            setUpdateStatementParameters(preparedStatement, retrievedTaskId, transformedData.get(retrievedTaskId));
        }
    }

    private void setUpdateStatementParameters(PreparedStatement preparedStatement, String retrievedTaskId, SplitTaskId splitTaskId)
        throws SQLException {
        preparedStatement.setString(1, splitTaskId.taskId);
        preparedStatement.setString(2, splitTaskId.taskExecutionId);
        preparedStatement.setString(3, retrievedTaskId);
        preparedStatement.addBatch();
    }

    static class SplitTaskId {

        String taskId;
        String taskExecutionId;

        SplitTaskId(String taskId, String taskExecutionId) {
            this.taskId = taskId;
            this.taskExecutionId = taskExecutionId;
        }

    }

}
