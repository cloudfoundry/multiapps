package com.sap.cloud.lm.sl.slp.activiti;

import static com.sap.cloud.lm.sl.slp.TestUtils.mockStepMetadata;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.history.HistoricVariableInstanceQuery;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.sap.cloud.lm.sl.persistence.services.ProgressMessageService;
import com.sap.cloud.lm.sl.slp.TestUtils;
import com.sap.cloud.lm.sl.slp.activiti.ProcessError.ErrorType;
import com.sap.cloud.lm.sl.slp.model.ParameterMetadata;
import com.sap.cloud.lm.sl.slp.model.ServiceMetadata;
import com.sap.cloud.lm.sl.slp.model.StepMetadata;
import com.sap.cloud.lm.sl.slp.model.VariableHandler;
import com.sap.cloud.lm.sl.slp.services.TaskExtensionService;
import com.sap.lmsl.slp.Action;
import com.sap.lmsl.slp.Actions;
import com.sap.lmsl.slp.Error;
import com.sap.lmsl.slp.Parameter;
import com.sap.lmsl.slp.Process;
import com.sap.lmsl.slp.Processes;
import com.sap.lmsl.slp.SlpActionEnum;
import com.sap.lmsl.slp.SlpProcessState;
import com.sap.lmsl.slp.SlpTaskState;
import com.sap.lmsl.slp.Tasklist;

public class ActivitiProcessTest {

    private static final String ACTION_ID_RETRY = "retry";

    private static final String ACTION_ID_ABORT = "abort";

    private class ActivitiProcessImpl extends ActivitiProcess {

        private List<HistoricActivityInstance> historicActivities = Collections.emptyList();

        protected ActivitiProcessImpl(ServiceMetadata serviceMetadata) {
            super(serviceMetadata);
        }

        @Override
        public SlpTaskState getCurrentState() {
            return processTaskStatus;
        }

        @Override
        public void executeAction(String userId, String actionId) {
        }

        @Override
        public String getCurrentActivityId() {
            return currentActivitiId;
        }

        @Override
        protected String getProcessInstanceId() {
            return processInstanceId;
        }

        @Override
        public SlpProcessState getStatus() {
            return processStatus;
        }

        @Override
        public List<String> getActionIds() {
            return actionIds;
        }

        @Override
        public ProcessError getProcessError() {
            return error;
        }

        @Override
        protected boolean includeParameters() {
            return includeParameters;
        }

        @Override
        protected void setVariables(Map<String, Object> variablesToSet) {
        }

        @Override
        public void delete(String userId) {
        }

        @Override
        public Date getProcessStartTime() {
            return null;
        }

        @Override
        protected List<HistoricActivityInstance> loadHistoricActivities() {
            return historicActivities;
        }

        public void setHistoricActivities(List<HistoricActivityInstance> historicActivities) {
            this.historicActivities = historicActivities;
        }
    }

    private final String processInstanceId = TestUtils.generateRandomString();
    private final ProcessError error = new ProcessError(ErrorType.PROCESS_EXECUTION_ERROR, TestUtils.generateRandomString());
    private final String currentActivitiId = TestUtils.generateRandomString();
    private SlpProcessState processStatus = SlpProcessState.SLP_PROCESS_STATE_FINISHED;
    private SlpTaskState processTaskStatus = SlpTaskState.SLP_TASK_STATE_FINISHED;

    private final String[] actionIdsArray = { ACTION_ID_ABORT, ACTION_ID_RETRY };
    private final List<String> actionIds = Arrays.asList(actionIdsArray);
    private final String stepId = TestUtils.generateRandomString();

    // return value of the includeParameters() method
    private boolean includeParameters = false;

    // parameters for the service
    private Set<ParameterMetadata> parameters = new HashSet<ParameterMetadata>();

    private ServiceMetadata serviceMetadata = mock(ServiceMetadata.class);

    @Mock
    private ProgressMessageService progressMessageService;

