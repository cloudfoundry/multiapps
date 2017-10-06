package com.sap.cloud.lm.sl.slp.activiti;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;

import com.sap.cloud.lm.sl.slp.activiti.action.AbortActivitiAction;
import com.sap.cloud.lm.sl.slp.activiti.action.ActivitiAction;
import com.sap.cloud.lm.sl.slp.activiti.action.ResumeActivitiAction;
import com.sap.cloud.lm.sl.slp.activiti.action.RetryActivitiAction;
import com.sap.cloud.lm.sl.slp.model.ServiceMetadata;
import com.sap.cloud.lm.sl.slp.model.StepMetadata;
import com.sap.lmsl.slp.SlpProcessState;
import com.sap.lmsl.slp.SlpTaskState;

public class ActiveActivitiProcess extends ActivitiProcess {

    private final ProcessInstance processInstance;

    // Cached state:
    private SlpTaskState status;
    private List<String> actionIds;

    public ActiveActivitiProcess(ServiceMetadata serviceMetadata, ProcessInstance processInstance) {
        super(serviceMetadata);
        this.processInstance = processInstance;
    }

    @Override
    public SlpProcessState getStatus() {
        return (processInstance.isEnded()) ? SlpProcessState.SLP_PROCESS_STATE_FINISHED : SlpProcessState.SLP_PROCESS_STATE_ACTIVE;
    }

    @Override
    public SlpTaskState getCurrentState() {
        if (status == null) {
            status = calculateCurrentTaskState();
        }
        return status;
    }

    @Override
    public List<String> getActionIds() {
        if (actionIds == null) {
            actionIds = calculateActionIds();
        }
        return actionIds;
    }

    @Override
    public void executeAction(String userId, String actionId) {
        // TODO: Check if the action is appropriate for the current status:
        ActivitiAction activitiAction = getActivitiAction(userId, actionId);
        if (activitiAction != null) {
            activitiAction.executeAction(processInstance.getProcessInstanceId());
        }
    }

    private ActivitiAction getActivitiAction(String userId, String actionId) {
        if (ACTION_ID_ABORT.equals(actionId)) {
            return new AbortActivitiAction(activitiFacade, userId);
        } else if (ACTION_ID_RESUME.equals(actionId) || ACTION_ID_START.equals(actionId)) {
            return new ResumeActivitiAction(activitiFacade, userId);
        } else if (ACTION_ID_RETRY.equals(actionId)) {
            return new RetryActivitiAction(activitiFacade, userId);
        }
        return null;
    }

    @Override
    protected String getProcessInstanceId() {
        return processInstance.getId();
    }

    @Override
    public String getCurrentActivityId() {
        return processInstance.getActivityId();
    }

    @Override
    public Date getProcessStartTime() {
        // TODO: Aggregate a historic instance as well and return value from it.
        return null;
    }

    @Override
    protected boolean includeParameters() {
        return true;
    }

    private SlpTaskState calculateCurrentTaskState() {
        if (isAtReceiveTask()) {
            return getReceiveTaskTaskState();
        } else if (getError() != null) {
            // TODO: No check for the gateways, etc. Listeners...
            return SlpTaskState.SLP_TASK_STATE_ERROR;
        } else {
            return SlpTaskState.SLP_TASK_STATE_RUNNING;
        }
    }

    private List<String> calculateActionIds() {
        List<String> actionIds = new ArrayList<String>();
        switch (getCurrentState()) {
            case SLP_TASK_STATE_ACTION_REQUIRED:
                actionIds.add(ACTION_ID_RESUME);
                actionIds.add(ACTION_ID_ABORT);
                break;
            case SLP_TASK_STATE_INITIAL:
                actionIds.add(ACTION_ID_START);
                actionIds.add(ACTION_ID_ABORT);
                break;
            case SLP_TASK_STATE_ERROR:
                actionIds.add(ACTION_ID_RETRY);
                actionIds.add(ACTION_ID_ABORT);
                break;
            case SLP_TASK_STATE_DIALOG:
                actionIds.add(ACTION_ID_RESUME);
                actionIds.add(ACTION_ID_ABORT);
                break;
            default:
                actionIds.add(ACTION_ID_ABORT);
                break;
        }
        return actionIds;
    }

    private boolean isAtReceiveTask() {
        HistoricActivityInstance currentHistoricActiviti = getCurrentHistoricActiviti();
        return currentHistoricActiviti != null && ACTIVITY_TASK_TYPE_RECEIVE.equals(currentHistoricActiviti.getActivityType());
    }

    private HistoricActivityInstance getCurrentHistoricActiviti() {
        List<String> subProcessIds = getActivitiFacade().getHistoricSubProcessIds(processInstance.getProcessInstanceId());
        List<Execution> subProcessExecutions = findProcessExecutions(subProcessIds);
        List<HistoricActivityInstance> historicActivities = getHistoricActivities();
        // iterate in reverse, historical instances are ordered chronologically
        for (Execution processExecution : subProcessExecutions) {
            HistoricActivityInstance historicActivityInstance = findHistoricActivityInProcessExecution(historicActivities,
                processExecution);
            if (historicActivityInstance != null) {
                return historicActivityInstance;
            }
        }
        return findHistoricActivityInProcessExecution(historicActivities, processInstance);
    }

    private HistoricActivityInstance findHistoricActivityInProcessExecution(List<HistoricActivityInstance> historicActivities,
        Execution processExecution) {
        for (ListIterator<HistoricActivityInstance> iterator = historicActivities.listIterator(
            historicActivities.size()); iterator.hasPrevious();) {
            HistoricActivityInstance historicActivityInstance = (HistoricActivityInstance) iterator.previous();
            if (historicActivityInstance.getActivityId().equals(processExecution.getActivityId())) {
                return historicActivityInstance;
            }
        }
        return null;
    }

    private List<Execution> findProcessExecutions(List<String> subProcessIds) {
        List<Execution> result = new ArrayList<>();
        for (String subProcessId : subProcessIds) {
            List<Execution> processExecutions = getActivitiFacade().getProcessExecutions(subProcessId);
            result.addAll(processExecutions);
        }
        return result;
    }

    private SlpTaskState getReceiveTaskTaskState() {
        StepMetadata correspondingStepMetadata = findStepMetadata(serviceMetadata, getCurrentActivityId());

        if (correspondingStepMetadata == null || correspondingStepMetadata.getTargetState() == null) {
            return SlpTaskState.SLP_TASK_STATE_ACTION_REQUIRED;
        }
        return correspondingStepMetadata.getTargetState();
    }

    private StepMetadata findStepMetadata(StepMetadata stepMetadata, String id) {
        if (stepMetadata.getId().equals(id)) {
            return stepMetadata;
        }
        for (StepMetadata childMetadata : stepMetadata.getChildren(getVariableHandler())) {
            StepMetadata result = findStepMetadata(childMetadata, id);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private void abortProcessExecution(String userId) {
        getActivitiFacade().deleteProcessInstance(userId, processInstance.getId(), SlpTaskState.SLP_TASK_STATE_ABORTED.value());
    }

    @Override
    protected void setVariables(Map<String, Object> variables) {
        getActivitiFacade().setRuntimeVariables(processInstance.getId(), variables);
    }

    @Override
    public void delete(String userId) {
        abortProcessExecution(userId);
        getActivitiFacade().deleteHistoricProcessInstance(userId, processInstance.getId());
    }

}
