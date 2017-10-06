package com.sap.cloud.lm.sl.slp.activiti.tasklist.factory;

import java.util.List;

import com.sap.cloud.lm.sl.slp.activiti.ActivitiTask;
import com.sap.cloud.lm.sl.slp.activiti.Step;
import com.sap.cloud.lm.sl.slp.activiti.tasklist.ProcessState;

public abstract class TaskFactory {

    protected ProcessState processState;
    protected String parentId;

    public TaskFactory(ProcessState processState, String parentId) {
        this.processState = processState;
        this.parentId = parentId;
    }

    public abstract List<ActivitiTask> createTask(Step step);

}
