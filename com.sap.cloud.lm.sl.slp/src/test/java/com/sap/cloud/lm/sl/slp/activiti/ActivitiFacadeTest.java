package com.sap.cloud.lm.sl.slp.activiti;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ActivitiOptimisticLockingException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.sap.cloud.lm.sl.slp.Constants;
import com.sap.cloud.lm.sl.slp.TestUtils;

public class ActivitiFacadeTest {

    private final String serviceId = TestUtils.generateRandomString();
    private final String processInstanceId = TestUtils.generateRandomString();
    private final String activityId = TestUtils.generateRandomString();
    private final String spaceId = TestUtils.generateRandomString();
    private String userId = TestUtils.generateRandomString();

    private ActivitiFacade activitiFacade = new ActivitiFacade();

    private ProcessEngine engine = mock(ProcessEngine.class);

    @Before
    public void setUp() {
        activitiFacade.init(engine);
        stub(engine.getIdentityService()).toReturn(mock(IdentityService.class));
    }

    @Test
    public void testDeleteProcessInstance1() {
        RuntimeService mockRuntimeService = mockRuntimeService();
        stub(engine.getRuntimeService()).toReturn(mockRuntimeService);
        activitiFacade.deleteProcessInstance(userId, null, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeleteProcessInstance2() {
        ActivitiFacade activitiFacade = new ActivitiFacade() {
            @Override
            protected boolean isPastDeadline(long deadlline) {
                return true;
            }
        };
        activitiFacade.init(engine);

        RuntimeService mockRuntimeService = mockRuntimeService();
        stub(engine.getRuntimeService()).toReturn(mockRuntimeService);
        activitiFacade.deleteProcessInstance(userId, null, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testSignalTimeout() throws Exception {
        testSignal(0);
    }

    @Test
    public void testSignal() throws Exception {
        testSignal(5000);
    }

    private void testSignal(long timeoutInMillis) {
        String executionId = TestUtils.generateRandomString();
        String parentId = TestUtils.generateRandomString();

        RuntimeService mockRuntimeService = mockRuntimeService();
        stub(engine.getRuntimeService()).toReturn(mockRuntimeService);

        ExecutionQuery mockExecutionQuery = mock(ExecutionQuery.class);
        stub(mockRuntimeService.createExecutionQuery()).toReturn(mockExecutionQuery);
        stub(mockExecutionQuery.processInstanceId(processInstanceId)).toReturn(mockExecutionQuery);
        stub(mockExecutionQuery.activityId(activityId)).toReturn(mockExecutionQuery);
        Execution mockExecution = mock(Execution.class);
        stub(mockExecution.getId()).toReturn(executionId);
        stub(mockExecution.getParentId()).toReturn(parentId);
        when(mockExecutionQuery.singleResult()).thenReturn(null, mockExecution);

        Map<String, Object> variables = new HashMap<>();
        variables.put("key", "value");

        ActivitiFacade activitiFacade = new ActivitiFacade() {
            @Override
            protected long getExecutionRetryIntervalMs() {
                return 0;
            }
        };
        activitiFacade.init(engine);
        activitiFacade.signal(userId, processInstanceId, activityId, variables, timeoutInMillis);

        verify(mockRuntimeService).signal(executionId, variables);
    }

    private RuntimeService mockRuntimeService() {
        ActivitiOptimisticLockingException exception = new ActivitiOptimisticLockingException(null);
        RuntimeService runtimeService = mock(RuntimeService.class);
        doThrow(exception).doThrow(exception).doNothing().when(runtimeService).deleteProcessInstance(anyString(), anyString());

        return runtimeService;
    }

    @Test
    public void testGetHistoricProcessInstances() throws Exception {
        HistoryService historyService = mock(HistoryService.class);
        stub(engine.getHistoryService()).toReturn(historyService);

        HistoricProcessInstance historicInstance = mockHistoricProcessInstance(serviceId, processInstanceId);

        HistoricProcessInstanceQuery query = mock(HistoricProcessInstanceQuery.class);
        stub(historyService.createHistoricProcessInstanceQuery()).toReturn(query);

        stub(query.processDefinitionKey(spaceId)).toReturn(query);
        stub(query.variableValueEquals(Constants.VARIABLE_NAME_SPACE_ID, spaceId)).toReturn(query);
        stub(query.excludeSubprocesses(true)).toReturn(query);
        stub(query.processInstanceId(any(String.class))).toReturn(query);
        stub(query.singleResult()).toReturn(historicInstance);
        stub(query.list()).toReturn(Arrays.asList(historicInstance));

        HistoricProcessInstance actualHistoricInstance = activitiFacade.getHistoricProcessInstance(processInstanceId, spaceId);
        assertEquals(historicInstance.getId(), actualHistoricInstance.getId());
    }

    private HistoricProcessInstance mockHistoricProcessInstance(String serviceId, String processInstanceId) {
        HistoricProcessInstance historicInstance = Mockito.mock(HistoricProcessInstance.class);
        stub(historicInstance.getId()).toReturn(processInstanceId);
        stub(historicInstance.getProcessDefinitionId()).toReturn(serviceId);
        return historicInstance;
    }

}
