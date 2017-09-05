package com.sap.cloud.lm.sl.slp.steps;

import org.activiti.engine.delegate.DelegateExecution;

public interface StepIndexProvider {
    Integer getDefaultStepIndex(DelegateExecution context);
}
