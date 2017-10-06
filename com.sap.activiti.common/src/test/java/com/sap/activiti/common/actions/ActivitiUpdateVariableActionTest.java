package com.sap.activiti.common.actions;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.history.HistoricVariableInstanceQuery;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.HistoricVariableInstanceQueryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.sap.activiti.common.ActivitiQuerySpyBuilder;
import com.sap.activiti.common.Constants;

public class ActivitiUpdateVariableActionTest extends AbstractCustomActivitiTraceableActionTestHelper {

    private static final String TEST_PROCESS_INSTANCE_ID = "12345";
    private static final String TEST_EDITABLE_VARIABLES = "[\"sid\", \"progress\"]";
    private static final String TEST_SID = "JJJ";
    private static final String TEST_PROGRESS = "0/10";

    private static final String TEST_USER_ID = "userid";
    private static final String TEST_USER_FIRST_NAME = "Jon";
    private static final String TEST_USER_LAST_NAME = "Doe";
    private static final String SID = "sid";
    private static final String PROGRESS = "progress";
    private static final String DB_HOST = "db_host";

    private ProcessEngine processEngine;
    private Map<String, Object> variables;

    @Before
    public void setUp() {
        variables = createVariables();
        processEngine = createProcessEngine();
    }

    private Map<String, Object> createVariables() {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put(SID, TEST_SID);
        map.put(PROGRESS, TEST_PROGRESS.getBytes());
        map.put(Constants.EDITABLE_VARIABLES, TEST_EDITABLE_VARIABLES);

        return map;
    }

    private ProcessEngine createProcessEngine() {
        User user = super.createUser(TEST_USER_FIRST_NAME, TEST_USER_LAST_NAME);
        ProcessEngine engine = super.createProcessEngine(user);

        mockRuntimeService(engine);
        mockHistoryService(engine);

        return engine;
    }

    @SuppressWarnings("unchecked")
    private void mockRuntimeService(ProcessEngine engine) {
        RuntimeService runtimeService = engine.getRuntimeService();
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Map<String, Object> newValues = (Map<String, Object>) invocation.getArguments()[1];
                for (Entry<String, Object> entry : newValues.entrySet()) {
                    variables.put(entry.getKey(), entry.getValue());
                }
                return null;
            }
        }).when(runtimeService).setVariables(anyString(), anyMap());
    }

    private void mockHistoryService(ProcessEngine engine) {
        HistoricVariableInstanceQuery query = ActivitiQuerySpyBuilder.<HistoricVariableInstanceQuery, HistoricVariableInstance> //
        createBuilder(new HistoricVariableInstanceQueryImpl()).setElementsList(createProcessVariables()) //
        .getQuery();

        HistoryService historyService = engine.getHistoryService();
        doReturn(query).when(historyService).createHistoricVariableInstanceQuery();
    }

    private List<HistoricVariableInstance> createProcessVariables() {
        List<HistoricVariableInstance> variablesList = new ArrayList<HistoricVariableInstance>(variables.size());

        for (Entry<String, Object> entry : variables.entrySet()) {
            variablesList.add(createHistoricVariable(entry.getKey(), entry.getValue(), entry.getValue() instanceof byte[]));
        }

        return variablesList;
    }

    private HistoricVariableInstance createHistoricVariable(String name, Object value, boolean isBinary) {
        HistoricVariableInstance variable = mock(HistoricVariableInstance.class);

        when(variable.getVariableName()).thenReturn(name);
        when(variable.getValue()).thenReturn(value);
        when(variable.getVariableTypeName()).thenReturn(isBinary ? "bytes" : "string");

        return variable;
    }

    private ActivitiUpdateVariablesAction createService(String json) {
        return new ActivitiUpdateVariablesAction(TEST_PROCESS_INSTANCE_ID, TEST_USER_ID, json) {
            @Override
            protected ProcessEngine getDefaultProcessEngine() {
                return processEngine;
            }
        };
    }

    @Test
    public void whenInputJsonIsEmpty_thenNoVariablesAreUpdated() {
        ActivitiUpdateVariablesAction service = createService("[]");
        service.execute();

        assertThat((String) variables.get(SID), is(TEST_SID));
        assertThat((byte[]) variables.get(PROGRESS), is(TEST_PROGRESS.getBytes()));
        assertThat((String) variables.get(Constants.EDITABLE_VARIABLES), is(TEST_EDITABLE_VARIABLES));
    }

    @Test
    public void validInputTest() {
        ActivitiUpdateVariablesAction service = createService(
            String.format("[{name:\"%s\",value:\"AAA\"},{name:\"%s\",value:\"1/10\"}]", SID, PROGRESS));
        service.execute();
        assertThat((String) variables.get(SID), is("AAA"));
        assertThat((byte[]) variables.get(PROGRESS), is("1/10".getBytes()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void noEditableVariablesTest() {
        variables.remove(Constants.EDITABLE_VARIABLES);
        mockHistoryService(processEngine);
        ActivitiUpdateVariablesAction service = createService(
            String.format("[{name:\"%s\",value:\"AAA\"},{name:\"%s\",value:\"1/10\"}]", SID, PROGRESS));
        service.execute();
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateNonEditableVariableTest() {
        variables.put(DB_HOST, "dbhost");
        mockHistoryService(processEngine);
        ActivitiUpdateVariablesAction service = createService(
            String.format("[{name:\"%s\",value:\"AAA\"},{name:\"%s\",value:\"dbhost2\"}]", SID, DB_HOST));
        service.execute();
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateNonExistingVariableTest() {
        ActivitiUpdateVariablesAction service = createService(
            String.format("[{name:\"%s\",value:\"AAA\"},{name:\"%s\",value:\"dbhost\"}]", SID, DB_HOST));
        service.execute();
    }
}
