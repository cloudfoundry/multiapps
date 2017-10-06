package com.sap.activiti.common.api;

/**
 * Marks a java class as part of a logical step of an Activiti BPMN process execution. Each Activiti logical step consists of one ore move
 * steps. An example for having a logical step with more than one step is the asynchronous execution and monitoring pattern, where the first
 * step initiates the asynchronous execution and the next one does polling/monitoring of the execution state.
 * 
 */
public interface ILogicalStep {

    /**
     * @return The name of the logical step, this class is part of.
     */
    String getLogicalStepName();
}
