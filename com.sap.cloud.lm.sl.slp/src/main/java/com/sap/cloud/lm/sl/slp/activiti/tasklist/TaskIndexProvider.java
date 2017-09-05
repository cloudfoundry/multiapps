package com.sap.cloud.lm.sl.slp.activiti.tasklist;

import java.util.HashMap;
import java.util.Map;

public class TaskIndexProvider {

    private Map<String, Integer> stepIndices = new HashMap<String, Integer>();

    public synchronized int getTaskIndex(String stepId) {
        Integer index = stepIndices.get(stepId);
        if (index == null)
            index = 0;
        else
            index++;
        stepIndices.put(stepId, index);
        return index;
    }
}
