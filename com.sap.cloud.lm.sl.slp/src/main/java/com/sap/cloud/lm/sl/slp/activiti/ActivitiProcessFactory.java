package com.sap.cloud.lm.sl.slp.activiti;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;

import com.sap.cloud.lm.sl.slp.model.ServiceMetadata;

public interface ActivitiProcessFactory {

    ActiveActivitiProcess create(ServiceMetadata serviceMetadata, ProcessInstance processInstance);

    FinishedActivitiProcess create(ServiceMetadata serviceMetadata, HistoricProcessInstance processInstance);
}
