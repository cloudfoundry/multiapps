package com.sap.activiti.common.deploy;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.activiti.common.ActivitiTestCfgRuleChain;

@RunWith(Parameterized.class)
public class BpmnDeployerTest {

    @Rule
    public RuleChain chain = ActivitiTestCfgRuleChain.getChain();
    private BpmnDeployerTestPO pageObject;

    private boolean shouldHavePredeployedVersion;
    private int expectedProcDefsCount;

    public BpmnDeployerTest(boolean shouldHavePredeployedVersion, int expectedProcDefsCount) {
        this.shouldHavePredeployedVersion = shouldHavePredeployedVersion;
        this.expectedProcDefsCount = expectedProcDefsCount;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { { false, 1 }, { true, 2 } });
    }

    @Before
    public void setUp() {
        pageObject = new BpmnDeployerTestPO();
    }

    @Test
    public void deploymentScenarios() throws IOException {
        if (shouldHavePredeployedVersion) {
            pageObject.deploySingleStepProcess();
        }
        pageObject.deploySingleStepProcessV2();

        assertEquals(expectedProcDefsCount, pageObject.getDeployedSingleStepProcDefCount());
    }

}
