package com.sap.cloud.lm.sl.slp;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;

public class RepositoryServiceDecorator implements Decorator<ProcessEngine> {

    private FlowElement flowElement;
    private String serviceId;

    public RepositoryServiceDecorator(String serviceId) {
        this.serviceId = serviceId;
    }

    public void setFlowElement(FlowElement flowElement) {
        this.flowElement = flowElement;
    }

    @Override
    public void decorate(ProcessEngine engine) {
        RepositoryService repoService = mock(RepositoryService.class);
        ProcessDefinitionQuery query = mock(ProcessDefinitionQuery.class);
        ProcessDefinition processDefinition = mock(ProcessDefinition.class);
        BpmnModel bpmnModel = mock(BpmnModel.class);
        org.activiti.bpmn.model.Process mainProcess = mock(Process.class);

        when(engine.getRepositoryService()).thenReturn(repoService);
        when(repoService.createProcessDefinitionQuery()).thenReturn(query);
        when(query.processDefinitionKey(serviceId)).thenReturn(query);
        when(query.latestVersion()).thenReturn(query);
        when(query.processDefinitionKey(serviceId).latestVersion().singleResult()).thenReturn(processDefinition);
        when(processDefinition.getId()).thenReturn(serviceId);
        when(engine.getRepositoryService().getBpmnModel(serviceId)).thenReturn(bpmnModel);

        when(engine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey(
            serviceId).latestVersion().singleResult()).thenReturn(processDefinition);
        when(bpmnModel.getMainProcess()).thenReturn(mainProcess);

        if (flowElement != null) {
            when(mainProcess.getFlowElement(flowElement.getId())).thenReturn(flowElement);
        }
    }

}
