package com.sap.cloud.lm.sl.slp.activiti;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sap.cloud.lm.sl.common.SLException;
import com.sap.cloud.lm.sl.persistence.model.ProgressMessage;
import com.sap.cloud.lm.sl.persistence.model.ProgressMessage.ProgressMessageType;
import com.sap.cloud.lm.sl.persistence.services.ProgressMessageService;
import com.sap.cloud.lm.sl.slp.Decorator;
import com.sap.cloud.lm.sl.slp.HistoryServiceDecorator;
import com.sap.cloud.lm.sl.slp.ManagementServiceDecorator;
import com.sap.cloud.lm.sl.slp.TestUtils;
import com.sap.cloud.lm.sl.slp.model.RoadmapStepMetadata;
import com.sap.cloud.lm.sl.slp.model.ServiceMetadata;
import com.sap.cloud.lm.sl.slp.model.StepMetadata;
import com.sap.cloud.lm.sl.slp.model.VariableHandler;
import com.sap.cloud.lm.sl.slp.services.TaskExtensionService;
import com.sap.lmsl.slp.SlpTaskState;
import com.sap.lmsl.slp.Task;

public class ActiveActivitiProcessTest {

    private static final String ERROR_MESSAGE = "Situation normal, A F U";
    private final String processInstanceId = TestUtils.generateRandomString();
    private String jobId = TestUtils.generateRandomString();

    @Mock
    private ProgressMessageService progressMessageService;

    @Mock
    private TaskExtensionService taskExtensionService;

    private ServiceMetadata serviceMetadata = mock(ServiceMetadata.class);

    private ProcessInstance processInstance = mock(ProcessInstance.class);

    @InjectMocks
    private ActiveActivitiProcess process = new ActiveActivitiProcess(serviceMetadata, processInstance);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetTaskTypeReceiveTask() throws SLException {
        prepareProcess(ActivitiProcess.ACTIVITY_TASK_TYPE_RECEIVE, null);
        SlpTaskState taskStatus = process.getCurrentState();
        assertEquals(SlpTaskState.SLP_TASK_STATE_ACTION_REQUIRED, taskStatus);
    }

    @Test
    public void testGetInitialTaskTypeReceiveTask() throws SLException {
        prepareProcess(ActivitiProcess.ACTIVITY_TASK_TYPE_RECEIVE, null, SlpTaskState.SLP_TASK_STATE_INITIAL);
        SlpTaskState taskStatus = process.getCurrentState();
        assertEquals(SlpTaskState.SLP_TASK_STATE_INITIAL, taskStatus);
    }

    @Test
    public void testGetActionsReceiveTask() throws SLException {
        prepareProcess(ActivitiProcess.ACTIVITY_TASK_TYPE_RECEIVE, null);
        List<String> actionList = process.getActionIds();
        assertEquals(2, actionList.size());
        assertContains(actionList, ActivitiProcess.ACTION_ID_RESUME);
        assertContains(actionList, ActivitiProcess.ACTION_ID_ABORT);
    }

    @Test
    public void testGetTaskTypeWithFailedJob() throws SLException {
        prepareProcess(ActivitiProcess.ACTIVITY_TASK_TYPE_SERVICE, ERROR_MESSAGE);
        SlpTaskState taskStatus = process.getCurrentState();
        assertEquals(SlpTaskState.SLP_TASK_STATE_ERROR, taskStatus);
    }

    @Test
    public void testGetActionsFailedJob() throws SLException {
        prepareProcess(ActivitiProcess.ACTIVITY_TASK_TYPE_SERVICE, ERROR_MESSAGE);
        List<String> actionList = process.getActionIds();
        assertEquals(2, actionList.size());
        assertContains(actionList, ActivitiProcess.ACTION_ID_RETRY);
        assertContains(actionList, ActivitiProcess.ACTION_ID_ABORT);
    }

    @Test
    public void testGetErrorMessageFailedJob() throws SLException {
        stub(progressMessageService.findByProcessIdAndType(processInstanceId, ProgressMessageType.ERROR)).toReturn(
            Arrays.asList(new ProgressMessage(processInstanceId, "", ProgressMessageType.ERROR, ERROR_MESSAGE,
                new Timestamp(System.currentTimeMillis()))));
        prepareProcess(ActivitiProcess.ACTIVITY_TASK_TYPE_SERVICE, ERROR_MESSAGE);
        ProcessError processError = process.getProcessError();
        assertEquals(ERROR_MESSAGE, processError.getMessage());
    }

    @Test
    public void testGetTaskTypeRunningJob() throws SLException {
        prepareProcess(ActivitiProcess.ACTIVITY_TASK_TYPE_SERVICE, null);
        SlpTaskState taskStatus = process.getCurrentState();
        assertEquals(SlpTaskState.SLP_TASK_STATE_RUNNING, taskStatus);
    }

    @Test
    public void testGetActionsRunningJob() throws SLException {
        prepareProcess(ActivitiProcess.ACTIVITY_TASK_TYPE_SERVICE, null);
        List<String> actionList = process.getActionIds();
        assertEquals(1, actionList.size());
        assertContains(actionList, ActivitiProcess.ACTION_ID_ABORT);
    }

    @Test
    public void testGetErrorMessageRunningJob() throws SLException {
        stub(progressMessageService.findByProcessIdAndType(processInstanceId, ProgressMessageType.ERROR)).toReturn(
            new ArrayList<ProgressMessage>());
        prepareProcess(ActivitiProcess.ACTIVITY_TASK_TYPE_SERVICE, null);
        ProcessError error = process.getProcessError();
        assertNull(error);
    }

