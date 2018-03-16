package com.sap.activiti.common.deploy;

import java.io.IOException;

import com.sap.activiti.common.ActivitiTestCfgRuleChain;
import com.sap.activiti.common.impl.ActivitiService;
import com.sap.activiti.common.impl.ActivitiTestConfiguration;

public class BpmnDeployerTestPO {
    static final Deployable SINGLE_STEP_DEPLOYABLE = new Deployable("single_step.bpmn", "/com/sap/activiti/common/cfg/");
    static final Deployable SINGLE_STEP_DEPLOYABLE_V2 = new Deployable("single_step_v2.bpmn", "/com/sap/activiti/common/cfg/");

    public void deploySingleStepProcessV2() throws IOException {
        deploy(SINGLE_STEP_DEPLOYABLE_V2);
    }

    public void deploySingleStepProcess() throws IOException {
        deploy(SINGLE_STEP_DEPLOYABLE);
    }

    public void deploy(Deployable deployable) throws IOException {
        ActivitiTestConfiguration config = ActivitiTestCfgRuleChain.createActivitiConfiguration();
        config.addDeployable(deployable);
        ((ActivitiService) ActivitiService.getInstance()).initialize(config);
    }

    public int getDeployedSingleStepProcDefCount() {
        return ActivitiTestCfgRuleChain.getActivitiRule()
            .getRepositoryService()
            .createProcessDefinitionQuery()
            .processDefinitionKey(SINGLE_STEP_DEPLOYABLE.getProcessDefinitionKey())
            .list()
            .size();
    }
}
