package com.sap.cloud.lm.sl.slp;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.JobQuery;
import org.mockito.Mockito;

public class ManagementServiceDecorator implements Decorator<ProcessEngine> {

    List<Decorator<ManagementService>> decorators = new ArrayList<Decorator<ManagementService>>();
    ArgsCaptureAnswer executeJobsArgsCapture = new ArgsCaptureAnswer();
    private String processInstanceId;

    public ManagementServiceDecorator(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public ManagementServiceDecorator setCurrentJob(Job job) {
        decorators.add(new CurrentJobDecorator(this.processInstanceId, job));
        return this;
    }

    @Override
    public void decorate(ProcessEngine engine) {
        // historic instances
        ManagementService service = mockManagementSerivce();
        stub(engine.getManagementService()).toReturn(service);

        // final ManagementService managementMock = mock(ManagementService.class);
        doAnswer(executeJobsArgsCapture).when(service).executeJob(any(String.class));

        if (engine.getRuntimeService() == null) {
            RuntimeService runtimeService = Mockito.mock(RuntimeService.class);
            stub(engine.getRuntimeService()).toReturn(runtimeService);
        }
    }

    public ManagementService mockManagementSerivce() {
        ManagementService service = Mockito.mock(ManagementService.class);

        for (Decorator<ManagementService> decorator : decorators) {
            decorator.decorate(service);
        }

        return service;
    }

    private class CurrentJobDecorator implements Decorator<ManagementService> {

        private String processInstanceId;
        private Job job;

        public CurrentJobDecorator(String processInstanceId, Job job) {
            super();
            this.processInstanceId = processInstanceId;
            this.job = job;
        }

        @Override
        public void decorate(ManagementService managementMock) {
            // mock a job query that returns
            JobQuery jobQuery = mock(JobQuery.class);
            stub(jobQuery.processInstanceId(processInstanceId)).toReturn(jobQuery);
            stub(managementMock.createJobQuery()).toReturn(jobQuery);

            stub(jobQuery.singleResult()).toReturn(job);
        }

    }

    public String getLastExectedJobId() {
        return executeJobsArgsCapture.getValue();
    }

}
