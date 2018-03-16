package com.sap.activiti.common.groupers.filters;

import java.util.List;

import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.Job;

public class RunningProcessFilter extends AbstractFilter<HistoricProcessInstance> {

    private List<Job> allJobs;
    private ManagementService managementService;

    public RunningProcessFilter() {
        managementService = ProcessEngines.getDefaultProcessEngine()
            .getManagementService();
    }

    @Override
    public String getPositiveGroupName() {
        return "Running";
    }

    @Override
    public String getNegativeGroupName() {
        return "Input required";
    }

    @Override
    public boolean isAccepted(HistoricProcessInstance instance) {
        return hasJobs(instance);
    }

    private boolean hasJobs(HistoricProcessInstance processInstance) {
        if (allJobs == null) {
            allJobs = managementService.createJobQuery()
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
}
