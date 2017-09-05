package com.sap.cloud.lm.sl.slp.activiti.tasklist.factory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.sap.cloud.lm.sl.persistence.model.ProgressMessage;
import com.sap.cloud.lm.sl.slp.activiti.ActivitiTask;
import com.sap.cloud.lm.sl.slp.activiti.ProcessError;
import com.sap.cloud.lm.sl.slp.activiti.Step;
import com.sap.cloud.lm.sl.slp.activiti.tasklist.ProcessState;
import com.sap.cloud.lm.sl.slp.ext.TaskExtensionUtil;
import com.sap.lmsl.slp.SlpTaskState;

public class CompositeTaskFactory extends TaskFactory {

    public CompositeTaskFactory(ProcessState processState, String parentId) {
        super(processState, parentId);
    }

    @Override
    public List<ActivitiTask> createTask(Step step) {
        String taskId = getTaskId(step);
        List<ActivitiTask> childrenActivitTasks = getChildrenAsActivitiTasks(taskId, step);
        Date startTime = getStartTime(childrenActivitTasks);
        Date endTime = getEndTime(childrenActivitTasks);
        SlpTaskState state = calculateCompositeState(taskId, childrenActivitTasks);
        double progress = calculateCompositeProgress(taskId, childrenActivitTasks);
        ProcessError error = calculateCompositeError(childrenActivitTasks);
        Map<String, String> taskExtensions = getTaskExtensions(step.getTaskExtensions());
        ActivitiTask currentTask = new ActivitiTask(taskId, step.getStepMetadata().getDisplayName(),
            step.getStepMetadata().getDescription(), step.getStepMetadata(), state, startTime, endTime, progress,
            getKidsProgressMessages(taskId, childrenActivitTasks, step.getProgressMessages()), error, taskExtensions, parentId);
        List<ActivitiTask> result = new ArrayList<>();
        result.add(currentTask);
        result.addAll(removeNonVisibleChildren(childrenActivitTasks));
        return result;
    }

    private Map<String, String> getTaskExtensions(List<ProgressMessage> taskExtensions) {
        return TaskExtensionUtil.getTaskExtensions(taskExtensions);
    }

    private Date getEndTime(List<ActivitiTask> childrenActivitTasks) {
        ListIterator<ActivitiTask> iterator = childrenActivitTasks.listIterator(childrenActivitTasks.size());
        while (iterator.hasPrevious()) {
            ActivitiTask activitiTask = iterator.previous();
            Date endTime = activitiTask.getEndTime();
            if (endTime != null) {
                return endTime;
            }
        }
        return null;
    }

    private Date getStartTime(List<ActivitiTask> childrenActivitTasks) {
        for (ActivitiTask activitiTask : childrenActivitTasks) {
            Date startTime = activitiTask.getStartTime();
            if (startTime != null) {
                return startTime;
            }
        }
        return null;
    }

    private String getTaskId(Step step) {
        if (!step.getProgressMessages().isEmpty()) {
            return step.getProgressMessages().get(0).getTaskId();
        }
        return step.getStepMetadata().getId();
    }

    private ProcessError calculateCompositeError(List<ActivitiTask> childrenActivitTasks) {
        for (ActivitiTask childTask : childrenActivitTasks) {
            if (childTask.getError() != null) {
                return childTask.getError();
            }
        }
        return null;
    }

    private List<ActivitiTask> removeNonVisibleChildren(List<ActivitiTask> childrenActivitTasks) {
        List<ActivitiTask> visibleChildren = new ArrayList<>();
        for (ActivitiTask childTask : childrenActivitTasks) {
            if (childTask.getStepMetadata().isVisible()) {
                visibleChildren.add(childTask);
            }
        }
        return visibleChildren;
    }

    private List<ProgressMessage> getKidsProgressMessages(String taskId, List<ActivitiTask> childrenActivitTasks,
        List<ProgressMessage> myProgressMessages) {
        List<ProgressMessage> result = new ArrayList<>();
        for (ActivitiTask childTask : childrenActivitTasks) {
            if (taskId.equals(childTask.getParentId())) {
                result.addAll(childTask.getProgressMessages());
            }
        }
        result.addAll(myProgressMessages);
        Collections.sort(result);
        return result;
    }

    private double calculateCompositeProgress(String taskId, List<ActivitiTask> childrenActivitTasks) {
        ProgressCalculator progressCalculator = new ProgressCalculator();
        for (ActivitiTask childActivitiTask : childrenActivitTasks) {
            progressCalculator.accumulateProgress(childActivitiTask.getProgress(), childActivitiTask.getStepMetadata().getProgressWeight());
        }
        // LOGGER.info("Progress for task " + taskId + ": " + progressCalculator.getRawProgress());
        // LOGGER.info("Total for task " + taskId + ": " + progressCalculator.getTotal());
        return progressCalculator.getProgress();
    }

    private SlpTaskState calculateCompositeState(String taskId, List<ActivitiTask> childrenActivitTasks) {
        if (taskId.equals(processState.getCurrentTaskId())) {
            processState.setCurrentReached(true);
            return processState.getCurrentTaskState();
        }
        StateCalculator stateCalculator = new StateCalculator();
        for (ActivitiTask childActivitiTask : childrenActivitTasks) {
            stateCalculator.updateState(childActivitiTask.getStatus());
        }
        return stateCalculator.getState();
    }

    private List<ActivitiTask> getChildrenAsActivitiTasks(String currentTaskId, Step step) {
        List<ActivitiTask> result = new LinkedList<>();
        for (Step child : step.getChildren()) {
            result.addAll(child.toActivitiTasks(currentTaskId));
        }
        return result;
    }

}