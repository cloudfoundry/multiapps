package com.sap.activiti.common.actions;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import com.sap.activiti.common.ActivitiIdentityServiceRule;
import com.sap.activiti.common.ActivitiTestCfgRuleChain;
import com.sap.activiti.common.Constants;
import com.sap.activiti.common.deploy.Deployable;

public class ActivitiAbortActionTestPO {
    private static final String ABORT_REASON = "abort reason";
    private static final String SINGLE_STEP_PROCESS_FILE = "single_step.bpmn";
    private static final String BPMN_PACKAGE_PATH = "/com/sap/activiti/common/cfg/";

    public String startTestProcess() throws Exception {
        Deployable deployable = new Deployable(SINGLE_STEP_PROCESS_FILE, BPMN_PACKAGE_PATH);
        String bpmn = IOUtils.toString(deployable.getBpmnStream());

        ActivitiTestCfgRuleChain.getActivitiRule()
            .getRepositoryService()
            .createDeployment()
            .addString(SINGLE_STEP_PROCESS_FILE, bpmn)
            .deploy();

        return ActivitiTestCfgRuleChain.startProcess("test.provision.hana.singlestep", null);
    }

    public ActivitiAbortAction createService(String processInstanceId) {
        return new ActivitiAbortAction(processInstanceId, ActivitiIdentityServiceRule.TEST_USER, ABORT_REASON);
    }

    public boolean isProcessRunning(String procInstId) {
        return ActivitiTestCfgRuleChain.getActivitiRule()
            .getRuntimeService()
            .createProcessInstanceQuery()
            .processInstanceId(procInstId)
            .singleResult() != null;
    }

    public boolean isUserSetInContext(String procInstId) {
        String actionLog = getActionLog(procInstId);

        return actionLog.contains(ActivitiIdentityServiceRule.TEST_FIRST_NAME)
            && actionLog.contains(ActivitiIdentityServiceRule.TEST_LAST_NAME);
    }

    public boolean isAbortReasonSetInContext(String procInstId) {
        return getActionLog(procInstId).contains(ABORT_REASON);
    }

    public boolean isExecutedActionTypeSetInContext(Enum<? extends Enum<?>> type, String procInstId) {
        byte[] actionsTypeLogBytes = (byte[]) getHistoricVariableValue(procInstId, Constants.EXECUTED_ACTIONS_TYPE_LOG);
        String actionsTypeLog = new String(actionsTypeLogBytes, Charsets.UTF_8);
        return actionsTypeLog.contains(type.name()
            .toUpperCase() + "\n");
    }

    private String getActionLog(String procInstId) {
        byte[] actionLogBytes = (byte[]) getHistoricVariableValue(procInstId, Constants.ACTION_LOG);
        return new String(actionLogBytes, Charsets.UTF_8);
    }

    private Object getHistoricVariableValue(String procInstId, String variableName) {
        return ActivitiTestCfgRuleChain.getActivitiRule()
            .getHistoryService()
            .createHistoricVariableInstanceQuery()
            .processInstanceId(procInstId)
            .variableName(variableName)
            .singleResult()
            .getValue();
    }
}
