package com.sap.cloud.lm.sl.slp.activiti.tasklist.factory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.JsonUtil;
import com.sap.cloud.lm.sl.common.util.TestUtil;
import com.sap.cloud.lm.sl.slp.activiti.ActivitiTask;
import com.sap.cloud.lm.sl.slp.activiti.Step;
import com.sap.cloud.lm.sl.slp.activiti.tasklist.TasksTest;

@RunWith(Parameterized.class)
public class JavaTaskFactoryTest extends TasksTest {

    protected TestInput testInput;
    private JavaTaskFactory factory;

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) With no error in the activiti tasks and matching current activiti id 
            {
                "java-task-factory-test-input-0.json"
            },
            // (1) With error in the progress messages
            {
                "java-task-factory-test-input-1.json"
            },
            // (2) With no error in the progress messages and non-matching activiti id
            {
                "java-task-factory-test-input-2.json"
            },
// @formatter:on
        });
    }

    public JavaTaskFactoryTest(String inputLocation) throws ParsingException, IOException {
        this.testInput = JsonUtil.fromJson(TestUtil.getResourceAsString(inputLocation, JavaTaskFactoryTest.class), TestInput.class);
    }

    @Before
    public void prepareFacotry() {
        factory = new JavaTaskFactory(testInput.processState.toRealObject(), testInput.parentId);
    }

    @Test
    public void testCreateTask() {
        List<ActivitiTask> actualTasks = factory.createTask(testInput.step);
        validateTasks(testInput.expectedError, testInput.expectedTasks, actualTasks);
    }

    protected static class TestInput {
        String parentId;
        SimpleProcessState processState;
        Step step;
        List<ActivitiTask> expectedTasks;
        String expectedError;

    }
}
