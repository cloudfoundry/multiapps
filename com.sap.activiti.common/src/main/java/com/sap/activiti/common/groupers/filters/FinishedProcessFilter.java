package com.sap.activiti.common.groupers.filters;

import org.activiti.engine.history.HistoricProcessInstance;

public class FinishedProcessFilter extends AbstractFilter<HistoricProcessInstance> {

    @SuppressWarnings("deprecation")
    @Override
    public boolean isAccepted(HistoricProcessInstance processInstance) {
        return processInstance.getEndActivityId() != null;
    }

    @Override
    public String getPositiveGroupName() {
        return "Succeeded";
    }

    @Override
    public String getNegativeGroupName() {
        return "Aborted";
    }

}
