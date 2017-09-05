package com.sap.cloud.lm.sl.slp.activiti.tasklist;

import java.util.List;

import org.activiti.engine.history.HistoricActivityInstance;

import com.sap.cloud.lm.sl.persistence.model.ProgressMessage;
import com.sap.lmsl.slp.SlpTaskState;

public class ProcessState {

    private final List<HistoricActivityInstance> historicInstances;
    private final List<ProgressMessage> progressMessages;
    private final List<ProgressMessage> taskExtensions;
    private final SlpTaskState currentTaskState;
    private final String currentTaskId;
    private final TaskIndexProvider taskIndexProvider = new TaskIndexProvider();
    private final List<String> stepIds;
    private boolean isCurrentReached;

    public ProcessState(List<HistoricActivityInstance> historicInstances, List<ProgressMessage> progressMessages,
        List<ProgressMessage> taskExtensions, SlpTaskState currentTaskState, String currentTaskId, List<String> stepIds) {
        this.historicInstances = historicInstances;
        this.progressMessages = progressMessages;
        this.taskExtensions = taskExtensions;
        this.currentTaskState = currentTaskState;
        this.currentTaskId = currentTaskId;
        this.stepIds = stepIds;
    }

    public List<HistoricActivityInstance> getHistoricInstances() {
        return historicInstances;
    }

    public TaskIndexProvider getTaskIndexProvider() {
        return taskIndexProvider;
    }

    public List<ProgressMessage> getProgressMessages() {
        return progressMessages;
    }

    public List<ProgressMessage> getTaskExtensions() {
        return taskExtensions;
    }

    public SlpTaskState getCurrentTaskState() {
        return currentTaskState;
    }

    public String getCurrentTaskId() {
        return currentTaskId;
    }

    public List<String> getStepIds() {
        return stepIds;
    }

    public boolean isCurrentReached() {
        return isCurrentReached;
    }

    public void setCurrentReached(boolean isCurrentReached) {
        this.isCurrentReached = isCurrentReached;
    }
}
