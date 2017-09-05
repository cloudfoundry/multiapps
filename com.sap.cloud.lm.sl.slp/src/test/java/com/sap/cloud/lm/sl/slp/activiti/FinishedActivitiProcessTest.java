package com.sap.cloud.lm.sl.slp.activiti;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricProcessInstance;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.sap.cloud.lm.sl.common.SLException;
import com.sap.cloud.lm.sl.persistence.model.ProgressMessage;
import com.sap.cloud.lm.sl.persistence.model.ProgressMessage.ProgressMessageType;
import com.sap.cloud.lm.sl.persistence.services.ProgressMessageService;
import com.sap.cloud.lm.sl.slp.Decorator;
import com.sap.cloud.lm.sl.slp.TestUtils;
import com.sap.cloud.lm.sl.slp.model.ServiceMetadata;
import com.sap.lmsl.slp.SlpProcessState;
import com.sap.lmsl.slp.SlpTaskState;

public class FinishedActivitiProcessTest {

    private ServiceMetadata serviceMetadata = Mockito.mock(ServiceMetadata.class);
    private HistoricProcessInstance processInstance = mock(HistoricProcessInstance.class);
    
    @Mock
    private ProgressMessageService progressMessageService;

    @InjectMocks
    private ActivitiProcess process = new FinishedActivitiProcess(serviceMetadata, processInstance);;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testTaskStatus() {
        stub(processInstance.getEndActivityId()).toReturn(TestUtils.generateRandomString());
        ActivitiProcess process = new FinishedActivitiProcess(serviceMetadata, processInstance);
        assertEquals(SlpTaskState.SLP_TASK_STATE_FINISHED, process.getCurrentState());

        stub(processInstance.getEndActivityId()).toReturn(null);
        assertEquals(SlpTaskState.SLP_TASK_STATE_ABORTED, process.getCurrentState());

        stub(processInstance.getDeleteReason()).toReturn(SlpTaskState.SLP_TASK_STATE_ERROR.value());
        assertEquals(SlpTaskState.SLP_TASK_STATE_ERROR, process.getCurrentState());

        stub(processInstance.getDeleteReason()).toReturn(SlpTaskState.SLP_TASK_STATE_BREAKPOINT.value());
        assertEquals(SlpTaskState.SLP_TASK_STATE_ABORTED, process.getCurrentState());

        stub(processInstance.getDeleteReason()).toReturn("InvalidTaskStatus");
        assertEquals(SlpTaskState.SLP_TASK_STATE_ABORTED, process.getCurrentState());
    }

    @Test
    public void testNoActions() {
        assertEquals(0, process.getActionIds().size());
    }

    @Test
    public void testStatus() {
        stub(processInstance.getEndTime()).toReturn(new Date());
        assertEquals(SlpProcessState.SLP_PROCESS_STATE_FINISHED, process.getStatus());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testExecuteAction() {
        process.executeAction("dido", null);
    }

    @Test
    public void testErrorMessage() throws SLException {
        final String errorMessage = "Error message";
        when(processInstance.getId()).thenReturn(TestUtils.generateRandomString());
        when(processInstance.getStartActivityId()).thenReturn(TestUtils.generateRandomString());
        when(progressMessageService.findByProcessId(processInstance.getId())).thenReturn(
            Arrays.asList(new ProgressMessage("", "", ProgressMessageType.ERROR, errorMessage, new Timestamp(System.currentTimeMillis()))));
        when(serviceMetadata.getStepIds()).thenReturn(Arrays.asList(""));
        ProcessError processError = process.getProcessError();
        assertNotNull(processError);
        assertEquals(errorMessage, processError.getMessage());
    }

    @Test
    public void testProcessInstanceId() {
        String processInstanceId = TestUtils.generateRandomString();
        stub(processInstance.getId()).toReturn(processInstanceId);
        assertEquals(processInstanceId, process.getProcessInstanceId());
    }

    @Test
    public void testDelete() {
        final HistoryService historyMock = mock(HistoryService.class);

        Decorator<ProcessEngine> decorator = new Decorator<ProcessEngine>() {
            @Override
            public void decorate(ProcessEngine engine) {
                stub(engine.getHistoryService()).toReturn(historyMock);
            }
        };
        TestUtils.initializeFacade(decorator);

        process.delete("myuser");

        verify(historyMock).deleteHistoricProcessInstance(process.getProcessInstanceId());
    }

}
