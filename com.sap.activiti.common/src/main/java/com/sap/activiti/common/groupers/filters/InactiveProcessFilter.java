package com.sap.activiti.common.groupers.filters;

import org.activiti.engine.history.HistoricProcessInstance;

public class InactiveProcessFilter extends AbstractFilter<HistoricProcessInstance> {

    @Override
    public boolean isAccepted(HistoricProcessInstance processInstance) {
        return processInstance.getEndTime() != null;
    }

    @Override
    public String getPositiveGroupName() {
        return "Finished";
    }

    @Override
    public String getNegativeGroupName() {
        return "Unfinished";
    }

}
