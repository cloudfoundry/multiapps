package com.sap.activiti.common.actions;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.rest.service.api.engine.variable.RestVariable;

import com.sap.activiti.common.Constants;
import com.sap.activiti.common.util.GsonHelper;

public class ActivitiUpdateVariablesAction extends AbstractTraceableAction {

    private static final String LOG_MESSAGE = "User [%s] called update variables for: %s";
    private static final String NO_VARIABLE_MSG = "No variable with name '%s' exists\n";
    private static final String CANNOT_MODIFY_VARIABLE_MSG = "Variable '%s' cannot be modified";
    private static final String CANNOT_MODIFY_VARIABLES_MSG = "The variables of that process instance cannot be modified";

    private static final String VAR_TYPE_BINARY = "bytes";

    private Map<String, Object> modifiedVariables;

    public ActivitiUpdateVariablesAction(String processInstanceId, String userId, Map<String, Object> modifiedVariables) {
        super(processInstanceId, userId, "");
        this.modifiedVariables = modifiedVariables;
    }

    public ActivitiUpdateVariablesAction(String processInstanceId, String userId, String jsonString) {
        this(processInstanceId, userId, createVariableMapFromJson(jsonString));
    }

    @Override
    public void execute() {
        if (modifiedVariables.isEmpty()) {
            return;
        }
        logTracibilityInformation(String.format(LOG_MESSAGE, getUserName(), modifiedVariables.keySet().toString()));
        validateModifiedVariables();
        updateProcessInstanceVariables();
    }

    protected void updateProcessInstanceVariables() {
        getDefaultProcessEngine().getRuntimeService().setVariables(getProcessInstanceId(), modifiedVariables);
    }

    private Map<String, HistoricVariableInstance> getProcessVariables(String processInstanceId) {
        List<HistoricVariableInstance> variablesList = getDefaultProcessEngine().getHistoryService().createHistoricVariableInstanceQuery().processInstanceId(
            processInstanceId).list();

        Map<String, HistoricVariableInstance> variablesMap = new HashMap<String, HistoricVariableInstance>();
        for (HistoricVariableInstance variable : variablesList) {
            variablesMap.put(variable.getVariableName(), variable);
        }

        return variablesMap;
    }

    private void validateModifiedVariables() {
        Map<String, HistoricVariableInstance> processVariables = getProcessVariables(getProcessInstanceId());
        Set<String> editableVariables = getEditableVariables(processVariables);
        StringBuilder messageBuilder = new StringBuilder();

        for (Entry<String, Object> entry : modifiedVariables.entrySet()) {
            HistoricVariableInstance historicVariable = processVariables.get(entry.getKey());

            if (historicVariable == null) {
                messageBuilder.append(String.format(NO_VARIABLE_MSG, entry.getKey()));
                continue;
            }

            if (!editableVariables.contains(entry.getKey())) {
                messageBuilder.append(String.format(CANNOT_MODIFY_VARIABLE_MSG, entry.getKey()));
                continue;
            }

            if (historicVariable.getVariableTypeName().equals(VAR_TYPE_BINARY)) {
                entry.setValue(getByteArray((String) entry.getValue()));
            }
        }

        if (messageBuilder.length() > 0) {
            throw new IllegalArgumentException(messageBuilder.toString());
        }
    }

    private byte[] getByteArray(String variable) {
        try {
            return variable.getBytes(Constants.CHARSET_UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private Set<String> getEditableVariables(Map<String, HistoricVariableInstance> processVariables) {
        HistoricVariableInstance editableVariables = processVariables.get(Constants.EDITABLE_VARIABLES);

        if (editableVariables == null) {
            throw new IllegalArgumentException(CANNOT_MODIFY_VARIABLES_MSG);
        }

        return GsonHelper.getFromStringJson((String) editableVariables.getValue(), Set.class);
    }

    private static Map<String, Object> createVariableMapFromJson(String jsonString) {
        RestVariable[] updateVariablesArray = GsonHelper.getFromStringJson(jsonString, RestVariable[].class);

        Map<String, Object> updateVariablesMap = new HashMap<String, Object>();
        for (RestVariable variable : updateVariablesArray) {
            updateVariablesMap.put(variable.getName(), variable.getValue());
        }

        return updateVariablesMap;
    }

    @Override
    public ActionType getType() {
        return IActivitiAction.ActionType.UPDATE_VARIABLES;
    }
}
