package com.sap.cloud.lm.sl.slp.activiti.tasklist.factory;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.activiti.engine.history.HistoricActivityInstance;

import com.sap.cloud.lm.sl.persistence.model.ProgressMessage;
import com.sap.cloud.lm.sl.slp.activiti.ActivitiTask;
import com.sap.cloud.lm.sl.slp.activiti.Step;
import com.sap.cloud.lm.sl.slp.activiti.tasklist.ProcessState;
import com.sap.cloud.lm.sl.slp.model.StepMetadata;
import com.sap.lmsl.slp.SlpTaskState;

public class NonJavaTaskFactory extends TaskFactory {

    public NonJavaTaskFactory(ProcessState processState, String parentId) {
        super(processState, parentId);
    }

    @Override
    public List<ActivitiTask> createTask(Step step) {
        int index = processState.getTaskIndexProvider().getTaskIndex(step.getStepMetadata().getId());
        HistoricActivityInstance historicActivity = step.getHistoricActivityInstance();
        Date startTime = historicActivity.getStartTime();
        Date endTime = historicActivity.getEndTime();
        SlpTaskState state = calculateTaskState(step, historicActivity);
        double progress = endTime == null ? 0 : 100;
        StepMetadata stepMetadata = step.getStepMetadata();
        String taskId = stepMetadata.getId() + index;
        return Arrays.asList(new ActivitiTask(taskId, stepMetadata.getDisplayName(), stepMetadata.getDescription(), stepMetadata, state, startTime, endTime, progress,
            Collections.<ProgressMessage> emptyList(), null, Collections.<String, String> emptyMap(), parentId));
    }

    private SlpTaskState calculateTaskState(Step step, HistoricActivityInstance historicActivity) {
        StepMetadata currentStepMetadata = step.getStepMetadata();
        if (historicActivity.getId().equals(processState.getCurrentTaskId())) {
            processState.setCurrentReached(true);
            return processState.getCurrentTaskState();
        }
        SlpTaskState targetState = currentStepMetadata.getTargetState();
        SlpTaskState defaultTargetState = null;
        if (historicActivity.getEndTime() != null) {
            return SlpTaskState.SLP_TASK_STATE_FINISHED;
        }
        if (historicActivity.getActivityType().equals("receiveTask")) {
            defaultTargetState = SlpTaskState.SLP_TASK_STATE_ACTION_REQUIRED;
        } else {
            defaultTargetState = SlpTaskState.SLP_TASK_STATE_RUNNING;
        }
        SlpTaskState state = targetState != null ? targetState : defaultTargetState;
        return state;
    }

}
