package com.sap.cloud.lm.sl.persistence.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sap.cloud.lm.sl.common.SLException;
import com.sap.cloud.lm.sl.common.util.TestDataSourceProvider;
import com.sap.cloud.lm.sl.persistence.model.ProgressMessage;
import com.sap.cloud.lm.sl.persistence.model.ProgressMessage.ProgressMessageType;
import com.sap.cloud.lm.sl.persistence.util.JdbcUtil;

public class ProgressMessageServiceTest {

    private static final String LIQUIBASE_CHANGELOG_LOCATION = "com/sap/cloud/lm/sl/persistence/db/changelog/db-changelog.xml";
    private static final String taskId1 = "test-taskId-1";
    private static final String taskId2 = "test-taskId-2";
    private static final String messageText1 = "test-message-1";
    private static final String messageText2 = "test-message-2";
    private static final String processInstanceId1 = "test-processInstanceId1";
    private static final String processInstanceId2 = "test-processInstanceId2";

    private ProgressMessageService service;

    private DataSource testDataSource;
    private ProgressMessage progressMessage1;
    private ProgressMessage progressMessage2;
    private ProgressMessage progressMessage3;
    private ProgressMessage progressMessage4;

    @Before
    public void setUp() throws Exception {
        setUpConnection();
        initializeData();
    }

    private void setUpConnection() throws Exception {
        service = new ProgressMessageService("PROGRESS_MESSAGE");
        testDataSource = TestDataSourceProvider.getDataSource(LIQUIBASE_CHANGELOG_LOCATION);
        service.init(testDataSource);
    }

    private void initializeData() throws SLException {
        progressMessage1 = new ProgressMessage(processInstanceId1, taskId1, ProgressMessageType.ERROR, messageText1,
            new Timestamp(System.currentTimeMillis()));
        progressMessage2 = new ProgressMessage(processInstanceId1, taskId2, ProgressMessageType.INFO, messageText2,
            new Timestamp(System.currentTimeMillis()));
        progressMessage3 = new ProgressMessage(processInstanceId2, taskId1, ProgressMessageType.INFO, messageText1,
            new Timestamp(System.currentTimeMillis()));
        progressMessage4 = new ProgressMessage(processInstanceId2, taskId2, ProgressMessageType.INFO, messageText2,
            new Timestamp(System.currentTimeMillis()));

        List<ProgressMessage> messages = Arrays.asList(progressMessage1, progressMessage2, progressMessage3, progressMessage4);
        for (ProgressMessage message : messages) {
            service.add(message);
        }
    }

    @After
    public void tearDown() throws Exception {
        service.removeByProcessId(processInstanceId1);
        service.removeByProcessId(processInstanceId2);
        service.removeByProcessId("test-processId");
        JdbcUtil.closeQuietly(testDataSource.getConnection());
    }

    @Test
    public void testInsert() throws SLException {
        ProgressMessage progressMessage = new ProgressMessage("test-processId", "test-activitId", ProgressMessageType.ERROR,
            "test-error-message", new Timestamp(System.currentTimeMillis()));
        boolean insertSuccess = service.add(progressMessage);
        assertTrue(insertSuccess);

        List<ProgressMessage> allMessages = service.findAll();
        assertEquals(5, allMessages.size());
        assertSameProgressMessage(progressMessage, allMessages.get(allMessages.size() - 1));
    }

    @Test
    public void testFindByProcessId() throws SLException {
        List<ProgressMessage> messages = service.findByProcessId(processInstanceId1);
        assertEquals(2, messages.size());
    }

    @Test
    public void testDeleteByProcessId() throws SLException {
        int deletedMessages = service.removeByProcessId(processInstanceId1);
        assertEquals(2, deletedMessages);

        List<ProgressMessage> messages = service.findByProcessId(processInstanceId1);
        assertEquals(0, messages.size());

        List<ProgressMessage> allMessages = service.findAll();
        assertEquals(2, allMessages.size());
        assertSameProgressMessage(progressMessage3, allMessages.get(0));
        assertSameProgressMessage(progressMessage4, allMessages.get(1));
    }

    @Test
    public void testDeleteByProcessIdAndTaskId() throws SLException {
        int deletedMessagesEven = service.removeByProcessIdAndTaskId(processInstanceId1, taskId1);
        assertEquals(1, deletedMessagesEven);
        assertTrue(service.findByProcessIdAndTaskId(processInstanceId1, taskId1).isEmpty());
    }

    @Test
    public void testFindAll() throws SLException {
        List<ProgressMessage> messages = service.findAll();
        assertEquals(4, messages.size());
    }

    @Test
    public void testFindByProcessIdAndTaskId() throws SLException {
        List<ProgressMessage> messagesEven = service.findByProcessIdAndTaskId(processInstanceId1, taskId1);
        assertEquals(1, messagesEven.size());
        assertSameProgressMessage(progressMessage1, messagesEven.get(0));
    }

    @Test
    public void testFindByProcessIdTaskIdAndType() throws SLException {
        List<ProgressMessage> messagesEmpty = service.findByProcessIdTaskIdAndType(processInstanceId1, taskId1, ProgressMessageType.INFO);
        assertTrue(messagesEmpty.isEmpty());

        List<ProgressMessage> messagesError = service.findByProcessIdTaskIdAndType(processInstanceId1, taskId1, ProgressMessageType.ERROR);
        assertEquals(1, messagesError.size());
        assertSameProgressMessage(progressMessage1, messagesError.get(0));
    }

    @Test
    public void testRemoveAllByProcessIds() throws SLException {
        int deletedMessages = service.removeAllByProcessIds(Arrays.asList("nonexisting"));
        assertEquals(0, deletedMessages);

        deletedMessages = service.removeAllByProcessIds(Arrays.asList(processInstanceId1));
        assertEquals(2, deletedMessages);

        List<ProgressMessage> allMessages = service.findAll();
        assertEquals(2, allMessages.size());
        assertSameProgressMessage(progressMessage3, allMessages.get(0));
        assertSameProgressMessage(progressMessage4, allMessages.get(1));

        deletedMessages = service.removeAllByProcessIds(Arrays.asList(processInstanceId2));
        assertEquals(2, deletedMessages);
        allMessages = service.findAll();
        assertEquals(0, allMessages.size());
    }

    private static void assertSameProgressMessage(ProgressMessage expected, ProgressMessage actual) {
        assertNotNull(actual.getId());
        assertEquals(expected.getProcessId(), actual.getProcessId());
        assertEquals(expected.getTaskId(), actual.getTaskId());
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getText(), actual.getText());
    }
}
