package com.sap.activiti.common.actions;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import com.sap.activiti.common.ActivitiIdentityServiceRule;
import com.sap.activiti.common.ActivitiTestCfgRuleChain;
import com.sap.activiti.common.Constants;
import com.sap.activiti.common.deploy.Deployable;

public class ActivitiWriteMemoActionTestPO {
    private static final String SINGLE_STEP_PROCESS_FILE = "single_step.bpmn";
    private static final String BPMN_PACKAGE_PATH = "/com/sap/activiti/common/cfg/";
    private static final String MEMO_TEXT = "Sample memo";
    private static final String TEST_STEP = "test_step";
    private static final String TEST_JOB_ID = "121211";

    public String startTestProcess() throws Exception {
        Deployable deployable = new Deployable(SINGLE_STEP_PROCESS_FILE, BPMN_PACKAGE_PATH);
        String bpmn = IOUtils.toString(deployable.getBpmnStream());

        ActivitiTestCfgRuleChain.getActivitiRule().getRepositoryService().createDeployment().addString(SINGLE_STEP_PROCESS_FILE,
            bpmn).deploy();

        return ActivitiTestCfgRuleChain.startProcess("test.provision.hana.singlestep", null);
    }

    public ActivitiWriteMemoAction createService(String processInstanceId) {
        return new ActivitiWriteMemoAction(processInstanceId, ActivitiIdentityServiceRule.TEST_USER, MEMO_TEXT, TEST_JOB_ID) {
            @Override
            protected String getActivityId() {
                return TEST_STEP;
            }
        };
    }

    public boolean isUserSetInContext(String procInstId) {
        String actionLog = getActionLog(procInstId);

        return actionLog.contains(ActivitiIdentityServiceRule.TEST_FIRST_NAME)
            && actionLog.contains(ActivitiIdentityServiceRule.TEST_LAST_NAME);
    }

    public boolean isJobIdSetInContext(String procInstId) {
        String actionLog = getActionLog(procInstId);
        return actionLog.contains(TEST_STEP);
    }

    public boolean isMemoTextSetInContext(String procInstId) {
        return getActionLog(procInstId).contains(MEMO_TEXT);
    }

    private String getActionLog(String procInstId) {
        byte[] actionLogBytes = (byte[]) getHistoricVariableValue(procInstId, Constants.ACTION_LOG);
        return new String(actionLogBytes, Charsets.UTF_8);
    }

    private Object getHistoricVariableValue(String procInstId, String variableName) {
        return ActivitiTestCfgRuleChain.getActivitiRule().getHistoryService().createHistoricVariableInstanceQuery().processInstanceId(
            procInstId).variableName(variableName).singleResult().getValue();
    }
}
