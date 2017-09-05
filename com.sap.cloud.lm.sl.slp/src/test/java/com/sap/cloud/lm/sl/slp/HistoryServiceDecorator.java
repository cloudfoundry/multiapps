package com.sap.cloud.lm.sl.slp;

import static org.mockito.Mockito.stub;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.mockito.Mockito;

public class HistoryServiceDecorator implements Decorator<ProcessEngine> {

    private HistoricVariablesDecorator variablesDecorator;
    private HistoricInstancesDecorator instancesDecorator;

    private String processInstanceId;

    public HistoryServiceDecorator(String processInstanceId) {
        super();
        this.processInstanceId = processInstanceId;
        this.variablesDecorator = new HistoricVariablesDecorator(processInstanceId, new ArrayList<HistoricVariableInstance>());
    }

    public HistoryServiceDecorator setHistoricInstances(List<HistoricActivityInstance> historicInstances) {
        instancesDecorator = (new HistoricInstancesDecorator(processInstanceId, historicInstances));
        return this;
    }

    public HistoryServiceDecorator setVariables(List<HistoricVariableInstance> historicVariables) {
        variablesDecorator = new HistoricVariablesDecorator(processInstanceId, historicVariables);
        return this;
    }

    @Override
    public void decorate(ProcessEngine engineMock) {
        // historic instances
        HistoryService historyService = mockHistoryService();
        stub(engineMock.getHistoryService()).toReturn(historyService);

        if (engineMock.getRuntimeService() == null) {
            RuntimeService runtimeService = Mockito.mock(RuntimeService.class);
            stub(engineMock.getRuntimeService()).toReturn(runtimeService);
        }
    }

    public HistoryService mockHistoryService() {
        HistoryService historyService = Mockito.mock(HistoryService.class);

        if (instancesDecorator != null)
            instancesDecorator.decorate(historyService);

        if (variablesDecorator != null)
            variablesDecorator.decorate(historyService);

        return historyService;
    }

}
