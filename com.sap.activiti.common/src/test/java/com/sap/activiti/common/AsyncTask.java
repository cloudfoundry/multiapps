package com.sap.activiti.common;

import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.bpmn.behavior.TaskActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;

public class AsyncTask extends TaskActivityBehavior {

    private static final long serialVersionUID = 5080343629188643782L;

    private ProcessEngine processEngine;

    @Override
    public void execute(ActivityExecution execution) {
        final String callbackId = execution.getId();

        // Save this id somewhere, or pass it to the outgoing asynchronous task,
        // as this will be needed to signal this activity later when the long running external task
        // is complete
        new Thread(new Runnable() {
            @Override
            public void run() {
                processEngine.getRuntimeService().signal(callbackId);
            }
            // Retrieve the execution using the callBack Id saved previously
        }).start();
    }

    @SuppressWarnings("unchecked")
    public void signal(ActivityExecution execution, String signalName, Object signalData) throws Exception {
        // TODO: throw exception is status is failed
        execution.setVariables((Map<String, ? extends Object>) signalData);
        leave(execution);
    }

}