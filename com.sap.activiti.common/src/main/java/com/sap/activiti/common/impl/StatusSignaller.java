package com.sap.activiti.common.impl;

import org.activiti.engine.delegate.DelegateExecution;

import com.sap.activiti.common.Constants;
import com.sap.activiti.common.ExecutionStatus;
import com.sap.activiti.common.api.IStatusSignaller;

public class StatusSignaller implements IStatusSignaller {

    private String logicalStepContextVarName;

    public StatusSignaller(String logicalStepContextVarName) {
        this.logicalStepContextVarName = Constants.STEP_NAME_PREFIX + logicalStepContextVarName;
    }

    @Override
    public void signal(DelegateExecution context, ExecutionStatus status) {
        context.setVariable(logicalStepContextVarName, status.name());
    }
}
