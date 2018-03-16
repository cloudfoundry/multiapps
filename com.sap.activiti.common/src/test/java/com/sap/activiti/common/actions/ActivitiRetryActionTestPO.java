package com.sap.activiti.common.actions;

import static com.sap.activiti.common.EmptyActivitiStep.EmptyActivitiStepBehaviours.FAIL;

import java.util.HashMap;

import org.activiti.engine.history.HistoricVariableInstance;
import org.apache.commons.io.Charsets;

import com.sap.activiti.common.ActivitiIdentityServiceRule;
import com.sap.activiti.common.ActivitiSingleStepTestUtils;
import com.sap.activiti.common.ActivitiTestCfgRuleChain;
import com.sap.activiti.common.Constants;

public class ActivitiRetryActionTestPO {

    public static final int DEFAULT_TRYCOUNT = 10;

    private static final String JOB_SHOULD_FAIL_MESSAGE = "Job execution should throw exception";
    public static final String REASON = "because";

    private ActivitiRetryAction service;
    private String processInstanceId;

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public Object getVariable(String variableName) {
        HistoricVariableInstance variable = ActivitiTestCfgRuleChain.getActivitiRule()
            .getHistoryService()
            .createHistoricVariableInstanceQuery()
            .processInstanceId(processInstanceId)
            .variableName(variableName)
            .singleResult();

        return (variable == null) ? null : variable.getValue();
    }

    public ActivitiRetryAction getService() {
        return this.service;
    }

    public void retryFailingJobSafely() {
        try {
            service.execute();
            throw new RuntimeException(JOB_SHOULD_FAIL_MESSAGE);
        } catch (Exception e) {

        }
    }

    public boolean hasTracibilityInfo() {
        byte[] actionLogBytes = (byte[]) getVariable(Constants.ACTION_LOG);
        String actionLog = new String(actionLogBytes, Charsets.UTF_8);

        return actionLog.contains(ActivitiIdentityServiceRule.TEST_FIRST_NAME)
            && actionLog.contains(ActivitiIdentityServiceRule.TEST_LAST_NAME) && actionLog.contains(REASON);
    }

    public boolean hasExecutedActionsTypeLog() {
        byte[] actionsTypeLog = (byte[]) getVariable(Constants.EXECUTED_ACTIONS_TYPE_LOG);
        return (actionsTypeLog != null) && (actionsTypeLog.length > 0);
    }

    private void failTheProcess() {
        try {
            ActivitiTestCfgRuleChain.executeFirstAvailableJob(processInstanceId);
            throw new RuntimeException(JOB_SHOULD_FAIL_MESSAGE);
        } catch (Exception e) {

        }
    }

    public void removeFailureVariable() {
        ActivitiTestCfgRuleChain.getActivitiRule()
            .getRuntimeService()
            .removeVariable(processInstanceId, FAIL.name());
    }

    private String getFailedJob() {
        return ActivitiTestCfgRuleChain.getActivitiRule()
            .getManagementService()
            .createJobQuery()
            .processInstanceId(processInstanceId)
            .singleResult()
            .getId();
    }

    public void init() throws Exception {
        ActivitiSingleStepTestUtils.deploySingleStepProcess();
        HashMap<String, Object> variables = new HashMap<String, Object>();
        variables.put(FAIL.name(), FAIL);
        processInstanceId = ActivitiSingleStepTestUtils.startSingleStepProcess(variables);
        failTheProcess();
        String jobId = getFailedJob();
        service = new ActivitiRetryAction(processInstanceId, ActivitiIdentityServiceRule.TEST_USER, REASON, jobId);
    }

    public void initWithTryCount(String taskName) throws Exception {
        ActivitiSingleStepTestUtils.deploySingleStepProcess();
        HashMap<String, Object> variables = new HashMap<String, Object>();
        variables.put(FAIL.name(), FAIL);
        variables.put(ActivitiRetryAction.TRYCOUNT_CONTEXT_PREFIX + taskName, DEFAULT_TRYCOUNT);
        processInstanceId = ActivitiSingleStepTestUtils.startSingleStepProcess(variables);
        failTheProcess();
        String jobId = getFailedJob();
        service = new ActivitiRetryAction(processInstanceId, ActivitiIdentityServiceRule.TEST_USER, REASON, jobId);
    }
}
