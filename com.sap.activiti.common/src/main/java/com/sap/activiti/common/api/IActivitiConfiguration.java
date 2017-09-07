package com.sap.activiti.common.api;

import java.util.List;

import com.sap.activiti.common.deploy.Deployable;

public interface IActivitiConfiguration {

    /**
     * @return The name of the activiti configuration file. This file should be located in the root of the project resource directory
     */
    public String getActivitiXmlConfig();

    public List<Deployable> getDeployables();
}
