package com.sap.cloud.lm.sl.slp.activiti;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricProcessInstance;

import com.sap.cloud.lm.sl.slp.model.ServiceMetadata;
import com.sap.lmsl.slp.SlpProcessState;
import com.sap.lmsl.slp.SlpTaskState;

public class FinishedActivitiProcess extends ActivitiProcess {

    private final HistoricProcessInstance processInstance;

    public FinishedActivitiProcess(ServiceMetadata serviceMetadata, HistoricProcessInstance processInstance) {
        super(serviceMetadata);
        this.processInstance = processInstance;
    }

    @Override
    public SlpProcessState getStatus() {
        return (processInstance.getEndTime() != null) ? SlpProcessState.SLP_PROCESS_STATE_FINISHED
            : SlpProcessState.SLP_PROCESS_STATE_ACTIVE;
    }

    @SuppressWarnings("deprecation")
    @Override
    public SlpTaskState getCurrentState() {
        if (processInstance.getEndActivityId() == null) {
            if (processInstance.getDeleteReason() != null && !processInstance.getDeleteReason().isEmpty()) {
                try {
                    SlpTaskState slpTaskState = SlpTaskState.fromValue(processInstance.getDeleteReason());

                    if (slpTaskState == SlpTaskState.SLP_TASK_STATE_FINISHED || slpTaskState == SlpTaskState.SLP_TASK_STATE_ERROR
                        || slpTaskState == SlpTaskState.SLP_TASK_STATE_ABORTED) {

                        return slpTaskState;
                    }
                } catch (IllegalArgumentException e) {
                    return SlpTaskState.SLP_TASK_STATE_ABORTED;
                }
            }
            return SlpTaskState.SLP_TASK_STATE_ABORTED;
        } else {
            return SlpTaskState.SLP_TASK_STATE_FINISHED;
        }
    }

    @Override
    public List<String> getActionIds() {
        // A finished process does not have any actions
        return new ArrayList<String>();
    }

    @Override
    public void executeAction(String userId, String actionId) {
        // A finished process cannot execute any actions
        throw new UnsupportedOperationException();
    }

    @Override
    protected String getProcessInstanceId() {
        return processInstance.getId();
    }

    @Override
    public String getCurrentActivityId() {
        return null;
    }

    @Override
    public Date getProcessStartTime() {
        return processInstance.getStartTime();
    }

    @Override
    protected boolean includeParameters() {
        // Do not include parameters for finished processes in order to reduce server-side
        // processing and response payload.
        return false;
    }

    @Override
    protected void setVariables(Map<String, Object> variablesToSet) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(String userId) {
        getActivitiFacade().deleteHistoricProcessInstance(userId, processInstance.getId());
    }
}