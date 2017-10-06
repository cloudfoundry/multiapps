package com.sap.cloud.lm.sl.slp.activiti.tasklist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;

import org.activiti.engine.history.HistoricActivityInstance;

import com.sap.cloud.lm.sl.persistence.model.ProgressMessage;
import com.sap.cloud.lm.sl.slp.activiti.Step;
import com.sap.cloud.lm.sl.slp.model.StepMetadata;
import com.sap.cloud.lm.sl.slp.model.VariableHandler;

public class StepBuilder {

    private final ProcessState processState;
    private final VariableHandler variableHandler;

    Map<String, List<ProgressMessage>> progressMessages;
    Map<String, List<ProgressMessage>> taskExtensions;

    public StepBuilder(ProcessState processState, VariableHandler variableHandler) {
        this.processState = processState;
        this.variableHandler = variableHandler;
        this.progressMessages = getProgressMessagesAsMap(processState.getProgressMessages());
        this.taskExtensions = getProgressMessagesAsMap(processState.getTaskExtensions());
    }

    private Map<String, List<ProgressMessage>> getProgressMessagesAsMap(List<ProgressMessage> progressMessages) {
        Map<String, List<ProgressMessage>> result = new TreeMap<>();
        for (ProgressMessage progressMessage : progressMessages) {
            String taskId = progressMessage.getTaskId();
            List<ProgressMessage> progressMessagesForTask = result.get(taskId);
            if (progressMessagesForTask == null) {
                progressMessagesForTask = new LinkedList<>();
            }
            progressMessagesForTask.add(progressMessage);
            result.put(taskId, progressMessagesForTask);
        }
        return result;
    }

    public Step buildStep(StepMetadata metadata) {
        HistoricActivityInstance historicActivities = findCorrespondingHistoricActivity(metadata.getId());
        List<ProgressMessage> progressMessagesForCurrentStep = getProgressMessages(metadata.getId(), progressMessages);
        List<ProgressMessage> taskExtensionsForCurrentStep = getProgressMessages(metadata.getId(), taskExtensions);
        List<Step> children = new ArrayList<>();
        for (StepMetadata childMetadata : metadata.getChildren(variableHandler)) {
            children.add(buildStep(childMetadata));
        }
        return new Step(metadata, historicActivities, progressMessagesForCurrentStep, taskExtensionsForCurrentStep, children, processState);
    }

    private List<ProgressMessage> getProgressMessages(String id, Map<String, List<ProgressMessage>> progressMessages) {
        for (String taskId : progressMessages.keySet()) {
            if (taskId.startsWith(id)) {
                return progressMessages.remove(taskId);
            }
        }
        return Collections.emptyList();
    }

    private HistoricActivityInstance findCorrespondingHistoricActivity(String stepId) {
        ListIterator<HistoricActivityInstance> iterator = processState.getHistoricInstances().listIterator();
        while (iterator.hasNext()) {
            HistoricActivityInstance historicActivity = iterator.next();
            if (historicActivity.getActivityId().equals(stepId)) {
                iterator.remove();
                return historicActivity;
            }
        }
        return null;
    }

}
