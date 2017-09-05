package com.sap.cloud.lm.sl.slp.activiti.tasklist.factory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.sap.cloud.lm.sl.persistence.model.ProgressMessage;
import com.sap.cloud.lm.sl.slp.activiti.ActivitiTask;
import com.sap.cloud.lm.sl.slp.activiti.Step;
import com.sap.cloud.lm.sl.slp.activiti.tasklist.ProcessState;
import com.sap.cloud.lm.sl.slp.model.StepMetadata;
import com.sap.lmsl.slp.SlpTaskState;

public class NonExecutedTaskFactory extends TaskFactory {

    private SlpTaskState targetState;

    public NonExecutedTaskFactory(ProcessState processState, String parentId, SlpTaskState targetState) {
        super(processState, parentId);
        this.targetState = targetState;
    }

    @Override
    public List<ActivitiTask> createTask(Step step) {
        StepMetadata metadata = step.getStepMetadata();
        double progress = targetState == SlpTaskState.SLP_TASK_STATE_SKIPPED ? 100.0 : 0.0;
        return Arrays.asList(new ActivitiTask(metadata.getId(), metadata.getDisplayName(), metadata.getDescription(), metadata, targetState,
            null, null, progress, Collections.<ProgressMessage> emptyList(), null, Collections.<String, String> emptyMap(), parentId));
    }

}
