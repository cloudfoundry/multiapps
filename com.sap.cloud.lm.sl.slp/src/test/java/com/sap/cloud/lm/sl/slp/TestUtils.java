package com.sap.cloud.lm.sl.slp;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.history.HistoricVariableInstanceQuery;
import org.mockito.Mockito;

import com.sap.cloud.lm.sl.slp.activiti.ActivitiFacade;
import com.sap.cloud.lm.sl.slp.model.ParameterMetadata;
import com.sap.cloud.lm.sl.slp.model.ServiceMetadata;
import com.sap.cloud.lm.sl.slp.model.StepMetadata;
import com.sap.cloud.lm.sl.slp.model.builder.ServiceMetadataBuilder;

public class TestUtils {

    public static String generateRandomString() {
        return new BigInteger(130, new SecureRandom()).toString(32);
    }

    public static ServiceMetadata mockServiceMetadata(String serviceId, String implementationId, Set<ParameterMetadata> parameters) {
        ServiceMetadataBuilder builder = ServiceMetadata.builder();
        builder.implementationId(implementationId);
        builder.id(serviceId);
        builder.displayName(TestUtils.generateRandomString());
        builder.description(TestUtils.generateRandomString());
        builder.parameters(parameters);
        builder.requiredPermission(TestUtils.generateRandomString());

        return builder.build();
    }

    public static ProcessEngine mockEngineWithHistoricInstancesAndVariables(String processInstanceId,
        List<HistoricActivityInstance> historicInstances, List<HistoricVariableInstance> variables) {
        ProcessEngine engine = Mockito.mock(ProcessEngine.class);
        RuntimeService runtimeService = Mockito.mock(RuntimeService.class);
        stub(engine.getRuntimeService()).toReturn(runtimeService);

        // historic instances
        HistoryService historyService = mockHistoryService(processInstanceId, historicInstances, variables);

        stub(engine.getHistoryService()).toReturn(historyService);

        return engine;
    }

    public static HistoryService mockHistoryService(String processInstanceId, List<HistoricActivityInstance> historicInstances) {
        return mockHistoryService(processInstanceId, historicInstances, new ArrayList<HistoricVariableInstance>());
    }

    public static HistoryService mockHistoryServiceWithVars(String processInstanceId, List<HistoricVariableInstance> variables) {
        return mockHistoryService(processInstanceId, null, variables);
    }

    public static HistoryService mockHistoryService(String processInstanceId, List<HistoricActivityInstance> historicInstances,
        List<HistoricVariableInstance> variables) {
        HistoryService historyService = Mockito.mock(HistoryService.class);
        stubHistoricInstancesQuery(processInstanceId, historicInstances, historyService);
        stubVariablesQuery(processInstanceId, historyService, variables);
        return historyService;
    }

    private static void stubVariablesQuery(String processInstanceId, HistoryService historyServiceMock,
        List<HistoricVariableInstance> variables) {
        HistoricVariableInstanceQuery historicVariableQuery = mock(HistoricVariableInstanceQuery.class);
        stub(historyServiceMock.createHistoricVariableInstanceQuery()).toReturn(historicVariableQuery);

        HistoricVariableInstanceQuery processVariablesQuery = mock(HistoricVariableInstanceQuery.class);
        stub(historicVariableQuery.processInstanceId(processInstanceId)).toReturn(processVariablesQuery);
        stub(processVariablesQuery.variableName(anyString())).toReturn(processVariablesQuery);
        stub(processVariablesQuery.list()).toReturn(variables);
        stub(processVariablesQuery.singleResult()).toReturn(null);
        for (HistoricVariableInstance variable : variables) {
            HistoricVariableInstanceQuery processVariablesQuery2 = mock(HistoricVariableInstanceQuery.class);
            stub(processVariablesQuery.variableName(variable.getVariableName())).toReturn(processVariablesQuery2);
            stub(processVariablesQuery2.singleResult()).toReturn(variable);
        }
    }

    private static void stubHistoricInstancesQuery(String processInstanceId, List<HistoricActivityInstance> historicInstances,
        HistoryService historyService) {
        HistoricActivityInstanceQuery query = Mockito.mock(HistoricActivityInstanceQuery.class);
        stub(historyService.createHistoricActivityInstanceQuery()).toReturn(query);
        stub(query.processInstanceId(processInstanceId)).toReturn(query);
        stub(query.orderByHistoricActivityInstanceStartTime()).toReturn(query);
        stub(query.asc()).toReturn(query);

        stub(query.list()).toReturn(historicInstances);
    }

    @SafeVarargs
    public static void initializeFacade(Decorator<ProcessEngine>... decorators) {
        ProcessEngine engine = mock(ProcessEngine.class);
        for (Decorator<ProcessEngine> decorator : decorators) {
            decorator.decorate(engine);
        }
        IdentityService identityMock = mock(IdentityService.class);
        stub(engine.getIdentityService()).toReturn(identityMock);
        ActivitiFacade.getInstance().init(engine);
    }

    public static StepMetadata mockStepMetadata(String id, boolean isVisible) {
        StepMetadata step = mock(StepMetadata.class);
        stub(step.isVisible()).toReturn(isVisible);
        stub(step.getId()).toReturn(id);
        return step;
    }

    public static Date generateRandomDate() {
        return new Date();
    }

}
