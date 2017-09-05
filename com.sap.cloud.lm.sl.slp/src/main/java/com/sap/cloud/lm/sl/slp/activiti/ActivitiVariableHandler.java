package com.sap.cloud.lm.sl.slp.activiti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricVariableInstance;

import com.sap.cloud.lm.sl.slp.model.VariableHandler;

/*
 * A {@link com.sap.cloud.lm.sl.slp.model.VariableHandler} implementation to pull variables out of Activiti
 */
class ActivitiVariableHandler implements VariableHandler {

    private final String processInstanceId;
    private Map<String, Object> variables = new HashMap<String, Object>();
    private ActivitiFacade activitiFacade;
    private List<String> subProcessIds;

    ActivitiVariableHandler(String processInstanceId, ActivitiFacade activitiFacade) {
        this.processInstanceId = processInstanceId;
        this.activitiFacade = activitiFacade;
        this.subProcessIds = activitiFacade.getHistoricSubProcessIds(processInstanceId);
    }

    @Override
    public Object getVariable(String variableName) {
        if (!variables.containsKey(variableName)) {
            List<Object> variablesIncludingFromSubProcesses = getVariablesIncludingFromSubProcesses(variableName);
            Object value = variablesIncludingFromSubProcesses.isEmpty() ? null : variablesIncludingFromSubProcesses.get(0);
            variables.put(variableName, value);
        }
        return variables.get(variableName);
    }

    @Override
    public List<Object> getVariablesIncludingFromSubProcesses(String variableName) {
        List<Object> variableValues = new ArrayList<>();
        HistoricVariableInstance variable = getHistoricVariableInstanceFromProcess(processInstanceId, variableName);
        if (variable != null) {
            variableValues.add(variable.getValue());
        }
        for (String subProcessId : subProcessIds) {
            variable = getHistoricVariableInstanceFromProcess(subProcessId, variableName);
            if (variable != null) {
                variableValues.add(variable.getValue());
            }
        }
        return variableValues;
    }

    private HistoricVariableInstance getHistoricVariableInstanceFromProcess(String processId, String variableName) {
        return activitiFacade.getHistoricVariableInstance(processId, variableName);
    }

}