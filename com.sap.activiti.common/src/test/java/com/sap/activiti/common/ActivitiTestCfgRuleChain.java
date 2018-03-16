package com.sap.activiti.common;

import java.io.InputStream;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.junit.rules.ExternalResource;
import org.junit.rules.RuleChain;

import com.sap.activiti.common.impl.ActivitiTestConfiguration;

public class ActivitiTestCfgRuleChain {
    public static final String ACTIVITI_CFG_XML = "activiti.test.cfg.xml";

    private static ActivitiRule activitiRule;

    public static ActivitiRule getActivitiRule() {
        return activitiRule;
    }

    public static RuleChain getChain() {
        activitiRule = new ActivitiRule(ACTIVITI_CFG_XML);

        return RuleChain.emptyRuleChain()
            .around(activitiRule)
            .around(new ActivitiCleanupRule());
    }

    public static RuleChain getChain(ProcessEngineConfiguration cfg) {
        ProcessEngine processEngine = cfg.buildProcessEngine();
        activitiRule = new ActivitiRule();
        activitiRule.setProcessEngine(processEngine);

        return RuleChain.emptyRuleChain()
            .around(activitiRule)
            .around(new ActivitiCleanupRule());
    }

    private static final class ActivitiCleanupRule extends ExternalResource {
        @Override
        protected void after() {
            stopAllProcessInstances();
            undeployAllProcesses();
            eraseHistory();
        }

        private void eraseHistory() {
            HistoryService historyService = activitiRule.getHistoryService();
            for (HistoricProcessInstance procInst : historyService.createHistoricProcessInstanceQuery()
                .list()) {
                historyService.deleteHistoricProcessInstance(procInst.getId());
            }
        }

        private void undeployAllProcesses() {
            RepositoryService repositoryService = activitiRule.getRepositoryService();
            for (ProcessDefinition processDef : repositoryService.createProcessDefinitionQuery()
                .list()) {
                activitiRule.getRepositoryService()
                    .deleteDeployment(processDef.getDeploymentId());
            }
        }

        private void stopAllProcessInstances() {
            RuntimeService runtimeService = activitiRule.getRuntimeService();
            for (ProcessInstance procInst : runtimeService.createProcessInstanceQuery()
                .list()) {
                runtimeService.deleteProcessInstance(procInst.getId(), null);
            }
        }
    }

    public static ActivitiTestConfiguration createActivitiConfiguration() {
        return new ActivitiTestConfiguration();
    }

    public static void deploy(InputStream resource, String resourceName) {
        activitiRule.getRepositoryService()
            .createDeployment()
            .addInputStream(resourceName, resource)
            .deploy();
    }

    public static void executeFirstAvailableJob(String procesInstanceId) {
        ManagementService managementService = activitiRule.getManagementService();

        String jobId = managementService.createJobQuery()
            .processInstanceId(procesInstanceId)
            .singleResult()
            .getId();

        managementService.executeJob(jobId);
    }

    public static String startProcess(String procDefKey) {
        return startProcess(procDefKey, null);
    }

    public static String startProcess(String procInstKey, String businessKey, Map<String, Object> params) {
        return getActivitiRule().getRuntimeService()
            .startProcessInstanceByKey(procInstKey, businessKey, params)
            .getId();
    }

    public static String startProcess(String procInstKey, Map<String, Object> params) {
        return startProcess(procInstKey, null, params);
    }
}
