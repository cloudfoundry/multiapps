package com.sap.activiti.common.actions;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;

import com.sap.activiti.common.ActivitiIdentityServiceRule;
import com.sap.activiti.common.ActivitiTestCfgRuleChain;

public class ActivitiWriteMemoActionTest {

    private ActivitiWriteMemoActionTestPO po;
    private String procInstId;
    private ActivitiWriteMemoAction writeMemoService;

    @Rule
    public RuleChain activitiChain = ActivitiTestCfgRuleChain.getChain().around(new ActivitiIdentityServiceRule());

    @Before
    public void setUp() throws Exception {
        po = new ActivitiWriteMemoActionTestPO();
        this.procInstId = po.startTestProcess();
        writeMemoService = po.createService(procInstId);
        writeMemoService.execute();
    }

    @Test
    public void whenMemoIsWritten_itIsCorrectlyPersistedInActionLog() {
        assertTrue(po.isMemoTextSetInContext(procInstId));
    }

    @Test
    public void whenMemoIsWritten_userNameIsCorrectlyPersistedInActionLog() {
        assertTrue(po.isUserSetInContext(procInstId));
    }

    @Test
    public void whenMemoIsWritten_jobIdIsCorrectlyPersistedInActionLog() {
        assertTrue(po.isJobIdSetInContext(procInstId));
    }
}
