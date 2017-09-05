package com.sap.cloud.lm.sl.slp;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.history.HistoricVariableInstanceQuery;
import org.mockito.Mockito;

public class HistoricVariablesDecorator implements Decorator<HistoryService> {

    private List<HistoricVariableInstance> historicVariables;
    private String processInstanceId;

    public HistoricVariablesDecorator(String processInstanceId, List<HistoricVariableInstance> historicVariables) {
        this.processInstanceId = processInstanceId;
        this.historicVariables = historicVariables;
    }

    @Override
    public void decorate(HistoryService historyServiceMock) {
        HistoricVariableInstanceQuery historicVariableQuery = mock(HistoricVariableInstanceQuery.class);
        stub(historyServiceMock.createHistoricVariableInstanceQuery()).toReturn(historicVariableQuery);
        stub(historicVariableQuery.processInstanceId(processInstanceId)).toReturn(historicVariableQuery);
        stub(historicVariableQuery.variableValueEquals(Mockito.eq("correlationId"), Mockito.anyString())).toReturn(historicVariableQuery);
        stub(historicVariableQuery.orderByProcessInstanceId()).toReturn(historicVariableQuery);
        stub(historicVariableQuery.asc()).toReturn(historicVariableQuery);
        stub(historicVariableQuery.list()).toReturn(historicVariables);
        if (!historicVariables.isEmpty()) {
            stub(historicVariableQuery.singleResult()).toReturn(historicVariables.get(0));
        }
    }

}
