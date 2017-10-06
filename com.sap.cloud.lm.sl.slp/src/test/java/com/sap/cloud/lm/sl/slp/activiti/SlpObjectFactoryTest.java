package com.sap.cloud.lm.sl.slp.activiti;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Element;

import com.sap.cloud.lm.sl.slp.TestUtils;
import com.sap.cloud.lm.sl.slp.activiti.ProcessError.ErrorType;
import com.sap.lmsl.slp.Metadata;
import com.sap.lmsl.slp.ProgressMessage;
import com.sap.lmsl.slp.SlpTaskState;
import com.sap.lmsl.slp.SlpTaskType;
import com.sap.lmsl.slp.Task;

public class SlpObjectFactoryTest {

    @Test
    public void testCreateTask() {
        Date startTime = TestUtils.generateRandomDate();
        Date endTime = TestUtils.generateRandomDate();
        ProcessError error = new ProcessError(ErrorType.PROCESS_EXECUTION_ERROR, TestUtils.generateRandomString());

        List<String> progressMessages = Arrays.asList("Starting", "In process");

        String taskId = "task";
        String displayName = "Task";
        String description = "Task";
        String parentId = "parentId";
        SlpTaskType taskType = SlpTaskType.SLP_TASK_TYPE_STEP;
        SlpTaskState taskState = SlpTaskState.SLP_TASK_STATE_RUNNING;
        int progress = 77;

        Task task = SlpObjectFactory.createTask(taskId, displayName, description, parentId, taskType, taskState, progress, startTime,
            endTime, error, progressMessages, Collections.singletonMap("extensionElementName", "extensionElementTextValue"));

        assertEquals(taskId, task.getId());
        assertEquals(displayName, task.getDisplayName());
        assertEquals(description, task.getDescription());
        assertEquals(parentId, task.getParent());
        assertEquals(taskType, task.getType());
        assertEquals(taskState, task.getStatus());

        assertEquals(progress, (int) task.getProgress());
        assertEquals(SlpObjectFactory.dateAsString(startTime), task.getStartedAt());
        assertEquals(SlpObjectFactory.dateAsString(endTime), task.getFinishedAt());
        assertEquals(SlpObjectFactory.getErrorURI(), task.getError());
        List<ProgressMessage> slpProgressMessages = task.getProgressMessages().getProgressMessage();
        assertEquals(progressMessages.size(), slpProgressMessages.size());
        for (int i = 0; i < progressMessages.size(); ++i) {
            assertEquals(progressMessages.get(i), slpProgressMessages.get(i).getMessage());
        }
        assertEquals(1, task.getAny().size());
        Element extensionElement = task.getAny().get(0);
        assertEquals("extensionElementName", extensionElement.getNodeName());
        assertEquals("extensionElementTextValue", extensionElement.getChildNodes().item(0).getTextContent());
    }

    @Test
    public void testCreateSlppMetadata() {
        Metadata slppMetadata = SlpObjectFactory.createSlppMetadata();
        assertEquals(SlpObjectFactory.SLPP_VERSION, slppMetadata.getSlppversion());
        assertNull(slppMetadata.getSlmpversion());
    }

    @Test
    public void testCreateSlmpMetadata() {
        Metadata slmpMetaData = SlpObjectFactory.createSlmpMetadata();
        assertNull(slmpMetaData.getSlppversion());
        assertEquals(SlpObjectFactory.SLMP_VERSION, slmpMetaData.getSlmpversion());
    }

}
