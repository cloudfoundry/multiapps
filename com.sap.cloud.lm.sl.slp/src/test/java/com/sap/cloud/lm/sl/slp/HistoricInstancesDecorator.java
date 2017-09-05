package com.sap.cloud.lm.sl.slp;

import static org.mockito.Mockito.stub;

import java.util.Collections;
import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.mockito.Mockito;

public class HistoricInstancesDecorator implements Decorator<HistoryService> {

    private String processInstanceId;
    private List<HistoricActivityInstance> historicInstances;

    public HistoricInstancesDecorator(String processInstanceId, List<HistoricActivityInstance> historicInstances) {
        super();
        this.processInstanceId = processInstanceId;
        this.historicInstances = historicInstances;
    }

    @Override
    public void decorate(HistoryService historyService) {
        HistoricActivityInstanceQuery query = Mockito.mock(HistoricActivityInstanceQuery.class);
        HistoricProcessInstanceQuery processQuery = Mockito.mock(HistoricProcessInstanceQuery.class);
        stub(historyService.createHistoricActivityInstanceQuery()).toReturn(query);
        stub(historyService.createHistoricProcessInstanceQuery()).toReturn(processQuery);
        stub(query.processInstanceId(processInstanceId)).toReturn(query);
        stub(query.activityType(Mockito.anyString())).toReturn(query);
        stub(query.orderByHistoricActivityInstanceStartTime()).toReturn(query);
        stub(query.asc()).toReturn(query);
        stub(processQuery.superProcessInstanceId(Mockito.anyString())).toReturn(processQuery);

        stub(processQuery.list()).toReturn(Collections.<HistoricProcessInstance> emptyList());
        stub(query.list()).toReturn(historicInstances);
    }

}
