package com.sap.cloud.lm.sl.slp.activiti;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.sap.cloud.lm.sl.persistence.model.ProgressMessage;
import com.sap.cloud.lm.sl.persistence.model.ProgressMessage.ProgressMessageType;
import com.sap.cloud.lm.sl.persistence.util.ProgressMessageUtil;
import com.sap.cloud.lm.sl.slp.model.StepMetadata;
import com.sap.lmsl.slp.SlpTaskState;
import com.sap.lmsl.slp.Task;
import com.sap.lmsl.slp.Tasklist;

public class ActivitiTask {

    public ActivitiTask(String indexedId, String indexedDisplayName, String indexedDescription, StepMetadata metadata, SlpTaskState status,
        Date startTime, Date endTime, double progress, List<ProgressMessage> progressMessages, ProcessError error,
        Map<String, String> taskExtensionElements, String parentId) {
        this.indexedId = indexedId;
        this.indexedDisplayName = indexedDisplayName;
        this.indexedDescription = indexedDescription;
        this.stepMetadata = metadata;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.progress = progress;
        this.progressMessages = progressMessages;
        this.error = error;
        this.taskExtensions = taskExtensionElements;
        this.parentId = parentId;
    }

    private final String indexedId;
    private final String indexedDisplayName;
    private final String indexedDescription;
    private final StepMetadata stepMetadata;
    private final Date startTime;
    private final Date endTime;
    private final double progress;
    private final List<ProgressMessage> progressMessages;
    private final SlpTaskState status;
    private final ProcessError error;
    private final Map<String, String> taskExtensions;
    private final String parentId;

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public String getIndexedId() {
        return indexedId;
    }

    public String getIndexedDisplayName() {
        return indexedDisplayName;
    }

    public String getIndexedDescription() {
        return indexedDescription;
    }

    public double getProgress() {
        return progress;
    }

    public List<ProgressMessage> getProgressMessages() {
        return progressMessages;
    }

    public Map<String, String> getTaskExtensions() {
        return taskExtensions;
    }

    public SlpTaskState getStatus() {
        return status;
    }

    public ProcessError getError() {
        return error;
    }

    public StepMetadata getStepMetadata() {
        return stepMetadata;
    }

    public String getParentId() {
        return parentId;
    }

    @Override
    public String toString() {
        return "ActivitiTaskImpl [id=" + indexedId + ", status=" + getStatus() + ", startTime=" + getStartTime() + ", endTime="
            + getEndTime() + ", progress=" + getProgress() + ", progressMessages=" + getProgressMessages() + ", error=" + getError()
            + ", StepMetadata=" + getStepMetadata() + "]\n";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((stepMetadata == null) ? 0 : stepMetadata.hashCode());
        result = prime * result + getStatus().hashCode();
        result = prime * result + getStartTime().hashCode();
        result = prime * result + getEndTime().hashCode();
        result = prime * result + Double.valueOf(getProgress()).hashCode();
        result = prime * result + getProgressMessages().hashCode();
        result = prime * result + getError().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ActivitiTask other = (ActivitiTask) obj;
// @formatter:off
        return Objects.equals(stepMetadata, other.stepMetadata) 
            && Objects.equals(getStatus(), other.getStatus())
            && Objects.equals(getStartTime(), other.getStartTime()) 
            && Objects.equals(getEndTime(), other.getEndTime())
            && getProgress() == other.getProgress() 
            && Objects.equals(getEndTime(), other.getEndTime())
            && Objects.equals(getProgressMessages(), other.getProgressMessages()) 
            && Objects.equals(getError(), other.getError());
// @formatter:on
    }

    public static Tasklist getTasklist(List<ActivitiTask> activitiTasks) {
        List<Task> tasks = new ArrayList<Task>();
        for (ActivitiTask activitiTask : activitiTasks) {
            tasks.add(activitiTask.getTask());
        }
        return SlpObjectFactory.createTasklist(tasks);
    }

    public Task getTask() {
        List<ProgressMessage> nonTechnicalProgressMessages = getNonTechnicalProgressMessages();
        return SlpObjectFactory.createTask(getIndexedId(), getIndexedDisplayName(), getIndexedDescription(), parentId,
            stepMetadata.getTaskType(), getStatus(), (int) Math.round(getProgress()), getStartTime(), getEndTime(), getError(),
            ProgressMessageUtil.getProgressMessagesAsString(nonTechnicalProgressMessages), getTaskExtensions());
    }

    private List<ProgressMessage> getNonTechnicalProgressMessages() {
        List<ProgressMessage> result = new ArrayList<>();
        for (ProgressMessage progressMessage : getProgressMessages()) {
            if (!progressMessage.getType().equals(ProgressMessageType.TASK_STARTUP)) {
                result.add(progressMessage);
            }
        }
        return result;
    }

}