package com.sap.activiti.common.impl;

import java.io.IOException;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;

import com.sap.activiti.common.api.IActivitiConfiguration;
import com.sap.activiti.common.api.IActivitiService;
import com.sap.activiti.common.deploy.BpmnDeployer;

public class ActivitiService implements IActivitiService {

    private static IActivitiService instance = new ActivitiService();

    public static IActivitiService getInstance() {
        return instance;
    }

    private ActivitiService() {
    }

    @Override
    public ProcessEngine initialize(IActivitiConfiguration config) throws IOException {
        String activitiXmlConfig = config.getActivitiXmlConfig();

        ProcessEngine processEngine = createProcessEngine(activitiXmlConfig);
        BpmnDeployer bpmnDeployer = new BpmnDeployer(config);
        bpmnDeployer.deploy(processEngine.getRepositoryService());

        return processEngine;
    }

    private ProcessEngine createProcessEngine(String activitiCfgXml) {
        ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource(activitiCfgXml)
            .buildProcessEngine();
        ProcessEngines.registerProcessEngine(processEngine);
        return processEngine;
    }
}
