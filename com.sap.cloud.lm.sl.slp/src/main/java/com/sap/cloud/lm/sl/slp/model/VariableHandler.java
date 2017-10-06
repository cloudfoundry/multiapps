package com.sap.cloud.lm.sl.slp.model;

import java.util.List;

public interface VariableHandler {

    Object getVariable(String variableName);

    List<Object> getVariablesIncludingFromSubProcesses(String variableName);

}