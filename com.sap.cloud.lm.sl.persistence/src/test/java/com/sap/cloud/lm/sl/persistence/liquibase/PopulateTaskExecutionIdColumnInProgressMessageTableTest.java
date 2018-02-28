package com.sap.cloud.lm.sl.persistence.liquibase;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.sap.cloud.lm.sl.persistence.liquibase.PopulateTaskExecutionIdColumnInProgressMessageTable.SplitTaskId;

public class PopulateTaskExecutionIdColumnInProgressMessageTableTest {

    private static final List<String> TEST_TASK_IDS = new LinkedList<>();

    static {
        TEST_TASK_IDS.add("a5");
        TEST_TASK_IDS.add("createDatabaseService");
        TEST_TASK_IDS.add("updateApp$50");
        TEST_TASK_IDS.add("createApp150");
        TEST_TASK_IDS.add("create_app2200");
        TEST_TASK_IDS.add("update_app4400");
        TEST_TASK_IDS.add("createApp2");
        TEST_TASK_IDS.add("updateApp4");
        TEST_TASK_IDS.add("$");
        TEST_TASK_IDS.add("0");
        TEST_TASK_IDS.add("");
    }

    private static final Map<String, SplitTaskId> EXPECTED_RESULT = new HashMap<>();

    static {
        EXPECTED_RESULT.put("a5", new SplitTaskId("a", "5"));
        EXPECTED_RESULT.put("createDatabaseService",
            new SplitTaskId("createDatabaseService", PopulateTaskExecutionIdColumnInProgressMessageTable.DEFAULT_TASK_EXECUTION_ID));
        EXPECTED_RESULT.put("updateApp$50",
            new SplitTaskId("updateApp$50", PopulateTaskExecutionIdColumnInProgressMessageTable.DEFAULT_TASK_EXECUTION_ID));
        EXPECTED_RESULT.put("createApp150", new SplitTaskId("createApp", "150"));
        EXPECTED_RESULT.put("create_app2200", new SplitTaskId("create_app", "2200"));
        EXPECTED_RESULT.put("update_app4400", new SplitTaskId("update_app", "4400"));
        EXPECTED_RESULT.put("createApp2", new SplitTaskId("createApp", "2"));
        EXPECTED_RESULT.put("updateApp4", new SplitTaskId("updateApp", "4"));
        EXPECTED_RESULT.put("$", new SplitTaskId("$", PopulateTaskExecutionIdColumnInProgressMessageTable.DEFAULT_TASK_EXECUTION_ID));
        EXPECTED_RESULT.put("0", new SplitTaskId("0", PopulateTaskExecutionIdColumnInProgressMessageTable.DEFAULT_TASK_EXECUTION_ID));
        EXPECTED_RESULT.put("", new SplitTaskId("", PopulateTaskExecutionIdColumnInProgressMessageTable.DEFAULT_TASK_EXECUTION_ID));
    }

    @Test
    public void testTransformData() {
        Map<String, SplitTaskId> result = new PopulateTaskExecutionIdColumnInProgressMessageTable().transformData(TEST_TASK_IDS);
        assertEquals(EXPECTED_RESULT, result);
    }

    private void assertEquals(Map<String, SplitTaskId> expectedResult, Map<String, SplitTaskId> result) {
        for (String taskId : result.keySet()) {
            assertEquals(expectedResult.get(taskId), result.get(taskId));
        }
    }

    private void assertEquals(SplitTaskId expectedSplitTaskId, SplitTaskId splitTaskId) {
        Assert.assertEquals(expectedSplitTaskId.taskExecutionId, splitTaskId.taskExecutionId);
        Assert.assertEquals(expectedSplitTaskId.taskId, splitTaskId.taskId);
    }

}
