package com.sap.activiti.common;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.DelegateExecution;

import com.sap.activiti.common.impl.AbstractActivitiStep;

public class EmptyActivitiStep extends AbstractActivitiStep {

    public static final String TEST_EXCEPTION_MESSAGE = "The task has failed intentionally";

    @Override
    protected ExecutionStatus executeStep(DelegateExecution context) throws Exception {
        if (context.hasVariable(EmptyActivitiStepBehaviours.FAIL.name())) {
            throw new ActivitiException(TEST_EXCEPTION_MESSAGE);
        }
        return ExecutionStatus.SUCCESS;
    }

    public enum EmptyActivitiStepBehaviours {
        FAIL;
    }

}
