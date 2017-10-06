package com.sap.cloud.lm.sl.slp.activiti.tasklist.factory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.persistence.model.ProgressMessage;
import com.sap.cloud.lm.sl.persistence.model.ProgressMessage.ProgressMessageType;
import com.sap.cloud.lm.sl.slp.activiti.ActivitiTask;
import com.sap.cloud.lm.sl.slp.activiti.ProcessError;
import com.sap.cloud.lm.sl.slp.activiti.ProcessError.ErrorType;
import com.sap.cloud.lm.sl.slp.activiti.Step;
import com.sap.cloud.lm.sl.slp.activiti.tasklist.ProcessState;
import com.sap.cloud.lm.sl.slp.ext.TaskExtensionUtil;
import com.sap.cloud.lm.sl.slp.model.StepMetadata;
import com.sap.lmsl.slp.SlpTaskState;

public class JavaTaskFactory extends TaskFactory {

    public JavaTaskFactory(ProcessState processState, String parentId) {
        super(processState, parentId);
    }

    @Override
    public List<ActivitiTask> createTask(Step step) {
        StepMetadata stepMetadata = step.getStepMetadata();
        List<ProgressMessage> progressMessages = step.getProgressMessages();
        String taskId = progressMessages.get(0).getTaskId();
        Date startTime = progressMessages.get(0).getTimestamp();
        Date endTime = progressMessages.get(progressMessages.size() - 1).getTimestamp();
        SlpTaskState state = calculateBasedOnProgressMessages(taskId, progressMessages);
        double progress = 100;
        ProcessError error = getProcessError(progressMessages);
        Map<String, String> taskExtensions = getTaskExtensions(step.getTaskExtensions());

        return Arrays.asList(new ActivitiTask(taskId, stepMetadata.getDisplayName(), stepMetadata.getDescription(), stepMetadata, state,
            startTime, endTime, progress, progressMessages, error, taskExtensions, parentId));
    }

    private Map<String, String> getTaskExtensions(List<ProgressMessage> taskExtensions) {
        return TaskExtensionUtil.getTaskExtensions(taskExtensions);
    }

    private SlpTaskState calculateBasedOnProgressMessages(String taskId, List<ProgressMessage> progressMessages) {
        if (taskId.equals(processState.getCurrentTaskId())) {
            processState.setCurrentReached(true);
            return processState.getCurrentTaskState();
        }
        return getProcessError(progressMessages) == null ? SlpTaskState.SLP_TASK_STATE_FINISHED : SlpTaskState.SLP_TASK_STATE_ERROR;
    }

    private ProcessError getProcessError(List<ProgressMessage> progressMessages) {
        for (ProgressMessage progressMessage : progressMessages) {
            if (progressMessage.getType() == ProgressMessageType.ERROR) {
                return new ProcessError(ErrorType.PROCESS_EXECUTION_ERROR, progressMessage.getText());
            }
        }
        return null;
    }

}