    @Mock
    private TaskExtensionService taskExtensionService;

    @InjectMocks
    private ActivitiProcessImpl process = new ActivitiProcessImpl(serviceMetadata);

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        stub(serviceMetadata.getId()).toReturn(stepId);
        stub(serviceMetadata.getDisplayName()).toReturn(TestUtils.generateRandomString());
        stub(serviceMetadata.getDescription()).toReturn(TestUtils.generateRandomString());
        stub(serviceMetadata.getParameters()).toReturn(parameters);
        stub(serviceMetadata.isVisible()).toReturn(true);

    }

    @Test
    public void testGetProcesses() {

        // mock the engine
        List<HistoricActivityInstance> historicInstances = new ArrayList<HistoricActivityInstance>();
        List<HistoricVariableInstance> variables = new ArrayList<HistoricVariableInstance>();
        ProcessEngine engine = mockEngine(historicInstances, variables);
        ActivitiFacade.getInstance().init(engine);

        List<ActivitiProcess> activitiProcesses = new ArrayList<ActivitiProcess>();
        activitiProcesses.add(process);
        Processes processes = ActivitiProcess.getProcesses(activitiProcesses);
        assertEquals(1, processes.getProcess().size());
    }

    @Test
    public void testGetProcessWithParameters() {
        List<ActivitiProcess> activitiProcesses = new ArrayList<ActivitiProcess>();
        // will be returned by the process's includeParameters method
        includeParameters = true;

        String sidVariableName = "SID";
        String sidVariableValue = "KYP";

        // mock the parameters
        List<HistoricActivityInstance> historicInstances = new ArrayList<HistoricActivityInstance>();
        List<HistoricVariableInstance> variables = new ArrayList<HistoricVariableInstance>();
        HistoricVariableInstance sidVariable = mock(HistoricVariableInstance.class);
        stub(sidVariable.getVariableName()).toReturn(sidVariableName);
        stub(sidVariable.getValue()).toReturn(sidVariableValue);
        // add a sid variable that corresponds to the sid parameter
        variables.add(sidVariable);
        ProcessEngine engine = mockEngine(historicInstances, variables);
        ActivitiFacade.getInstance().init(engine);

        ParameterMetadata sidParameter = ParameterMetadata.builder().id(sidVariableName).required(true).build();
        parameters.add(sidParameter);

        activitiProcesses.add(process);
        Processes processes = ActivitiProcess.getProcesses(activitiProcesses);
        assertEquals(1, processes.getProcess().size());

        List<Parameter> processParameters = processes.getProcess().get(0).getParameters().getParameter();
        assertEquals(1, processParameters.size());

        // check that the value matches the one of the Activiti variable
        assertEquals(sidVariableValue, processParameters.get(0).getValue());
    }

    @Test
    public void testGetServiceMetadata() {
        assertSame(serviceMetadata, process.getServiceMetadata());
    }

    @Test
    public void testGetProcess() {

        // mock the engine
        List<HistoricActivityInstance> historicInstances = new ArrayList<HistoricActivityInstance>();
        List<HistoricVariableInstance> variables = new ArrayList<HistoricVariableInstance>();
        ProcessEngine engine = mockEngine(historicInstances, variables);
        ActivitiFacade.getInstance().init(engine);

        Process slpProcess = process.getProcess();
        assertNotNull(slpProcess);

        // check that the id of the generated slp process comes from the current process instance id
        assertNotNull(slpProcess.getId());
        assertEquals(slpProcess.getId(), processInstanceId);

        assertNotNull(slpProcess.getDisplayName());
        assertEquals(slpProcess.getDisplayName(), serviceMetadata.getDisplayName());

        assertNotNull(slpProcess.getDescription());
        assertEquals(slpProcess.getDescription(), serviceMetadata.getDescription());

        assertNotNull(slpProcess.getService());
        assertEquals(slpProcess.getService(), serviceMetadata.getId());

        assertNotNull(slpProcess.getStatus());
        assertEquals(slpProcess.getStatus(), processStatus);
    }

    @Test
    public void testGetActions() {
        Actions actions = process.getActions();
        List<Action> actionList = actions.getAction();
        for (Action action : actionList) {
            assertTrue(actionIds.contains(action.getId()));
        }
    }

    @Test
    public void testGetActin() {
        Action action = process.getAction(ACTION_ID_ABORT);
        assertEquals(SlpActionEnum.SLP_ACTION_ABORT.value(), action.getActionType());

        action = process.getAction(ACTION_ID_RETRY);
        assertEquals(SlpActionEnum.SLP_ACTION_REPEAT.value(), action.getActionType());

        action = process.getAction(TestUtils.generateRandomString());
        assertNull(action);
    }

    @Test
    public void testGetError() {
        Error error = process.getError();
        assertNotNull(error.getDescription());
        assertEquals(process.getProcessError().getMessage(), error.getDescription());
    }

    @Test
    public void testGetInvisibleCompositeTaskNotReturned() {
        List<HistoricActivityInstance> historicInstances = mockHistoricInstances(stepId);
        process.setHistoricActivities(historicInstances);
        ProcessEngine engine = mockEngine(historicInstances, new ArrayList<HistoricVariableInstance>());

        List<StepMetadata> childSteps = new ArrayList<StepMetadata>();
        childSteps.add(mockStepMetadata("step1", false));
        childSteps.add(mockStepMetadata("step2", false));
        stub(serviceMetadata.getChildren(any(VariableHandler.class))).toReturn(childSteps);

        stub(serviceMetadata.isVisible()).toReturn(false);

        ActivitiFacade.getInstance().init(engine);
        Tasklist tasklist = process.getTasklist();
        assertEquals(1, tasklist.getTask().size());
    }

    @Test
    public void testSingleTaskProcess() {
        List<HistoricActivityInstance> historicInstances = mockHistoricInstances(stepId);
        ProcessEngine engine = mockEngine(historicInstances, new ArrayList<HistoricVariableInstance>());

        ActivitiFacade.getInstance().init(engine);
        Tasklist tasklist = process.getTasklist();
        assertEquals(1, tasklist.getTask().size());
    }

    @Test
    public void testWithOneInvisibleChild() {
        List<HistoricActivityInstance> historicInstances = mockHistoricInstances(stepId);
        ProcessEngine engine = mockEngine(historicInstances, new ArrayList<HistoricVariableInstance>());

        List<StepMetadata> childSteps = new ArrayList<StepMetadata>();
        childSteps.add(mockStepMetadata("step1", true));
        childSteps.add(mockStepMetadata("step2", false));
        stub(serviceMetadata.getChildren(any(VariableHandler.class))).toReturn(childSteps);

        ActivitiFacade.getInstance().init(engine);
        Tasklist tasklist = process.getTasklist();
        assertEquals(2, tasklist.getTask().size());
    }

    @Test
    public void testGetCompositeTask() {
        List<HistoricActivityInstance> historicInstances = mockHistoricInstances(stepId);
        ProcessEngine engine = mockEngine(historicInstances, new ArrayList<HistoricVariableInstance>());

        // make the service metadata composite by returning some child tasks
        List<StepMetadata> childSteps = new ArrayList<StepMetadata>();
        childSteps.add(mockStepMetadata("step1", true));
        childSteps.add(mockStepMetadata("step2", true));
        stub(serviceMetadata.getChildren(any(VariableHandler.class))).toReturn(childSteps);

        ActivitiFacade.getInstance().init(engine);
        Tasklist tasklist = process.getTasklist();
        assertEquals(3, tasklist.getTask().size());
    }

    private List<HistoricActivityInstance> mockHistoricInstances(String... stepIds) {
        List<HistoricActivityInstance> historicInstances = new ArrayList<HistoricActivityInstance>();
        for (String stepId : stepIds) {
            HistoricActivityInstance historicInstance = mock(HistoricActivityInstance.class);
            // historic instance's id corresponds to taks id
            stub(historicInstance.getActivityId()).toReturn(stepId);
            stub(historicInstance.getEndTime()).toReturn(new Date());
            historicInstances.add(historicInstance);
        }

        return historicInstances;
    }

    private ProcessEngine mockEngine(List<HistoricActivityInstance> historicInstances, List<HistoricVariableInstance> variables) {
        ProcessEngine engine = Mockito.mock(ProcessEngine.class);
        RuntimeService runtimeService = Mockito.mock(RuntimeService.class);
        stub(engine.getRuntimeService()).toReturn(runtimeService);

        // historic instances
        HistoryService historyService = mockHistoryService(historicInstances, variables);

        stub(engine.getHistoryService()).toReturn(historyService);

        return engine;
    }

    private HistoryService mockHistoryService(List<HistoricActivityInstance> historicInstances, List<HistoricVariableInstance> variables) {
        HistoryService historyService = Mockito.mock(HistoryService.class);
        stubHistoricInstancesQuery(historicInstances, historyService);
        stubVariablesQuery(historyService, variables);
        return historyService;
    }

    private void stubVariablesQuery(HistoryService historyServiceMock, List<HistoricVariableInstance> variables) {
        HistoricVariableInstanceQuery historicVariableQuery = mock(HistoricVariableInstanceQuery.class);
        stub(historyServiceMock.createHistoricVariableInstanceQuery()).toReturn(historicVariableQuery);

        HistoricVariableInstanceQuery processVariablesQuery = mock(HistoricVariableInstanceQuery.class);
        stub(historicVariableQuery.processInstanceId(processInstanceId)).toReturn(processVariablesQuery);
        stub(processVariablesQuery.variableName(anyString())).toReturn(processVariablesQuery);
        stub(historicVariableQuery.variableValueEquals("correlationId", processInstanceId)).toReturn(historicVariableQuery);
        stub(historicVariableQuery.orderByProcessInstanceId()).toReturn(historicVariableQuery);
        stub(historicVariableQuery.asc()).toReturn(historicVariableQuery);
        stub(processVariablesQuery.list()).toReturn(variables);
        stub(processVariablesQuery.singleResult()).toReturn(null);
        for (HistoricVariableInstance variable : variables) {
            HistoricVariableInstanceQuery processVariablesQuery2 = mock(HistoricVariableInstanceQuery.class);
            stub(processVariablesQuery.variableName(variable.getVariableName())).toReturn(processVariablesQuery2);
            stub(processVariablesQuery2.singleResult()).toReturn(variable);
        }
    }

    private void stubHistoricInstancesQuery(List<HistoricActivityInstance> historicInstances, HistoryService historyService) {
        HistoricActivityInstanceQuery historicInstanceQuery = Mockito.mock(HistoricActivityInstanceQuery.class);
        stub(historyService.createHistoricActivityInstanceQuery()).toReturn(historicInstanceQuery);

        HistoricActivityInstanceQuery historicInstanceForProcessQuery = Mockito.mock(HistoricActivityInstanceQuery.class);
        stub(historicInstanceQuery.processInstanceId(processInstanceId)).toReturn(historicInstanceForProcessQuery);

        HistoricActivityInstanceQuery orderedHPIQuery = Mockito.mock(HistoricActivityInstanceQuery.class);
        stub(historicInstanceForProcessQuery.orderByHistoricActivityInstanceStartTime()).toReturn(orderedHPIQuery);

        HistoricActivityInstanceQuery actualQuery = Mockito.mock(HistoricActivityInstanceQuery.class);
        stub(orderedHPIQuery.asc()).toReturn(actualQuery);

        stub(actualQuery.list()).toReturn(historicInstances);
    }

}