    @Test
    public void testGetTaskListReceiveTask() throws SLException {
        stub(progressMessageService.findByProcessId(processInstanceId)).toReturn(new ArrayList<ProgressMessage>());
        prepareProcess(ActivitiProcess.ACTIVITY_TASK_TYPE_RECEIVE, null);
        List<Task> tasklist = process.getTasklist().getTask();

        assertEquals(3, tasklist.size());
        for (Task task : tasklist) {
            assertEquals(SlpTaskState.SLP_TASK_STATE_ACTION_REQUIRED, task.getStatus());
        }
    }

    @Test
    public void testDeleteProcess() {
        final RuntimeService runtimeMock = mock(RuntimeService.class);
        final HistoryService historyMock = mock(HistoryService.class);

        Decorator<ProcessEngine> decorator = new Decorator<ProcessEngine>() {
            @Override
            public void decorate(ProcessEngine engine) {
                stub(engine.getRuntimeService()).toReturn(runtimeMock);
                stub(engine.getHistoryService()).toReturn(historyMock);
            }
        };

        String errorMessage = "Error message";
        Job job = mock(Job.class);
        when(job.getExceptionMessage()).thenReturn(errorMessage);

        ManagementServiceDecorator managementServiceDecorator = new ManagementServiceDecorator(processInstanceId);
        managementServiceDecorator.setCurrentJob(job);

        TestUtils.initializeFacade(decorator, managementServiceDecorator);
        stub(processInstance.getId()).toReturn(processInstanceId);

        process.delete("myuser");

        verify(runtimeMock).deleteProcessInstance(processInstanceId, SlpTaskState.SLP_TASK_STATE_ABORTED.value());
        verify(historyMock).deleteHistoricProcessInstance(processInstanceId);
    }

    private String assertContains(List<String> actionList, String action) {
        String errorTemplate = "Expected %s, actual eactions are %s";
        assertTrue(String.format(errorTemplate, action, actionList), actionList.contains(action));
        return errorTemplate;
    }

    private void prepareProcess(String taskTypeOfLastTask, String exceptionMessage) throws SLException {
        prepareProcess(taskTypeOfLastTask, exceptionMessage, null);
    }

    private void prepareProcess(String taskTypeOfLastTask, String exceptionMessage, SlpTaskState receiveTaskTargetState)
        throws SLException {
        List<HistoricActivityInstance> historicInstances = new ArrayList<HistoricActivityInstance>();
        HistoricActivityInstance instance1 = mock(HistoricActivityInstance.class);
        stub(instance1.getStartTime()).toReturn(new Date(System.currentTimeMillis() - 600));
        stub(instance1.getEndTime()).toReturn(new Date(System.currentTimeMillis() - 500));

        HistoricActivityInstance receiveTaskInstance = mock(HistoricActivityInstance.class);
        stub(receiveTaskInstance.getId()).toReturn("test-id");
        stub(receiveTaskInstance.getActivityType()).toReturn(taskTypeOfLastTask);
        String receiveTaskActivitiId = TestUtils.generateRandomString();
        stub(receiveTaskInstance.getActivityId()).toReturn(receiveTaskActivitiId);
        stub(receiveTaskInstance.getStartTime()).toReturn(new Date(System.currentTimeMillis() - 300));

        historicInstances.add(instance1);
        historicInstances.add(receiveTaskInstance);

        List<ProgressMessage> asProgressMessage = getAsProgressMessage(exceptionMessage, new Date(System.currentTimeMillis() - 200),
            receiveTaskActivitiId);
        when(progressMessageService.findByProcessId(processInstanceId)).thenReturn(asProgressMessage);

        Job job = mock(Job.class);
        stub(job.getId()).toReturn(jobId);
        TestUtils.initializeFacade(new HistoryServiceDecorator(processInstanceId).setHistoricInstances(historicInstances),
            new ManagementServiceDecorator(processInstanceId).setCurrentJob(job));

        RoadmapStepMetadata roadmapStepMetadata = mock(RoadmapStepMetadata.class);
        StepMetadata receiveTaskStep = mock(StepMetadata.class);
        when(receiveTaskStep.getId()).thenReturn(receiveTaskActivitiId);
        when(receiveTaskStep.getTargetState()).thenReturn(receiveTaskTargetState);
        when(roadmapStepMetadata.getChildren(any(VariableHandler.class))).thenReturn(Collections.singletonList(receiveTaskStep));
        when(roadmapStepMetadata.getId()).thenReturn("executeRoadmapStep");

        stub(serviceMetadata.getStepIds()).toReturn(Collections.singletonList(receiveTaskActivitiId));
        when(serviceMetadata.getId()).thenReturn("serviceId");
        when(serviceMetadata.getImplementationId()).thenReturn("serviceId");
        when(receiveTaskStep.isVisible()).thenReturn(true);
        when(roadmapStepMetadata.isVisible()).thenReturn(true);
        when(serviceMetadata.isVisible()).thenReturn(true);
        // when(serviceMetadata.getStepIds()).thenReturn(Arrays.asList(receiveTaskActivitiId));

        List<StepMetadata> steps = new ArrayList<StepMetadata>();
        steps.add(roadmapStepMetadata);
        when(serviceMetadata.getChildren(any(VariableHandler.class))).thenReturn(steps);

        stub(processInstance.getId()).toReturn(processInstanceId);
        stub(processInstance.getActivityId()).toReturn(receiveTaskActivitiId);
    }

    private List<ProgressMessage> getAsProgressMessage(String exceptionMessage, Date date, String taskId) {
        if (exceptionMessage == null) {
            return Collections.emptyList();
        }
        ProgressMessage message = new ProgressMessage();
        message.setText(exceptionMessage);
        message.setTaskId(taskId);
        message.setTimestamp(date);
        message.setType(ProgressMessageType.ERROR);
        return Arrays.asList(message);
    }

}
