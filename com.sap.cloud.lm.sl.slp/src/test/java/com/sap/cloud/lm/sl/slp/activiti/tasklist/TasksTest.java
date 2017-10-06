package com.sap.cloud.lm.sl.slp.activiti.tasklist;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.activiti.engine.history.HistoricActivityInstance;
import org.mockito.Mockito;

import com.sap.cloud.lm.sl.common.util.CommonUtil;
import com.sap.cloud.lm.sl.persistence.model.ProgressMessage;
import com.sap.cloud.lm.sl.slp.activiti.ActivitiTask;
import com.sap.lmsl.slp.SlpTaskState;

public class TasksTest {

    protected static class SimpleProcessState implements SimpleObject<ProcessState> {
        String taskId;
        SlpTaskState state;
        boolean currentReached;
        List<SimpleHistoricActiviti> historicActivities;
        List<SimpleProgressMessage> progressMessages;

        @Override
        public ProcessState toRealObject() {
            ProcessState processState = Mockito.mock(ProcessState.class);
            List<HistoricActivityInstance> realHistoricActivities = toHistoricActivities();
            List<ProgressMessage> realProgressMessages = SimpleProgressMessage.getProgressMessages(progressMessages);
            Mockito.when(processState.getHistoricInstances()).thenReturn(realHistoricActivities);
            Mockito.when(processState.getProgressMessages()).thenReturn(realProgressMessages);
            Mockito.when(processState.getTaskExtensions()).thenReturn(Collections.<ProgressMessage> emptyList());
            Mockito.when(processState.getTaskIndexProvider()).thenReturn(new TaskIndexProvider());
            Mockito.when(processState.getCurrentTaskState()).thenReturn(state);
            Mockito.when(processState.getCurrentTaskId()).thenReturn(taskId);
            Mockito.when(processState.isCurrentReached()).thenReturn(currentReached);
            return processState;
        }

        private List<HistoricActivityInstance> toHistoricActivities() {
            List<HistoricActivityInstance> historicActivityInstances = new ArrayList<>();
            List<SimpleHistoricActiviti> simpleHistoricActivities = CommonUtil.getOrDefault(historicActivities,
                Collections.<SimpleHistoricActiviti> emptyList());
            for (SimpleHistoricActiviti simpleHistoricActiviti : simpleHistoricActivities) {
                historicActivityInstances.add(simpleHistoricActiviti.toRealObject());
            }
            return historicActivityInstances;
        }
    }

    protected static class SimpleProgressMessage extends ProgressMessage implements SimpleObject<ProgressMessage> {

        long simpleTimestamp;

        @Override
        public ProgressMessage toRealObject() {
            this.setTimestamp(new Date(simpleTimestamp));
            return this;
        }

        public static List<ProgressMessage> getProgressMessages(List<SimpleProgressMessage> simpleProgressMessages) {
            List<ProgressMessage> realProgressMessages = new ArrayList<>();
            List<SimpleProgressMessage> progressMessagesToConvert = CommonUtil.getOrDefault(simpleProgressMessages,
                Collections.<SimpleProgressMessage> emptyList());
            for (SimpleProgressMessage simpleProgressMessage : progressMessagesToConvert) {
                realProgressMessages.add(simpleProgressMessage.toRealObject());
            }
            return realProgressMessages;
        }

    }

    protected class SimpleHistoricActiviti implements SimpleObject<HistoricActivityInstance> {
        String id;
        String type;
        long startTime;
        long endTime;

        @Override
        public HistoricActivityInstance toRealObject() {
            HistoricActivityInstance mockHistoricActivity = Mockito.mock(HistoricActivityInstance.class);
            Mockito.when(mockHistoricActivity.getId()).thenReturn(id);
            Mockito.when(mockHistoricActivity.getActivityId()).thenReturn(id);
            Mockito.when(mockHistoricActivity.getActivityType()).thenReturn(type);
            Mockito.when(mockHistoricActivity.getStartTime()).thenReturn(new Date(startTime));
            Mockito.when(mockHistoricActivity.getEndTime()).thenReturn(new Date(endTime));
            return mockHistoricActivity;
        }
    }

    protected interface SimpleObject<T> {
        public T toRealObject();
    }

    protected void validateTasks(String expectedError, List<ActivitiTask> expectedTasks, List<ActivitiTask> builtTasks) {
        assertEquals(expectedTasks.size(), builtTasks.size());
        for (int i = 0; i < expectedTasks.size(); i++) {
            ActivitiTask expectedTask = expectedTasks.get(i);
            ActivitiTask resultTask = builtTasks.get(i);
            validateTaskIds(expectedTask, resultTask);
            validateStartEndTime(expectedTask, resultTask);
            validateState(expectedTask, resultTask);
            validateError(expectedError, resultTask);
            validateTaskExtensions(expectedTask, resultTask);
            validateProgressMessages(expectedTask, resultTask);
            assertEquals(expectedTask.getIndexedId(), resultTask.getIndexedId());
            assertEquals(expectedTask.getIndexedDisplayName(), resultTask.getIndexedDisplayName());
            assertEquals(expectedTask.getIndexedDescription(), resultTask.getIndexedDescription());
            assertEquals(expectedTask.getStepMetadata().getTaskType(), resultTask.getStepMetadata().getTaskType());
            assertEquals(expectedTask.getParentId(), resultTask.getParentId());
        }
    }

    private void validateProgressMessages(ActivitiTask expectedTask, ActivitiTask resultTask) {
        List<ProgressMessage> resultProgressMessages = resultTask.getProgressMessages();
        List<ProgressMessage> expectedProgressMessages = expectedTask.getProgressMessages();
        assertEquals(expectedProgressMessages.size(), resultProgressMessages.size());
        for (int i = 0; i < expectedProgressMessages.size(); i++) {
            ProgressMessage expected = expectedProgressMessages.get(i);
            ProgressMessage actual = resultProgressMessages.get(i);
            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getProcessId(), actual.getProcessId());
            assertEquals(expected.getTaskId(), actual.getTaskId());
            assertEquals(expected.getText(), actual.getText());
            assertEquals(expected.getTimestamp(), actual.getTimestamp());
            assertEquals(expected.getType(), actual.getType());
        }
    }

    private void validateTaskExtensions(ActivitiTask expectedTask, ActivitiTask actualTask) {
        assertEquals(expectedTask.getTaskExtensions(), actualTask.getTaskExtensions());
    }

    private void validateError(String expectedError, ActivitiTask actualTask) {
        if (expectedError != null && actualTask.getError() != null) {
            assertEquals(expectedError, actualTask.getError().getMessage());
        }
    }

    private void validateState(ActivitiTask expectedTask, ActivitiTask actualTask) {
        assertEquals(expectedTask.getStatus(), actualTask.getStatus());
    }

    private void validateStartEndTime(ActivitiTask expectedTask, ActivitiTask actualTask) {
        assertEquals(getTime(expectedTask.getStartTime()), getTime(actualTask.getStartTime()));
        assertEquals(getTime(expectedTask.getEndTime()), getTime(actualTask.getEndTime()));
    }

    private long getTime(Date date) {
        if (date == null) {
            return 0;
        }

        return date.getTime();
    }

    private void validateTaskIds(ActivitiTask expectedTask, ActivitiTask actualTask) {
        assertEquals(expectedTask.getIndexedId(), actualTask.getIndexedId());
    }
}
