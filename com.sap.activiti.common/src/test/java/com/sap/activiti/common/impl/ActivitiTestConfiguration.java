package com.sap.activiti.common.impl;

import java.util.ArrayList;
import java.util.List;

import com.sap.activiti.common.api.IActivitiConfiguration;
import com.sap.activiti.common.deploy.Deployable;

public class ActivitiTestConfiguration implements IActivitiConfiguration {

    public static final String ACTIVITI_CFG_XML = "activiti.test.cfg.xml";
    private final List<Deployable> testDeployables = new ArrayList<Deployable>();

    public void addDeployable(Deployable deployable) {
        testDeployables.add(deployable);
    }

    @Override
    public List<Deployable> getDeployables() {
        return testDeployables;
    }

    @Override
    public String getActivitiXmlConfig() {
        return "/" + ACTIVITI_CFG_XML;
    }

}
