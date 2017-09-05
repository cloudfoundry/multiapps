package com.sap.cloud.lm.sl.slp.activiti;

import com.sap.cloud.lm.sl.slp.model.ServiceMetadata;

public interface ActivitiServiceFactory {

    ActivitiService createActivitiService(ServiceMetadata serviceMetadata);
}
