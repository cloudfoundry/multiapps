package com.sap.activiti.common.groupers.filters;

import java.util.List;

import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.Job;

public class InProgressProcessFilter extends AbstractFilter<HistoricProcessInstance> {

    private List<Job> allJobs;

    public InProgressProcessFilter() {
        managementService = ProcessEngines.getDefaultProcessEngine()
            .getManagementService();
    }

    private ManagementService managementService;

    @Override
    public boolean isAccepted(HistoricProcessInstance processInstance) {
        return !hasFailedJobs(processInstance);
    }

    private boolean hasFailedJobs(HistoricProcessInstance processInstance) {
        if (allJobs == null) {
            allJobs = managementService.createJobQuery()
                .withException()//
                .list();
        }
        for (Job job : allJobs) {
            if (job.getProcessInstanceId()
                .equals(processInstance.getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getPositiveGroupName() {
        return "Running";
    }

    @Override
    public String getNegativeGroupName() {
        return "Failed";
    }
}