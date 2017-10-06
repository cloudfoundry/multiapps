package com.sap.cloud.lm.sl.slp.activiti;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricActivityInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.JsonUtil;
import com.sap.cloud.lm.sl.common.util.TestUtil;
import com.sap.cloud.lm.sl.persistence.model.ProgressMessage;
import com.sap.cloud.lm.sl.slp.activiti.tasklist.TasksTest;
import com.sap.cloud.lm.sl.slp.model.StepMetadata;
import com.sap.lmsl.slp.SlpTaskState;
import com.sap.lmsl.slp.SlpTaskType;

@RunWith(Parameterized.class)
public class StepTest extends TasksTest {

    private TestInput input;

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // With children for the parent step and without any progress messages
            {
                "step-test-input-00.json"
            },
            // Without any historic activities
            {
                "step-test-input-01.json"
            },
            // Without any historic activities and progress messages 
            {
                "step-test-input-02.json"
            },
            // Without any historic activities and with progress messages and error
            {
                "step-test-input-03.json"
            },
            // With progress messages and with skipped tasks
            {
                "step-test-input-04.json"
            },
            // With progress messages and with initial tasks
            {
                "step-test-input-05.json"
            },
            // With progress messages, historic activities
            {
                "step-test-input-06.json"
            },
            {
                "step-test-input-07.json"
            }
// @formatter:on
        });
    }

    public StepTest(String inputLocation) throws ParsingException, IOException {
        this.input = JsonUtil.fromJson(TestUtil.getResourceAsString(inputLocation, StepTest.class), TestInput.class);
    }

    @Test
    public void testToActivitiTasks() {
        Step step = input.step.toRealObject();
        List<ActivitiTask> actualTasks = step.toActivitiTasks();
        validateTasks(input.expectedError, toActivitiTasks(input.expectedTasks), actualTasks);
    }

    private List<ActivitiTask> toActivitiTasks(List<SimpleActivitiTask> expectedTasks) {
        List<ActivitiTask> result = new ArrayList<>();
        for (SimpleActivitiTask simpleActivitiTask : expectedTasks) {
            result.add(simpleActivitiTask.toRealObject());
        }
        return result;
    }

    private static class TestInput {
        SimpleStep step;
        List<SimpleActivitiTask> expectedTasks;
        String expectedError;
    }

    private class SimpleStep implements SimpleObject<Step> {
        SimpleStepMetadata metadata;
        SimpleHistoricActiviti historicActiviti;
        List<SimpleProgressMessage> progressMessages;
        List<ProgressMessage> extensions;
        SimpleProcessState processState;
        List<SimpleStep> children;

        @Override
        public Step toRealObject() {
            return new Step(metadata.toRealObject(), getHistoricActiviti(), SimpleProgressMessage.getProgressMessages(progressMessages),
                extensions, getChildrenStep(), processState.toRealObject());
        }

        private HistoricActivityInstance getHistoricActiviti() {
            if (historicActiviti == null) {
                return null;
            }

            return historicActiviti.toRealObject();
        }

        private List<Step> getChildrenStep() {
            List<Step> childrenStep = new ArrayList<>();
            for (SimpleStep simpleStep : children) {
                childrenStep.add(simpleStep.toRealObject());
            }
            return childrenStep;
        }
    }

    private class SimpleStepMetadata implements SimpleObject<StepMetadata> {
        String id;
        String displayName;
        String description;
        SlpTaskType type;
        SlpTaskState targetState;

        @Override
        public StepMetadata toRealObject() {
            StepMetadata mockStepMetadata = Mockito.mock(StepMetadata.class);
            Mockito.when(mockStepMetadata.getId()).thenReturn(id);
            Mockito.when(mockStepMetadata.getDisplayName()).thenReturn(displayName);
            Mockito.when(mockStepMetadata.getDescription()).thenReturn(description);
            Mockito.when(mockStepMetadata.getTaskType()).thenReturn(type);
            Mockito.when(mockStepMetadata.getTargetState()).thenReturn(targetState);
            Mockito.when(mockStepMetadata.isVisible()).thenReturn(true);
            return mockStepMetadata;
        }

    }

    private class SimpleActivitiTask extends ActivitiTask implements SimpleObject<ActivitiTask> {

        long startTimeSimple;
        long endTimeSimple;
        SimpleStepMetadata simpleMetadata;
        List<SimpleProgressMessage> simpleProgressMessages;

        public SimpleActivitiTask(String indexedId, String indexedDisplayName, String indexedDescription, StepMetadata metadata,
            SlpTaskState status, Date startTime, Date endTime, double progress, List<ProgressMessage> progressMessages, ProcessError error,
            Map<String, String> taskExtensionElements, String parentId) {
            super(indexedId, indexedDisplayName, indexedDescription, metadata, status, startTime, endTime, progress, progressMessages,
                error, taskExtensionElements, parentId);
        }

        @Override
        public ActivitiTask toRealObject() {
            return new ActivitiTask(getIndexedId(), getIndexedDisplayName(), getIndexedDescription(), simpleMetadata.toRealObject(),
                getStatus(), new Date(startTimeSimple), new Date(endTimeSimple), getProgress(),
                SimpleProgressMessage.getProgressMessages(simpleProgressMessages), getError(), getTaskExtensions(), getParentId());

        }

    }
}
