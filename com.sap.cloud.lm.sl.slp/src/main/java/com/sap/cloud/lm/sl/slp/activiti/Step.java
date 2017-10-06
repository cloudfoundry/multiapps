package com.sap.cloud.lm.sl.slp.activiti;

import java.util.List;

import org.activiti.engine.history.HistoricActivityInstance;

import com.sap.cloud.lm.sl.persistence.model.ProgressMessage;
import com.sap.cloud.lm.sl.slp.activiti.tasklist.ProcessState;
import com.sap.cloud.lm.sl.slp.activiti.tasklist.factory.CompositeTaskFactory;
import com.sap.cloud.lm.sl.slp.activiti.tasklist.factory.JavaTaskFactory;
import com.sap.cloud.lm.sl.slp.activiti.tasklist.factory.NonExecutedTaskFactory;
import com.sap.cloud.lm.sl.slp.activiti.tasklist.factory.NonJavaTaskFactory;
import com.sap.cloud.lm.sl.slp.model.StepMetadata;
import com.sap.lmsl.slp.SlpTaskState;

public class Step {

    private StepMetadata metadata;
    private HistoricActivityInstance historicActivityInstance;
    private List<ProgressMessage> progressMessages;
    private List<ProgressMessage> taskExtensions;
    private List<Step> children;
    private ProcessState processState;

    public Step(StepMetadata stepMetadata, HistoricActivityInstance historicActivityInstance, List<ProgressMessage> progressMessages,
        List<ProgressMessage> extensions, List<Step> children, ProcessState processState) {
        this.metadata = stepMetadata;
        this.historicActivityInstance = historicActivityInstance;
        this.progressMessages = progressMessages;
        this.taskExtensions = extensions;
        this.children = children;
        this.processState = processState;
    }

    public StepMetadata getStepMetadata() {
        return metadata;
    }

    public HistoricActivityInstance getHistoricActivityInstance() {
        return historicActivityInstance;
    }

    public List<ProgressMessage> getProgressMessages() {
        return progressMessages;
    }

    public List<ProgressMessage> getTaskExtensions() {
        return taskExtensions;
    }

    public ProcessState getProcessState() {
        return processState;
    }

    public List<Step> getChildren() {
        return children;
    }

    public List<ActivitiTask> toActivitiTasks() {
        return toActivitiTasks(null);
    }

    public List<ActivitiTask> toActivitiTasks(String parentId) {
        if (!children.isEmpty()) {
            return new CompositeTaskFactory(processState, parentId).createTask(this);
        }
        if (historicActivityInstance != null) {
            return new NonJavaTaskFactory(processState, parentId).createTask(this);
        }
        if (!progressMessages.isEmpty()) {
            return new JavaTaskFactory(processState, parentId).createTask(this);
        }
        SlpTaskState state = !processState.isCurrentReached() ? SlpTaskState.SLP_TASK_STATE_SKIPPED : SlpTaskState.SLP_TASK_STATE_INITIAL;
        return new NonExecutedTaskFactory(processState, parentId, state).createTask(this);
    }

}
